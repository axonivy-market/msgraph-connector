package com.axonivy.connector.office365.test.integration.helper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.ivy.environment.Ivy;

public class SetupHelper {
  private static final String APP_ID_PROP = "test.azure.app.id";
  private static final String SECRET_KEY_PROP = "test.azure.app.secretKey";
  private static final String TENANT_ID_PROP = "test.azure.app.tenantId";

  public static void setup() {
    load().entrySet().forEach(prop -> {
      Ivy.var().set(prop.getKey(), prop.getValue());
    });
  }

  public static Map<String, String> load() {
    Map<String, String> loaded = new HashMap<>();

    var appId = new AtomicReference<>(System.getProperty(APP_ID_PROP));
    var secretKey = new AtomicReference<>(System.getProperty(SECRET_KEY_PROP));
    var tenantId = new AtomicReference<>(System.getProperty(TENANT_ID_PROP));

    if (StringUtils.isEmpty(secretKey.get())) {
      consume(props -> {
        appId.set(props.getProperty(APP_ID_PROP));
        secretKey.set(props.getProperty(SECRET_KEY_PROP));
        tenantId.set(props.getProperty(TENANT_ID_PROP));
      });
    }

    loaded.put("microsoftConnector.appId", appId.get());
    loaded.put("microsoftConnector.secretKey", secretKey.get());
    loaded.put("microsoftConnector.tenantId", tenantId.get());
    return loaded;
  }

  public static void consume(Consumer<Properties> reader) {
    String resource = "client.properties";
    try (var in = SetupHelper.class.getResourceAsStream(resource)) {
      System.out.println("trying to load secrets from "+resource);
      if (in != null) {
        var props = new Properties();
        props.load(in);
        if (props.isEmpty()) {
          System.err.println("no properties defined in "+resource);
          return;
        }
        reader.accept(props);
      }
    } catch (IOException ex) {
      throw new RuntimeException("Failed to load client.props", ex);
    }
  }
}
