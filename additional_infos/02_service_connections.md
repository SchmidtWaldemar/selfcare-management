# Verbindungen bei Microservices

Bei der letzten Version konnte man die einzelnen Microservices zwar starten und auch die Verbindungen darauf liefen bereits über den Gateway Server, doch die Schnittstellen innerhalb der Services blieben noch unberührt. In dieser Version sollte sich das ändern. 

Wie in der 'gateway-service.yml' bereits festgelegt, sollen von außen nur die Services zu den Teilnehmern und Gruppen erreichbar sein. Der Service zu den Benachrichtigungen laufen nur lokal zwischen den Services ab. 

## Szenario zwischen Services
Der Service für Gruppen kommuniziert mit dem Participant Service und nicht umgekehrt. Bis auf die URL kennt die Gruppe die Service-Instanz vom Participant nicht im Detail. Das ist auch nicht nötig, da die Services auch komplett unabhängig voneinander laufen können sollen und sich gegenseitig eher als Blackbox sehen. In unserem Fall wird eine Mitgliedschaft vom Participant bei einer Gruppe beantragt. Und bevor die Mitgliedschaft verbucht werden kann, überprüft der Gruppen-Service die Identitäg des Teilnehmers. Bei erfolgreicher Buchung werden alle anderen Teilnehmer innerhalb der Gruppe benachricht, dass ein neues Mitglied vorhanden ist. Hier soll der Gruppen-Service weder die E-Mail noch andere persönlichen Informationen vom Participant erfahren müssen. So können in Zukunft auch andere Services an diese Schnittstelle angebunden werden können, wie z.B. Moderatoren einer Selbsthilfegruppe, die im separaten Microservice verwaltet werden.

## Benachrichtigung
Folgende Benachrichtigungen sind aktuell vorgesehen:

1. bei der Teilnehmer Registrierung
2. wenn eine neue Gruppe erstellt wird
3. andere Teilnehmer innh. der Gruppe erhalten Nachricht über eine neue Mitgliedschaft

Die Services Gruppe und Teilnehmer erzeugen (als s.g. Producer) die Nachricht und senden diese per Message Brocker <b>Kafka</b> an den notification-service. Der notification-service wiederum holt sich als s.g. Consumer die Nachrichten aus der Pipeline und speichert diese in der Datenbank. Eine E-Mail wird nur bei Punkt 1. und 3. an die Teilnehmer versendet. Punkt 2. dient nur als Protokoll für den Admin.

## REST-API testen
Im aktuellem Stadium haben wir noch keine GUI implementiert, die später mit Angular auf die Gateway zugreifen soll. Deshalb lohnt es sich die REST-API zunächst mit <b>Postman</b> zu testen. Die folgenden Use-Cases können in dieser Version als kleines Workaround getestet werden.

### Registrierung
Die Registrierung eines Teilnehmers erfolgt zwar noch nicht, kann jedoch als Test wie folgt aufgerufen werden, was eine erfolgreiche Antwort von 200 ergeben sollte:


```
[POST] 
http://localhost:8222/api/participants/register

[Body && raw && Type:JSON]
{
    "email": "test@test.de"
}
```

### Gruppe erzeugen
Um eine (Selbsthilfe-)Gruppe zu ereugen kann unter Postman folgende JSON Daten an die jeweilige Adresse übermittelt werden::

```
[POST] 
http://localhost:8222/api/groups/create

[Body && raw && Type:JSON]
{
    "name": "Die Ausgebrannten"
}
```

### Mitgliedschaft buchen
Nun wird es etwas spannender. Ein Teilnehmer kann nun sein Wunschnickname setzen und die ID als String übermitteln. Hier wird statt participantId die clientNumber verwendet, weil wir dadurch unabhängig bei der Konzeption bleiben möchten. Spätere Services können z.B. stattdessen eine alphanumerische Kundennummer zur Identifikation verweden. Damit sollte es schon reichen, einen Servicetyp einzuführen, um jede ID an den richtigen Microservice zu senden.

```
[POST] 
http://localhost:8222/api/groups/createMembership

[Body && raw && Type:JSON]
{
    "groupId": 1,
    "clientNumber": "2",
    "nickname": "derGroeste"
}
```

Achtung: beim ersten Mitglied wird noch keine E-Mail an die anderen Teilnehmer innh. der Gruppe gesendet. Das erfolgt erst wenn die Gruppe bereits > 1 Teilnehmer hat.

## Datenbanken auf Inhalt prüfen

Sicherheitshalber kann es sich lohnen zu prüfen, ob die Datenbanken auch mit Inhalten gefüllt werden konnten. Da wir pro Microservice jeweils eine andere Datenbank verwenden, können die Prüfungen wie folgt ausfallen:

Containernamen bzw. <containerId> auslesen

```
docker ps
```

### MongoDB auslesen

```
?> docker exec -it <containerId> mongosh -u "selfcare" -p "selfcare"

test> show dbs
admin         100.00 KiB
config         12.00 KiB
local          72.00 KiB
notification   40.00 KiB

test> use notification

notification> db.notification.find()
[
  {
    _id: ObjectId('670a4dbf67fa0d77623b6883'),
    type: 'REGISTRATION',
    _class: 'com.platform.selfcare.notification.Notification'
  },
  {
    _id: ObjectId('670a4dc667fa0d77623b6884'),
    type: 'NEW_GROUP',
    _class: 'com.platform.selfcare.notification.Notification'
  },
  {
    _id: ObjectId('670a4de167fa0d77623b6885'),
    type: 'REGISTRATION',
    _class: 'com.platform.selfcare.notification.Notification'
  },
  {
    _id: ObjectId('670a4deb67fa0d77623b6886'),
    type: 'NEW_MEMBER_IN_GROUP',
    _class: 'com.platform.selfcare.notification.Notification'
  }
]
```

### MariaDB auslesen

```
?> docker exec -it <containerId> /bin/bash

?> mariadb -u root --password=selfcare

MariaDB [(none)]> show databases;
+--------------------+
| Database           |
+--------------------+
| groupDB            |
| information_schema |
| mysql              |
| performance_schema |
| sys                |
+--------------------+
5 rows in set (0.008 sec)

MariaDB [(none)]> select * from groupDB.groups;
+----+-------------------+
| id | name              |
+----+-------------------+
|  1 | Die Ausgebrannten |
+----+-------------------+
1 row in set (0.000 sec)
```

### PostgreSQL auslesen

```
?> docker exec -it <containerId> /bin/bash

?> psql -U postgres

postgres=# \l
                                                        List of databases
     Name      |  Owner   | Encoding | Locale Provider |  Collate   |   Ctype    | ICU Locale | ICU Rules |   Access privileges   
---------------+----------+----------+-----------------+------------+------------+------------+-----------+-----------------------
 participantDB | postgres | UTF8     | libc            | en_US.utf8 | en_US.utf8 |            |           | 
 postgres      | postgres | UTF8     | libc            | en_US.utf8 | en_US.utf8 |            |           | 
 template0     | postgres | UTF8     | libc            | en_US.utf8 | en_US.utf8 |            |           | =c/postgres          +
               |          |          |                 |            |            |            |           | postgres=CTc/postgres
 template1     | postgres | UTF8     | libc            | en_US.utf8 | en_US.utf8 |            |           | =c/postgres          +
               |          |          |                 |            |            |            |           | postgres=CTc/postgres
(4 rows)

postgres=# \c participantDB
You are now connected to database "participantDB" as user "postgres".

participantDB=# \dt
            List of relations
 Schema |    Name     | Type  |  Owner   
--------+-------------+-------+----------
 public | participant | table | postgres
(1 row)

participantDB=# select * from participant;
 id |     email     
----+---------------
  1 | test@test.de
  2 | test2@test.de
(2 rows)
```
