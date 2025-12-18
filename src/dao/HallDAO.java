package dao;

import exceptions.DatabaseException;
import model.Hall;
import util.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO für Hall-Entitäten
 */
public class HallDAO implements GenericDAO<Hall, Long> {

    private final DatabaseConfig dbConfig;

    public HallDAO() {
        this.dbConfig = DatabaseConfig.getInstance();
    }

    @Override
    public Hall save(Hall hall) throws DatabaseException {
        String sql = "INSERT INTO halls (name, capacity) VALUES (?, ?)";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, hall.getName());
            stmt.setInt(2, hall.getCapacity());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    hall.setId(keys.getLong(1));
                }
            }

            return hall;

        } catch (SQLException e) {
            throw new DatabaseException("Fehler beim Speichern des Saals", e);
        }
    }

    @Override
    public void update(Hall hall) throws DatabaseException {
        String sql = "UPDATE halls SET name = ?, capacity = ? WHERE id = ?";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, hall.getName());
            stmt.setInt(2, hall.getCapacity());
            stmt.setLong(3, hall.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Fehler beim Aktualisieren des Saals", e);
        }
    }

    @Override
    public void delete(Long id) throws DatabaseException {
        String sql = "DELETE FROM halls WHERE id = ?";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Fehler beim Löschen des Saals", e);
        }
    }

    @Override
    public Optional<Hall> findById(Long id) throws DatabaseException {
        String sql = "SELECT * FROM halls WHERE id = ?";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToHall(rs));
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Fehler beim Finden des Saals", e);
        }

        return Optional.empty();
    }

    @Override
    public List<Hall> findAll() throws DatabaseException {
        String sql = "SELECT * FROM halls ORDER BY name";
        List<Hall> halls = new ArrayList<>();

        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                halls.add(mapResultSetToHall(rs));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Fehler beim Laden aller Säle", e);
        }

        return halls;
    }

    /**
     * Mappt ein ResultSet auf ein Hall-Objekt
     */
    private Hall mapResultSetToHall(ResultSet rs) throws SQLException {
        Hall hall = new Hall();
        hall.setId(rs.getLong("id"));
        hall.setName(rs.getString("name"));
        hall.setCapacity(rs.getInt("capacity"));
        return hall;
    }
}
