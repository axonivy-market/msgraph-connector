# Callable Sub Connector Starts

## ./msgraph-connector/processes/msCalendar.p.json
- Signature: upcomingEvents
  Input: none
  Result: myEvents: java.util.List<com.microsoft.graph.MicrosoftGraphEvent>

- Signature: createMeeting
  Input: evt: msgraph.connector.NewEvent
  Result: meeting: com.microsoft.graph.MicrosoftGraphEvent


## ./msgraph-connector/processes/msChat.p.json
- Signature: recentMessages
  Input: none
  Result: messages: java.util.List<com.microsoft.graph.MicrosoftGraphChatMessage>


## ./msgraph-connector/processes/msFiles.p.json
- Signature: uploadFile
  Input: file: java.io.File, siteId: String
  Result: none

- Signature: uploadFile
  Input: file: java.io.File
  Result: none

- Signature: myRecentFiles
  Input: none
  Result: items: java.util.List<com.microsoft.graph.MicrosoftGraphDriveItem>


## ./msgraph-connector/processes/msMail.p.json
- Signature: writeMail
  Input: mail: msgraph.connector.NewMail
  Result: message: com.microsoft.graph.MicrosoftGraphMessage


## ./msgraph-connector/processes/msToDo.p.json
- Signature: allTasks
  Input: none
  Result: tasks: java.util.List<com.microsoft.graph.MicrosoftGraphTodoTask>

- Signature: createNewTask
  Input: task: msgraph.connector.NewToDo
  Result: todo: com.microsoft.graph.MicrosoftGraphTodoTask


