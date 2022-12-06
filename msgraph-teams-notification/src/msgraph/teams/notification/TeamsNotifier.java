package msgraph.teams.notification;

import static java.util.function.Predicate.not;

import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.axonivy.wf.ext.notification.NewTaskAssignmentListener;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.microsoft.graph.MicrosoftGraphBodyType;
import com.microsoft.graph.MicrosoftGraphChat;
import com.microsoft.graph.MicrosoftGraphChatMessage;
import com.microsoft.graph.MicrosoftGraphItemBody;
import com.microsoft.graph.MicrosoftGraphUser;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.security.IRole;
import ch.ivyteam.ivy.security.ISecurity;
import ch.ivyteam.ivy.security.ISecurityMember;
import ch.ivyteam.ivy.security.IUser;
import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.IWorkflowManager;

public class TeamsNotifier extends NewTaskAssignmentListener {

  private WebTarget client;
  private MicrosoftGraphUser me;

  public TeamsNotifier() {
    super(IWorkflowManager.instance());
    taskHandler(this::notifyGraph);
    client = Ivy.rest().client(UUID.fromString("fb0c277e-35a3-481f-8f79-edca67ce1145"));
  }

  public void notifyGraph(ITask newTask) {
    Ivy.log().info("notify ms-teams clients on new teask "+newTask);

    // exec as system session: avoid token clash!
    ISecurityMember activator = newTask.getActivator();
    getUsers(activator).map(IUser::getExternalId)
      .filter(Objects::nonNull)
      .filter(not(String::isBlank))
      .forEach(userId -> notify(userId, newTask));
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