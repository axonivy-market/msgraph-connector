# Microsoft Graph Connector
The Microsoft Graph connector integrates Microsoft 365 services into Axon Ivy, enabling processes to send emails, manage calendars, upload files to SharePoint, handle ToDo tasks, and interact with Teams chats. It provides pre-built callable subs and a Microsoft Graph OpenAPI-backed REST client to access Mail, Calendar, Files, ToDo and Chat features.

This connector:

- Send emails and manage recipients directly from your processes.
- Create and manage calendar events with attendee support.
- Upload and manage files on SharePoint sites from workflows.
- Read and create ToDo tasks to integrate task management.
- Access recent chats and messages for Teams notifications and integrations.
- Use Microsoft Graph OpenAPI-backed REST clients for consistent API access.

## Demo

Check the demo implementations provided for Mail, Calendar, ToDo, Chat and SharePoint under the demo modules.

### Demo workflows

#### Mail (msgraph-mail-demo)

##### Write Mail

1. Launch the WriteMail demo from the demo menu.
2. A dialog opens to compose a new email.
3. Fill in recipients, subject, and body, then send.
4. Check your mailbox for the sent message.

#### Calendar (msgraph-calendar-demo)

##### Upcoming Events

1. Launch the Upcoming Events demo from the demo menu.
2. The connector reads events from your calendar and displays them in a list.
3. Optionally create a meeting using the Create Meeting dialog.
4. Review the meeting in your calendar.

#### ToDo (msgraph-todo-demo)

##### Manage Tasks

1. Launch the Manage Tasks demo from the demo menu.
2. View existing ToDo tasks and create new ones.
3. Fill in task details and save.
4. Verify the new task appears in your ToDo list.

#### SharePoint (msgraph-sharepoint-demo)

##### Upload Files

1. Launch the Upload Files demo from the demo menu.
2. Select or upload a file to SharePoint.
3. Confirm upload and check the target site for the file.
4. Optionally review recent files.

#### Teams (msgraph-teams-demo)

##### Send Notification

1. Launch the Teams Notification demo from the demo menu.
2. Compose a message and select the target chat or channel.
3. Send the message and verify delivery in Teams.

## Setup

- **Roles:** Everybody (configured in config/roles.xml)
- **OpenAPI:** https://graphexplorerapi.azurewebsites.net/openapi?tags=me.user,me.calendar,users.calendar,me.message,me.Actions,me.todo,me.site,sites.Actions,me.drive,me.chat,chats.chat,chats.chatMessage&openApiVersion=3&graphVersion=v1.0&format=yaml&style=PowerShell  (Namespace: com.microsoft.graph)

### Variables

```
@variables.yaml@
```

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
    permissions: "user.read Calendars.ReadWrite mail.readWrite mail.send Tasks.ReadWrite\
      \ Chat.Read offline_access"
    
    # this property specifies the library used to create and manage HTTP connections for Jersey client. 
    # it sets the connection provider class for the Jersey client.
    # while the default provider works well for most methods, if you specifically need to use the PATCH method, consider switching the provider to:
    #   org.glassfish.jersey.apache.connector.ApacheConnectorProvider
    connectorProvider: "org.glassfish.jersey.client.HttpUrlConnectorProvider"
```

1. Register an application on Azure as described in the Microsoft's Java Tutorial https://docs.microsoft.com/en-us/graph/tutorials/java?tutorial-step=2
2. Navigate to `Overview` in the Azure App menu and copy the 'Application (client) ID' into your variable called `appId` within the `microsoftConnector` section.
3. Navigate to `Authentication` in the Azure App menu.
   3.1. Add a `Redirect URI` in the `Web` Section.
      - Axon Ivy has an authentication callback URI which follows the pattern `{scheme}://{host}:{port}/oauth2/callback`. This URI must be registered in the Azure App.
      - Consequently, for the Axon Ivy Designer this URI can always be set to the following value: `http://localhost:8081/oauth2/callback`
      - For the Axon Ivy Engine, the URI must contain the full URI where your Engine is reachable plus the callback path. E.g.: `https://my.workflows.ch/oauth2/callback`

      
      ![set-redirect](../../doc/img/azure_authCallback.png)

4. Navigate to `Certificate & secrets` in the Azure App menu.
   4.1. Create a new secret by pressing `New client secret` and select any validity period.
   4.2. Copy the value of the generated secret into your variable called `secretKey` within the `microsoftConnector` section.

   ![new-secret](../../doc/img/azure_createSecret.png)

   ![copy-secret](../../doc/img/azure_copySecret.png)

5. Navigate to `API Permissions` in the Azure App menu.
   - Add permissions via `Add a permission` > `Microsoft Graph` > `Delegated permissions`.
   - Grant each of the permissions outlined in the `permissions` block of your variables.yaml file.

   ![add-perms](../../doc/img/azure_addPermission.png)

6. Done. Start any process that connects with Microsoft 365.

## Components

### Connector Processes

#### msCalendar.p.json

- **upcomingEvents() -> myEvents: java.util.List<com.microsoft.graph.MicrosoftGraphEvent>**
    - Input: (none)
    - Result:
        - `myEvents` (java.util.List<com.microsoft.graph.MicrosoftGraphEvent>) - List with upcoming events from your calendar

- **createMeeting(msgraph.connector.NewEvent evt) -> meeting: com.microsoft.graph.MicrosoftGraphEvent**
    - Input:
        - `evt` (msgraph.connector.NewEvent) - The new event that should be created in your calendar
    - Result:
        - `meeting` (com.microsoft.graph.MicrosoftGraphEvent) - The event that was created in your calendar

#### msChat.p.json

- **recentMessages() -> messages: java.util.List<com.microsoft.graph.MicrosoftGraphChatMessage>**
    - Input: (none)
    - Result:
        - `messages` (java.util.List<com.microsoft.graph.MicrosoftGraphChatMessage>) - (none)

#### msFiles.p.json

- **uploadFile(java.io.File file, String siteId) -> (none)**
    - Input:
        - `file` (java.io.File) - file to upload unto sharepoint site
        - `siteId` (String) - optional: GUID of a site, if empty the first site that the user follows will be used
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

#### msToDo.p.json

- **allTasks() -> tasks: java.util.List<com.microsoft.graph.MicrosoftGraphTodoTask>**
    - Input: (none)
    - Result:
        - `tasks` (java.util.List<com.microsoft.graph.MicrosoftGraphTodoTask>) - List with all todo tasks

- **createNewTask(msgraph.connector.NewToDo task) -> todo: com.microsoft.graph.MicrosoftGraphTodoTask**
    - Input:
        - `task` (msgraph.connector.NewToDo) - The new todo task to create
    - Result:
        - `todo` (com.microsoft.graph.MicrosoftGraphTodoTask) - The created todo task

### Form Components

- **NewMail — Mail input data**
  - **Namespace:** msgraph.connector
  - **Component type:** Data Class
  - **Fields:**
     - `receivers` (java.util.List<String>) — (no description available)
     - `subject` (String) — (no description available)
     - `body` (String) — (no description available)

- **NewEvent — Event input data**
  - **Namespace:** msgraph.connector
  - **Component type:** Data Class
  - **Fields:**
     - `participants` (java.util.List<String>) — (no description available)
     - `subject` (String) — (no description available)
     - `description` (String) — (no description available)

- **NewToDo — ToDo input data**
  - **Namespace:** msgraph.connector
  - **Component type:** Data Class
  - **Fields:**
     - `title` (String) — (no description available)
     - `content` (String) — (no description available)

- **FilesData — File upload data**
  - **Namespace:** msgraph.connector
  - **Component type:** Data Class
  - **Fields:**
     - `upload` (java.io.File) — (no description available)
     - `sites` (java.util.List<com.microsoft.graph.MicrosoftGraphSite>) — (no description available)
     - `siteId` (String) — GUID
     - `items` (java.util.List<com.microsoft.graph.MicrosoftGraphDriveItem>) — (no description available)

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
