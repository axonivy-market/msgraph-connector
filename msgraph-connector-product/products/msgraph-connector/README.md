# msgraph-connector

The msgraph-connector integrates Microsoft 365 services through Microsoft Graph into Axon Ivy processes. It gives you ready-made callable subprocesses and demo workflows for mail, calendar, files, SharePoint, Teams, and ToDo so you can build practical automations quickly.

## Key features

- Send emails and manage recipients directly from your Axon Ivy workflows.
- Read upcoming calendar events and create new meetings from process logic.
- Upload files to SharePoint, load followed sites, and browse recent files.
- Read Teams chat messages and trigger Teams-based task notifications.
- Create, list, and review ToDo items with ready-made dialogs and subprocesses.
- Configure Azure App authentication for delegated or app permissions with a shared rest client setup.

## Demo

Check the demo implementations for Mail, Calendar, ToDo, SharePoint, and Teams. They show how the connector behaves in real process steps and help you adapt the same patterns to your own solution.

Explore [Microsoft Graph overview](https://learn.microsoft.com/en-us/graph/overview) and the [API reference](https://learn.microsoft.com/en-us/graph/api/overview?view=graph-rest-1.0) when you extend the demos.

### Demo Workflows

#### Mail Demo (msgraph-mail-demo)

##### Inbox
1. Launch the Inbox demo from the demo menu.
2. The demo shows recent emails from your mailbox in a dialog.
3. Review sender, subject, and preview text, then close the dialog.

##### Write Mail
1. Launch the Write Mail demo from the demo menu.
2. Enter recipients, subject, and message body in the compose dialog.
3. Add or remove recipients as needed.
4. Send the mail and confirm the message is created.

#### Calendar Demo (msgraph-calendar-demo)

##### Read Calendar
1. Launch the Read Calendar demo from the demo menu.
2. The demo shows upcoming events from your calendar in a dialog.
3. Review the event subject and time details, then close the dialog.

##### Create Meeting
1. Launch the Create Meeting demo from the demo menu.
2. Fill in the event subject, description, and participants.
3. Create the event and review the updated calendar view.
4. Check your calendar to confirm the meeting was added.

#### ToDo Demo (msgraph-todo-demo)

##### Create Task
1. Launch the Create Task demo from the demo menu.
2. Enter the task title and content in the dialog.
3. Create the task and confirm it appears in the list.

##### My ToDo
1. Launch the My ToDo demo from the demo menu.
2. The demo shows your current tasks in a dialog.
3. Inspect a task's title and content, then close the dialog.

#### SharePoint Demo (msgraph-sharepoint-demo)

##### My Sites
1. Launch the My Sites demo from the demo menu.
2. The demo lists the SharePoint sites you follow.
3. Review the site names and IDs for integration.

##### Recent Files
1. Launch the Recent Files demo from the demo menu.
2. The demo lists recently used files for quick access.
3. Review the file names and use them as a reference for your own workflow.

##### Upload
1. Launch the Upload demo from the demo menu.
2. Create a sample file and choose the target site when needed.
3. Upload the file and confirm the transfer completed.

#### Teams Demo (msgraph-teams-demo)

##### Read Messages
1. Launch the Read Messages demo from the demo menu.
2. The demo loads recent Teams chat messages.
3. Review the message list and close the view when you are done.

##### MS Teams Task Notification
1. Launch the MS Teams Task Notification demo from the demo menu.
2. Trigger the task notification flow and simulate the Azure AD user if needed.
3. Check your Teams client to confirm the notification arrived.

##### Teams Web
1. Launch the Teams Web demo from the demo menu.
2. Select a chat and browse the web-based message view.
3. Review the conversation and continue with your own test scenario.

## Setup

- **Roles:** Everybody (configured in config/roles.xml)
- **OpenAPI:** SpecUrl: https://graphexplorerapi.azurewebsites.net/openapi?tags=me.user,me.calendar,users.calendar,me.message,me.Actions,me.todo,me.site,sites.Actions,me.drive,me.chat,chats.chat,chats.chatMessage&openApiVersion=3&graphVersion=v1.0&format=yaml&style=PowerShell
  Namespace: com.microsoft.graph

### Variables

```yaml
@variables.yaml@
```

> [!NOTE]
> The variable format changed from version 13.
> For example, `microsoft-connector` was renamed to `microsoftConnector`, and `teams-notification` was renamed to `teamsNotification`.

### Azure App

1. Register an application in Azure as described in Microsoft's Java Tutorial https://docs.microsoft.com/en-us/graph/tutorials/java?tutorial-step=2
2. Open `Overview` and copy the Application (client) ID into `appId` inside the `microsoftConnector` section.
3. Open `Authentication` in the Azure App menu.
   3.1. Add a `Redirect URI` in the `Web` section.
      - Axon Ivy uses a callback URI in the form `{scheme}://{host}:{port}/oauth2/callback`. Register this URI in the Azure App.
      - For Axon Ivy Designer, you can use `http://localhost:8081/oauth2/callback`.
      - For Axon Ivy Engine, use the full engine URI plus `/oauth2/callback`, for example `https://my.workflows.ch/oauth2/callback`.

      ![Set redirect URI](../../doc/img/azure_authCallback.png)

4. Open `Certificate & secrets` in the Azure App menu.
   4.1. Create a new secret by selecting `New client secret` and choose any validity period.

      ![Create new secret](../../doc/img/azure_createSecret.png)

   4.2. Copy the generated secret value into `secretKey` inside the `microsoftConnector` section.

      ![Copy secret](../../doc/img/azure_copySecret.png)

5. Open `API Permissions` in the Azure App menu.
   - Add permissions via `Add a permission` > `Microsoft Graph` > `Delegated permissions`.
   - Grant every permission listed in the `permissions` block of your `variables.yaml` file.

      ![Add permissions](../../doc/img/azure_addPermission.png)

6. Start any process that connects with Microsoft 365.

## Components

### Callable Subprocesses

#### msCalendar.p.json

- **Signature**: upcomingEvents() -> myEvents: java.util.List<com.microsoft.graph.MicrosoftGraphEvent>
    - Input: (none)
    - Result:
        - `myEvents` (java.util.List<com.microsoft.graph.MicrosoftGraphEvent>) - List with upcoming events from your calendar

- **Signature**: createMeeting(msgraph.connector.NewEvent evt) -> meeting: com.microsoft.graph.MicrosoftGraphEvent
    - Input:
        - `evt` (msgraph.connector.NewEvent) - The new event that should be created in your calendar
    - Result:
        - `meeting` (com.microsoft.graph.MicrosoftGraphEvent) - The event that was created in your calendar

#### msChat.p.json

- **Signature**: recentMessages() -> messages: java.util.List<com.microsoft.graph.MicrosoftGraphChatMessage>
    - Input: (none)
    - Result:
        - `messages` (java.util.List<com.microsoft.graph.MicrosoftGraphChatMessage>)

#### msFiles.p.json

- **Signature**: uploadFile(java.io.File file, String siteId) -> (none)
    - Input:
        - `file` (java.io.File) - file to upload unto SharePoint site
        - `siteId` (String) - optional: GUID of a site, if empty the first site that the user follows will be used
    - Result: (none)

- **Signature**: uploadFile(java.io.File file) -> (none)
    - Input:
        - `file` (java.io.File) - file to upload unto SharePoint site
    - Result: (none)

- **Signature**: myRecentFiles() -> items: java.util.List<com.microsoft.graph.MicrosoftGraphDriveItem>
    - Input: (none)
    - Result:
        - `items` (java.util.List<com.microsoft.graph.MicrosoftGraphDriveItem>) - recently used items

#### msMail.p.json

- **Signature**: writeMail(msgraph.connector.NewMail mail) -> message: com.microsoft.graph.MicrosoftGraphMessage
    - Input:
        - `mail` (msgraph.connector.NewMail) - The mail to send
    - Result:
        - `message` (com.microsoft.graph.MicrosoftGraphMessage) - The message that was sent

#### msToDo.p.json

- **Signature**: allTasks() -> tasks: java.util.List<com.microsoft.graph.MicrosoftGraphTodoTask>
    - Input: (none)
    - Result:
        - `tasks` (java.util.List<com.microsoft.graph.MicrosoftGraphTodoTask>) - List with all todo tasks

- **Signature**: createNewTask(msgraph.connector.NewToDo task) -> todo: com.microsoft.graph.MicrosoftGraphTodoTask
    - Input:
        - `task` (msgraph.connector.NewToDo) - The new todo task to create
    - Result:
        - `todo` (com.microsoft.graph.MicrosoftGraphTodoTask) - The created todo task

### Dialog Components

#### WriteMail
- **Namespace:** msgraph.mail.demo.WriteMail
- **Component type:** UI dialog
- **Fields:** - (none)
- **Purpose:** Write a mail on your behalf, add or remove recipients, and send the message.

#### Mails
- **Namespace:** msgraph.mail.demo.Mails
- **Component type:** UI dialog
- **Fields:** - (none)
- **Purpose:** Show recent mailbox messages in a read-only list.

#### Events
- **Namespace:** msgraph.calendar.demo.Events
- **Component type:** UI dialog
- **Fields:**
   - `events` (java.util.List<com.microsoft.graph.MicrosoftGraphEvent>) — (not documented in source)
- **Purpose:** Show upcoming calendar events with subject and time details.

#### CreateEvent
- **Namespace:** msgraph.calendar.demo.CreateEvent
- **Component type:** UI dialog
- **Fields:** - (none)
- **Purpose:** Create a new event with subject, description, and participants.

#### Tasks
- **Namespace:** msgraph.todo.demo.Tasks
- **Component type:** UI dialog
- **Fields:**
   - `todo` (java.util.List<com.microsoft.graph.MicrosoftGraphTodoTask>) — (not documented in source)
- **Purpose:** Show the current ToDo list in a clean, read-only overview.

#### CreateTask
- **Namespace:** msgraph.todo.demo.CreateTask
- **Component type:** UI dialog
- **Fields:** - (none)
- **Purpose:** Capture a title and content for a new task and create it.

#### TeamsWeb
- **Namespace:** msgraph.teams.demo.TeamsWeb
- **Component type:** UI dialog
- **Fields:** - (none)
- **Purpose:** Browse Teams chat messages in a web-based view.

### Web Services

SpecUrl: https://graphexplorerapi.azurewebsites.net/openapi?tags=me.user,me.calendar,users.calendar,me.message,me.Actions,me.todo,me.site,sites.Actions,me.drive,me.chat,chats.chat,chats.chatMessage&openApiVersion=3&graphVersion=v1.0&format=yaml&style=PowerShell

Namespace: com.microsoft.graph

### Maven Artifacts

1. msgraph-connector

```xml
<dependency>
  <groupId>com.axonivy.connector.office365</groupId>
  <artifactId>msgraph-connector</artifactId>
  <type>iar</type>
</dependency>
```

2. msgraph-mail-demo

```xml
<dependency>
  <groupId>com.axonivy.connector.office365</groupId>
  <artifactId>msgraph-mail-demo</artifactId>
  <type>iar</type>
</dependency>
```

3. msgraph-calendar-demo

```xml
<dependency>
  <groupId>com.axonivy.connector.office365</groupId>
  <artifactId>msgraph-calendar-demo</artifactId>
  <type>iar</type>
</dependency>
```

4. msgraph-todo-demo

```xml
<dependency>
  <groupId>com.axonivy.connector.office365</groupId>
  <artifactId>msgraph-todo-demo</artifactId>
  <type>iar</type>
</dependency>
```
