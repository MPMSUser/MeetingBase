# MeetingBase

MeetingBase ist ein Open-Source-Softwarepaket zur Unterstützung der Anmeldung, Registrierung und Abstimmung bei Hauptversammlungen und Meetings.

## ⚠️ Wichtiger Hinweis zur Architektur
Dieses Repository enthält den Open-Source-Kern der Anwendung ("Core"). Proprietäre Komponenten von Drittanbietern (z.B. kommerzielle Reporting-Engines 
oder spezifische Schnittstellen) wurden entfernt. Um die Software vollständig ausführbar zu machen, müssen diese Abhängigkeiten (z.B. durch 
Open-Source-Alternativen wie JasperReports) ergänzt werden.

## 🚀 Voraussetzungen
* **Java** (JDK 17 empfohlen)
* **Wildfly** (Application Server)
* **MySQL** (Datenbank)

## 🛠 Installation & Build

Das Projekt besteht aus drei Modulen, die in folgender Reihenfolge gebaut werden müssen:

1. **meetingportal** (Server-Anwendung)
2. **meetingclient** (Client-Anwendung für die Administration)

Als erstes muss das meetingportal mit mvn install ins lokale Maven Repo installiert werden.
Der Client braucht das meetingportal beim build. Mit mvn package wird das .war file erzeugt.

cd meetingportal
mvn clean install package

Der meetingclient wir mit mvn clean package als zip File erzeugt. Dieses Zip File kann auf dem Client 
installiert werden.

cd meetingclient
mvn clean package

### Datenbank

Datenbankstrukturen können über die Routinen in der Klasse
meetingportal/meetComBVerwaltung/BvDatenbank.java
angelegt werden.

### meetingportal

Das Artefakt aus meetingportal (WAR-File) muss auf dem Wildfly Application Server deployed werden.

Das Teilnehmerportal wird z.B. über
http://<your-host>/meetingportal/BO01/portal.xhtml
aufgerufen (entsprechende Parameter siehe portal.xhtml)

Das Tool-Portal wird z.B. über
http://<your-host>/meetingportal/ULOGIN/uLogin.xhtml
aufgerufen.

### meetingclient

Der Meetingclient wird über die Mitgelieferten Scripte gestartet (siehe src/main/scripts)

## 📄 Lizenz
Dieses Projekt ist lizenziert unter der Apache License 2.0. Siehe LICENSE Datei für Details.