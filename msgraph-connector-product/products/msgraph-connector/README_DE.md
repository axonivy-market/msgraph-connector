# Microsoft Graph Connector

Der Microsoft Graph Connector von Axon Ivy hilft dir, deine Business-Prozesse mit Microsoft-365-Services wie Mail, Kalender, To Do, SharePoint-Dateien und Teams-Chat zu verbinden. Du kannst sofort einsetzbare Connector-Prozesse wiederverwenden, um typische Kollaborationsaufgaben mit deutlich weniger Integrationsaufwand zu automatisieren.

[Microsoft Graph overview](https://learn.microsoft.com/en-us/graph/overview)

[Microsoft Graph API reference](https://learn.microsoft.com/en-us/graph/api/overview?view=graph-rest-1.0)

### Hauptfunktionen

- Automatisiere tägliche Microsoft-365-Aktionen direkt in deinen Prozess-Workflows, damit Teams schneller arbeiten und weniger manuelle Übergaben brauchen.
- Sende E-Mails mit strukturierten Empfängern, Betreff und Inhalt direkt aus deinem Prozesskontext.
- Lies kommende Kalendereinträge und erstelle Meetings programmatisch für Terminabläufe.
- Synchronisiere Aufgaben mit Microsoft To Do, indem du Aufgaben liest und neue Aufgaben aus Prozessdaten erstellst.
- Arbeite mit Dateien und Sites, indem du Dokumente hochlädst und zuletzt verwendete Dateien in Microsoft 365 abrufst.
- Lies aktuelle Teams-Chatnachrichten und baue benachrichtigungsfähige Kollaborationsszenarien auf.

## Demo

Sieh dir die Demo-Implementierungen in diesem Repository an:

- [Graph Calendar](../msgraph-calendar/README.md)
- [Graph Mail](../msgraph-mail/README.md)
- [Graph ToDo](../msgraph-todo/README.md)
- [Graph Chat](../msgraph-chat/README.md)

### Demo-Workflows

#### Mail Demo (msgraph-mail-demo)

##### Inbox

1. Starte den Inbox-Demoprozess.
2. Der Prozess authentifiziert sich gegen Microsoft Graph und oeffnet den Mail-Dialog.
3. Pruefe die aufgelisteten Nachrichten und bestaetige, dass Mailbox-Daten verfuegbar sind.

##### Write Mail

1. Starte den Write-Mail-Demoprozess.
2. Fuell Empfaenger, Betreff und Nachrichtentext im Dialog aus.
3. Sende das Formular ab, um die Nachricht ueber Microsoft Graph zu senden.
4. Bestaetige, dass die Nachricht erfolgreich verarbeitet wurde.

#### Calendar Demo (msgraph-calendar-demo)

##### Read Calendar

1. Starte den Read-Calendar-Demoprozess.
2. Der Prozess laedt kommende Termine aus deinem Microsoft-365-Kalender.
3. Pruefe die Terminliste im Dialog, um die Kalenderverbindung zu bestaetigen.

##### Meet

1. Starte den Meet-Demoprozess.
2. Gib die Meeting-Details im Create-Event-Dialog ein.
3. Sende das Formular ab, um einen neuen Kalendereintrag zu erstellen.
4. Pruefe den erstellten Eintrag in der Ergebnisliste.

#### ToDo Demo (msgraph-todo-demo)

##### My ToDo

1. Starte den My-ToDo-Demoprozess.
2. Der Prozess liest deine aktuellen Microsoft-To-Do-Aufgaben.
3. Pruefe die zurueckgegebenen Aufgaben im Dialog.

##### Create Task

1. Starte den Create-Task-Demoprozess.
2. Gib Titel und Inhalt fuer die neue Aufgabe ein.
3. Sende den Dialog ab, um die Aufgabe in Microsoft To Do zu erstellen.
4. Pruefe, dass die erstellte Aufgabe im Ergebnis enthalten ist.

#### SharePoint Demo (msgraph-sharepoint-demo)

##### My Sites

1. Starte den My-Sites-Demoprozess.
2. Der Prozess liest gefolgte SharePoint-Sites aus Microsoft 365.
3. Pruefe die Log-Ausgabe, um den Site-Zugriff zu bestaetigen.

##### Upload

1. Starte den Upload-Demoprozess.
2. Eine Beispieldatei wird automatisch vorbereitet.
3. Die Datei wird ueber den Connector-Prozess nach SharePoint hochgeladen.
4. Bestaetige den erfolgreichen Upload im Prozessergebnis.

##### Recent Files

1. Starte den Recent-Files-Demoprozess.
2. Der Prozess liest zuletzt verwendete Drive-Elemente.
3. Pruefe die Ausgabe, um die geladenen Dateinamen zu bestaetigen.

#### Teams Demo (msgraph-teams-demo)

##### Read Messages

1. Starte den Read-Messages-Demoprozess.
2. Der Prozess ruft aktuelle Chatnachrichten aus deinen Teams-Chats ab.
3. Pruefe die Ausgabe, um den Rueckgabewert der Nachrichteninhalte zu bestaetigen.

##### Teams Web

1. Starte den Teams-Web-Demoprozess.
2. Der Prozess authentifiziert den aktuellen Benutzer.
3. Der Teams-Dialog oeffnet sich zur interaktiven Pruefung.

##### MS Teams Task Notification

1. Starte den Demo-Prozess MS Teams Task Notification.
2. Die Demo simuliert eine Benutzerzuordnung fuer lokale Tests.
3. Ein Beispiel-Task-Event wird ausgeloest, um den Benachrichtigungsfluss zu zeigen.
4. Pruefe, dass das Teams-Benachrichtigungsszenario ausgefuehrt wird.

## Setup

- **Rollen:** Everybody (konfiguriert in config/roles.xml)
- **OpenAPI:** Spec URL: https://graphexplorerapi.azurewebsites.net/openapi?tags=me.user,me.calendar,users.calendar,me.message,me.Actions,me.todo,me.site,sites.Actions,me.drive,me.chat,chats.chat,chats.chatMessage&openApiVersion=3&graphVersion=v1.0&format=yaml&style=PowerShell, Namespace: com.microsoft.graph

Nutze die folgende Konfiguration, um den Azure-Zugriff einzurichten und die Connector-Prozesse auszufuehren.

1. Registriere eine Anwendung in Azure wie im Microsoft-Java-Tutorial beschrieben: https://docs.microsoft.com/en-us/graph/tutorials/java?tutorial-step=2
2. Gehe im Azure-Menue zu `Overview` und kopiere die `Application (client) ID` nach `microsoftConnector.appId`.
3. Gehe im Azure-App-Menue zu `Authentication`.
   3.1. Füge eine `Redirect URI` im Bereich `Web` hinzu.
      - Axon-Ivy-Callback-Muster: `{scheme}://{host}:{port}/oauth2/callback`.
      - Designer-Standard-Callback: `http://localhost:8081/oauth2/callback`.
      - Engine-Callback-Beispiel: `https://my.workflows.ch/oauth2/callback`.

      ![set-redirect](../../doc/img/azure_authCallback.png)

4. Gehe im Azure-App-Menue zu `Certificate & secrets`.
   4.1. Erstelle mit `New client secret` ein neues Client-Secret.

      ![new-secret](../../doc/img/azure_createSecret.png)

   4.2. Kopiere den generierten Secret-Wert nach `microsoftConnector.secretKey`.

      ![copy-secret](../../doc/img/azure_copySecret.png)

5. Gehe im Azure-App-Menue zu `API Permissions`.
   5.1. Fuege Berechtigungen hinzu ueber `Add a permission` > `Microsoft Graph` > `Delegated permissions`.
   5.2. Erteile alle Berechtigungen, die in der Eigenschaft `permissions` deiner `variables.yaml` stehen.

      ![add-perms](../../doc/img/azure_addPermission.png)

6. Starte einen Connector- oder Demo-Prozess und pruefe die Verbindung zu Microsoft 365.

### Variablen

Damit du dieses Produkt nutzen kannst, musst du mehrere Variablen konfigurieren.

Fuege den folgenden Block in deine `config/variables.yaml` deiner
Haupt-Business-Applikation ein, die dieses Produkt verwendet:

```
@variables.yaml@ 
```

Setze danach die Werte wie im Azure-App-Setup oben gezeigt.

> [!NOTE]
> Das Format der Variablen wurde ab Version 13 geaendert.
> Z. B.
> Der Variablenpfad `microsoft-connector` wurde in `microsoftConnector` umbenannt.
> Der Variablenpfad `teams-notification` wurde in `teamsNotification` umbenannt.

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

### Optionale Authentifizierungs- und Runtime-Abschnitte

Du kannst optionale Runtime-Authentifizierungsmodi ueber Variablen aktivieren:

1. Setze `microsoftConnector.useAppPermissions` auf `true`, um mit Anwendungsberechtigungen (mandantenweit, nicht benutzerdelegiert) zu arbeiten.
2. Setze `microsoftConnector.tenantId` auf deine Azure-Directory-Tenant-ID, wenn App-Berechtigungen aktiv sind.
3. Aktiviere `microsoftConnector.useUserPassFlow.enabled` nur, wenn du explizit eine technische Benutzeranmeldung brauchst.
4. Wenn der User/Pass-Flow aktiv ist, hinterlege `user` und `pass` sicher und pruefe die Permission-Scopes vor dem Produktiveinsatz.

## Komponenten

### Connector-Prozesse

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
        - `meeting` (com.microsoft.graph.MicrosoftGraphEvent) - The event that was created in your your calendar
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

### Form-Komponenten

- No information was detected for this section.

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
