package com.axonivy.connector.office365.test.integration;

import static org.assertj.core.api.Assertions.assertThat;

import javax.ws.rs.core.MediaType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.axonivy.connector.office365.test.integration.helper.SetupHelper;
import com.microsoft.graph.GraphTestClient;

import ch.ivyteam.ivy.application.IApplication;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.environment.IvyTest;

@IvyTest(enableWebServer = true)
class RestOauthIvyTest{

  @BeforeEach
  void beforeEach(IApplication app) {
    SetupHelper.setup();
    GraphTestClient.resetForApp(app);
  }

  @Test
  void callbackOauthRedirect() {
    var restClient = Ivy.rest().client(GraphTestClient.GRAPH_CLIENT_ID);
    String appId = Ivy.var().get("microsoft-connector.appId");
    String secret = Ivy.var().get("microsoft-connector.secretKey");
    String tenantId = Ivy.var().get("microsoft-connector.tenantId");
    var response = restClient.path("/applications")
      .property("AUTH.appId", appId)
      .property("AUTH.secretKey", secret)
      .property("AUTH.useAppPermissions", Boolean.TRUE.toString())
      .property("AUTH.scope", "https://graph.microsoft.com/.default")
      .property("AUTH.baseUri", "https://login.microsoftonline.com/" + tenantId + "/oauth2/v2.0")
      .request(MediaType.APPLICATION_JSON).get();
    assertThat(response.readEntity(String.class))
      .contains("\"uri\":\"http://localhost:8081/oauth2/callback\"")
      .contains("\"publisherDomain\":\"azureivyteam.onmicrosoft.com\"");
  }

}

