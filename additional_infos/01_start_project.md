# Projekt starten
Während der Entwicklung wird Ubuntu 22.04 eingesetzt. Sollte ein anderes Betriebssystem verwendet werden, kann die Syntax entsprechend abweichen.

## Voraussetzungen installieren
Bevor das Projekt gestartet werden kann, sollten folgenden Anwendungen vorher installiert sein:

* Java 17 (falls aktueller, dann auch in den pom.xml innerhalb der Projekte manuelle Anpassung u.U. nötig)

```
sudo apt install openjdk-17-jdk
```

* Maven

```
sudo apt install maven
```

* Eclipse

Unter: https://www.eclipse.org/downloads/

Nach dem Start von Eclipse kann nun Spring Boot unter "Help > Eclipse Marcetplace..." installiert werden. Hier nach "Spring" suchen und "Spring Tools 4" installieren.

Anschließend sollte Lombok installiert sein. Hier unter "Help > Install New Software..." wechseln und die folgende Adresse eintragen: 'https://projectlombok.org/p2'. Danach sollte Lombok auswählbar sein. Die Installation kann erfolgen, wenn auch die Zertifikate akzeptiert werden.

* Git

```
sudo apt install git
```

* Docker / Docker Compose

```
# Quellen laden

sudo apt update
sudo apt install ca-certificates curl
sudo install -m 0755 -d /etc/apt/keyrings
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
sudo chmod a+r /etc/apt/keyrings/docker.asc

echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
  $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
sudo apt update

# Pakete laden

sudo apt install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin docker-compose

```

* Docker Desktop (optional)

Unter: https://www.docker.com/products/docker-desktop/

* Postman (optional)

Unter Ubuntu Software zu finden oder unter: https://www.postman.com/downloads/

## Start

Nun kann das Projekt heruntergeladen werden:

```
cd ~
mkdir -p git
cd git/
git clone https://github.com/SchmidtWaldemar/selfcare-management.git
```

Jetzt noch die nötigen Packete mit Docker installieren und starten:

```
cd ~/git/selfcare-management/
docker-compose -f docker-compose.yml up --build -d
```

Sobald alles installiert und gestartet ist, lässt sich der Status mit <b>Docker Desktop</b> näher überprüfen. 

Falls Eclipse verwendet wird, müssen die ganzen Projekte erst einmal importiert werden. Hier empfehle ich erst das Root-Projekt 'selfcare-management' zu importieren. Sollte Eclipse die einzelnen Services im 'backend' Ordner nicht automatisch als Maven Projekt importieren, dann können diese im Nachgang ebenfalls einzeln importieren. 

Danach können die Projekte einzeln entweder als 'Spring Boot App' oder 'Java Application' starten. Hier wird empfohlen die Start-Reihenfolge wie folgt zu wählen:

1. config-server
2. discovery
3. gateway
4. participant
5. group
6. notification

Sobald alle Projekte gestartet sind, lässt sich mit <b>Eureka</b> unter 'http://localhost:8761' der Status der Registrierung der Services Gateway, Participant, Group und Notification genauer überprüfen.

Achtung: bei meiner 

## Stop
Soll das Projekt wieder komplett gestoppt werden, so werden i.d.R. zwei Schritte nötig:

1. Unter Eclipse alle Projekte stoppen
2. Mit Docker Compose folgenden Befehl ausführen:

```
docker-compose down
```

## Troubleshoting
Eine Cloud zu starten, die noch dazu mit mehreren Microservices läuft, kann immer wieder zu Problemen führen. Aus eigenen Erfahrungen kann ich daher folgende Tips geben, wenn es mal nicht ganz so Rund läuft:

### Docker Container starten nicht richtig
Dazu hilft mir oft die folgenden Komandos weiter:

Status der Container kontrollieren.

```
docker ps
docker-compose ps
```

Bei <b>Docker Desktop</b> lässt sich auch jedes Container auf deren Starter LOGs hin überprüfen. 

Manchmal hat man bereits ein Service bereits installiert und am laufen. Z.B. wenn MySQL schon auf dem Betriebssystem läuft, kann Docker einen weiteren Container nicht unter dem gleichen Port starten. Hier kann es z.B. helfen den Service (in diesem Fall MariaDB) zu stoppen:

```
sudo systemctl stop mariadb.service
```

Wenn auch das nicht hilft, sollte man einfach mal den Port prüfen:

```
sudo lsof -i :3306
```

Sind hier welche gelistet, lassen sich diese z.B. mit dem Befehl 'kill <port>' stoppen.
