# msgraph-connector

Der msgraph-connector integriert Microsoft-365-Dienste über Microsoft Graph in Axon-Ivy-Prozesse. Er stellt dir fertige aufrufbare Subprozesse und Demo-Workflows für Mail, Kalender, Dateien, SharePoint, Teams und ToDo bereit, damit du praktische Automatisierungen schnell umsetzen kannst.

## Wichtigste Funktionen

- Sende E-Mails und verwalte Empfänger direkt aus deinen Axon-Ivy-Workflows.
- Lies kommende Kalendereinträge und erstelle neue Besprechungen direkt aus der Prozesslogik heraus.
- Lade Dateien in SharePoint hoch, rufe gefolgte Sites ab und durchsuche zuletzt verwendete Dateien.
- Lies Teams-Chatnachrichten und löse Teams-basierte Aufgabenbenachrichtigungen aus.
- Erstelle, liste und prüfe ToDo-Aufgaben mit fertigen Dialogen und Subprozessen.
- Konfiguriere Azure-App-Authentifizierung für delegierte Berechtigungen oder App-Berechtigungen mit einer gemeinsamen REST-Client-Einrichtung.

## Demo

Sieh dir die Demo-Implementierungen für Mail, Kalender, ToDo, SharePoint und Teams an. Sie zeigen dir, wie sich der Connector in echten Prozessschritten verhält, und helfen dir, dieselben Muster auf deine eigene Lösung zu übertragen.

Erkunde die [Microsoft Graph-Übersicht](https://learn.microsoft.com/en-us/graph/overview) und die [API-Referenz](https://learn.microsoft.com/en-us/graph/api/overview?view=graph-rest-1.0), wenn du die Demos erweiterst.

### Demo-Workflows

#### Mail-Demo (msgraph-mail-demo)

##### Posteingang
1. Starte die Inbox-Demo über das Demo-Menü.
2. Die Demo zeigt aktuelle E-Mails aus deinem Postfach in einem Dialog.
3. Prüfe Absender, Betreff und Vorschautext und schließe den Dialog anschließend.

##### E-Mail schreiben
1. Starte die Demo „E-Mail schreiben“ über das Demo-Menü.
2. Gib Empfänger, Betreff und Nachrichtentext im Verfassen-Dialog ein.
3. Füge Empfänger nach Bedarf hinzu oder entferne sie.
4. Sende die E-Mail und bestätige, dass die Nachricht erstellt wurde.

#### Kalender-Demo (msgraph-calendar-demo)

##### Kalender lesen
1. Starte die Demo „Kalender lesen“ über das Demo-Menü.
2. Die Demo zeigt kommende Termine aus deinem Kalender in einem Dialog.
3. Prüfe Betreff und Zeitangaben des Termins und schließe den Dialog anschließend.

##### Besprechung erstellen
1. Starte die Demo „Besprechung erstellen“ über das Demo-Menü.
2. Fülle Betreff, Beschreibung und Teilnehmende aus.
3. Erstelle den Termin und prüfe die aktualisierte Kalenderansicht.
4. Kontrolliere deinen Kalender, um zu bestätigen, dass die Besprechung hinzugefügt wurde.

#### ToDo-Demo (msgraph-todo-demo)

##### Aufgabe erstellen
1. Starte die Demo „Aufgabe erstellen“ über das Demo-Menü.
2. Gib den Aufgabentitel und den Inhalt im Dialog ein.
3. Erstelle die Aufgabe und bestätige, dass sie in der Liste erscheint.

##### Meine ToDos
1. Starte die Demo „Meine ToDos“ über das Demo-Menü.
2. Die Demo zeigt deine aktuellen Aufgaben in einem Dialog.
3. Prüfe Titel und Inhalt einer Aufgabe und schließe den Dialog anschließend.

#### SharePoint-Demo (msgraph-sharepoint-demo)

##### Meine Sites
1. Starte die Demo „Meine Sites“ über das Demo-Menü.
2. Die Demo listet die SharePoint-Sites auf, denen du folgst.
3. Prüfe die Site-Namen und IDs für deine Integration.

##### Zuletzt verwendete Dateien
1. Starte die Demo „Zuletzt verwendete Dateien“ über das Demo-Menü.
2. Die Demo listet zuletzt verwendete Dateien für den schnellen Zugriff.
3. Nutze die Dateinamen als Referenz für deinen eigenen Workflow.

##### Hochladen
1. Starte die Demo „Hochladen“ über das Demo-Menü.
2. Erstelle bei Bedarf eine Beispieldatei und wähle das Zielsystem aus.
3. Lade die Datei hoch und bestätige, dass die Übertragung abgeschlossen wurde.

#### Teams-Demo (msgraph-teams-demo)

##### Nachrichten lesen
1. Starte die Demo „Nachrichten lesen“ über das Demo-Menü.
2. Die Demo lädt aktuelle Teams-Chatnachrichten.
3. Prüfe die Nachrichtenliste und schließe die Ansicht, wenn du fertig bist.

##### MS Teams-Aufgabenbenachrichtigung
1. Starte die Demo „MS Teams-Aufgabenbenachrichtigung“ über das Demo-Menü.
2. Löse den Aufgabenbenachrichtigungsfluss aus und simuliere bei Bedarf den Azure-AD-Benutzer.
3. Prüfe deinen Teams-Client, um zu bestätigen, dass die Benachrichtigung angekommen ist.

##### Teams Web
1. Starte die Demo „Teams Web“ über das Demo-Menü.
2. Wähle einen Chat aus und durchsuche die webbasierte Nachrichtenansicht.
3. Prüfe die Unterhaltung und fahre mit deinem eigenen Testszenario fort.

## Einrichtung

- **Rollen:** Everybody (konfiguriert in config/roles.xml)
- **OpenAPI:** SpecUrl: https://graphexplorerapi.azurewebsites.net/openapi?tags=me.user,me.calendar,users.calendar,me.message,me.Actions,me.todo,me.site,sites.Actions,me.drive,me.chat,chats.chat,chats.chatMessage&openApiVersion=3&graphVersion=v1.0&format=yaml&style=PowerShell
  Namespace: com.microsoft.graph

### Variablen

```yaml
@variables.yaml@
```

> [!NOTE]
> Das Variablenformat hat sich ab Version 13 geändert.
> Zum Beispiel wurde `microsoft-connector` in `microsoftConnector` umbenannt, und `teams-notification` wurde in `teamsNotification` umbenannt.

### Azure-App

1. Registriere eine Anwendung in Azure, wie im Java-Tutorial von Microsoft beschrieben: https://docs.microsoft.com/en-us/graph/tutorials/java?tutorial-step=2
2. Öffne `Overview` und kopiere die Application (client) ID in `appId` im Abschnitt `microsoftConnector`.
3. Öffne `Authentication` im Azure-App-Menü.
   3.1. Füge im Bereich `Web` eine `Redirect URI` hinzu.
      - Axon Ivy verwendet eine Callback-URI in der Form `{scheme}://{host}:{port}/oauth2/callback`. Registriere diese URI in der Azure-App.
      - Für Axon Ivy Designer kannst du `http://localhost:8081/oauth2/callback` verwenden.
      - Für Axon Ivy Engine verwende die vollständige Engine-URI plus `/oauth2/callback`, zum Beispiel `https://my.workflows.ch/oauth2/callback`.

      ![Set redirect URI](../../doc/img/azure_authCallback.png)

4. Öffne `Certificate & secrets` im Azure-App-Menü.
   4.1. Erstelle ein neues Secret, indem du `New client secret` auswählst, und wähle eine beliebige Laufzeit.

      ![Create new secret](../../doc/img/azure_createSecret.png)

   4.2. Kopiere den generierten Secret-Wert in `secretKey` im Abschnitt `microsoftConnector`.

      ![Copy secret](../../doc/img/azure_copySecret.png)

5. Öffne `API Permissions` im Azure-App-Menü.
   - Füge Berechtigungen über `Add a permission` > `Microsoft Graph` > `Delegated permissions` hinzu.
   - Gewähre alle Berechtigungen, die im `permissions`-Block deiner `variables.yaml`-Datei aufgeführt sind.

      ![Add permissions](../../doc/img/azure_addPermission.png)

6. Starte einen beliebigen Prozess, der sich mit Microsoft 365 verbindet.

## Komponenten

### Aufrufbare Unterprozesse

#### msCalendar.p.json

- **Signatur**: upcomingEvents() -> myEvents: java.util.List<com.microsoft.graph.MicrosoftGraphEvent>
    - Beschreibung: Liest kommende Termine aus deinem Kalender.
    - Eingaben: (keine)
    - Ergebnis:
        - `myEvents` (java.util.List<com.microsoft.graph.MicrosoftGraphEvent>) - Liste mit kommenden Terminen aus deinem Kalender

- **Signatur**: createMeeting(msgraph.connector.NewEvent evt) -> meeting: com.microsoft.graph.MicrosoftGraphEvent
    - Beschreibung: Erstellt eine neue Besprechung in deinem Kalender.
    - Eingaben:
        - `evt` (msgraph.connector.NewEvent) - Der neue Termin, der in deinem Kalender erstellt werden soll
    - Ergebnis:
        - `meeting` (com.microsoft.graph.MicrosoftGraphEvent) - Der Termin, der in deinem Kalender erstellt wurde

#### msChat.p.json

- **Signatur**: recentMessages() -> messages: java.util.List<com.microsoft.graph.MicrosoftGraphChatMessage>
    - Eingaben: (keine)
    - Ergebnis:
        - `messages` (java.util.List<com.microsoft.graph.MicrosoftGraphChatMessage>) - Aktuelle Chatnachrichten

#### msFiles.p.json

- **Signatur**: uploadFile(java.io.File file, String siteId) -> (none)
    - Eingaben:
        - `file` (java.io.File) - Datei, die in eine SharePoint-Site hochgeladen werden soll
        - `siteId` (String) - optional: GUID einer Site; wenn sie leer ist, wird die erste Site, der der Benutzer folgt, verwendet
    - Ergebnis: (none)

- **Signatur**: uploadFile(java.io.File file) -> (none)
    - Eingaben:
        - `file` (java.io.File) - Datei, die in eine SharePoint-Site hochgeladen werden soll
    - Ergebnis: (none)

- **Signatur**: myRecentFiles() -> items: java.util.List<com.microsoft.graph.MicrosoftGraphDriveItem>
    - Eingaben: (keine)
    - Ergebnis:
        - `items` (java.util.List<com.microsoft.graph.MicrosoftGraphDriveItem>) - Zuletzt verwendete Elemente

#### msMail.p.json

- **Signatur**: writeMail(msgraph.connector.NewMail mail) -> message: com.microsoft.graph.MicrosoftGraphMessage
    - Eingaben:
        - `mail` (msgraph.connector.NewMail) - Die E-Mail, die gesendet werden soll
    - Ergebnis:
        - `message` (com.microsoft.graph.MicrosoftGraphMessage) - Die gesendete Nachricht

#### msToDo.p.json

- **Signatur**: allTasks() -> tasks: java.util.List<com.microsoft.graph.MicrosoftGraphTodoTask>
    - Eingaben: (keine)
    - Ergebnis:
        - `tasks` (java.util.List<com.microsoft.graph.MicrosoftGraphTodoTask>) - Liste mit allen ToDo-Aufgaben

- **Signatur**: createNewTask(msgraph.connector.NewToDo task) -> todo: com.microsoft.graph.MicrosoftGraphTodoTask
    - Eingaben:
        - `task` (msgraph.connector.NewToDo) - Die neue ToDo-Aufgabe, die erstellt werden soll
    - Ergebnis:
        - `todo` (com.microsoft.graph.MicrosoftGraphTodoTask) - Die erstellte ToDo-Aufgabe

### Dialogkomponenten

#### WriteMail
- Namensraum: msgraph.mail.demo.WriteMail
- Komponententyp: UI dialog
- Felder: - (keine)
- Zweck: Verfasse eine E-Mail, füge Empfänger hinzu oder entferne sie und sende die Nachricht.

#### Mails
- Namensraum: msgraph.mail.demo.Mails
- Komponententyp: UI dialog
- Felder: - (keine)
- Zweck: Zeige aktuelle Postfachnachrichten in einer schreibgeschützten Liste an.

#### Events
- Namensraum: msgraph.calendar.demo.Events
- Komponententyp: UI dialog
- Felder:
   - `events` (java.util.List<com.microsoft.graph.MicrosoftGraphEvent>) — (not documented in source)
- Zweck: Zeige kommende Kalendereinträge mit Betreff und Zeitangaben an.

#### CreateEvent
- Namensraum: msgraph.calendar.demo.CreateEvent
- Komponententyp: UI dialog
- Felder: - (keine)
- Zweck: Erstelle einen neuen Termin mit Betreff, Beschreibung und Teilnehmenden.

#### Tasks
- Namensraum: msgraph.todo.demo.Tasks
- Komponententyp: UI dialog
- Felder:
   - `todo` (java.util.List<com.microsoft.graph.MicrosoftGraphTodoTask>) — (not documented in source)
- Zweck: Zeige die aktuelle ToDo-Liste in einer klaren, schreibgeschützten Übersicht an.

#### CreateTask
- Namensraum: msgraph.todo.demo.CreateTask
- Komponententyp: UI dialog
- Felder: - (keine)
- Zweck: Erfasse Titel und Inhalt für eine neue Aufgabe und erstelle sie.

#### TeamsWeb
- Namensraum: msgraph.teams.demo.TeamsWeb
- Komponententyp: UI dialog
- Felder: - (keine)
- Zweck: Durchsuche Teams-Chatnachrichten in einer webbasierten Ansicht.

### Web-Services

SpecUrl: https://graphexplorerapi.azurewebsites.net/openapi?tags=me.user,me.calendar,users.calendar,me.message,me.Actions,me.todo,me.site,sites.Actions,me.drive,me.chat,chats.chat,chats.chatMessage&openApiVersion=3&graphVersion=v1.0&format=yaml&style=PowerShell

Namespace: com.microsoft.graph

### Maven-Artefakte

1. msgraph-connector

```xml
<dependency>
  <groupId>com.axonivy.connector.office365</groupId>
  <artifactId>msgraph-connector</artifactId>
  <type>iar</type>
</dependency>
```

2. msgraph-mail-demo *(optional)*

```xml
<dependency>
  <groupId>com.axonivy.connector.office365</groupId>
  <artifactId>msgraph-mail-demo</artifactId>
  <type>iar</type>
</dependency>
```

3. msgraph-calendar-demo *(optional)*

```xml
<dependency>
  <groupId>com.axonivy.connector.office365</groupId>
  <artifactId>msgraph-calendar-demo</artifactId>
  <type>iar</type>
</dependency>
```

4. msgraph-todo-demo *(optional)*

```xml
<dependency>
  <groupId>com.axonivy.connector.office365</groupId>
  <artifactId>msgraph-todo-demo</artifactId>
  <type>iar</type>
</dependency>
```