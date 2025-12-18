# 🎟 Event Booking System v2.0 - Java Desktop Anwendung

## 📌 Projektbeschreibung
Das **Event Booking System** ist eine professionelle Desktop-Anwendung in **Java**, entwickelt zur Verwaltung von Veranstaltungen, Sälen, Sitzplätzen und Kundenbuchungen. Es demonstriert moderne Softwarearchitektur und Best Practices in der Java-Entwicklung.

Das Projekt dient als **Lern- und Demonstrationsprojekt** für:
- **MVC-Architektur** (Model-View-Controller)
- **DAO Pattern** (Data Access Object) für saubere Datenbanktrennung
- **Professionelles Exception Handling**
- **Java Swing GUI** Entwicklung

---

## ✨ Hauptfunktionen & Features

### 🔧 Core Features
- **Event-Management**: Erstellen, Bearbeiten und Löschen von Veranstaltungen.
- **Kundenverwaltung**: Suche und Management von Kundendaten.
- **Buchungssystem**: 
  - Grafische Sitzplatzauswahl.
  - Generierung von Buchungen.
  - Statusverwaltung (`AVAILABLE` → `RESERVED` → `CONFIRMED` → `CANCELLED`).
- **Reporting**: Übersicht über Auslastung und Buchungsstatistiken.

### ⚙️ Technische Highlights
- **Datenbank**: MySQL Integration mit `JDBC`.
- **Konfiguration**: Zentrale Steuerung über `db.properties` Datei.
- **Sicherheit**: Verwendung von `PreparedStatement` gegen SQL-Injection.
- **Validierung**: Duplikaterkennung (z.B. E-Mail, Sitzplätze).
- **Verbindungspooling**: Effizientes Ressourcenmanagement.

---

### Projektstruktur
```
event-booking-system/
├── src/
│   ├── model/         # Datenmodelle (POJOs)
│   ├── view/          # GUI (Swing Frames & Panels)
│   ├── controller/    # Logik & Event Handling
│   ├── dao/           # Datenbankzugriff (CRUD)
│   ├── service/       # Geschäftslogik
│   ├── exceptions/    # Custom Exceptions
│   ├── util/          # Hilfsklassen (DB Config)
│   └── Main.java      # Programmeinstieg
├── db/                # SQL Scripte & Schema
├── lib/               # Externe Bibliotheken
├── db.properties      # Konfigurationsdatei
└── README.md          # Dokumentation
```

---

## 🚀 Schnellstart (Quickstart)

### Voraussetzungen
*   **Java 17** oder höher
*   **MySQL 8.0** Server
*   **IntelliJ IDEA** (empfohlen) oder Eclipse/VS Code

### 1️⃣ Datenbank Setup
Stellen Sie sicher, dass MySQL läuft.

```bash
# 1. Datenbank erstellen und Tabellen anlegen
mysql -u root -p --port=3307 < db/event_booking_db.sql
```
*(Hinweis: Passen Sie den Port `3307` an, falls Sie `3306` nutzen.)*

### 2️⃣ Konfiguration
Bearbeiten Sie die Datei `db.properties` im Hauptverzeichnis:

```properties
db.url=jdbc:mysql://localhost:3307/event_booking_db?serverTimezone=UTC&useSSL=false
db.username=root
db.password=IHR_PASSWORT  # <-- Hier Ihr MySQL Passwort eintragen!
```

### 3️⃣ Starten
**In IntelliJ IDEA:**
1. Öffnen Sie `src/Main.java`.
2. Klicken Sie auf den grünen **Play-Button** ▶️.

**Via Kommandozeile:**
```bash
# Kompilieren
javac -cp ".:lib/*" -d out src/**/*.java

# Ausführen (Linux/Mac)
java -cp "out:lib/*" Main

# Ausführen (Windows)
java -cp "out;lib/*" Main
```

---

## 🐛 Troubleshooting (Fehlerbehebung)

| Fehler | Mögliche Ursache | Lösung |
| :--- | :--- | :--- |
| **"MySQL JDBC Treiber nicht gefunden"** | JAR fehlt im Classpath | `mysql-connector-j-*.jar` zu den Libraries hinzufügen. |
| **"Access denied for user..."** | Falsches Passwort | Passwort in `db.properties` prüfen. |
| **"Communications link failure"** | MySQL läuft nicht / Falscher Port | Prüfen ob MySQL Service läuft und ob Port (3306/3307) stimmt. |
| **"Unknown database 'event_booking_db'"** | DB nicht angelegt | SQL-Script `db/event_booking_db.sql` ausführen. |

---

## 📞 Kontakt
Entwickelt im Rahmen eines Java-Projekts.
Bei Fragen oder Problemen: [den Programmierer kontaktieren](https://giovyangy.github.io/Lebenlauf/index.html#kontakt)
