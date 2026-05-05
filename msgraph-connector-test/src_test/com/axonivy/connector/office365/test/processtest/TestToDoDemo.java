package com.axonivy.connector.office365.test.processtest;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.axonivy.connector.office365.test.GraphTestClient;
import com.microsoft.graph.MicrosoftGraphTodoTask;

import ch.ivyteam.ivy.bpm.engine.client.BpmClient;
import ch.ivyteam.ivy.bpm.engine.client.ExecutionResult;
import ch.ivyteam.ivy.bpm.engine.client.element.BpmElement;
import ch.ivyteam.ivy.bpm.exec.client.IvyProcessTest;
import ch.ivyteam.ivy.environment.AppFixture;
import ch.ivyteam.ivy.security.ISession;
import msgraph.connector.NewToDo;
import msgraph.todo.demo.ToDoDemo;

@IvyProcessTest(enableWebServer = true)
public class TestToDoDemo {

  @BeforeEach
  void mockService(AppFixture fixture) {
    GraphTestClient.configureFixture(fixture);
  }

  @Test
  public void readList(BpmClient bpmClient, ISession session) {
    bpmClient.mock()
            .element(BpmElement.pid("176F208BF8721ECC-f6"))
            .withNoAction();

    ExecutionResult result = bpmClient.start()
            .process("Demo/ms365ToDo/myToDo.ivp")
            .as().session(session)
            .execute();
    assertThat(result.http().redirectLocation()).isNotEmpty();

    ToDoDemo toDo = result.data().last();
    assertThat(toDo.getTodo()).hasSize(1);
    MicrosoftGraphTodoTask reviewTask = toDo.getTodo().get(0);
    assertThat(reviewTask.getTitle()).startsWith("Digitalize your business");
  }

  @Test
  public void createTask(BpmClient bpmClient, ISession session) {
    mockTaskUi(bpmClient);

    ExecutionResult result = bpmClient.start()
            .process("Demo/ms365ToDo/createTask.ivp")
            .as().session(session)
            .execute();

    ToDoDemo toDo = result.data().last();
    assertThat(toDo.getTodo()).hasSize(1);
    MicrosoftGraphTodoTask reviewTask = toDo.getTodo().get(0);
    assertThat(reviewTask.getTitle()).isEqualTo("Test task");
  }

  private void mockTaskUi(BpmClient bpmClient) {
    NewToDo task = new NewToDo();
    task.setTitle("Test task");
    task.setContent("This is a test task");
    bpmClient.mock()
            .element(BpmElement.pid("176F208BF8721ECC-f11"))
            .with((in, out) -> {
              try {
                in.set("task", task);
              } catch (NoSuchFieldException ex) {
              }
            });
  }
}
