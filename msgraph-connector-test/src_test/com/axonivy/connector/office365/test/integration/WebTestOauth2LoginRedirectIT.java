package com.axonivy.connector.office365.test.integration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.connector.office365.test.GraphTestClient;
import com.axonivy.connector.office365.test.integration.helper.SetupHelper;
import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.engine.EngineUrl;
import com.axonivy.ivy.webtest.engine.WebAppFixture;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverConditions;

@IvyWebTest
class WebTestOauth2LoginRedirectIT {
  public static final String WEBTEST_USERNAME = "test.azure.webtest.username";
  public static final String WEBTEST_PASS = "test.azure.webtest.pass";

  private static String USERNAME;
  private static String PASS;

  @BeforeAll
  static void setup() throws IOException {
    var username = System.getProperty(WEBTEST_USERNAME);
    var pass = System.getProperty(WEBTEST_PASS);
    if (StringUtils.isEmpty(username)) {
      try (var in = SetupHelper.class.getResourceAsStream("client.properties")) {
        if (in != null) {
          var props = new Properties();
          props.load(in);
          username = (String) props.get(WEBTEST_USERNAME);
          pass = (String) props.get(WEBTEST_PASS);
        }
      }
    }
    USERNAME = username;
    PASS = pass;
  }

  @Test
  @Disabled
  void redirect(WebAppFixture fixture) {
    SetupHelper.load().entrySet().forEach(prop -> {
      System.out.println("setting up "+prop.getKey());
      fixture.var(prop.getKey(), prop.getValue());
    });
    GraphTestClient.configureFixture(fixture);

    Selenide.open(EngineUrl.createProcessUrl("/msgraph-connector-test/185F3CF9C3910C01/start.ivp"));
    WebDriverConditions.currentFrameUrlContaining("https://login.microsoft.com");
    Selenide.sleep(1000);

    $(By.name("loginfmt")).sendKeys(USERNAME);
    $(By.id("idSIButton9")).click();

    Selenide.sleep(1000);
    $(By.name("passwd")).sendKeys(PASS);
    $(By.id("idSIButton9")).submit();

    Selenide.sleep(1000);
    $(By.id("idSIButton9")).shouldBe(visible).click();

    $(By.className("exception-content")).shouldNotBe(visible);
    $(By.id("form:graphName")).shouldHave(text("alex"));
  }
}