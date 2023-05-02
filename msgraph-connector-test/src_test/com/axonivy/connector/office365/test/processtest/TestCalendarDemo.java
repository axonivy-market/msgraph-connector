package com.axonivy.connector.office365.test.processtest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.microsoft.graph.GraphTestClient;
import com.microsoft.graph.MicrosoftGraphEvent;

import ch.ivyteam.ivy.application.IApplication;
import ch.ivyteam.ivy.bpm.engine.client.BpmClient;
import ch.ivyteam.ivy.bpm.engine.client.ExecutionResult;
import ch.ivyteam.ivy.bpm.engine.client.element.BpmElement;
import ch.ivyteam.ivy.bpm.exec.client.IvyProcessTest;
import ch.ivyteam.ivy.security.ISession;
import msgraph.calendar.demo.CalendarDemo;
import msgraph.connector.NewEvent;

@IvyProcessTest
class TestCalendarDemo {

  @BeforeEach
  void mockService(IApplication app) {
    GraphTestClient.mockForApp(app);
  }

  @Test
  void readPersonal(BpmClient bpmClient, ISession session) {
    bpmClient.mock()
            .element(BpmElement.pid("176D21535A8FEE20-f20"))
            .withNoAction();

    ExecutionResult result = bpmClient.start()
            .process("Demo/ms365Calendar/readCalendar.ivp")
            .as().session(session)
            .execute();

    CalendarDemo cal = result.data().last();
    assertThat(cal.getEvents()).hasSize(1);
    MicrosoftGraphEvent wfUiReview = cal.getEvents().get(0);
    assertThat(wfUiReview.getSubject())
            .contains("Workflow UI: Review");
  }

  @Test
  void createMeeting(BpmClient bpmClient, ISession session) {
    mockMeetingUi(bpmClient);

    ExecutionResult result = bpmClient.start()
            .process("Demo/ms365Calendar/meet.ivp")
            .as().session(session)
            .execute();

    CalendarDemo cal = result.data().last();
    assertThat(cal.getEvents()).hasSize(1);
    MicrosoftGraphEvent wfUiReview = cal.getEvents().get(0);
    assertThat(wfUiReview.getSubject())
            .isEqualTo("Define digitalization roadmap");
  }

  private void mockMeetingUi(BpmClient bpmClient) {
    NewEvent meet = new NewEvent();
    meet.setParticipants(List.of("me@mailinator.com"));
    meet.setSubject("Define digitalization roadmap");
    meet.setDescription("go for more");
    bpmClient.mock()
            .element(BpmElement.pid("176D21535A8FEE20-f13"))
            .with((in, out) -> {
              try {
                out.set("newEvent", meet);
              } catch (NoSuchFieldException ex) {
              }
            });

    BpmElement calViewer = BpmElement.pid("176D21535A8FEE20-f11");
    bpmClient.mock().element(calViewer).withNoAction();
  }
}
