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

  public static final String GRAPH_CLIENT_ID = "007036dc-72d1-429f-88a7-ba5d5cf5ae58";
  public static void configureFixture(AppFixture fixture) {
	System.setProperty("test.azure.app.id", GRAPH_CLIENT_ID);
	System.setProperty("test.azure.app.secretKey", "1");
	System.setProperty("test.azure.app.tenantId", "mock");
    fixture.config("RestClients.'Microsoft 365 (Partial Graph API)'.Url", GraphServiceMock.URI);
    fixture.config("RestClients.'Microsoft 365 (Partial Graph API)'.Properties.AUTH.baseUri", GraphAuthMock.URI);
    fixture.config("RestClients.'Microsoft 365 (Partial Graph API)'.Properties.AUTH.secretKey", "1");
    fixture.config("RestClients.'Microsoft 365 (Partial Graph API)'.Properties.scope", "user.read calendars.read");
  }

  public static void configureFixture(WebAppFixture fixture) {
	System.setProperty("test.azure.app.id", GRAPH_CLIENT_ID);
	System.setProperty("test.azure.app.secretKey", "1");
	System.setProperty("test.azure.app.tenantId", "mock");
    fixture.config("RestClients.'Microsoft 365 (Partial Graph API)'.Url", GraphServiceMock.URI);
    fixture.config("RestClients.'Microsoft 365 (Partial Graph API)'.Properties.AUTH.baseUri", GraphAuthMock.URI);
    fixture.config("RestClients.'Microsoft 365 (Partial Graph API)'.Properties.AUTH.secretKey", "1");
    fixture.config("RestClients.'Microsoft 365 (Partial Graph API)'.Properties.scope", "user.read calendars.read");
  }
}
