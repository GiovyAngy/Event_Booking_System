package exceptions;

/**
 * Exception für ungültige Statuswechsel bei Buchungen
 */
public class InvalidBookingStatusTransitionException extends Exception {

    public InvalidBookingStatusTransitionException(String message) {
        super(message);
    }

    public InvalidBookingStatusTransitionException(String message, Throwable cause) {
        super(message, cause);
    }
}