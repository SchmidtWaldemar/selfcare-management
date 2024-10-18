# Authentifizierung mit OAuth2

In der aktuellen Version wird <b>Keycloak</b> als Authentifizierungsquelle herangezogen. Mit Keycloak sparen wir uns die umständliche Implementierung mehrerer Sicherheitskomponenten wie zum Beispiel unter dem Projekt [selfcare-connection](https://github.com/SchmidtWaldemar/selfcare-connection).
Außerdem funktioniert Spring Boot und Angular hervorragend mit Keycloak zusammen, weshalb auch weitere Anpassungen möglich sind.

## Erweiterungen installieren

Mit Docker Compose muss erst Keycloak geladen werden:

```
docker-compose -f docker-compose.yml up --build -d
```

Auch Angular muss aktuallisiert werden:

```
npm install
```

Nun können wir auf die Keycloak Seite wechseln:

```
http://localhost:7080/
```

Für Admin ist der Benutzername und Passwort auf 'admin' gesetzt. Damit der Start einfach klappt und Konfigurationen erst nicht händisch gesetzt werden müssen, sollte erst die Datei unter 'frontend/keycloak/realm-export.json' unter Keycloak importiert werden. Dazu klicken Sie oben auf den Selectfeld, wo 'Keycloak - master' steht, dann auf 'create realm' und importieren Sie die Datei entsprechend.
Damit sollten alle Einstellungen erstmal reichen, um die GUI durch Angular zu starten und damit Keykloak zu nutzen:

```
ng serve
```

Die Registrierung Seite, die in der Vorversion vorhanden war, wurde nun ausgeblendet. Stattdessen werden Sie auf eine neue Login Seite weitergeleitet und ein Login klappt z.B. mit dem Benutzernamen: 'test@test.de' und Passwort: 'test', die unter der Rubrik Users gefunden werden. 

Eine Registrierung neuer Benutzer funktioniert auch. Im Hintergrund registriert sich ein neuer Nutzer, sobald die Seite Gruppenübersicht aufgerufen wird. Die Authentifizierung erfolgt mittels 'JWT-Token' und 'OAuth 2.0'.

Auch die Kommunikation unter den Microservices verläuft ähnlich. Hier wird der JWT-Token lediglich mittels einem Interceptor im Header durchgeleitet. Damit wird auch die Kommunikation unter den Microservices relativ sicher.