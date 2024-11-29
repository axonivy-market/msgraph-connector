package com.microsoft.graph;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import com.microsoft.auth.OAuth2Feature;

import ch.ivyteam.ivy.application.IApplication;
import ch.ivyteam.ivy.rest.client.RestClient;
import ch.ivyteam.ivy.rest.client.RestClients;
import ch.ivyteam.ivy.rest.client.security.CsrfHeaderFeature;

public class GraphTestClient {

  public static final UUID GRAPH_CLIENT_ID = UUID.fromString("007036dc-72d1-429f-88a7-ba5d5cf5ae58");
  private static final AtomicReference<RestClient> ORIGINAL = new AtomicReference<>();

  public static void resetForApp(IApplication app) {
    RestClient client = ORIGINAL.get();
    if (client != null) {
      RestClients.of(app).set(client);
      ORIGINAL.set(null);
    }
  }

  public static void mockForApp(IApplication app) {
    RestClients clients = RestClients.of(app);
    RestClient graphClient = clients.find(GRAPH_CLIENT_ID);
    if (ORIGINAL.get() == null) {
      ORIGINAL.set(graphClient);
    }

    var graphMock = graphClient
      .toBuilder()
      .uri(GraphServiceMock.URI)
      .feature(CsrfHeaderFeature.class.getName())
      .removeFeature(OAuth2Feature.class.getName())
      .property("AUTH.baseUri", GraphAuthMock.URI)
      .property("AUTH.secretKey", "1")
      .property("scope", "user.read calendars.read")
      .toRestClient();

    graphMock = new RestClient(graphMock.uri(), graphMock.name(), graphMock.uniqueId(), graphMock.description(),
      graphMock.features(), graphMock.properties(), graphMock.metas());

    clients.set(graphMock);
  }

}