package service;

import dao.BookingDAO;
import exceptions.DatabaseException;
import exceptions.InvalidBookingStatusTransitionException;
import model.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service-Klasse für Buchungs-Geschäftslogik
 * Enthält Lambda-Ausdrücke, Stream API und Comparatoren
 */
public class BookingService {

    private final BookingDAO bookingDAO;

    public BookingService() {
        this.bookingDAO = new BookingDAO();
    }

    /**
     * Erstellt eine neue Buchung
     */
    public Booking createBooking(Customer customer, Event event, Seat seat)
            throws DatabaseException, InvalidBookingStatusTransitionException {

        // Prüfe ob Sitzplatz verfügbar ist
        if (!isSeatAvailable(event, seat)) {
            throw new IllegalStateException("Sitzplatz ist bereits gebucht");
        }

        double price = calculatePrice(event, seat);
        Booking booking = new Booking(customer, event, seat, price);

        // Reserviere die Buchung
        booking.reserve();

        // Speichere in Datenbank
        return bookingDAO.save(booking);
    }

    /**
     * Bestätigt eine Buchung
     */
    public void confirmBooking(Long bookingId)
            throws DatabaseException, InvalidBookingStatusTransitionException {

        Booking booking = bookingDAO.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Buchung nicht gefunden"));

        booking.confirm();
        bookingDAO.update(booking);
    }

    /**
     * Storniert eine Buchung
     */
    public void cancelBooking(Long bookingId)
            throws DatabaseException, InvalidBookingStatusTransitionException {

        Booking booking = bookingDAO.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Buchung nicht gefunden"));

        booking.cancel();
        bookingDAO.update(booking);
    }

    /**
     * Prüft ob ein Sitzplatz verfügbar ist
     */
    public boolean isSeatAvailable(Event event, Seat seat) throws DatabaseException {
        List<Booking> bookings = bookingDAO.findByEvent(event.getId());

        return bookings.stream()
                .noneMatch(b -> b.getSeat().equals(seat) &&
                        b.getStatus() != BookingStatus.CANCELLED);
    }

    /**
     * Berechnet den Preis für eine Buchung
     * Hier könnte z.B. ein Aufschlag für VIP-Sitze kommen
     */
    private double calculatePrice(Event event, Seat seat) {
        // Basispreis vom Event
        double price = event.getBasePrice();

        // Beispiel: Erste 3 Reihen haben 20% Aufschlag
        if (seat.getRow().contains("1") ||
                seat.getRow().contains("2") ||
                seat.getRow().contains("3")) {
            price *= 1.2;
        }

        return Math.round(price * 100.0) / 100.0; // 2 Dezimalstellen
    }

    /**
     * Findet alle Buchungen eines Kunden
     */
    public List<Booking> findBookingsByCustomer(Long customerId) throws DatabaseException {
        return bookingDAO.findByCustomer(customerId);
    }

    /**
     * Findet alle Buchungen für ein Event
     */
    public List<Booking> findBookingsByEvent(Long eventId) throws DatabaseException {
        return bookingDAO.findByEvent(eventId);
    }

    /**
     * Gibt alle aktiven Buchungen zurück (nicht storniert)
     * Demonstriert Stream API und Lambda
     */
    public List<Booking> findActiveBookings() throws DatabaseException {
        return bookingDAO.findAll().stream()
                .filter(b -> b.getStatus() != BookingStatus.CANCELLED)
                .collect(Collectors.toList());
    }

    /**
     * Sortiert Buchungen nach Datum (neueste zuerst)
     * Demonstriert Comparator
     */
    public List<Booking> sortBookingsByDateDesc(List<Booking> bookings) {
        return bookings.stream()
                .sorted(Comparator.comparing(Booking::getBookingDate).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Sortiert Buchungen nach Preis (teuerste zuerst)
     * Demonstriert Comparator mit Lambda
     */
    public List<Booking> sortBookingsByPriceDesc(List<Booking> bookings) {
        return bookings.stream()
                .sorted((b1, b2) -> Double.compare(b2.getPrice(), b1.getPrice()))
                .collect(Collectors.toList());
    }

    /**
     * Filtert Buchungen nach Status
     * Demonstriert Stream API
     */
    public List<Booking> filterBookingsByStatus(List<Booking> bookings, BookingStatus status) {
        return bookings.stream()
                .filter(b -> b.getStatus() == status)
                .collect(Collectors.toList());
    }

    /**
     * Berechnet den Gesamtumsatz aller bestätigten Buchungen
     * Demonstriert Stream API mit reduce
     */
    public double calculateTotalRevenue() throws DatabaseException {
        return bookingDAO.findAll().stream()
                .filter(b -> b.getStatus() == BookingStatus.CONFIRMED)
                .mapToDouble(Booking::getPrice)
                .sum();
    }

    /**
     * Findet die teuerste Buchung
     * Demonstriert Optional und Stream API
     */
    public Booking findMostExpensiveBooking() throws DatabaseException {
        return bookingDAO.findAll().stream()
                .max(Comparator.comparing(Booking::getPrice))
                .orElse(null);
    }
}