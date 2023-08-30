package com.microsoft.graph;

import java.util.List;

import ch.ivyteam.ivy.environment.AppFixture;
import ch.ivyteam.ivy.rest.client.mapper.JsonFeature;
import ch.ivyteam.ivy.rest.client.security.CsrfHeaderFeature;

public class GraphFixture {

  public static void apply(AppFixture fixture) {
    var client = "RestClients.'Microsoft 365 (Partial Graph API)'";
    fixture.config(client+".Url", GraphServiceMock.URI);
    fixture.config(client+".Features", List.of(JsonFeature.class.getName(), CsrfHeaderFeature.class.getName()));
  }

}