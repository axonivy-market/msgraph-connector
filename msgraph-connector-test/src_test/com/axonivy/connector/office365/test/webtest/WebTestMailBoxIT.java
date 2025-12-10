package com.axonivy.connector.office365.test.webtest;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.connector.office365.test.GraphTestClient;
import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.engine.EngineUrl;
import com.axonivy.ivy.webtest.engine.WebAppFixture;
import com.axonivy.ivy.webtest.primeui.PrimeUi;
import com.axonivy.ivy.webtest.primeui.widget.Table;

@IvyWebTest()
public class WebTestMailBoxIT {

  @BeforeEach
  void setup(WebAppFixture fixture) {
    GraphTestClient.configureFixture(fixture);
  }

  @Test
  public void checkMailContent() {
    open(EngineUrl.createProcessUrl("/msgraph-mail-demo/17F262FCF88E26A2/inbox.ivp"));

    Table table = PrimeUi.table(By.id("form:theTable"));
    table.contains("Vincent Biret");
    table.contains("Thanks for the contribution!");

    $(By.id("form:proceed")).shouldBe(visible).click();
  }

}
