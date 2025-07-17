package msgraph.teams.notification;

import static java.util.function.Predicate.not;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;

import com.axonivy.wf.ext.notification.NewTaskAssignmentListener;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.microsoft.auth.OAuth2Feature;
import com.microsoft.graph.MicrosoftGraphBodyType;
import com.microsoft.graph.MicrosoftGraphChat;
import com.microsoft.graph.MicrosoftGraphChatMessage;
import com.microsoft.graph.MicrosoftGraphItemBody;
import com.microsoft.graph.MicrosoftGraphUser;
import com.microsoft.graph.MsGraph;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.security.IRole;
import ch.ivyteam.ivy.security.ISecurity;
import ch.ivyteam.ivy.security.ISecurityMember;
import ch.ivyteam.ivy.security.IUser;
import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.IWorkflowManager;

public class TeamsNotifier extends NewTaskAssignmentListener {

  private static final String CHAT_PERMISSIONS = "User.Read Chat.Create Chat.ReadWrite";

  private WebTarget client;
  private MicrosoftGraphUser me;

  public TeamsNotifier() {
    super(IWorkflowManager.instance());
    taskHandler(this::notifyGraph);
    client = backendClient();
  }

  private static WebTarget backendClient() {
    return new MsGraph().client()
      .property(OAuth2Feature.Property.SCOPE, CHAT_PERMISSIONS)
      .property(OAuth2Feature.Property.USE_APP_PERMISSIONS, Boolean.FALSE.toString())
      .property(OAuth2Feature.Property.USE_USER_PASS_FLOW, Boolean.TRUE.toString())
      .property(OAuth2Feature.Property.USER, Ivy.var().get("teamsNotification.username"))
      .property(OAuth2Feature.Property.PASS, Ivy.var().get("teamsNotification.userpass"));
  }

  private boolean isEnabled() {
    return Boolean.parseBoolean((Ivy.var().get("teamsNotification.enabled")));
  }

  public void notifyGraph(ITask newTask) {
    if (!isEnabled()) {
      return;
    }
    Ivy.log().info("notify ms-teams clients on new teask "+newTask);

    // exec as system session: avoid token clash!
    List<ch.ivyteam.ivy.workflow.task.responsible.Responsible> responsibles = newTask.responsibles().all();
    for (ch.ivyteam.ivy.workflow.task.responsible.Responsible responsible : responsibles) {
      ISecurityMember activator = responsible.get();
      getUsers(activator).map(IUser::getExternalId).filter(Objects::nonNull).filter(not(String::isBlank))
          .forEach(userId -> notify(userId, newTask));
    }
  }

  private void notify(String azureUserId, ITask newTask) {
    try {
      var chat = createChat(getMe().getId(), azureUserId);
      var msg = new MicrosoftGraphChatMessage();
      var body = new MicrosoftGraphItemBody();
      msg.setBody(body);
      body.setContentType(MicrosoftGraphBodyType.HTML);
      body.setContent(toHtml(newTask));

      Ivy.log().info("sending "+msg);
      client.path("/chats/{chat-id}/messages")
        .resolveTemplate("chat-id", chat.getId())
        .request(MediaType.APPLICATION_JSON)
        .post(Entity.entity(msg, MediaType.APPLICATION_JSON));
    } catch (Exception ex) {
      Ivy.log().error("Failed to notify user "+azureUserId+" on new task "+newTask, ex);
    }
  }

  private MicrosoftGraphUser getMe() {
    if (me == null) {
      me = client.path("/me")
        .request(MediaType.APPLICATION_JSON)
        .get(MicrosoftGraphUser.class);
    }
    return me;
  }

  private static String toHtml(ITask newTask) {
    var html = new StringBuilder();
    html.append("<h1>New Task ").append(newTask.getName()).append("</h1>");
    String description = newTask.getDescription();
    if (StringUtils.isNotEmpty(description)) {
      html.append("<p>").append(description).append("</p>");
    }
    html.append("<a href='").append(newTask.getStartLink().getAbsolute()).append("'>Start Task</a>");
    return html.toString();
  }

  /**
   * @param sender UUID
   * @param receiver UUID
   * @return created chat
   */
  private MicrosoftGraphChat createChat(String sender, String receiver) {
    ObjectNode initChat = JsonNodeFactory.instance.objectNode();
    initChat.set("chatType", initChat.textNode("oneOnOne"));
    ArrayNode members = initChat.putArray("members");
    members.add(createOwner(sender));
    members.add(createOwner(receiver));

    var result = client.path("/chats")
      .request(MediaType.APPLICATION_JSON)
      .post(Entity.entity(initChat, MediaType.APPLICATION_JSON));
    return result.readEntity(MicrosoftGraphChat.class);
  }

  private static ObjectNode createOwner(String userId) {
    ObjectNode member = JsonNodeFactory.instance.objectNode();
    member.set("@odata.type", member.textNode("#microsoft.graph.aadUserConversationMember"));
    member.putArray("roles").add("owner");
    member.set("user@odata.bind", member.textNode("https://graph.microsoft.com/v1.0/users('"+userId+"')"));
    return member;
  }

  private static Stream<IUser> getUsers(ISecurityMember activator) {
    if (activator.isUser()) {
      return Stream.of(ISecurity.current().users().findById(activator.getSecurityMemberId()));
    }
    IRole role = ISecurity.current().roles().findById(activator.getSecurityMemberId());
    return role.users().allPaged().stream();
  }

}