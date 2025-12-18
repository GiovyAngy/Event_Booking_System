package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import exceptions.DatabaseException;
import model.*;
import util.DatabaseConfig;

/**
 * DAO-Klasse für Booking-Entitäten.
 * Verbesserte Version mit konsistenter Fehlerbehandlung
 */
public class BookingDAO implements GenericDAO<Booking, Long> {

    private final DatabaseConfig dbConfig;
    private final CustomerDAO customerDAO;
    private final EventDAO eventDAO;
    private final SeatDAO seatDAO;

    public BookingDAO() {
        this.dbConfig = DatabaseConfig.getInstance();
        this.customerDAO = new CustomerDAO();
        this.eventDAO = new EventDAO();
        this.seatDAO = new SeatDAO();
    }

    @Override
    public Booking save(Booking booking) throws DatabaseException {
        String sql = "INSERT INTO bookings (customer_id, event_id, seat_id, status, booking_date, price) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, booking.getCustomer().getId());
            stmt.setLong(2, booking.getEvent().getId());
            stmt.setLong(3, booking.getSeat().getId());
            stmt.setString(4, booking.getStatus().name());
            stmt.setTimestamp(5, Timestamp.valueOf(booking.getBookingDate()));
            stmt.setDouble(6, booking.getPrice());

            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new DatabaseException("Buchung konnte nicht gespeichert werden");
            }

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    booking.setId(rs.getLong(1));
                }
            }
            
            return booking;

        } catch (SQLException e) {
            throw new DatabaseException("Fehler beim Speichern der Buchung", e);
        }
    }

    @Override
    public void update(Booking booking) throws DatabaseException {
        String sql = "UPDATE bookings SET status = ?, price = ? WHERE id = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, booking.getStatus().name());
            stmt.setDouble(2, booking.getPrice());
            stmt.setLong(3, booking.getId());

            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new DatabaseException("Buchung mit ID " + booking.getId() + " nicht gefunden");
            }

        } catch (SQLException e) {
            throw new DatabaseException("Fehler beim Aktualisieren der Buchung", e);
        }
    }

    @Override
    public void delete(Long id) throws DatabaseException {
        String sql = "DELETE FROM bookings WHERE id = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new DatabaseException("Buchung mit ID " + id + " nicht gefunden");
            }

        } catch (SQLException e) {
            throw new DatabaseException("Fehler beim Löschen der Buchung", e);
        }
    }

    @Override
    public Optional<Booking> findById(Long id) throws DatabaseException {
        String sql = "SELECT * FROM bookings WHERE id = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToBooking(rs));
                }
            }
            
            return Optional.empty();

        } catch (SQLException e) {
            throw new DatabaseException("Fehler beim Finden der Buchung nach ID", e);
        }
    }

    @Override
    public List<Booking> findAll() throws DatabaseException {
        String sql = "SELECT * FROM bookings ORDER BY booking_date DESC";
        List<Booking> bookings = new ArrayList<>();

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                try {
                    bookings.add(mapRowToBooking(rs));
                } catch (DatabaseException e) {
                    // Logge Fehler aber fahre fort
                    System.err.println("⚠ Warnung: Buchung ID=" + rs.getLong("id") + " übersprungen: " + e.getMessage());
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Fehler beim Laden aller Buchungen", e);
        }

        return bookings;
    }

    /**
     * Findet alle Buchungen eines bestimmten Kunden.
     */
    public List<Booking> findByCustomer(Long customerId) throws DatabaseException {
        String sql = "SELECT * FROM bookings WHERE customer_id = ? ORDER BY booking_date DESC";
        List<Booking> bookings = new ArrayList<>();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, customerId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapRowToBooking(rs));
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Fehler beim Laden der Buchungen für Kunde ID " + customerId, e);
        }
        
        return bookings;
    }

    /**
     * Findet alle Buchungen eines bestimmten Events.
     */
    public List<Booking> findByEvent(Long eventId) throws DatabaseException {
        String sql = "SELECT * FROM bookings WHERE event_id = ? ORDER BY booking_date DESC";
        List<Booking> bookings = new ArrayList<>();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, eventId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapRowToBooking(rs));
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Fehler beim Laden der Buchungen für Event ID " + eventId, e);
        }
        
        return bookings;
    }

    /**
     * Prüft ob ein Sitz für ein Event bereits gebucht ist
     */
    public boolean isSeatBookedForEvent(Long eventId, Long seatId) throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM bookings WHERE event_id = ? AND seat_id = ? AND status != 'CANCELLED'";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, eventId);
            stmt.setLong(2, seatId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Fehler beim Prüfen der Sitzbelegung", e);
        }
        
        return false;
    }

    /**
     * Hilfsmethode zum Mapping eines ResultSets zu einer Booking-Entität.
     */
    private Booking mapRowToBooking(ResultSet rs) throws DatabaseException, SQLException {
        Booking booking = new Booking();
        long bookingId = rs.getLong("id");

        // Customer laden
        long customerId = rs.getLong("customer_id");
        Optional<Customer> customer = customerDAO.findById(customerId);
        if (customer.isEmpty()) {
            throw new DatabaseException("Customer mit ID " + customerId + " nicht gefunden (Booking ID=" + bookingId + ")");
        }
        booking.setCustomer(customer.get());

        // Event laden
        long eventId = rs.getLong("event_id");
        Optional<Event> event = eventDAO.findById(eventId);
        if (event.isEmpty()) {
            throw new DatabaseException("Event mit ID " + eventId + " nicht gefunden (Booking ID=" + bookingId + ")");
        }
        booking.setEvent(event.get());

        // Seat laden
        long seatId = rs.getLong("seat_id");
        Optional<Seat> seat = seatDAO.findById(seatId);
        if (seat.isEmpty()) {
            throw new DatabaseException("Seat mit ID " + seatId + " nicht gefunden (Booking ID=" + bookingId + ")");
        }
        booking.setSeat(seat.get());

        // BookingStatus setzen
        try {
            booking.setStatus(BookingStatus.valueOf(rs.getString("status")));
        } catch (IllegalArgumentException e) {
            throw new DatabaseException("Ungültiger BookingStatus: " + rs.getString("status") + " (Booking ID=" + bookingId + ")");
        }

        // Weitere Felder
        booking.setId(bookingId);
        booking.setBookingDate(rs.getTimestamp("booking_date").toLocalDateTime());
        booking.setPrice(rs.getDouble("price"));

        return booking;
    }
}
