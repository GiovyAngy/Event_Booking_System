

import model.BookingStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 Tests für BookingStatus Enum
 */
class BookingStatusTest {

    @Test
    @DisplayName("Statuswechsel von AVAILABLE zu RESERVED ist erlaubt")
    void testValidTransitionFromAvailableToReserved() {
        BookingStatus status = BookingStatus.AVAILABLE;
        assertTrue(status.canTransitionTo(BookingStatus.RESERVED));
    }

    @Test
    @DisplayName("Statuswechsel von AVAILABLE zu CONFIRMED ist nicht erlaubt")
    void testInvalidTransitionFromAvailableToConfirmed() {
        BookingStatus status = BookingStatus.AVAILABLE;
        assertFalse(status.canTransitionTo(BookingStatus.CONFIRMED));
    }

    @Test
    @DisplayName("Statuswechsel von RESERVED zu CONFIRMED ist erlaubt")
    void testValidTransitionFromReservedToConfirmed() {
        BookingStatus status = BookingStatus.RESERVED;
        assertTrue(status.canTransitionTo(BookingStatus.CONFIRMED));
    }

    @Test
    @DisplayName("Statuswechsel von RESERVED zu CANCELLED ist erlaubt")
    void testValidTransitionFromReservedToCancelled() {
        BookingStatus status = BookingStatus.RESERVED;
        assertTrue(status.canTransitionTo(BookingStatus.CANCELLED));
    }

    @Test
    @DisplayName("Statuswechsel von CONFIRMED zu CANCELLED ist erlaubt")
    void testValidTransitionFromConfirmedToCancelled() {
        BookingStatus status = BookingStatus.CONFIRMED;
        assertTrue(status.canTransitionTo(BookingStatus.CANCELLED));
    }

    @Test
    @DisplayName("Statuswechsel von CANCELLED zu irgendeinem Status ist nicht erlaubt")
    void testNoTransitionFromCancelled() {
        BookingStatus status = BookingStatus.CANCELLED;
        assertFalse(status.canTransitionTo(BookingStatus.AVAILABLE));
        assertFalse(status.canTransitionTo(BookingStatus.RESERVED));
        assertFalse(status.canTransitionTo(BookingStatus.CONFIRMED));
    }

    @Test
    @DisplayName("DisplayName wird korrekt zurückgegeben")
    void testGetDisplayName() {
        assertEquals("Verfügbar", BookingStatus.AVAILABLE.getDisplayName());
        assertEquals("Reserviert", BookingStatus.RESERVED.getDisplayName());
        assertEquals("Bestätigt", BookingStatus.CONFIRMED.getDisplayName());
        assertEquals("Storniert", BookingStatus.CANCELLED.getDisplayName());
    }

    @Test
    @DisplayName("toString gibt DisplayName zurück")
    void testToString() {
        assertEquals("Verfügbar", BookingStatus.AVAILABLE.toString());
    }

    @Test
    @DisplayName("Description ist nicht null")
    void testDescriptionNotNull() {
        for (BookingStatus status : BookingStatus.values()) {
            assertNotNull(status.getDescription());
            assertFalse(status.getDescription().isEmpty());
        }
    }
}