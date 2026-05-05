package com.axonivy.connector.office365.test.processtest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Deque;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.axonivy.connector.office365.test.GraphTestClient;
import com.axonivy.wf.ext.notification.NewTaskAssignmentListener;
import com.fasterxml.jackson.databind.JsonNode;
import com.microsoft.graph.GraphServiceMock;

import ch.ivyteam.ivy.bpm.engine.client.BpmClient;
import ch.ivyteam.ivy.bpm.engine.client.ExecutionResult;
import ch.ivyteam.ivy.bpm.exec.client.IvyProcessTest;
import ch.ivyteam.ivy.environment.AppFixture;
import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.ivy.workflow.IWorkflowManager;
import msgraph.teams.notification.TeamsNotifier;

@IvyProcessTest(enableWebServer = true)
class TestTeamsNotificationDemo {

  @BeforeEach
  void mockService(AppFixture fixture) {
    GraphTestClient.configureFixture(fixture);
  }

  @Test
  void createTask(BpmClient bpmClient, ISession session) {
    IWorkflowManager wfMan = IWorkflowManager.instance();
    NewTaskAssignmentListener notify = new TeamsNotifier();
    wfMan.addWorkflowListener(notify);
    try {
      ExecutionResult result = bpmClient.start()
              .process("ms365teams/teamsNotification.ivp")
              .as().session(session)
              .execute();

      assertThat(result.bpmError()).isNull();

      Deque<JsonNode> chats = GraphServiceMock.CHATS;
      assertThat(chats).isNotEmpty();
      JsonNode chat = chats.getLast();
      assertThat(chat.get("chatType").asText()).isEqualTo("oneOnOne");

      JsonNode message = GraphServiceMock.MESSAGES.getLast();
      assertThat(message).isNotNull();
      assertThat(message.get("body").get("content").asText())
      .contains("<h1>New Task");
    }
    finally {
      wfMan.removeWorkflowListener(notify);
    }

  }

}
