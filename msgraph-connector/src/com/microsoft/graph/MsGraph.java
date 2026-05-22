package com.microsoft.graph;

import javax.ws.rs.client.WebTarget;

import ch.ivyteam.ivy.environment.Ivy;

public class MsGraph {

  private final WebTarget client;

  public MsGraph() {
    client = Ivy.rest().client("Microsoft365PartialGraphAPI");
  }

  public WebTarget client() {
    return client;
  }

}
