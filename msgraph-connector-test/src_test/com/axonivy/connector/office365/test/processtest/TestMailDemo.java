package com.axonivy.connector.office365.test.processtest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.axonivy.connector.office365.test.GraphTestClient;

import ch.ivyteam.ivy.application.IApplication;
import ch.ivyteam.ivy.bpm.engine.client.BpmClient;
import ch.ivyteam.ivy.bpm.engine.client.ExecutionResult;
import ch.ivyteam.ivy.bpm.engine.client.element.BpmElement;
import ch.ivyteam.ivy.bpm.exec.client.IvyProcessTest;
import ch.ivyteam.ivy.environment.AppFixture;
import ch.ivyteam.ivy.security.ISession;
import msgraph.connector.NewMail;

@IvyProcessTest(enableWebServer = true)
class TestMailDemo {

  @BeforeEach
  void mockService(AppFixture fixture) {
    GraphTestClient.configureFixture(fixture);
  }

  @Test
  void writeMail(BpmClient bpmClient, ISession session) {
    mockMailUi(bpmClient);

    ExecutionResult result = bpmClient.start()
            .process("Demo/ms365Mail/writeMail.ivp")
            .as().session(session)
            .execute();

    assertThat(result.bpmError()).isNull();
  }

  private void mockMailUi(BpmClient bpmClient) {
    NewMail mail = new NewMail();
    mail.setReceivers(List.of("me@mailinator.com"));
    mail.setSubject("Meet for Lunch?");
    mail.setBody("the new cafeteria is open");
    bpmClient.mock()
            .element(BpmElement.pid("17F262FCF88E26A2-f14"))
            .with((in, out) -> {
              try {
                in.set("mail", mail);
              } catch (NoSuchFieldException ex) {
              }
            });
  }

}
