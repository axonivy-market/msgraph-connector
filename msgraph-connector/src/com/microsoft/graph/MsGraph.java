package com.microsoft.graph;

import javax.ws.rs.client.WebTarget;

import ch.ivyteam.ivy.environment.Ivy;

public class MsGraph {
  private static final String REST_CLIENT_NAME = "Microsoft365";
  private final WebTarget client;

  public MsGraph() {
    client = Ivy.rest().client(REST_CLIENT_NAME);
  }

  public WebTarget client() {
    return client;
  }

}
