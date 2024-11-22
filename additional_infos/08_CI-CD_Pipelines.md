# CI/CD Pipelines mit Microservices

Bei den Recherchen im Internet fand ich nur Bruchstücke von Beschreibungen, wie die CI/CD unter GitHub aufgebaut und betrieben werden kann. Besonders zu den maßgeschneiderten Pipelines, wo z.B. Frontend und Backend getrennt behandelt werden und wo Microservices unter Docker im sicherem Modus laufen, wird nirgends der eine goldenen Weg beschrieben, an dem man sich gut orientieren kann.
Deshalb möchte ich bei dieser Version einen solchen Aufbau dokumentieren, was hoffentlich anderen IT-Architekten gut weiterhelfen kann.

Ohne eine Skizze zeigen zu müssen, begnügen wir uns hier nur mit einer Wortbeschreibung aller Vorhaben und des Architekturaufbaus:

* Das Deployment soll sowohl auf dem lokalen Rechner mit dem 'docker-compose' Befehl gestartet werden können, als auch durch die Actions (CI/CD Pipelines) innerhalb des GitHub.
* Schützenswerte und variable Informationen wie Passwörter und IPs sollen unter Secrets von GitHub verwaltet werden.
* Frontend und Backend laufen auf zwei getrennten Systemen.

## Einleitung:

Im Vergleich zum normalen Entwickeln auf Codebasis wie einer Applikation unter Spring Boot, erfordert die Administration von Pipelines andere Sichtweisen beim Blick auf die verwendeten Systeme. Schnell kann man sich hier in der Konfigurationswelt verirren, wodurch plötzlich nichts mehr läuft, weil man nur einen Parameter auf dem falschen Platz gesetzt hat oder einen Buchstaben falsch geschrieben hat. Um so hilfreicher kann ein funktionierendes Anwendungsbeispiel sein, wo möglichst viele Anforderungen vom angestrebten Gesamtkonzept erfüllt werden. Insgesamt kann man die in dieser Version vorgenommenen Abläufe so formulieren:

### Verwendete Systeme:
- Docker Hub
- GitHub
- Proxmox mit separatem Rechner
- lokaler Arbeitsrechner

### Test Git-Branch anlegen

Vor dem Start wird ein neuer Branch verwendet, wo wir die CI-Pipelines testen möchten:

Unter GitHub einen Branch anlegen und wie in unserem Fall auschecken:

```
git checkout -b test/pipeline
```

Sobald wir fertig sind, können wir den Branch wieder auf den Master bzw. main setzen. Dazu verwenden wir den folgenden Befehl:

```
git rebase main
```

### Docker Hub Account anlegen

Es gibt die Möglichkeit den Docker Hub Account direkt mit dem GitHub zu verbinden. Nach der Registrierung unter 'https://hub.docker.com'. Dann erstellen wir einen neuen s.g. 'Personal access token', den wir unter den Settings durch Klick auf 'Generate new token' anlegen. Den Token bewahren wir auf, da wir den noch für GitHub verwenden möchten. Bei Docker Hub existiert auch ein Benutzername, wie z.B. bei mir der durch GitHub übernommen worden ist: 'schmidtwaldemar'. Beides werden wir gleich unter GitHub unter den Secrets Varialben speichern, damit GitHub darauf Zugriff hat, sobald wir unsere Anwendung durch 'git push' dort hochgeladen haben.

### Proxmox Hosts aufsetzen

Erstmal laden wir Ubunu Server mit der Version 22.04 als ISO herunter. Anschließend den Proxmox unter 'proxmox.com' und 'Proxmox Virtual Environment'. Die ISO Datei installieren wir nun auf dem dafür vorgesehenem Rechner z.B. mit einem USB-Stick, wo die ISO geladen ist.
Ideal ist es, wenn der Rechner die folgenden Mindestanforderungen erfüllt:

- CPU: 4 virtuelle Kerne
- RAM: 8 GB
- Festplatte: 128 GB

Im Normalfall muss der Rechner noch im BIOS Modus angepasst werden, damit Virtualisierung funktionieren kann. Unter den Prozessor Einstellungen sucht am besten eine Einstellung die z.B. wie folgt genannt wird: 'Intel(R) Virtualization Technology'. Hier von Disabled auf Enabled setzen und die Einstellung speichern. Sollte der Rechner vom Strom genommen werden, kann es passieren, dass diese Einstellung wieder zurückgesetzt wird. Dann muss erneut die Virtuallisierung aktiviert werden. 

Der Rechner, wo Proxmox läuft, sollte auch wenn möglich direkt durch den LAN-Kabel mit dem Internet verbunden sein. Mit WLAN kann es hier u.U. zu Problemen führen, wenn der WLAN-Stick vom Proxmox Betriebssystem nicht erkannt werden kann. Sobald Proxmox installiert und gestartet ist, können wir nun ein Update durchführen. Am Anfang kann es unter Umständen kein Problem sein, dass Proxmox z.B. in der Testphase die Updates mit 'sudo apt update' durchführt. Nach der Testphase kann man dann etweder für die Enterprice Version zahlen oder einfach die Sourcen auf die kostenlose Version ändern: 

https://pve.proxmox.com/wiki/Package_Repositories

Nun können wir Proxmox unter dem gleichen Netz unter dem Browser aufrufen. Wer jedoch die IP nicht kennt, findet diese mit einem einfachen 'ifconfig' Befehl heraus:

```
sudo apt install net_tools
ifconfig
```

Jetzt nur noch die IP und Port im Browser eingeben: https://&lt;IP:8006>

Links sehen wir nun ein Menü "Datacenter > pve > local (pve)" wo wir draufklicken. Dann auf 'ISO Images' und laden hier unsere Ubuntu Server ISO hoch.
Danach klicken wir auf pve mit Rechtsklick und hier auf 'Create VM'. Die erste VM nennen wir 'vps-ui'. Bei der CPU verwenden wir mindestens 2 virtuelle CPUs, 3 GB RAM und 25 GB Festplattenspeicher.

Bei den anderen VMs installieren wir den 'vps', der später unsere Backend Microservices startet und den 'hub-runner' der die Pipelines von GitHub holt und ausführt. Bei beiden Maschinen verwenden wir mindestens 2 virtuelle CPUs, 3 GB RAM und 30 GB Festplattenspeicher.
 
Dann einfach überall uner 'Console' Ubuntu installieren und dabei möglichst die Standardeinstellungen akzeptieren. Beim setzen des Benutzernamens auch hier am besten überall den gleichen Usernamen verwenden.

Als Übersicht haben wir bei uns folgende Anfoderungen:

mindestens 3 virtuelle Rechnersysteme (besser 3 + lokal)

```
VM 1:
  Verwendung: Runner von GitHub 
  Hyperwiser: Proxmox
  OS: Ubuntu Server 22.04
  mindestens 30 GB Festplattenspeicher
  mindestens 3 GB RAM
  lokale IP: 192.168.178.148

VM 2:
  Verwendung: Backend Deployment
  Hyperwiser: Proxmox
  OS: Ubuntu Server 22.04
  mindestens 30 GB Festplattenspeicher
  mindestens 3 GB RAM
  lokale IP: 192.168.178.157
  
VM 3:
  Verwendung: Frontend Deployment
  Hyperwiser: Proxmox
  OS: Ubuntu Server 22.04
  mindestens 25 GB Festplattenspeicher
  mindestens 3 GB RAM
  lokale IP: 192.168.178.159
```

### GitHub Secret Varialben setzen

Bei GitHub wählen wir unser Projekt aus, den wir mit den CI-Pipelines triggern möchten. Hier unter "Settings > 'Secrets and varialbes' > Actions" können wir nun unsere ganzen geheimen Passwörter und sonstigen Variablen setzen. Bei uns existieren nun folgende Secrets:

```
DOCKERHUB_TOKEN=<der Token von Docker Hub>
DOCKERHUB_USERNAME=<der Username wie in unserem Fall schmidtwaldemar>
VPS_IP=<IP Adresse von unserer 'vps' Maschine aus Proxmox>
VPS_UI_IP=<IP Adresse von unserer 'vps-ui' Maschine aus Proxmox>
VPS_USERNAME=<da bei Proxmox der gleiche Username verwendet wird, hier den einen Namen eingeben>
```

### GitHub Runner anlegen und starten

Nun haben wir unter GitHub erneut unser Projekt ausgewählt und wechseln zu den Settings. Hier unter Actions > Runners klicken wir auf 'New self-hosted runner'. Sollte hier bereits ein offline Runner vorhanden sein, können wir den getrost entfernen, wie weiter unten beschrieben.

Direkt nach dem erstellen eines Runners bekommen wir Scripte angezeigt. Diese Scripte führen wir später auf unserer VM 'hub-runner' aus. Am besten speichern Sie diese Scripte, da darin der Token nur einmal angezeigt wird und nach einiger Zeit der Inaktivität ungültig gemacht wird.

### 'hub-runner' in Betrieb nehmen

Zuerst brauchen wir den 'jq' JSON Intepreter, den wir wie folgt installieren:

```
sudo apt install jq
```

Nun setzen wir die Lese- und Schreibrechte beim Docker Socket:

```
sudo chmod 666 /var/run/docker.sock
```

Nun sollte nur der hub-runner mit den vps per SSH sprechen können. Dazu bauen wir die Verbindungen wie folgt auf:

```
ssh-keygen
ssh-copy-id username@<IP vom vps-backend>
ssh-copy-id username@<IP vom vps-ui>
```

Wenn nun die Verbindung z.B. per 'ssh username@vps-backend' direkt aufgeht, dann nächsten Schritt überspringen:

```
eval $(ssh-agent -s)
ssh-add ~/.ssh/id_pub
```

Nun sollte dann auf beide VPS Rechner eine direkte Verbindung vorhanden sein. 

<b>Achtung:</b> die gleiche Commandline bzw. Konsole verwenden um die nächsten Schritte auszuführen!

Nun führen wir die von GitHub angezeigten Scripte der Reihe nach aus und stellen somit eine dauerhafte Verbindung zw. unserem Runner und GitHub auf.

### Hinweise zu Pipelines

Damit unsere CI/CD Pipeline Prozedur funktionieren kann, sind dazu die folgende Dateien erforderlich:

```
.github/workflows/pipeline-backend.yml
.github/workflows/pipeline-frontend.yml
```

In diesen Dateien werden die Dateien des Projekts durchforstet und dabei folgende Schritte ausgeführt:

1. Der Branch wie in unserem Fall der 'test/pipeline' verwendet.
2. Die benötigten Pfade im Projekt beschrieben.
3. Die erforderliche Java Version geladen und die Kompilierung der Java Dateien durchgeführt.
4. Mit Maven werden die Packages erstellt.
5. Eine Verbindung zu Docker Hub hergestellt.
6. Die Versionen von den Microservices herausgelesen und in eine Variable gespeichert.
7. Die jeweiligen Dockerfiles werden nun ausgeführt. Dabei werden auch Parameter mit übermittelt.
8. Die mit Docker erstellten Container werden dann bei Docker Hub hochgeladen. Dabei wird auch die Versionierung mit beachtet. Übrigens: gleiche Versionen werden direkt übersschrieben.
9. Beim Deployment werden die Daten vom Runner direkt an die VPS übermittelt. Auch die Angaben zu den Versionen werden dynamisch übermittelt, die später u.a. beim Docker Compose verwendet werden.

Die Rechenarbeit dieser ganzen Schritte erfolgt direkt bei unserem Runner. GitHub dient hier lediglich als Vermittler und Docker Hub als Backup bzw. Bereitsteller der versionierten Paketen. Die pipeline*.yml Scripte sollten eigentlich für sich sprechen. Wer diese Nutzen möchte, muss also vorher dieses Projekt bei sich unter GitHub erst hochladen müssen oder ein ähnliches eigenes Projekt aufsetzen. Am Ende kann unser Proxmox Server dauerhaft laufen und wir haben so eine vollständig funktionierende Web-Anwendung im lokalem Netzwerk.

Alternativ könnt Ihr die gesammte Anwendung auch mit Docker Compose wie folgt starten:

```
# backend start
docker-compose -f docker-compose.yml up --build -d

# frontend start
ng serve
```

### Hilfreiche Befehle und Hinweise:

Sollte zu wenig Speicher vorhanden sein, weil bereits öffter mit Docker Builds durchgeführt worden sind, dann heißt es aufräumen mit:

```
docker system prune -a
```

#### dynamische URL nach OpenApi Änderung

Sobald mit OpenApi die Schnittstelle neu kompiliert wird, erhalten die Services 'moderator', 'group' und 'participant' statische rootUrl unter 'api-configuration.ts'. Diese URL sollte wie folgt dynamisch gesetzt werden:

```
import { environment } from '../../../environments/environment';
...
rootUrl: string = environment.backendProtocolSchema + '://' + environment.backendHost + ':' + environment.backendPort;
```

Damit können die Parameter aus der Umgebungsvariablen eingesetzt werden, sobald sich das Deployment entsprechend ändern sollte.


#### Moderator CORS Variable anpassen

Unter dem Moderator Microservice müssen u.U. die Parameter bei den CORS unter 'application.properties' angepasst werden, damit die Kommunikation zwischen Frontend und Backend funktionieren kann. Bei anderen Microservices ist die CORS origin Prüfung deaktiviert. Kann aber leicht implementiert werden. Eine Lösung findet man z.B. ausgeklammert im Gateway Projekt.

#### runner nicht entfernt, start nicht möglich

Sobald der Token für den Start des Runners abgelaufen ist, muss dieser erst beim Runner Host entfernt werden und anschließend der Runner mit einem neuen Token gestartet werden. GigHub zeigt für gewähnlich den Befehl für das Entfernen des alten Runnner Befehls nur einmal. Deshalb kommt es auch mal vor, dass man den alten Runner Befehl nicht mehr löschen kann (durch den Befehl './config.sh remove &lt;token>') und damit den neuen Runner nicht mehr starten kann (durch den Befehl ./config.sh --url &lt;github Projekt URL> --token &lt;token>). 
Dafür gibt es eine einfache Lösung: Entfernt die Datei '.runner' im Verzeichnis, wo der Runner gestartet wird. Danach könnt Ihr den neuen Runner mit den neuen Token initialisieren und wieder starten.

#### Beim Frontend beim Weiterleiten zu Keycloak wird auf falsche Adresse verwiesen

Sobald sich die Adresse ändert (statt localhost wird eine IP genutzt), muss auch unter Keycloak die redirect-url angepasst werden. Dazu geht Ihr einfach auf das von uns genutzte Realm 'selfcare-network' und dort unter Clients auf 'selfcare'. Nun sollten unten 4 Adressen mit 'localhost:4200' gesetzt sein. Diese in unserem Fall auf 'http://192.168.178.159' ändern und dann sollten die Weiterleitungen wieder funktionieren.

#### MongoDB wird eine veraltete Version verwendet

Bei unseren Tests unter Proxmox konnten wir keine aktuelle Version von MongoDB verwenden, was die Fehlermeldung '... CPU with AVX support' bestätigte. Deshalb erfolgte bei uns ein Downgrade auf eine 4x Version. Im Livesystem sollte man daher bei docker-compose.yml die Version entfernen oder stattdessen 'latest' verwenden.


