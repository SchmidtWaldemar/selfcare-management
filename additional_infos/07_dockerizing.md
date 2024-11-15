# Microservices unter Docker

Bei der letzten Version mussten wir die Microservices manuell starten und nur die erweiterten Komponenten wie Keycloak sind durch Docker gestartet worden. In der aktuellen Version können wir praktisch alle Services mit Docker Compose starten und aktuell nur Lokal ausführen.
Diese Containisierung soll uns später helfen, unsere Builds unter Docker Hub zu verwalten und per Kubernetes zu veröffentlichen. Dadurch können unsere Container nach belieben hoch- und runterskalieren.

## Start

Bevor wir den Container mit Quarkus für das Moderator Projekt starten, müssen wir vorher das Projekt wie folgt kompilieren:

```
cd backend/moderator
./mvnw compile quarkus:dev
# oder
./mvnw package
```

Danach starten wir das Docker Compose:

```
docker-compose down 
docker-compose -f ../docker-compose.yml up --build -d
```

Ob alles erfolgreich gestartet werden konnte, kontrollieren wir am besten unter Docker Desktop. Der Start kann je nach Rechner unterschiedlich lang dauern. Die Abhängigkeiten der jeweiligen Container wird bereits durch einen s.g. 'healthcheck' getimed, weshalb die Reihenfolge hier nicht weiter berücksichtigt werden muss.

### localhost

Während Implementierens gab es beider bisherigen Netzwerkeinstellung zunächst Probleme. Besonders bei Keycloak war die Hostadresse innerhalb und außerhalb der Container nicht immer erreichbar. Deshalb haben wir hier einen Trick durch das s.g. 'alpine' ausgenutzt, indem wir die 'localhost' Adresse als Standard gesetzt haben, wodurch alle Ports nur noch durch diesen Container durchgeleitet werden. Wie durch eine Art Gateway können nun so alle Verbindungen sowohl innerhalb als auch außerhalb der Container erfolgreich aufgebaut werden. 

### UI

Bei der UI durch Angular gab es trotz starker CPU Probleme bei der Rechenleistung, sobald die GUI durch den Container gestartet worden ist. Deshalb kann ich hier nur empfehlen, die GUI separat außerhalb von Docker zu starten. Sollte Jemand eine Ressourcenschonende Lösung kennen, um ca. 10 Container + UI innh. eines Containers zu starten, dann kontaktiert mich gerne hierzu.k
