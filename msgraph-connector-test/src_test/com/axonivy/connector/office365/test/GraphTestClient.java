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

  public static final UUID GRAPH_CLIENT_ID = UUID.fromString("007036dc-72d1-429f-88a7-ba5d5cf5ae58");

  
  public static void configureFixture(AppFixture fixture) {
	String[] clients = { "Microsoft 365 (Partial Graph API)",
				"Microsoft 365 (OData Service for namespace microsoft.graph)" };
	System.setProperty("test.azure.app.id", GRAPH_CLIENT_ID.toString());
	
	for (String CLIENT : clients) {
		fixture.config("RestClients.'" + CLIENT + "'.Url", GraphServiceMock.URI);

		fixture.config("RestClients.'" + CLIENT + "'.Properties.AUTH.baseUri", GraphAuthMock.URI);
		fixture.config("RestClients.'" + CLIENT + "'.Properties.AUTH.tokenUri", GraphAuthMock.URI + "/token");
		fixture.config("RestClients.'" + CLIENT + "'.Properties.AUTH.authorizeUri", GraphAuthMock.URI + "/authorize");

		fixture.config("RestClients.'" + CLIENT + "'.Properties.AUTH.appId", GRAPH_CLIENT_ID.toString());
		fixture.config("RestClients.'" + CLIENT + "'.Properties.AUTH.secretKey", "1");

		fixture.config("RestClients.'" + CLIENT + "'.Properties.scope", "user.read calendars.read");
	}
  }

  public static void configureFixture(WebAppFixture fixture) {
	String[] clients = { "Microsoft 365 (Partial Graph API)",
			"Microsoft 365 (OData Service for namespace microsoft.graph)" };
	System.setProperty("test.azure.app.id", GRAPH_CLIENT_ID.toString());

	for (String CLIENT : clients) {
		fixture.config("RestClients.'" + CLIENT + "'.Url", GraphServiceMock.URI);

		fixture.config("RestClients.'" + CLIENT + "'.Properties.AUTH.baseUri", GraphAuthMock.URI);
		fixture.config("RestClients.'" + CLIENT + "'.Properties.AUTH.tokenUri", GraphAuthMock.URI + "/token");
		fixture.config("RestClients.'" + CLIENT + "'.Properties.AUTH.authorizeUri",
				GraphAuthMock.URI + "/authorize");

		fixture.config("RestClients.'" + CLIENT + "'.Properties.AUTH.appId", GRAPH_CLIENT_ID.toString());
		fixture.config("RestClients.'" + CLIENT + "'.Properties.AUTH.secretKey", "1");

		fixture.config("RestClients.'" + CLIENT + "'.Properties.scope", "user.read calendars.read");
	}
  }
}
