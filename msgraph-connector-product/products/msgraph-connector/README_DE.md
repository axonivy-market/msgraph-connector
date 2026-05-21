# Microsoft Graph Connector
Der Microsoft Graph Connector integriert Microsoft 365-Dienste in Axon Ivy und ermöglicht es deinen Prozessen, E-Mails zu senden, Kalender zu verwalten, Dateien in SharePoint hochzuladen, ToDo-Aufgaben zu verwalten und mit Teams-Chats zu interagieren. Er stellt vorgefertigte Callable-Subs sowie einen REST-Client auf Basis der Microsoft Graph OpenAPI bereit, sodass Funktionen für Mail, Kalender, Dateien, ToDo und Chat direkt nutzbar sind.

Dieser Connector:

- E-Mails senden und Empfänger direkt aus deinen Prozessen verwalten.
- Kalenderereignisse erstellen und verwalten mit Unterstützung für Teilnehmer.
- Dateien in SharePoint-Sites hochladen und aus Workflows verwalten.
- ToDo-Aufgaben lesen und erstellen, um Aufgabenmanagement zu integrieren.
- Auf aktuelle Chats und Nachrichten zugreifen für Teams-Benachrichtigungen und Integrationen.
- REST-Clients auf Basis der Microsoft Graph OpenAPI für konsistenten API-Zugriff verwenden.

## Demo

Sieh dir die bereitgestellten Demo-Implementierungen für Mail, Kalender, ToDo, Chat und SharePoint in den Demo-Modulen an.

### Demo-Abläufe

#### Mail (msgraph-mail-demo)

##### Write Mail

1. Starte das WriteMail-Demo über das Demo-Menü.
2. Ein Dialog öffnet sich, um eine neue E-Mail zu verfassen.
3. Fülle Empfänger, Betreff und Text aus und sende die Nachricht.
4. Prüfe dein Postfach auf die gesendete Nachricht.

#### Calendar (msgraph-calendar-demo)

##### Upcoming Events

1. Starte das Upcoming Events-Demo über das Demo-Menü.
2. Der Connector liest Ereignisse aus deinem Kalender und zeigt sie in einer Liste an.
3. Optional: Erstelle ein Meeting über den Create Meeting-Dialog.
4. Prüfe das erstellte Meeting in deinem Kalender.

#### ToDo (msgraph-todo-demo)

##### Manage Tasks

1. Starte das Manage Tasks-Demo über das Demo-Menü.
2. Sieh vorhandene ToDo-Aufgaben ein und lege neue an.
3. Fülle die Aufgabendaten aus und speichere.
4. Verifiziere, dass die neue Aufgabe in deiner ToDo-Liste erscheint.

#### SharePoint (msgraph-sharepoint-demo)

##### Upload Files

1. Starte das Upload Files-Demo über das Demo-Menü.
2. Wähle eine Datei aus oder lade sie zu SharePoint hoch.
3. Bestätige den Upload und prüfe die Ziel-Site auf die Datei.
4. Optional: Sieh dir kürzlich verwendete Dateien an.

#### Teams (msgraph-teams-demo)

##### Send Notification

1. Starte das Teams Notification-Demo über das Demo-Menü.
2. Verfasse eine Nachricht und wähle den Ziel-Chat oder Kanal aus.
3. Sende die Nachricht und verifiziere die Zustellung in Teams.

## Einrichtung

- **Rollen:** Everybody (konfiguriert in config/roles.xml)
- **OpenAPI:** https://graphexplorerapi.azurewebsites.net/openapi?tags=me.user,me.calendar,users.calendar,me.message,me.Actions,me.todo,me.site,sites.Actions,me.drive,me.chat,chats.chat,chats.chatMessage&openApiVersion=3&graphVersion=v1.0&format=yaml&style=PowerShell  (Namespace: com.microsoft.graph)

### Variablen

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

1. Registriere eine Anwendung in Azure, wie im Microsoft Java-Tutorial beschrieben: https://docs.microsoft.com/en-us/graph/tutorials/java?tutorial-step=2
2. Navigiere im Azure App-Menü zu Overview und kopiere die 'Application (client) ID' in deine Variable `appId` innerhalb des `microsoftConnector`-Abschnitts.
3. Navigiere im Azure App-Menü zu Authentication.
   3.1. Füge im Web-Bereich eine `Redirect URI` hinzu.
      - Axon Ivy verwendet eine Authentifizierungs-Callback-URI im Format `{scheme}://{host}:{port}/oauth2/callback`. Diese URI muss in der Azure App registriert werden.
      - Für den Axon Ivy Designer kann diese URI standardmäßig auf `http://localhost:8081/oauth2/callback` gesetzt werden.
      - Für die Axon Ivy Engine muss die URI die erreichbare Engine-URL plus den Callback-Pfad enthalten, z. B.: `https://my.workflows.ch/oauth2/callback`

   
   ![Setze Redirect URI](../../doc/img/azure_authCallback.png)

4. Navigiere zu `Certificate & secrets` im Azure App-Menü.
   4.1. Erstelle ein neues Secret über `New client secret` und wähle eine Gültigkeitsdauer.
   4.2. Kopiere den generierten Secret-Wert in deine Variable `secretKey` im Abschnitt `microsoftConnector`.

   ![Neues Secret erstellen](../../doc/img/azure_createSecret.png)

   ![Secret kopieren](../../doc/img/azure_copySecret.png)

5. Navigiere zu `API Permissions` im Azure App-Menü.
   - Füge Berechtigungen hinzu über `Add a permission` > `Microsoft Graph` > `Delegated permissions`.
   - Vergib die in der `permissions`-Block deiner `variables.yaml` aufgeführten Berechtigungen.

   ![Berechtigungen hinzufügen](../../doc/img/azure_addPermission.png)

6. Fertig. Starte einen Prozess, der eine Verbindung zu Microsoft 365 herstellt.

## Komponenten

### Connector-Prozesse

#### msCalendar.p.json

- **upcomingEvents() -> myEvents: java.util.List<com.microsoft.graph.MicrosoftGraphEvent>**
    - Input: (none)
    - Result:
        - `myEvents` (java.util.List<com.microsoft.graph.MicrosoftGraphEvent>) - Liste mit anstehenden Ereignissen aus deinem Kalender

- **createMeeting(msgraph.connector.NewEvent evt) -> meeting: com.microsoft.graph.MicrosoftGraphEvent**
    - Input:
        - `evt` (msgraph.connector.NewEvent) - Das neue Ereignis, das in deinem Kalender erstellt werden soll
    - Result:
        - `meeting` (com.microsoft.graph.MicrosoftGraphEvent) - Das Ereignis, das in deinem Kalender erstellt wurde

#### msChat.p.json

- **recentMessages() -> messages: java.util.List<com.microsoft.graph.MicrosoftGraphChatMessage>**
    - Input: (none)
    - Result:
        - `messages` (java.util.List<com.microsoft.graph.MicrosoftGraphChatMessage>) - (none)

#### msFiles.p.json

- **uploadFile(java.io.File file, String siteId) -> (none)**
    - Input:
        - `file` (java.io.File) - Datei, die auf eine SharePoint-Site hochgeladen werden soll
        - `siteId` (String) - optional: GUID einer Site; falls leer, wird die erste vom Benutzer gefollowte Site verwendet
    - Result: (none)

- **uploadFile(java.io.File file) -> (none)**
    - Input:
        - `file` (java.io.File) - Datei, die auf eine SharePoint-Site hochgeladen werden soll
    - Result: (none)

- **myRecentFiles() -> items: java.util.List<com.microsoft.graph.MicrosoftGraphDriveItem>**
    - Input: (none)
    - Result:
        - `items` (java.util.List<com.microsoft.graph.MicrosoftGraphDriveItem>) - kürzlich verwendete Einträge

#### msMail.p.json

- **writeMail(msgraph.connector.NewMail mail) -> message: com.microsoft.graph.MicrosoftGraphMessage**
    - Input:
        - `mail` (msgraph.connector.NewMail) - Die zu sendende E-Mail
    - Result:
        - `message` (com.microsoft.graph.MicrosoftGraphMessage) - Die gesendete Nachricht

#### msToDo.p.json

- **allTasks() -> tasks: java.util.List<com.microsoft.graph.MicrosoftGraphTodoTask>**
    - Input: (none)
    - Result:
        - `tasks` (java.util.List<com.microsoft.graph.MicrosoftGraphTodoTask>) - Liste mit allen ToDo-Aufgaben

- **createNewTask(msgraph.connector.NewToDo task) -> todo: com.microsoft.graph.MicrosoftGraphTodoTask**
    - Input:
        - `task` (msgraph.connector.NewToDo) - Die neue ToDo-Aufgabe, die erstellt werden soll
    - Result:
        - `todo` (com.microsoft.graph.MicrosoftGraphTodoTask) - Die erstellte ToDo-Aufgabe

### Formularkomponenten

- **NewMail — Mail-Eingabedaten**
  - **Namespace:** msgraph.connector
  - **Component type:** Data Class
  - **Fields:**
     - `receivers` (java.util.List<String>) — (keine Beschreibung verfügbar)
     - `subject` (String) — (keine Beschreibung verfügbar)
     - `body` (String) — (keine Beschreibung verfügbar)

- **NewEvent — Event-Eingabedaten**
  - **Namespace:** msgraph.connector
  - **Component type:** Data Class
  - **Fields:**
     - `participants` (java.util.List<String>) — (keine Beschreibung verfügbar)
     - `subject` (String) — (keine Beschreibung verfügbar)
     - `description` (String) — (keine Beschreibung verfügbar)

- **NewToDo — ToDo-Eingabedaten**
  - **Namespace:** msgraph.connector
  - **Component type:** Data Class
  - **Fields:**
     - `title` (String) — (keine Beschreibung verfügbar)
     - `content` (String) — (keine Beschreibung verfügbar)

- **FilesData — Datei-Upload-Daten**
  - **Namespace:** msgraph.connector
  - **Component type:** Data Class
  - **Fields:**
     - `upload` (java.io.File) — (keine Beschreibung verfügbar)
     - `sites` (java.util.List<com.microsoft.graph.MicrosoftGraphSite>) — (keine Beschreibung verfügbar)
     - `siteId` (String) — GUID
     - `items` (java.util.List<com.microsoft.graph.MicrosoftGraphDriveItem>) — (keine Beschreibung verfügbar)

### Maven-Artefakte

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
