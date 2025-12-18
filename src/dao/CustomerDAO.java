package dao;

import exceptions.DatabaseException;
import model.Customer;
import util.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO für Customer-Entitäten
 * Verbesserte Version mit konsistenter Fehlerbehandlung
 */
public class CustomerDAO implements GenericDAO<Customer, Long> {

    private final DatabaseConfig dbConfig;

    public CustomerDAO() {
        this.dbConfig = DatabaseConfig.getInstance();
    }

    @Override
    public Customer save(Customer customer) throws DatabaseException {
        String sql = "INSERT INTO customers (first_name, last_name, email, phone) VALUES (?, ?, ?, ?)";

        try (Connection conn = dbConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, customer.getFirstName());
            stmt.setString(2, customer.getLastName());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getPhone());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new DatabaseException("Kunde konnte nicht gespeichert werden");
            }

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    customer.setId(keys.getLong(1));
                }
            }

            return customer;

        } catch (SQLException e) {
            // Prüfe auf Duplicate Email
            if (e.getMessage().contains("Duplicate entry") && e.getMessage().contains("email")) {
                throw new DatabaseException("Ein Kunde mit dieser E-Mail existiert bereits: " + customer.getEmail(), e);
            }
            throw new DatabaseException("Fehler beim Speichern des Kunden", e);
        }
    }

    @Override
    public void update(Customer customer) throws DatabaseException {
        String sql = "UPDATE customers SET first_name = ?, last_name = ?, email = ?, phone = ? WHERE id = ?";

        try (Connection conn = dbConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customer.getFirstName());
            stmt.setString(2, customer.getLastName());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getPhone());
            stmt.setLong(5, customer.getId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new DatabaseException("Kunde mit ID " + customer.getId() + " nicht gefunden");
            }

        } catch (SQLException e) {
            throw new DatabaseException("Fehler beim Aktualisieren des Kunden", e);
        }
    }

    @Override
    public void delete(Long id) throws DatabaseException {
        String sql = "DELETE FROM customers WHERE id = ?";

        try (Connection conn = dbConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new DatabaseException("Kunde mit ID " + id + " nicht gefunden");
            }

        } catch (SQLException e) {
            // Prüfe auf Foreign Key Constraint
            if (e.getMessage().contains("foreign key constraint")) {
                throw new DatabaseException("Kunde kann nicht gelöscht werden, da noch Buchungen existieren", e);
            }
            throw new DatabaseException("Fehler beim Löschen des Kunden", e);
        }
    }

    @Override
    public Optional<Customer> findById(Long id) throws DatabaseException {
        String sql = "SELECT * FROM customers WHERE id = ?";

        try (Connection conn = dbConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToCustomer(rs));
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new DatabaseException("Fehler beim Suchen des Kunden", e);
        }
    }

    @Override
    public List<Customer> findAll() throws DatabaseException {
        // Query optimized to include active bookings count
        String sql = """
                    SELECT c.*,
                    (SELECT COUNT(*) FROM bookings b WHERE b.customer_id = c.id AND b.status != 'CANCELLED') as active_booking_count
                    FROM customers c ORDER BY c.last_name, c.first_name
                """;
        List<Customer> customers = new ArrayList<>();

        try (Connection conn = dbConfig.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Fehler beim Laden aller Kunden", e);
        }

        return customers;
    }

    /**
     * Sucht einen Kunden nach E-Mail
     */
    public Optional<Customer> findByEmail(String email) throws DatabaseException {
        String sql = "SELECT * FROM customers WHERE email = ?";

        try (Connection conn = dbConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToCustomer(rs));
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new DatabaseException("Fehler beim Suchen nach E-Mail", e);
        }
    }

    /**
     * Sucht Kunden nach Name (Teilstring-Suche)
     */
    public List<Customer> searchByName(String searchTerm) throws DatabaseException {
        String sql = """
                    SELECT c.*,
                    (SELECT COUNT(*) FROM bookings b WHERE b.customer_id = c.id AND b.status != 'CANCELLED') as active_booking_count
                    FROM customers c
                    WHERE c.first_name LIKE ? OR c.last_name LIKE ?
                    ORDER BY c.last_name, c.first_name
                """;
        List<Customer> customers = new ArrayList<>();

        try (Connection conn = dbConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    customers.add(mapResultSetToCustomer(rs));
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Fehler beim Suchen nach Namen", e);
        }

        return customers;
    }

    /**
     * Mappt ein ResultSet auf ein Customer-Objekt
     */
    private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setId(rs.getLong("id"));
        customer.setFirstName(rs.getString("first_name"));
        customer.setLastName(rs.getString("last_name"));
        customer.setEmail(rs.getString("email"));
        customer.setPhone(rs.getString("phone"));

        // Set transient count if available
        try {
            customer.setActiveBookingsCount(rs.getInt("active_booking_count"));
        } catch (SQLException e) {
            // Ignore if column missing
        }

        return customer;
    }
}
