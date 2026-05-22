package com.axonivy.connector.office365.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.microsoft.graph.MicrosoftGraphUser;

import ch.ivyteam.ivy.environment.AppFixture;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.environment.IvyTest;

@IvyTest(enableWebServer = true)
public class RestIvyTest {

  @BeforeEach
  void beforeEach(AppFixture fixture) {
    // Disable OAuth feature for mock rest service
    fixture.config(GraphTestClient.Config.FEATURES.getProperty(),
        "ch.ivyteam.ivy.rest.client.mapper.JsonFeature");
    fixture.config(GraphTestClient.Config.URL.getProperty(),
        "{ivy.app.baseurl}/api/graphMock");
  }

  @Test
  public void restApi() {
    var response = Ivy.rest().client(GraphTestClient.GRAPH_CLIENT_ID)
        .path("/me").request().get().readEntity(MicrosoftGraphUser.class);
    assertThat(response.getMail()).isEqualTo("reguel.wermelinger@mailinator.com");
  }

}
