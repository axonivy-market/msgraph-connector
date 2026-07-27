package msgraph.wrappers;

import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;

import com.microsoft.graph.MsGraph;

import msgraph.models.CollectionOfMessageWithCount;

public class MsGraphMailRest {

  private final WebTarget restClient;

  public MsGraphMailRest() {
    restClient = new MsGraph().client();
  }

  public CollectionOfMessageWithCount getMessages(int first, int pageSize) {
    return restClient
            .path("/me/messages")
            .queryParam("$count", true)
            .queryParam("$skip", first)
            .queryParam("$top", pageSize)
            .request(MediaType.APPLICATION_JSON).get()
            .readEntity(CollectionOfMessageWithCount.class);
  }

}
