package dao;

import exceptions.DatabaseException;
import model.Event;
import model.Hall;
import util.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO für Event-Entitäten
 * Verbesserte Version mit konsistenter Fehlerbehandlung
 */
public class EventDAO implements GenericDAO<Event, Long> {

    private final DatabaseConfig dbConfig;
    private final HallDAO hallDAO;

    public EventDAO() {
        this.dbConfig = DatabaseConfig.getInstance();
        this.hallDAO = new HallDAO();
    }

    @Override
    public Event save(Event event) throws DatabaseException {
        String sql = "INSERT INTO events (name, description, date_time, category, base_price, hall_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, event.getName());
            stmt.setString(2, event.getDescription());
            stmt.setTimestamp(3, Timestamp.valueOf(event.getDateTime()));
            stmt.setString(4, event.getCategory());
            stmt.setDouble(5, event.getBasePrice());
            stmt.setLong(6, event.getHall().getId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new DatabaseException("Event konnte nicht gespeichert werden");
            }

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    event.setId(keys.getLong(1));
                }
            }

            return event;

        } catch (SQLException e) {
            throw new DatabaseException("Fehler beim Speichern des Events", e);
        }
    }

    @Override
    public void update(Event event) throws DatabaseException {
        String sql = "UPDATE events SET name = ?, description = ?, date_time = ?, category = ?, base_price = ?, hall_id = ? WHERE id = ?";

        try (Connection conn = dbConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, event.getName());
            stmt.setString(2, event.getDescription());
            stmt.setTimestamp(3, Timestamp.valueOf(event.getDateTime()));
            stmt.setString(4, event.getCategory());
            stmt.setDouble(5, event.getBasePrice());
            stmt.setLong(6, event.getHall().getId());
            stmt.setLong(7, event.getId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new DatabaseException("Event mit ID " + event.getId() + " nicht gefunden");
            }

        } catch (SQLException e) {
            throw new DatabaseException("Fehler beim Aktualisieren des Events", e);
        }
    }

    @Override
    public void delete(Long id) throws DatabaseException {
        String sql = "DELETE FROM events WHERE id = ?";

        try (Connection conn = dbConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new DatabaseException("Event mit ID " + id + " nicht gefunden");
            }

        } catch (SQLException e) {
            throw new DatabaseException("Fehler beim Löschen des Events", e);
        }
    }

    @Override
    public Optional<Event> findById(Long id) throws DatabaseException {
        String sql = "SELECT * FROM events WHERE id = ?";

        try (Connection conn = dbConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEvent(rs));
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new DatabaseException("Fehler beim Finden des Events", e);
        }
    }

    @Override
    public List<Event> findAll() throws DatabaseException {
        // Query optimized to include booked count
        String sql = """
                    SELECT e.*,
                    (SELECT COUNT(*) FROM bookings b WHERE b.event_id = e.id AND b.status != 'CANCELLED') as booked_count
                    FROM events e ORDER BY e.date_time
                """;
        List<Event> events = new ArrayList<>();

        try (Connection conn = dbConfig.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                events.add(mapResultSetToEvent(rs));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Fehler beim Laden aller Events", e);
        }

        return events;
    }

    /**
     * Sucht Events nach Kategorie
     */
    public List<Event> findByCategory(String category) throws DatabaseException {
        String sql = "SELECT * FROM events WHERE category = ? ORDER BY date_time";
        List<Event> events = new ArrayList<>();

        try (Connection conn = dbConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    events.add(mapResultSetToEvent(rs));
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Fehler beim Suchen nach Kategorie", e);
        }

        return events;
    }

    /**
     * Sucht Events nach Name (Teilstring-Suche)
     */
    public List<Event> searchByName(String searchTerm) throws DatabaseException {
        String sql = "SELECT * FROM events WHERE name LIKE ? OR description LIKE ? ORDER BY date_time";
        List<Event> events = new ArrayList<>();

        try (Connection conn = dbConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    events.add(mapResultSetToEvent(rs));
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Fehler beim Suchen nach Events", e);
        }

        return events;
    }

    /**
     * Findet zukünftige Events (ab jetzt)
     */
    public List<Event> findUpcomingEvents() throws DatabaseException {
        String sql = "SELECT * FROM events WHERE date_time >= NOW() ORDER BY date_time";
        List<Event> events = new ArrayList<>();

        try (Connection conn = dbConfig.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                events.add(mapResultSetToEvent(rs));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Fehler beim Laden zukünftiger Events", e);
        }

        return events;
    }

    /**
     * Mappt ein ResultSet auf ein Event-Objekt
     */
    private Event mapResultSetToEvent(ResultSet rs) throws SQLException, DatabaseException {
        Event event = new Event();
        event.setId(rs.getLong("id"));
        event.setName(rs.getString("name"));
        event.setDescription(rs.getString("description"));
        event.setDateTime(rs.getTimestamp("date_time").toLocalDateTime());
        event.setCategory(rs.getString("category"));
        event.setBasePrice(rs.getDouble("base_price"));

        // Set transient booked count if available in RS
        try {
            event.setBookedCount(rs.getInt("booked_count"));
        } catch (SQLException e) {
            // Column might not exist in simple SELECT * queries, ignore or default to 0
        }

        // Lade zugehörigen Saal
        Long hallId = rs.getLong("hall_id");
        Optional<Hall> hall = hallDAO.findById(hallId);

        if (hall.isPresent()) {
            event.setHall(hall.get());
        } else {
            throw new DatabaseException("Hall mit ID " + hallId + " nicht gefunden für Event ID " + event.getId());
        }

        return event;
    }
}
