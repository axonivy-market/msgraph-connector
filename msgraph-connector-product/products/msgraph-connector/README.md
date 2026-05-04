# Microsoft 365 Connector
Axon Ivy’s [Microsoft 365](https://docs.microsoft.com/en-us/graph/overview)
connector helps you to integrate
Microsoft Graph features into your process application. The
Microsoft Graph builds on Microsoft 365 APIs and allows developers to integrate
their services with Microsoft products, including Windows, Microsoft 365, and
Azure. 

This connector:

- enables fast integration into any Microsoft 365 product easily.
- offers a single endpoint to the Microsoft world
- supports easy access to data from services like Excel, Microsoft Bookings,
  Microsoft Search, Microsoft Teams, OneDrive, OneNote, Outlook/Exchange,
  Planner, SharePoint, Workplace Analytics, Advanced Threat Analytics, Advanced
  Threat Protection, Azure Active Directory, Identity Manager, Intune and much
  more...

Microsoft Graph is the gateway to data and intelligence in Microsoft 365. It
provides a unified programmability model that you can use to access the
tremendous amount of data in Microsoft 365, Windows 10, and Enterprise Mobility + Security. 

![ms-graph](https://docs.microsoft.com/en-us/graph/images/edugraph.png)

### Key features

- Integrate Microsoft 365 (Mail, Calendar, Teams, OneDrive, SharePoint) through a single connector.
- Send emails and create calendar events directly from your Axon Ivy processes.
- Upload and manage files on SharePoint/OneDrive; access recent files from your processes.
- Read and post Microsoft Teams messages and send Teams notifications.
- Create and manage Microsoft To Do tasks from within processes.
- Secure Azure AD OAuth2 authentication with flexible app or user flows; includes OpenAPI support for advanced API access.

## Demo

Check the demo implementations we have prepared for the various services from Microsoft:

[Microsoft Calendar](https://market.axonivy.com/msgraph-calendar) - this connector integrates Microsoft Outlook features into your process application.

[Microsoft Excel](https://market.axonivy.com/excel-connector) - depending on which study you take as a reference, 50-75% of all companies work with Excel - a good reason to provide a connector for process automation. 

[Microsoft Outlook](https://market.axonivy.com/msgraph-mail) and [Microsoft Teams](https://market.axonivy.com/msgraph-chat) - can also easily be integrated.

Check [Microsoft ToDo](https://market.axonivy.com/msgraph-todo) if you would like to integrate the Microsoft Task Manager.

### Demo workflows

#### Calendar (msgraph-calendar-demo)

- Read upcoming events: run the `readCalendar` demo (calls `msCalendar:upcomingEvents()`), then open the "Events" dialog to browse results.
- Create a meeting: run the `meet` demo and use the "CreateEvent" dialog to fill details (calls `msCalendar:createMeeting(NewEvent)`).

#### Mail (msgraph-mail-demo)

- Send mail: open the `writeMail` demo, compose your message and send (calls `msMail:writeMail(NewMail)`).
- Browse inbox: run the `inbox` demo to list messages in the "Mails" dialog.

#### Files / SharePoint (msgraph-sharepoint-demo)

- Upload a file: run the `upload` demo to create a sample file and upload it (calls `msFiles:uploadFile(File)`).
- Recent files: run the `recentFiles` demo to list recently used items (calls `msFiles:myRecentFiles()`).

#### To Do (msgraph-todo-demo)

- List tasks: run the `myToDo` demo to view your tasks (calls `msToDo:allTasks()`).
- Create a task: run the `createTask` demo and use the "CreateTask" dialog (calls `msToDo:createNewTask(NewToDo)`).

#### Teams / Chat (msgraph-teams-demo)

- Read recent messages: run the `readMessages` demo to fetch recent chat messages (calls `msChat:recentMessages()`).
- Teams web demo: run `teamsWeb` to explore the web-integrated view.


## Setup

- **Roles:** `Everybody` (configured in `config/roles.xml`).

- **OpenAPI:** the connector exposes an OpenAPI specification. External spec URL (from `config/rest-clients.yaml`):

    https://graphexplorerapi.azurewebsites.net/openapi?tags=me.user,me.calendar,users.calendar,me.message,me.Actions,me.todo,me.site,sites.Actions,me.drive,me.chat,chats.chat,chats.chatMessage&openApiVersion=3&graphVersion=v1.0&format=yaml&style=PowerShell

- **Configuration variables:**

```
@variables.yaml@
```

## Components

### Callable Sub Processes

The connector exposes the following callable sub processes (grouped by process file):

#### msMail.p.json

- `writeMail(mail: msgraph.connector.NewMail) -> message: com.microsoft.graph.MicrosoftGraphMessage`
    - Input:
        - `mail` (msgraph.connector.NewMail) — The mail to send
    - Result:
        - `message` (com.microsoft.graph.MicrosoftGraphMessage) — The message that was sent

#### msCalendar.p.json

- `upcomingEvents() -> myEvents: java.util.List<com.microsoft.graph.MicrosoftGraphEvent>`
    - Result:
        - `myEvents` (java.util.List<com.microsoft.graph.MicrosoftGraphEvent>) — List with upcoming events from the calendar

- `createMeeting(evt: msgraph.connector.NewEvent) -> meeting: com.microsoft.graph.MicrosoftGraphEvent`
    - Input:
        - `evt` (msgraph.connector.NewEvent) — The new event to create
    - Result:
        - `meeting` (com.microsoft.graph.MicrosoftGraphEvent) — The event that was created in the calendar

#### msChat.p.json

- `recentMessages() -> messages: java.util.List<com.microsoft.graph.MicrosoftGraphChatMessage)`
    - Result:
        - `messages` (java.util.List<com.microsoft.graph.MicrosoftGraphChatMessage>)

#### msFiles.p.json

- `uploadFile(file: java.io.File, siteId: String)`
    - Input:
        - `file` (java.io.File) — file to upload to SharePoint
        - `siteId` (String) — optional: GUID of a site
- `uploadFile(file: java.io.File)`
    - Input:
        - `file` (java.io.File) — file to upload
- `myRecentFiles() -> items: java.util.List<com.microsoft.graph.MicrosoftGraphDriveItem>`
    - Result:
        - `items` (java.util.List<com.microsoft.graph.MicrosoftGraphDriveItem>) — recently used items

#### msToDo.p.json

- `allTasks() -> tasks: java.util.List<com.microsoft.graph.MicrosoftGraphTodoTask)`
    - Result:
        - `tasks` (java.util.List<com.microsoft.graph.MicrosoftGraphTodoTask>) — List with all todo tasks

- `createNewTask(task: msgraph.connector.NewToDo) -> todo: com.microsoft.graph.MicrosoftGraphTodoTask`
    - Input:
        - `task` (msgraph.connector.NewToDo) — The new todo task to create
    - Result:
        - `todo` (com.microsoft.graph.MicrosoftGraphTodoTask) — The created todo task

### Form Components

No form components detected in the main module's `src_hd` directory.

### Maven artifacts

1. maven-import — `com.axonivy.connector.office365:@artifact.id@` (version: `13.2.1`, type: `iar`)

```xml
<dependency>
    <groupId>com.axonivy.connector.office365</groupId>
    <artifactId>@artifact.id@</artifactId>
    <version>13.2.1</version>
    <type>iar</type>
</dependency>
```

2. maven-dependency — `com.axonivy.connector.office365:@dependent.product@` (version: `13.2.1`, type: `iar`)

```xml
<dependency>
    <groupId>com.axonivy.connector.office365</groupId>
    <artifactId>@dependent.product@</artifactId>
    <version>13.2.1</version>
    <type>iar</type>
</dependency>
```


