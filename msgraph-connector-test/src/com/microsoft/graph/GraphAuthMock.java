package com.microsoft.graph;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;

import io.swagger.v3.oas.annotations.Hidden;

@Path(GraphAuthMock.PATH_SUFFIX)
@PermitAll
@Hidden
public class GraphAuthMock
{
  static final String PATH_SUFFIX = "graphAuthMock";
  // URI where this mock can be reached: to be referenced in tests that use it!
  public static final String URI = "{ivy.app.baseurl}/api/" + PATH_SUFFIX;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("authorize")
  public Response auth()
  {
    return Response.status(301)
            .build();
  }

  @POST
  @Path("token")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response token()
  {
    return Response.status(200)
      .entity(load("json/accessToken.json"))
      .build();
  }

  private static String load(String path)
  {
    try(InputStream is = GraphAuthMock.class.getResourceAsStream(path))
    {
      return IOUtils.toString(is, StandardCharsets.UTF_8);
    }
    catch (IOException ex)
    {
      throw new RuntimeException("Failed to read resource: "+path);
    }
  }
}
