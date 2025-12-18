package model;

/**
 * Enum für den Status einer Buchung
 * Definiert die möglichen Zustände im Buchungslebenszyklus
 */
public enum BookingStatus {
    AVAILABLE("Verfügbar", "Der Sitzplatz ist verfügbar"),
    RESERVED("Reserviert", "Der Sitzplatz ist reserviert"),
    CONFIRMED("Bestätigt", "Die Buchung ist bestätigt"),
    CANCELLED("Storniert", "Die Buchung wurde storniert");

    private final String displayName;
    private final String description;

    /**
     * Konstruktor für BookingStatus
     * @param displayName Anzeigename des Status
     * @param description Beschreibung des Status
     */
    BookingStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Prüft, ob ein Statuswechsel gültig ist
     * @param newStatus Der neue Status
     * @return true wenn der Wechsel erlaubt ist
     */
    public boolean canTransitionTo(BookingStatus newStatus) {
        return switch (this) {
            case AVAILABLE -> newStatus == RESERVED;
            case RESERVED -> newStatus == CONFIRMED || newStatus == CANCELLED;
            case CONFIRMED -> newStatus == CANCELLED;
            case CANCELLED -> false; // Stornierte Buchungen können nicht mehr geändert werden
        };
    }

    @Override
    public String toString() {
        return displayName;
    }
}