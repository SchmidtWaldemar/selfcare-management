# Deployment mit Kubernetes

In dieser Version möchten wir das ganze Projekt unter Kubernetes deployen. Bei der Bereitstellung aller Services verwenden wir lediglich unseren lokalen Rechner mit 20 GB RAM. Alle Services laufen genauso wie bei Docker in Containern. Im Prinzip ähnelt sich Kubernetes sehr stark mit Docker und beinhaltet viele Eigenschaften davon. Wer also mit Docker fit ist, sollte sich unter Kubernetes sehr schnell zurechtfinden können.

## minikube einrichten

Damit <b>Kubernetes</b> lokal auf dem System laufen kann, gibt es (zumindest für mich) zwei mögliche Lösungen, um es einzurichten:

1. Kubernetes bei Docker Desktop aktivieren

- Unter Einstellungen > Kubernetes > 'Enable Kubernetes' 

2. Paket installieren

- Beschreibung der Installation unter https://minikube.sigs.k8s.io/docs/start
- und anschließend lässt sich Minikube mit dem Befehl 'minikube start' starten

Beide Lösungen haben auch ihre Tücken, denn z.B. bei Kubernetes unter Docker Desktop (Lösung 1.) werden viele Pakete heruntergeladen und gestartet. Das kann u. U. ein paar Minuten dauern. Erfahrungsgemäß verbraucht diese Lösung auch viel Speicher, was bei vielen Containern zum Nachteil werden kann.

<b>Minikube</b> als Lösung Nr. 2 erhalten wir dagegen eine etwas konsistentere Alternative, die allerdings beim System-Neustart die Verbindung zu unseren Containern verlieren kann. Dabei taucht dann eine Fehlermeldung wie die folgende auf:

```
The connection to the server <ip>:<port> was refused - did you specify the right host or port?
```

Um diese Fehlermeldung aufzulösen, gibt es auch hier zwei Möglichkeiten:

1. Minikube Instanz löschen und neu installieren:

```
minikube delete --all
minikube start
```

2. Minikube mit Docker Hub verbinden:

```
sudo apt install pass
gpg --full-generate-key
# z.B. RSA als Schlüsselart wählen
# dann vorgeschlagene Schlüssellänge wählen
# Verfallsdatum, Email und andere Identifikationsdaten eingeben
# gutes Passwort wählen, den man später häufig eingeben muss
# nach dem Erstellen den Public Key speichern oder später aus dem Dateinamen auslesen unter '~/.gnupg/openpgp-revocs.d/<pub-key-name>.rev'
```

Nun haben wir ein Schlüsselpaar, den wir für den Login bei Docker Hub verwenden können:

```
pass init "<pub-key-name>"
docker login -u <docker hub username> # bei mir: schmidtwaldemar
# docker hub Passwort
```

Damit überlebt nun auch Minikube einen Neustart des Systems. Nach dem Neustart muss man allerdings vorher Docker Desktop öffnen und das Passwort, dass für GPG verwendet worden ist eingeben. Nun ist der Zugang auf die minikube Container mit dem folgenden Befehl wieder möglich:

```
kubectl get pods -n selfcare
```

Der ganze Aufwand oben mit der Verbindung mit Docker Hub ist leider nötig, um die Arbeitsdaten beim Neustart nicht immer wieder neu eingeben zu müssen. Wer für einen Kunden arbeitet und zum Beispiel keinen Zugang zu Docker Hub hat, kann sich hier vom Administrator den GPG Key erstellen lassen und sich bei Docker Hub einmal einloggen lassen. Dadurch muss der Administrator auch die geheimen Credentials Ihnen auch nicht verraten müssen. Nur den Zugriff auf Ihren Rechner z.B. mit SSH müssen Sie hier dem Administrator einmal gewähren.

## Services starten

Als erstes sollten die Infrastruktur Services gestartet und eingerichtet sein, bevor wir unsere Projekt Services betreiben können. Diese Services befinden sich unter 'k8s/infrastructure/'. So wie die Verzeichnisse und Dateien mit einer Aufzählung benannt sind, sollte auch die Reihenfolge beim Start eingehalten werden, z.B:

```
kubectl apply -n selfcare -f k8s/infrastructure/01-namespace/01-namespace.yaml
kubectl apply -n selfcare -f k8s/infrastructure/02-kafka/01-zookeeper.yaml
```

Nachdem Zookeeper gestartet worden ist, sollte noch die interne IP davon ermittelt werden und unter Kafka beim Parameter 'KAFKA_ZOOKEEPER_CONNECT' gesetzt sein:

```
kubectl get svc -n selfcare 
NAME                TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)             AGE
zookeeper-service   NodePort    10.105.7.186     <none>        2181:30182/TCP      29h
```

Dann in der Datei 'k8s/infrastructure/02-kafka/02-kafka.yaml' die IP an der folgenden Stelle ersetzen:

```
- name: KAFKA_ZOOKEEPER_CONNECT
  value: 10.105.7.186:2181
```

Und schließlich Kafka starten:

```
kubectl apply -n selfcare -f k8s/infrastructure/02-kafka/02-kafka.yaml
```

Nun können wir den Status prüfen, ob die Container korrekt gestartet werden konnten und die Logs davon überprüfen:

```
kubectl get pods -n selfcare
kubectl logs -n selfcare svc/zookeeper-service
kubectl logs -n selfcare svc/kafka-service
```

Sobald die Container laufen, starten wir auch die anderen in der richtigen Reihenfolge. 

```
kubectl apply -n selfcare -f k8s/infrastructure/03-database/01-mongodb.yaml
kubectl apply -n selfcare -f k8s/infrastructure/03-database/02-postgres.yaml
kubectl apply -n selfcare -f k8s/infrastructure/03-database/03-mariadb.yaml
```

Bei Postgres und MariaDB müssen anschließend Datenbanken erstellt werden. z.B. bei MariaDB die Datenbank 'groupDB' mit 'CREATE DATABASE groupDB;'. Bei Postgres die Datenbanken 'moderatordb' und 'keycloakdb'. In den vorherigen Beschreibungen haben wir dazu bereits eine Anleitung gegeben.

Dann können wir schließlich auch die weiteren Services starten wie:

```
kubectl apply -n selfcare -f k8s/infrastructure/04-tools/01-maildev.yaml
kubectl apply -n selfcare -f k8s/infrastructure/04-tools/02-keycloak.yaml
```

Und zum Schluss dann unsere Services:

```
kubectl apply -n selfcare -f k8s/services/01-config-service.yaml
kubectl apply -n selfcare -f k8s/services/02-discovery-service.yaml
kubectl apply -n selfcare -f k8s/services/03-gateway-service.yaml
kubectl apply -n selfcare -f k8s/services/04-moderator-service.yaml
kubectl apply -n selfcare -f k8s/services/05-group-service.yaml
kubectl apply -n selfcare -f k8s/services/06-participant-service.yaml
kubectl apply -n selfcare -f k8s/services/07-notification-service.yaml
kubectl apply -n selfcare -f k8s/services/08-frontend.yaml
```

Auch hier sollte immer wieder geprüft werden, ob die Pakete korrekt gestartet werden konnten, indem immer wieder die Logs kontrolliert werden.

Sollten Fehler auftauchen, z.B. weil die Reihenfolge nicht beachtet wurde, kann der Container gelöscht und neu erstellt werden:

```
kubectl delete -n selfcare -f k8s/services/02-discovery-service.yaml
kubectl apply -n selfcare -f k8s/services/02-discovery-service.yaml
```

## services starten

```
$ kubectl get pods -n selfcare
NAME                            READY   STATUS    RESTARTS        AGE
config-7b74ffbc68-ssj6n         1/1     Running   0               7h19m
discovery-6c4677d665-2vdr2      1/1     Running   0               7h18m
frontend-5b48986d76-xgscq       1/1     Running   0               3m23s
gateway-5855b5f886-tn2v5        1/1     Running   0               4h43m
group-6cf795bd4-tfgls           1/1     Running   0               5h3m
kafka-broker-6b56fd8d57-vnjwp   1/1     Running   0               7h25m
keycloak-694fbf97bf-ff2ln       1/1     Running   0               4h35m
maildev-69cd4b797f-56rq6        1/1     Running   0               7h6m
mariadb-6ff98f6f57-cqnmn        1/1     Running   1 (7h23m ago)   7h24m
moderator-5cbddf6cf8-hz4pp      1/1     Running   0               7h9m
mongo-f7dd79f9b-vn9lk           1/1     Running   0               7h13m
notification-bc9f6b4f-s8c8z     1/1     Running   0               7h10m
participant-5cd68f5cc5-t8qvg    1/1     Running   0               4h57m
postgres-f5bd8b6f6-w2vjb        1/1     Running   0               7h24m
zookeeper-7d467df7bf-lxhzv      1/1     Running   0               7h26m
```

### Keycloak Datei importieren

```
# unter dem Container von Postgres einloggen
kubectl exec -it -n selfcare svc/postgres -- /bin/bash

# als Root in Postgres einloggen
psql -U postgres

# Datenbank erstellen und wieder aus dem Container ausloggen
postgres=# create database keycloakdb;

# Datei in den Keycloak Container hochladen
cat ./frontend/keycloak/realm-export.json | kubectl exec -i -n selfcare svc/keycloak -- /bin/bash -c "cat > /tmp/realm-export.json"

# in den Keycloak Container einloggen
kubectl exec -it -n selfcare svc/keycloak -- /bin/bash

# Datei in die Datenbank einlesen
$ /opt/keycloak/bin/kc.sh import --file /tmp/realm-export.json
```

### Port Forwarding

Damit alle Services für uns lokal erreichbar sind, müssen wir die Container erreichbar machen. Dazu nutzen wir das Port-Forwarding wie folgt:

```
# System Status durch Eureka
kubectl port-forward -n selfcare svc/discovery 8761:8761

# Gateway
kubectl port-forward -n selfcare svc/gateway 8222:8222

# Participant
kubectl port-forward -n selfcare svc/participant 8010:8010

# Group Microservice
kubectl port-forward -n selfcare svc/group 8020:8020

# Keycloak
kubectl port-forward -n selfcare svc/keycloak 7080:7080

# Mederator
kubectl port-forward -n selfcare svc/moderator 8040:8040

# Maildev
kubectl port-forward -n selfcare svc/maildev 1080:1080

# Frontend
kubectl port-forward -n selfcare svc/frontend 4200:80
```

### Keycloak + Kafka

Damit Keycloak und Kafka unter Kubernetes von den Microservices korrekt angesprochen werden können, müssen wir zunächst jeweils einen Eintrag in die Host Datei vornehmen:

```
sudo echo "127.0.0.1 keycloak.selfcare.svc.cluster.local" >> /etc/hosts
sudo echo "127.0.0.1 kafka-service.selfcare.svc.cluster.local" >> /etc/hosts
```

### Services Deployen

Ähnlich wie bei den CI/CD Piplines mittels der GigHub Actions lassen sich auch durch Maven die Builds deployen. Allerdings werden dazu mehrere manuelle Aufrufe nötig, damit die Builds nach dem Prinzip mererer paralleler Versionen auf Docker Hub hochgeladen werden. Z.B. möchten wir dass die letzte und aktuelleste Version identisch mit der 'latest' ist. Dazu rufen wir schließlich bis auf die Frontend (wegen Angular) und Moderator (wegen Quarkus) die Befehle wie folgt auf:

```
cd backend/gateway
mvn spring-boot:build-image -DskipTests -DactivatedProperties=k8s -DdockerPassword=<Token bei Docker Hub>
mvn spring-boot:build-image -DskipTests -DactivatedProperties=k8s -DprojectVersion=latest -DdockerPassword=<Token bei Docker Hub>
```

Der erste Aufruf erstellt eine Version unter Docker Hub, die z.B. den Versions-Prefix '' enthällt. Der zweite Aufruf nutzt den Tag 'latest' und überschreibt dadurch unter Docker Hub die letzte Version durch die Aktuelle.

Beim Moderator sieht der Build etwas anders aus:

```
cd backend/moderator
./mvnw clean package k8s:build -DskipTests -DactivatedProperties=k8s
username=schmidtwaldemar
password=<Token bei Docker Hub>
./mvnw k8s:push -Djkube.docker.push.username=$username -Djkube.docker.push.password=$password -DactivatedProperties=k8s
```

<b>Achtung</b>: Wer docker-compose nutzt, sollte bei der application.properties die auskomentierten URL Parameter entsprechend aktivieren.

Beim Frontend haben wir es mit Angular zu tun und können auf unser spezielles Dockerfile zugreifen:

```
docker build -t schmidtwaldemar/selfcare-frontend-k8s:latest -f ./frontend/Dockerfile-k8s ./frontend/
# Passwort von GPG eingeben
docker push schmidtwaldemar/selfcare-frontend-k8s:latest
```

### Hilfreiche Tipps

Bei Kubernetes läuft es meistens darauf hinaus, die Netzwerkeinstellungen korrekt zu setzen, damit die Cluster miteinander kommunizieren können. Darum kann es hilfreich sein, die Netzwerkeinstellungen unter den Containern zunächst zu prüfen, bevor diese Einstellung bei der jeweilgen Konfigurationsdatei übernommen wird. Geholfen haben mir hier folgende Befehle und Aufrufe:

 
```
kubectl run -it --tty --rm debug --image=alpine --restart=Never -n selfcare -- sh

/ # nslookup kafka-service
Server:		10.96.0.10
Address:	10.96.0.10:53

** server can't find kafka-service.svc.cluster.local: NXDOMAIN

** server can't find kafka-service.cluster.local: NXDOMAIN

Name:	kafka-service.selfcare.svc.cluster.local
Address: 10.109.56.13
```

Im Beispiel oben finden wir heraus, wie wir auf unseren Kafka Service zugreifen können. Dabei können wir zwischen der dynamisch wechselnden IP-Adresse oder der Domain 'kafka-service.selfcare.svc.cluster.local' wählen.

Falls nach dem Test das Pod beim erneutem Start stören sollte:

```
kubectl delete pod debug -n selfcare
```

#### Probleme beim Deployment

Es kommt hin und wieder vor, dass sich einige Deployment Pakete von Docker Hub nicht richtig abholen lassen. Dabei tritt bei Kubernetes der Status-Fehler 'ImagePullBackOff' wie folgende auf:

```
kubectl get pods -n selfcare 
NAME                            READY   STATUS             RESTARTS     AGE
...
gateway-6dcc96bdb7-sv9j6        0/1     ImagePullBackOff   0            18m
```

Das Paket unter Docker Hub zu entfernen und neu zu erstellen hat bei meinen Tests nicht geholfen. Stattdessen konnte ich diesen Fehler nur durch eine Namensänderung umgehen wie zum Beispiel beim Hinzufügen des Prefix '-2' beim Paketnamen.

Wer eine Lösung für dieses Problem kennt, bitte Bescheidgeben.