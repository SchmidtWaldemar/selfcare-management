# E-Mail Benachrichtigung

Bei den letzten Versionen haben die Teilnehmer noch keine E-Mail erhalten können. In dieser Version ist für diesen Zweck ein s.g. <b>maildev</b> Tool verwendet worden. Dieser E-Mail Server / Client lässt sich sehr einfach mit docker einbinden und als Webinterface z.B. für die Entwicklung einsetzen. 

Die E-Mail Inhalte werden mit <b>Thymeleaf</b> erstellt und lassen sich dadurch auch flexibel im Layout verändern. Um das Tool einzubinden, reicht es den folgenden Befehl einmal aufzurufen:

```
docker-compose -f docker-compose.yml up --build -d
```

Die Weboberfläche ist anschließend im Browser durch folgende URL erreichbar:

```
http://localhost:1080
oder
http://0.0.0.0:1080
```
