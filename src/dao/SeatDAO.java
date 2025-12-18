package dao;

import exceptions.DatabaseException;
import model.Hall;
import model.Seat;
import util.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO für Seat-Entitäten
 * Verbesserte Version mit konsistenter Fehlerbehandlung
 */
public class SeatDAO implements GenericDAO<Seat, Long> {

    private final DatabaseConfig dbConfig;
    private final HallDAO hallDAO;

    public SeatDAO() {
        this.dbConfig = DatabaseConfig.getInstance();
        this.hallDAO = new HallDAO();
    }

    @Override
    public Seat save(Seat seat) throws DatabaseException {
        String sql = "INSERT INTO seats (row_label, seat_number, hall_id) VALUES (?, ?, ?)";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, seat.getRow());
            stmt.setInt(2, seat.getNumber());
            stmt.setLong(3, seat.getHall().getId());

            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new DatabaseException("Sitzplatz konnte nicht gespeichert werden");
            }

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    seat.setId(keys.getLong(1));
                }
            }

            return seat;

        } catch (SQLException e) {
            // Prüfe auf Duplicate Seat
            if (e.getMessage().contains("Duplicate entry") || e.getMessage().contains("unique_seat")) {
                throw new DatabaseException("Sitzplatz existiert bereits: Reihe " + seat.getRow() + ", Nummer " + seat.getNumber(), e);
            }
            throw new DatabaseException("Fehler beim Speichern des Sitzplatzes", e);
        }
    }

    @Override
    public void update(Seat seat) throws DatabaseException {
        String sql = "UPDATE seats SET row_label = ?, seat_number = ?, hall_id = ? WHERE id = ?";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, seat.getRow());
            stmt.setInt(2, seat.getNumber());
            stmt.setLong(3, seat.getHall().getId());
            stmt.setLong(4, seat.getId());

            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new DatabaseException("Sitzplatz mit ID " + seat.getId() + " nicht gefunden");
            }

        } catch (SQLException e) {
            throw new DatabaseException("Fehler beim Aktualisieren des Sitzplatzes", e);
        }
    }

    @Override
    public void delete(Long id) throws DatabaseException {
        String sql = "DELETE FROM seats WHERE id = ?";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new DatabaseException("Sitzplatz mit ID " + id + " nicht gefunden");
            }

        } catch (SQLException e) {
            // Prüfe auf Foreign Key Constraint
            if (e.getMessage().contains("foreign key constraint")) {
                throw new DatabaseException("Sitzplatz kann nicht gelöscht werden, da noch Buchungen existieren", e);
            }
            throw new DatabaseException("Fehler beim Löschen des Sitzplatzes", e);
        }
    }

    @Override
    public Optional<Seat> findById(Long id) throws DatabaseException {
        String sql = "SELECT * FROM seats WHERE id = ?";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToSeat(rs));
                }
            }
            
            return Optional.empty();

        } catch (SQLException e) {
            throw new DatabaseException("Fehler beim Finden des Sitzplatzes", e);
        }
    }

    @Override
    public List<Seat> findAll() throws DatabaseException {
        String sql = "SELECT * FROM seats ORDER BY hall_id, row_label, seat_number";
        List<Seat> seats = new ArrayList<>();

        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                seats.add(mapResultSetToSeat(rs));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Fehler beim Laden aller Sitzplätze", e);
        }

        return seats;
    }

    /**
     * Findet alle Sitzplätze eines Saals
     */
    public List<Seat> findByHall(Long hallId) throws DatabaseException {
        String sql = "SELECT * FROM seats WHERE hall_id = ? ORDER BY row_label, seat_number";
        List<Seat> seats = new ArrayList<>();

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, hallId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    seats.add(mapResultSetToSeat(rs));
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Fehler beim Laden der Sitzplätze für Hall ID " + hallId, e);
        }

        return seats;
    }

    /**
     * Findet einen Sitzplatz nach Reihe, Nummer und Saal
     */
    public Optional<Seat> findByRowNumberAndHall(String row, int seatNumber, Long hallId) throws DatabaseException {
        String sql = "SELECT * FROM seats WHERE row_label = ? AND seat_number = ? AND hall_id = ?";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, row);
            stmt.setInt(2, seatNumber);
            stmt.setLong(3, hallId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToSeat(rs));
                }
            }
            
            return Optional.empty();

        } catch (SQLException e) {
            throw new DatabaseException("Fehler beim Suchen des Sitzplatzes", e);
        }
    }

    /**
     * Mappt ein ResultSet auf ein Seat-Objekt
     */
    private Seat mapResultSetToSeat(ResultSet rs) throws SQLException, DatabaseException {
        Seat seat = new Seat();
        seat.setId(rs.getLong("id"));
        seat.setRow(rs.getString("row_label"));
        seat.setNumber(rs.getInt("seat_number"));

        // Lade zugehörigen Saal
        Long hallId = rs.getLong("hall_id");
        Optional<Hall> hall = hallDAO.findById(hallId);
        
        if (hall.isPresent()) {
            seat.setHall(hall.get());
        } else {
            throw new DatabaseException("Hall mit ID " + hallId + " nicht gefunden für Seat ID " + seat.getId());
        }

        return seat;
    }
}
