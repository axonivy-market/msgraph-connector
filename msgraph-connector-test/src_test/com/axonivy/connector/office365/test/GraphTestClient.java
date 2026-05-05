package com.axonivy.connector.office365.test;

import java.util.List;
import java.util.UUID;

import com.axonivy.ivy.webtest.engine.WebAppFixture;
import com.microsoft.graph.GraphAuthMock;
import com.microsoft.graph.GraphServiceMock;

import ch.ivyteam.ivy.environment.AppFixture;
import ch.ivyteam.ivy.rest.client.mapper.JsonFeature;
import ch.ivyteam.ivy.rest.client.security.CsrfHeaderFeature;

public class GraphTestClient {

  public static final UUID GRAPH_CLIENT_ID = UUID.fromString(System.getProperty("test.azure.app.id"));
  
  public static void configureFixture(AppFixture fixture) {
    fixture.config("RestClients.'Microsoft 365 (OData Service for namespace microsoft.graph)'.Url", GraphServiceMock.URI);
    fixture.config("RestClients.'Microsoft 365 (OData Service for namespace microsoft.graph)'.Features",
        List.of(JsonFeature.class.getName(), CsrfHeaderFeature.class.getName()));
    fixture.config("RestClients.'Microsoft 365 (OData Service for namespace microsoft.graph)'.Properties.AUTH.baseUri", GraphAuthMock.URI);
    fixture.config("RestClients.'Microsoft 365 (OData Service for namespace microsoft.graph)'.Properties.AUTH.secretKey", "1");
    fixture.config("RestClients.'Microsoft 365 (OData Service for namespace microsoft.graph)'.Properties.scope", "user.read calendars.read");
    fixture.config("RestClients.'Microsoft 365 (OData Service for namespace microsoft.graph)'.Properties.AUTH.appId", GRAPH_CLIENT_ID.toString());
  }

  public static void configureFixture(WebAppFixture fixture) {
    fixture.config("RestClients.'Microsoft 365 (OData Service for namespace microsoft.graph)'.Url", GraphServiceMock.URI);
    fixture.config("RestClients.'Microsoft 365 (OData Service for namespace microsoft.graph)'.Features",
        List.of(JsonFeature.class.getName(), CsrfHeaderFeature.class.getName()));
    fixture.config("RestClients.'Microsoft 365 (OData Service for namespace microsoft.graph)'.Properties.AUTH.baseUri", GraphAuthMock.URI);
    fixture.config("RestClients.'Microsoft 365 (OData Service for namespace microsoft.graph)'.Properties.AUTH.secretKey", "1");
    fixture.config("RestClients.'Microsoft 365 (OData Service for namespace microsoft.graph)'.Properties.scope", "user.read calendars.read");
    fixture.config("RestClients.'Microsoft 365 (OData Service for namespace microsoft.graph)'.Properties.AUTH.appId", GRAPH_CLIENT_ID.toString());
  }
}
