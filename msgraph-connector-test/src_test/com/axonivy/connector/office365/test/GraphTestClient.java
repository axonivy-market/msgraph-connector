package com.axonivy.connector.office365.test;

import java.util.List;
import java.util.Map;

import com.axonivy.ivy.webtest.engine.WebAppFixture;
import com.microsoft.graph.GraphAuthMock;
import com.microsoft.graph.GraphServiceMock;

import ch.ivyteam.ivy.environment.AppFixture;
import ch.ivyteam.ivy.rest.client.mapper.JsonFeature;
import ch.ivyteam.ivy.rest.client.security.CsrfHeaderFeature;

public class GraphTestClient {

	private static final String REST_CLIENT_GRAPH_CLIENT = "RestClients.Microsoft365PartialGraphAPI.";
	public static final String GRAPH_CLIENT_ID = "Microsoft365PartialGraphAPI";
	public static final Map<String, String> REST_PROPERTIES = Map.of(
			Config.URL.property, GraphServiceMock.URI,
			Config.BASE_URI.property, GraphAuthMock.URI,
			Config.APP_ID.property, "notmyid",
			Config.SECRET_KEY.property, "1",
			Config.SCOPE.property, "user.read calendars.read");

	public static void configureFixture(AppFixture fixture) {
		REST_PROPERTIES.entrySet().forEach(entry -> fixture.config(entry.getKey(), entry.getValue()));
		fixture.config(Config.FEATURES.property,
				List.of(CsrfHeaderFeature.class.getName(), JsonFeature.class.getName()));
	}

	public static void configureFixture(WebAppFixture fixture) {
		REST_PROPERTIES.entrySet().forEach(entry -> fixture.config(entry.getKey(), entry.getValue()));
		fixture.config(Config.FEATURES.property,
				List.of(CsrfHeaderFeature.class.getName(), JsonFeature.class.getName()));
	}

	public enum Config {
		FEATURES(REST_CLIENT_GRAPH_CLIENT.concat("Features")),
		URL(REST_CLIENT_GRAPH_CLIENT.concat("Url")),
		BASE_URI(REST_CLIENT_GRAPH_CLIENT.concat("Properties.AUTH.baseUri")),
		APP_ID(REST_CLIENT_GRAPH_CLIENT.concat("Properties.AUTH.appId")),
		SECRET_KEY(REST_CLIENT_GRAPH_CLIENT.concat("Properties.AUTH.secretKey")),
		SCOPE(REST_CLIENT_GRAPH_CLIENT.concat("Properties.scope"));

		private String property;

		Config(String property) {
			this.property = property;
		}

		public String getProperty() {
			return property;
		}
	}
}
