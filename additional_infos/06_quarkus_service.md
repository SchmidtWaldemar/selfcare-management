# Neues (Micro-)service mit Quarkus

In der heutigen Version wird ein weiteres Microservice für die Verwaltung von Moderatoren hinzugefügt. Genauer gesagt handelt es sich nicht wirklich um ein Microservice, sondern um ein eigenständiges Projekt, dass mit dem Framework <b>Quarkus</b> realisiert ist. Quarkus bietet viele Vorteile gegenüber Spring Boot, wie zum Beispiel bei der schnelleren Ausführung von Anfragen. Spring boot und Quarkus teilen sich zwar unterschiedliche Pakete, doch die Struktur ist hier dennoch anders, was bei der Nutzung einem schnell auffällt. 

Selbst wollte ich zeigen, dass eine hybride Erweiterung (wie in diesem Fall) um einen komplett andren Frameworks ebenfalls relativ einfach umzusetzen ist. Der Start von Quarkus funktioniert zwar etwas anders als Spring boot, wenn erstmal der Dreh einmal raus ist, findet man sich ziemlich schnell in die Umgebung rein.

## Einstellungen

Wir nutzen erneut PostgresSQL, allerdings mit einer neuen Datenbank. Mit Docker-Desktop unter 'Exec' kann man die Datenbank wie folgt erstellen:

```
psql -U postgres
postgres=# CREATE DATABASE moderatordb;
```

Für die GUI verwenden wir erneut Swagger bzw. OpenApi generierte REST-API Schnittstellen, die wir unter 'frontend/openapi/moderator' ablegen:

```
http://localhost:8080/q/swagger-ui/
```

Nun können wir das Quarkus Projekt starten. Eclipse bietet unter dem Marketplace zwar ein Quarkus Tool, allerdings kann ich durch eigene Erfahrungen eher davon abragen. Ob die aktuelle Version von Spring Boot und Quarkus sich irgendwie beeinflussen, kann ich nicht sagen, bei mir war die Entwicklung mit beiden Frameworks irgendwann wegen unzähligen Abstürzen nicht mehr möglich. 
Hier kann ich stattdessen dei Verwendung von Visual Studio Code empfehlen. 

Der Start des unter 'backend/moderator' befindenen Projekts kann mit dem folgenden Befehl erfolgen:

```
./mvnw compile quarkus:dev
```

Danach kann unter der GUI von Angular der Link 'Moderator hinzufügen' verwendet werden, um den Vor- und Nachnamen vom Moderator unter dem neuen Projekt zu speichern und eine Nachricht durch 'Kafka' im 'notification' Microservice zu speichern.

### Hinweis

Die Übertragung eines (JSON-)Objekts von Quarkus über Kafka nach Spring Boot funktionierte bei meinen Tests nicht. Warum die Deserialisierung der Objekte grundsätzlich schiefgelaufen ist, konnte ich nicht herausfinden. Gut möglich, dass Quarkus die Anfragen durch Kafka an andere Frameworks außer Quarkus selbst noch nicht unterstüzt. Deshalb werden alle Nachrichten unter Kafka per String übermittelt und anschließend auf die Objekte gemappt. Sollte Jemand eine gute Lösung kennen, wie man von Quarkus Klassenobjekte an Spring Boot übermitteln kann, wäre ich über eine Lösungsbeschreibung sehr erfreut.
