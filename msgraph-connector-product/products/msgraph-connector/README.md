# Microsoft Graph Connector

The Microsoft Graph Connector from Axon Ivy helps you connect your business processes with Microsoft 365 services such as Mail, Calendar, To Do, SharePoint files, and Teams chat. You can reuse ready-to-run connector processes to automate common collaboration tasks with less custom integration effort.

[Microsoft Graph overview](https://learn.microsoft.com/en-us/graph/overview)

[Microsoft Graph API reference](https://learn.microsoft.com/en-us/graph/api/overview?view=graph-rest-1.0)

### Key features

- Automate everyday Microsoft 365 actions directly from your process workflows, so teams can work faster with less manual handoff.
- Send emails with structured recipients, subject, and body data from your process context.
- Read upcoming calendar events and create meetings programmatically for scheduling workflows.
- Sync tasks with Microsoft To Do by reading all tasks and creating new tasks from process data.
- Work with files and sites by uploading documents and listing recently used files in Microsoft 365.
- Read recent Teams chat messages and build notification-enabled collaboration scenarios.

## Demo

Check the demo implementations provided in this repository:

- [Graph Calendar](../msgraph-calendar/README.md)
- [Graph Mail](../msgraph-mail/README.md)
- [Graph ToDo](../msgraph-todo/README.md)
- [Graph Chat](../msgraph-chat/README.md)

### Demo workflows

#### Mail Demo (msgraph-mail-demo)

##### Inbox

1. Launch the Inbox demo process.
2. The process authenticates against Microsoft Graph and opens the mail dialog.
3. Review the listed messages and validate that mailbox data is available.

##### Write Mail

1. Launch the Write Mail demo process.
2. Fill in recipients, subject, and message content in the dialog.
3. Submit the form to send the message through Microsoft Graph.
4. Confirm that the message was processed successfully.

#### Calendar Demo (msgraph-calendar-demo)

##### Read Calendar

1. Launch the Read Calendar demo process.
2. The process loads upcoming events from your Microsoft 365 calendar.
3. Review the event list in the dialog to verify calendar connectivity.

##### Meet

1. Launch the Meet demo process.
2. Enter meeting details in the create-event dialog.
3. Submit the form to create a new calendar event.
4. Verify the created event in the resulting event list.

#### ToDo Demo (msgraph-todo-demo)

##### My ToDo

1. Launch the My ToDo demo process.
2. The process reads your current Microsoft To Do tasks.
3. Review the returned tasks in the dialog.

##### Create Task

1. Launch the Create Task demo process.
2. Enter title and content for the new task.
3. Submit the dialog to create the task in Microsoft To Do.
4. Verify that the created task is returned in the result.

#### SharePoint Demo (msgraph-sharepoint-demo)

##### My Sites

1. Launch the My Sites demo process.
2. The process reads followed SharePoint sites from Microsoft 365.
3. Check the logged output to confirm site access.

##### Upload

1. Launch the Upload demo process.
2. A sample file is prepared automatically.
3. The file is uploaded to SharePoint through the connector process.
4. Confirm upload completion in the process result.

##### Recent Files

1. Launch the Recent Files demo process.
2. The process reads recently used drive items.
3. Check the output to verify retrieved file names.

#### Teams Demo (msgraph-teams-demo)

##### Read Messages

1. Launch the Read Messages demo process.
2. The process requests recent chat messages from your Teams chats.
3. Review the output to confirm that message content is returned.

##### Teams Web

1. Launch the Teams Web demo process.
2. The process authenticates the current user.
3. The Teams dialog opens for interactive validation.

##### MS Teams Task Notification

1. Launch the MS Teams Task Notification demo process.
2. The demo simulates a user mapping for local testing.
3. A sample task event is triggered to demonstrate notification flow.
4. Verify that the Teams notification scenario is executed.

## Setup

- **Roles:** Everybody (configured in config/roles.xml)
- **OpenAPI:** Spec URL: https://graphexplorerapi.azurewebsites.net/openapi?tags=me.user,me.calendar,users.calendar,me.message,me.Actions,me.todo,me.site,sites.Actions,me.drive,me.chat,chats.chat,chats.chatMessage&openApiVersion=3&graphVersion=v1.0&format=yaml&style=PowerShell, Namespace: com.microsoft.graph

Use the following setup to configure Azure access and run the connector processes.

1. Register an application on Azure as described in the Microsoft Java tutorial: https://docs.microsoft.com/en-us/graph/tutorials/java?tutorial-step=2
2. Navigate to `Overview` in the Azure menu and copy the `Application (client) ID` into `microsoftConnector.appId`.
3. Navigate to `Authentication` in the Azure app menu.
   3.1. Add a `Redirect URI` in the `Web` section.
      - Axon Ivy callback pattern: `{scheme}://{host}:{port}/oauth2/callback`.
      - Designer default callback: `http://localhost:8081/oauth2/callback`.
      - Engine callback example: `https://my.workflows.ch/oauth2/callback`.

      ![set-redirect](../../doc/img/azure_authCallback.png)

4. Navigate to `Certificate & secrets` in the Azure app menu.
   4.1. Create a new client secret with `New client secret`.

      ![new-secret](../../doc/img/azure_createSecret.png)

   4.2. Copy the generated secret value into `microsoftConnector.secretKey`.

      ![copy-secret](../../doc/img/azure_copySecret.png)

5. Navigate to `API Permissions` in the Azure app menu.
   5.1. Add permissions via `Add a permission` > `Microsoft Graph` > `Delegated permissions`.
   5.2. Grant each permission listed in your `variables.yaml` permissions property.

      ![add-perms](../../doc/img/azure_addPermission.png)

6. Start a connector or demo process and validate the Microsoft 365 connection.

### Variables

In order to use this product you must configure multiple variables.

Add the following block to your `config/variables.yaml` file of our 
main Business Project that will make use of this product:

```
@variables.yaml@ 
```

Afterwards set the values as shown in the Azure App setup above.

> [!NOTE]
> The format of variable is changed from version 13.
> E.g. 
> The variable path `microsoft-connector` is renamed to `microsoftConnector`.
> The variable path `teams-notification` is renamed to `teamsNotification`.

```yaml
# yaml-language-server: $schema=https://json-schema.axonivy.com/app/12.0.0/variables.json
Variables:
  
  microsoftConnector:
    
    # Your Azure Application (client) ID
    appId: ""
    
    # Secret key from your applications "certificates & secrets"
    # [password]
    secretKey: ""
    
    # work with app permissions rather than in delegate of a user
    # set to 'true' if no user consent should be acquired and adjust the 'tenantId' below.
    useAppPermissions: false
    
    # tenant to use for OAUTH2 request.
    # the default 'common' fits for user delegate requests.
    # set the Azure Directory (tenant) ID, for application requests.
    tenantId: "common"
    
    # use a static user+password authentication to work in the name of technical user.
    # most insecure but valid, if you must work with user permissions, while no real user is able to consent the action.
    useUserPassFlow:
      enabled: false
      # technical user to login
      user: ""
      # technical users password
      # [password]
      pass: ""
    
    # permissions to request access to.
    # you may exclude or add some, as your azure administrator allows or restricts them.
    # for sharepoint-demos, the following must be added: Sites.Read.All Files.ReadWrite
    permissions: "user.read Calendars.ReadWrite mail.readWrite mail.send Tasks.ReadWrite\\
      \\ Chat.Read offline_access"
    
    # this property specifies the library used to create and manage HTTP connections for Jersey client. 
    # it sets the connection provider class for the Jersey client.
    # while the default provider works well for most methods, if you specifically need to use the PATCH method, consider switching the provider to:
    #   org.glassfish.jersey.apache.connector.ApacheConnectorProvider
    connectorProvider: "org.glassfish.jersey.client.HttpUrlConnectorProvider"
```

### Optional authentication and runtime sections

You can enable optional runtime authentication modes through variables:

1. Set `microsoftConnector.useAppPermissions` to `true` to run with application permissions (tenant-level, non-user-delegated).
2. Set `microsoftConnector.tenantId` to your Azure Directory tenant ID when app permissions are enabled.
3. Enable `microsoftConnector.useUserPassFlow.enabled` only if you explicitly need technical user authentication.
4. If user/password flow is enabled, provide `user` and `pass` securely and verify permission scopes before production use.

## Components

### Connector processes

#### msCalendar.p.json

- **upcomingEvents() -> myEvents: java.util.List<com.microsoft.graph.MicrosoftGraphEvent>**
    - Input: (none)
    - Result:
        - `myEvents` (java.util.List<com.microsoft.graph.MicrosoftGraphEvent>) - List with upcoming events from your calendar
    - Description: Reads upcoming events from your calendar.

- **createMeeting(msgraph.connector.NewEvent evt) -> meeting: com.microsoft.graph.MicrosoftGraphEvent**
    - Input:
        - `evt` (msgraph.connector.NewEvent) - The new event that should be created in your calendar
    - Result:
        - `meeting` (com.microsoft.graph.MicrosoftGraphEvent) - The event that was created in your calendar
    - Description: Creates a new meeting in your your calendar.

#### msChat.p.json

- **recentMessages() -> messages: java.util.List<com.microsoft.graph.MicrosoftGraphChatMessage>**
    - Input: (none)
    - Result:
        - `messages` (java.util.List<com.microsoft.graph.MicrosoftGraphChatMessage>) - (no description)

#### msFiles.p.json

- **uploadFile(java.io.File file, String siteId) -> (none)**
    - Input:
        - `file` (java.io.File) - file to upload unto sharepoint site
        - `siteId` (String) - optional: GUID of a site, if empty the first site, that the user follows will be used
    - Result: (none)

- **uploadFile(java.io.File file) -> (none)**
    - Input:
        - `file` (java.io.File) - file to upload unto sharepoint site
    - Result: (none)

- **myRecentFiles() -> items: java.util.List<com.microsoft.graph.MicrosoftGraphDriveItem>**
    - Input: (none)
    - Result:
        - `items` (java.util.List<com.microsoft.graph.MicrosoftGraphDriveItem>) - recently used items

#### msMail.p.json

- **writeMail(msgraph.connector.NewMail mail) -> message: com.microsoft.graph.MicrosoftGraphMessage**
    - Input:
        - `mail` (msgraph.connector.NewMail) - The mail to send
    - Result:
        - `message` (com.microsoft.graph.MicrosoftGraphMessage) - The message that was sent
    - Description: Sends a mail.

#### msToDo.p.json

- **allTasks() -> tasks: java.util.List<com.microsoft.graph.MicrosoftGraphTodoTask>**
    - Input: (none)
    - Result:
        - `tasks` (java.util.List<com.microsoft.graph.MicrosoftGraphTodoTask>) - List with all todo tasks
    - Description: Reads all todo tasks.

- **createNewTask(msgraph.connector.NewToDo task) -> todo: com.microsoft.graph.MicrosoftGraphTodoTask**
    - Input:
        - `task` (msgraph.connector.NewToDo) - The new todo task to create
    - Result:
        - `todo` (com.microsoft.graph.MicrosoftGraphTodoTask) - The created todo task
    - Description: Creates a new todo task.

### Form components

- No information was detected for this section.

### Maven artifacts

1. msgraph-connector

```xml
<dependency>
  <groupId>com.axonivy.connector.office365</groupId>
  <artifactId>msgraph-connector</artifactId>
  <version>@version@</version>
  <type>iar</type>
</dependency>
```

2. msgraph-mail-demo

```xml
<dependency>
  <groupId>com.axonivy.connector.office365</groupId>
  <artifactId>msgraph-mail-demo</artifactId>
  <version>@version@</version>
  <type>iar</type>
</dependency>
```

3. msgraph-calendar-demo

```xml
<dependency>
  <groupId>com.axonivy.connector.office365</groupId>
  <artifactId>msgraph-calendar-demo</artifactId>
  <version>@version@</version>
  <type>iar</type>
</dependency>
```

4. msgraph-todo-demo

```xml
<dependency>
  <groupId>com.axonivy.connector.office365</groupId>
  <artifactId>msgraph-todo-demo</artifactId>
  <version>@version@</version>
  <type>iar</type>
</dependency>
```
