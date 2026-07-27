package msgraph.teams;

import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.graph.MicrosoftGraphChatMessageCollectionResponse;
import com.microsoft.graph.MsGraph;

public class MsGraphTeamsChat {

  private final WebTarget restClient;
  private final String chatId;

  public MsGraphTeamsChat(String chatId) {
    this.restClient = new MsGraph().client();
    this.chatId = chatId;
  }

  public CollectionOfMessageWithCount getMessages(int first, int pageSize) {
    return restClient
      .path("me/chats/{chat-id}/messages")
      .resolveTemplate("chat-id", chatId)
//      .queryParam("$count", true)
//      .queryParam("$skip", first)
//      .queryParam("$top", pageSize)
      .request(MediaType.APPLICATION_JSON).get()
      .readEntity(CollectionOfMessageWithCount.class);
  }

  /**
   * CollectionOfMessage with added count field
   */
  public static class CollectionOfMessageWithCount extends MicrosoftGraphChatMessageCollectionResponse {
    @JsonProperty("@odata.count")
    private Integer _count = null;

    public Integer getCount() {
      return _count;
    }
  }

}
