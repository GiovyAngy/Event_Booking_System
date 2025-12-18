package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Verwaltung der Datenbankverbindung
 * Singleton Pattern für zentrale Konfiguration
 * Unterstützt Konfiguration via db.properties Datei
 */
public class DatabaseConfig {
    private static DatabaseConfig instance;
    private final String url;
    private final String username;
    private final String password;

    /**
     * Privater Konstruktor für Singleton
     * Lädt die Konfiguration aus db.properties oder verwendet Defaults
     */
    private DatabaseConfig() {
        Properties props = loadProperties();
        
        this.url = props.getProperty("db.url", "jdbc:mysql://localhost:3307/event_booking_db?serverTimezone=UTC&useSSL=false");
        this.username = props.getProperty("db.username", "root");
        this.password = props.getProperty("db.password", "");

        // Lade MySQL JDBC Treiber
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✓ MySQL JDBC Treiber erfolgreich geladen");
        } catch (ClassNotFoundException e) {
            System.err.println("✗ FEHLER: MySQL JDBC Treiber nicht gefunden!");
            System.err.println("  Bitte fügen Sie mysql-connector-j zur Klassenpfad hinzu.");
            throw new RuntimeException("MySQL JDBC Treiber nicht gefunden", e);
        }
        
        // Teste Datenbankverbindung beim Start
        testConnection();
    }

    /**
     * Lädt Properties aus db.properties Datei
     * Falls nicht gefunden, werden Default-Werte verwendet
     */
    private Properties loadProperties() {
        Properties props = new Properties();
        
        // Versuche db.properties zu laden
        String[] possiblePaths = {
            "db.properties",
            "./db.properties",
            "../db.properties",
            "config/db.properties"
        };
        
        for (String path : possiblePaths) {
            try (InputStream input = new FileInputStream(path)) {
                props.load(input);
                System.out.println("✓ Konfiguration geladen aus: " + path);
                return props;
            } catch (IOException e) {
                // Nächsten Pfad versuchen
            }
        }
        
        System.out.println("⚠ db.properties nicht gefunden, verwende Standard-Konfiguration");
        return props;
    }

    /**
     * Gibt die Singleton-Instanz zurück
     */
    public static synchronized DatabaseConfig getInstance() {
        if (instance == null) {
            instance = new DatabaseConfig();
        }
        return instance;
    }

    /**
     * Erstellt eine neue Datenbankverbindung
     * @return Connection-Objekt
     * @throws SQLException wenn Verbindung fehlschlägt
     */
    public Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            return conn;
        } catch (SQLException e) {
            System.err.println("✗ Datenbankverbindung fehlgeschlagen!");
            System.err.println("  URL: " + url);
            System.err.println("  User: " + username);
            System.err.println("  Fehler: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Testet die Datenbankverbindung beim Start
     */
    private void testConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("✓ Datenbankverbindung erfolgreich getestet");
                System.out.println("  Database: " + conn.getCatalog());
            }
        } catch (SQLException e) {
            System.err.println("✗ WARNUNG: Datenbankverbindung konnte nicht hergestellt werden!");
            System.err.println("  Stellen Sie sicher, dass:");
            System.err.println("  1. MySQL auf Port 3307 läuft");
            System.err.println("  2. Die Datenbank 'event_booking_db' existiert");
            System.err.println("  3. Benutzername und Passwort korrekt sind");
            System.err.println("  4. Führen Sie das SQL-Script aus: db/event_booking_db.sql");
        }
    }

    /**
     * Schließt eine Verbindung sicher
     * @param connection zu schließende Verbindung
     */
    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Fehler beim Schließen der Verbindung: " + e.getMessage());
            }
        }
    }

    // Getter für Testzwecke
    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }
}
