package com.axonivy.connector.office365.test;

import java.util.List;

import com.axonivy.ivy.webtest.engine.WebAppFixture;
import com.microsoft.graph.GraphAuthMock;
import com.microsoft.graph.GraphServiceMock;

import ch.ivyteam.ivy.environment.AppFixture;

public class GraphTestClient {

  public static final String GRAPH_CLIENT_ID = "007036dc-72d1-429f-88a7-ba5d5cf5ae58";
  public static void configureFixture(AppFixture fixture) {
    fixture.config("RestClients.'Microsoft 365 (OData Service for namespace microsoft.graph)'.Url", GraphServiceMock.URI);
    fixture.config("RestClients.'Microsoft 365 (OData Service for namespace microsoft.graph)'.Properties.AUTH.baseUri", GraphAuthMock.URI);
    fixture.config("RestClients.'Microsoft 365 (OData Service for namespace microsoft.graph)'.Properties.AUTH.appId", GRAPH_CLIENT_ID);
    fixture.config("RestClients.'Microsoft 365 (OData Service for namespace microsoft.graph)'.Properties.AUTH.secretKey", "1");
    fixture.config("RestClients.'Microsoft 365 (OData Service for namespace microsoft.graph)'.Properties.scope", "user.read calendars.read");
	fixture.config("RestClients.'Microsoft 365 (OData Service for namespace microsoft.graph)'.Features",
			"ch.ivyteam.ivy.rest.client.mapper.JsonFeature");
  }

  public static void configureFixture(WebAppFixture fixture) {
    fixture.config("RestClients.'Microsoft 365 (OData Service for namespace microsoft.graph)'.Url", GraphServiceMock.URI);
    fixture.config("RestClients.'Microsoft 365 (OData Service for namespace microsoft.graph)'.Properties.AUTH.baseUri", GraphAuthMock.URI);
    fixture.config("RestClients.'Microsoft 365 (OData Service for namespace microsoft.graph)'.Properties.AUTH.appId", GRAPH_CLIENT_ID);
    fixture.config("RestClients.'Microsoft 365 (OData Service for namespace microsoft.graph)'.Properties.AUTH.secretKey", "1");
    fixture.config("RestClients.'Microsoft 365 (OData Service for namespace microsoft.graph)'.Properties.scope", "user.read calendars.read");
	fixture.config("RestClients.'Microsoft 365 (OData Service for namespace microsoft.graph)'.Features",
			"ch.ivyteam.ivy.rest.client.mapper.JsonFeature");
  }
}
