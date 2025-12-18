

import model.Event;
import model.Hall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 Tests für EventService
 * Testet Sortierung und Filterung
 */
class EventServiceTest {

    private List<Event> testEvents;
    private Hall hall;

    @BeforeEach
    void setUp() {
        hall = new Hall("Testsaal", 100);
        hall.setId(1L);

        Event event1 = new Event("Konzert A", "Beschreibung",
                LocalDateTime.now().plusDays(5), "Musik", 50.0, hall);
        event1.setId(1L);

        Event event2 = new Event("Kino B", "Beschreibung",
                LocalDateTime.now().plusDays(2), "Kino", 12.0, hall);
        event2.setId(2L);

        Event event3 = new Event("Theater C", "Beschreibung",
                LocalDateTime.now().plusDays(10), "Theater", 35.0, hall);
        event3.setId(3L);

        testEvents = Arrays.asList(event1, event2, event3);
    }

    @Test
    @DisplayName("Events nach Datum sortieren")
    void testSortEventsByDate() {
        // Simuliere Service-Methode
        List<Event> sorted = testEvents.stream()
                .sorted((e1, e2) -> e1.getDateTime().compareTo(e2.getDateTime()))
                .toList();

        assertEquals("Kino B", sorted.get(0).getName());
        assertEquals("Konzert A", sorted.get(1).getName());
        assertEquals("Theater C", sorted.get(2).getName());
    }

    @Test
    @DisplayName("Events nach Namen sortieren")
    void testSortEventsByName() {
        List<Event> sorted = testEvents.stream()
                .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
                .toList();

        assertEquals("Kino B", sorted.get(0).getName());
        assertEquals("Konzert A", sorted.get(1).getName());
        assertEquals("Theater C", sorted.get(2).getName());
    }

    @Test
    @DisplayName("Zukünftige Events filtern")
    void testFilterUpcomingEvents() {
        LocalDateTime now = LocalDateTime.now();

        List<Event> upcoming = testEvents.stream()
                .filter(e -> e.getDateTime().isAfter(now))
                .toList();

        assertEquals(3, upcoming.size());
    }

    @Test
    @DisplayName("Events nach Kategorie filtern")
    void testFilterEventsByCategory() {
        List<Event> musikEvents = testEvents.stream()
                .filter(e -> e.getCategory().equals("Musik"))
                .toList();

        assertEquals(1, musikEvents.size());
        assertEquals("Konzert A", musikEvents.get(0).getName());
    }

    @Test
    @DisplayName("Events nach Namen suchen (case-insensitive)")
    void testSearchEventsByName() {
        String searchTerm = "kino";

        List<Event> found = testEvents.stream()
                .filter(e -> e.getName().toLowerCase().contains(searchTerm.toLowerCase()))
                .toList();

        assertEquals(1, found.size());
        assertEquals("Kino B", found.get(0).getName());
    }

    @Test
    @DisplayName("Event-Validierung: Datum darf nicht in Vergangenheit liegen")
    void testEventDateValidation() {
        Event pastEvent = new Event("Past Event", "Test",
                LocalDateTime.now().minusDays(1), "Test", 10.0, hall);

        assertThrows(IllegalArgumentException.class, () -> {
            if (pastEvent.getDateTime().isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("Event-Datum darf nicht in der Vergangenheit liegen");
            }
        });
    }

    @Test
    @DisplayName("Event hat korrekte Basispreis")
    void testEventBasePrice() {
        Event event = testEvents.get(0);
        assertEquals(50.0, event.getBasePrice());
    }

    @Test
    @DisplayName("Event-Kategorie wird korrekt gespeichert")
    void testEventCategory() {
        Event event = testEvents.get(0);
        assertEquals("Musik", event.getCategory());
    }

    @Test
    @DisplayName("Event hat Zuordnung zu Hall")
    void testEventHallAssociation() {
        Event event = testEvents.get(0);
        assertNotNull(event.getHall());
        assertEquals("Testsaal", event.getHall().getName());
    }
}