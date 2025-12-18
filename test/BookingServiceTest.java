

import model.*;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 Tests für BookingService
 * Testet Geschäftslogik und Stream API Verwendung
 */
class BookingServiceTest {

    private List<Booking> testBookings;
    private Customer customer1, customer2;
    private Event event;
    private Hall hall;
    private Seat seat1, seat2, seat3;

    @BeforeEach
    void setUp() {
        // Setup Test-Daten
        customer1 = new Customer("Max", "Mustermann", "max@test.de", "0123456789");
        customer1.setId(1L);

        customer2 = new Customer("Anna", "Schmidt", "anna@test.de", "0987654321");
        customer2.setId(2L);

        hall = new Hall("Testsaal", 100);
        hall.setId(1L);

        seat1 = new Seat("Reihe 1", 1, hall);
        seat1.setId(1L);
        seat2 = new Seat("Reihe 1", 2, hall);
        seat2.setId(2L);
        seat3 = new Seat("Reihe 2", 1, hall);
        seat3.setId(3L);

        event = new Event("Testkonzert", "Beschreibung",
                LocalDateTime.now().plusDays(7), "Musik", 50.0, hall);
        event.setId(1L);

        // Test-Buchungen erstellen
        Booking booking1 = new Booking(customer1, event, seat1, 50.0);
        booking1.setId(1L);
        try {
            booking1.reserve();
            booking1.confirm();
        } catch (Exception e) {}

        Booking booking2 = new Booking(customer2, event, seat2, 50.0);
        booking2.setId(2L);
        try {
            booking2.reserve();
        } catch (Exception e) {}

        Booking booking3 = new Booking(customer1, event, seat3, 50.0);
        booking3.setId(3L);
        try {
            booking3.reserve();
            booking3.confirm();
            booking3.cancel();
        } catch (Exception e) {}

        testBookings = Arrays.asList(booking1, booking2, booking3);
    }

    @Test
    @DisplayName("Filtere aktive Buchungen (nicht storniert)")
    void testFilterActiveBookings() {
        List<Booking> active = testBookings.stream()
                .filter(b -> b.getStatus() != BookingStatus.CANCELLED)
                .toList();

        assertEquals(2, active.size());
    }

    @Test
    @DisplayName("Filtere bestätigte Buchungen")
    void testFilterConfirmedBookings() {
        List<Booking> confirmed = testBookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.CONFIRMED)
                .toList();

        assertEquals(1, confirmed.size());
    }

    @Test
    @DisplayName("Sortiere Buchungen nach Preis (absteigend)")
    void testSortBookingsByPriceDesc() {
        // Alle haben gleichen Preis, Test mit verschiedenen Preisen
        testBookings.get(0).setPrice(60.0);
        testBookings.get(1).setPrice(40.0);
        testBookings.get(2).setPrice(50.0);

        List<Booking> sorted = testBookings.stream()
                .sorted((b1, b2) -> Double.compare(b2.getPrice(), b1.getPrice()))
                .toList();

        assertEquals(60.0, sorted.get(0).getPrice());
        assertEquals(50.0, sorted.get(1).getPrice());
        assertEquals(40.0, sorted.get(2).getPrice());
    }

    @Test
    @DisplayName("Berechne Gesamtumsatz bestätigter Buchungen")
    void testCalculateTotalRevenue() {
        double totalRevenue = testBookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.CONFIRMED)
                .mapToDouble(Booking::getPrice)
                .sum();

        assertEquals(50.0, totalRevenue);
    }

    @Test
    @DisplayName("Finde teuerste Buchung")
    void testFindMostExpensiveBooking() {
        testBookings.get(0).setPrice(100.0);
        testBookings.get(1).setPrice(50.0);
        testBookings.get(2).setPrice(75.0);

        Booking mostExpensive = testBookings.stream()
                .max((b1, b2) -> Double.compare(b1.getPrice(), b2.getPrice()))
                .orElse(null);

        assertNotNull(mostExpensive);
        assertEquals(100.0, mostExpensive.getPrice());
    }

    @Test
    @DisplayName("Zähle Buchungen pro Kunde")
    void testCountBookingsPerCustomer() {
        long customer1Bookings = testBookings.stream()
                .filter(b -> b.getCustomer().equals(customer1))
                .count();

        long customer2Bookings = testBookings.stream()
                .filter(b -> b.getCustomer().equals(customer2))
                .count();

        assertEquals(2, customer1Bookings);
        assertEquals(1, customer2Bookings);
    }

    @Test
    @DisplayName("Prüfe ob alle Buchungen ein Event haben")
    void testAllBookingsHaveEvent() {
        boolean allHaveEvent = testBookings.stream()
                .allMatch(b -> b.getEvent() != null);

        assertTrue(allHaveEvent);
    }

    @Test
    @DisplayName("Prüfe ob mindestens eine Buchung bestätigt ist")
    void testAnyBookingConfirmed() {
        boolean anyConfirmed = testBookings.stream()
                .anyMatch(b -> b.getStatus() == BookingStatus.CONFIRMED);

        assertTrue(anyConfirmed);
    }

    @Test
    @DisplayName("Gruppiere Buchungen nach Status")
    void testGroupBookingsByStatus() {
        var grouped = testBookings.stream()
                .collect(java.util.stream.Collectors.groupingBy(Booking::getStatus));

        assertTrue(grouped.containsKey(BookingStatus.CONFIRMED));
        assertTrue(grouped.containsKey(BookingStatus.RESERVED));
        assertTrue(grouped.containsKey(BookingStatus.CANCELLED));
    }

    @Test
    @DisplayName("Berechne Durchschnittspreis")
    void testCalculateAveragePrice() {
        double average = testBookings.stream()
                .mapToDouble(Booking::getPrice)
                .average()
                .orElse(0.0);

        assertEquals(50.0, average, 0.01);
    }

    @Test
    @DisplayName("Finde Buchungen mit Preis über Schwellwert")
    void testFindBookingsAbovePrice() {
        testBookings.get(0).setPrice(60.0);
        testBookings.get(1).setPrice(40.0);
        testBookings.get(2).setPrice(55.0);

        List<Booking> expensive = testBookings.stream()
                .filter(b -> b.getPrice() > 50.0)
                .toList();

        assertEquals(2, expensive.size());
    }

    @Test
    @DisplayName("Mappe Buchungen zu Kundennamen")
    void testMapBookingsToCustomerNames() {
        List<String> customerNames = testBookings.stream()
                .map(b -> b.getCustomer().getFullName())
                .distinct()
                .toList();

        assertEquals(2, customerNames.size());
        assertTrue(customerNames.contains("Max Mustermann"));
        assertTrue(customerNames.contains("Anna Schmidt"));
    }

    @Test
    @DisplayName("Berechne Anzahl Buchungen pro Status")
    void testCountBookingsPerStatus() {
        long confirmed = testBookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.CONFIRMED)
                .count();

        long reserved = testBookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.RESERVED)
                .count();

        long cancelled = testBookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.CANCELLED)
                .count();

        assertEquals(1, confirmed);
        assertEquals(1, reserved);
        assertEquals(1, cancelled);
    }
}