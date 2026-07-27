package msgraph.teams;

import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.graph.MicrosoftGraphChatCollectionResponse;
import com.microsoft.graph.MsGraph;

public class MsGraphTeams {

  private final WebTarget restClient;

  public MsGraphTeams() {
    this.restClient = new MsGraph().client();
  }

  public CollectionOfChatWithCount getMessages() {
    return restClient
      .path("/me/chats")
      .queryParam("$top", 20)
      .request(MediaType.APPLICATION_JSON).get()
      .readEntity(CollectionOfChatWithCount.class);
  }

  public static class CollectionOfChatWithCount extends MicrosoftGraphChatCollectionResponse {
    @JsonProperty("@odata.count")
    private Integer _count = null;

    public Integer getCount() {
      return _count;
    }
  }

}
