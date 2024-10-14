# GUI durch Angular

Die GUI Ansicht wird dynamisch mithilfe der OpenAPI und Swagger UI erstellt. Anschließend werden die API Zugänge mit einem Angular Tool erzeugt und die daraus generierten Methoden für den Zugriff durch Frontend auf Backend verwendet.

## wie gingen wir vor?
Die folgenden Schritte beschreiben nur Installationsschritte, die wir während der Implementierung angegangen sind. Diese Schritte müssen beim Projektstart nicht erneut durchgeführt werden.

### Angular Projekt neu erstellen

```
cd ~/git/selfcare-management/
ng new frontend --routing --standalone=false
```

### Look and Feel

```
cd frontend/
npm i bootstrap@latest
npm i @fortawesome/fontawesome-free
```

Beide Pakete haben wir dann unter styles.scss importiert.

### OpenApi Generator mit SwaggerUI

Zuerst brauchen wir die Daten aus dem Backend. Diese erhalten wir beim Aufruf des folgenden Links im Browser:

http://localhost:8222/swagger-ui.html

Info: einige Funktionen sollen von Außen (durch die GUI) nicht aufgerufen werden. Dazu fügen wir über die entsprechenden Funktionen die Annotation @Hidden hinzu.

Hier können wir oben zwischen unseren Services wählen. Z.B. bei 'Group Service' klicken wir auf den Link '/group-service/v3/api-docs'. Wir erhalten JSON Informationen, die wir als Rohdaten kopieren und beim Angular unter 'frontend/src/openapi/group/openapi.json' speichern. Gleiches machen wir bei der 'Participant Service'. Sobald unsere REST-API (in den Controller Dateien) sich einmal ändern, können wir so immer wieder diese openapi.json Dateiinhalte ersetzen und die folgenden Schritte wiederholen.

Damit wir die OpenApi Service Dateien unter Angular dynamisch erzeugen können, müssen wir vorher im frontend Ordner die ng-openapi-gen installieren:

```
npm i ng-openapi-gen
```

Innerhalb der package.json zwei Kurzbefehle hinzugefügt zum erstellen der OpenApi services.
Danach können die APIs wie folgt erstellt werden:

```
npm run group-api-gen
npm run participant-api-gen
```

Jetzt nur noch die Seiten erstellen, die wir anschließend als GUI aufrufen möchten:

```
mkdir src/app/pages
ng g c groups
ng g c register
```

## Troubleshoting
Um die aktuelle Angular Version unter Ubuntu zu nutzen, erfordert es in der Regel die neueste Version von <b>Node.js</b> zu installieren bzw. ein Upgrade davon. Die oftmals bereits installierte Version per apt-get kann zum Frust führen: Die alte Version muss erst restlos deinstalliert sein, damit die aktuelle Version rein kann.

Am besten die alte Version dann wie folgt entfernen und wenn auch die Deinstallation schiefgeht:

```
sudo apt-get remove nodejs --purge
sudo apt-get --fix-broken install
```

Nun kann die neue Version z.B. die 18 installiert werden:

```
url -sL https://deb.nodesource.com/setup_18.x -o nodesource_setup.sh
sudo bash nodesource_setup.sh
sudo apt install nodejs -y
```

Wenn alles geklappt hat, sollte die neue Version mit dem Befehl erscheinen:

```
node -v
```
