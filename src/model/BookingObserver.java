package model;

/**
 * Observer Interface für Buchungsänderungen
 * Implementiert das Observer Pattern
 */
@FunctionalInterface
public interface BookingObserver {
    /**
     * Wird aufgerufen, wenn sich der Status einer Buchung ändert
     * @param booking Die betroffene Buchung
     * @param oldStatus Der alte Status
     * @param newStatus Der neue Status
     */
    void onStatusChanged(Booking booking, BookingStatus oldStatus, BookingStatus newStatus);
}