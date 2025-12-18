package util;

import exceptions.ValidationException;
import java.util.regex.Pattern;

/**
 * Utility-Klasse für Validierungen
 * Bietet statische Methoden zur Validierung von Eingaben
 */
public final class Validator {

    // Private Konstruktor - Utility-Klasse soll nicht instanziiert werden
    private Validator() {
        throw new UnsupportedOperationException("Utility-Klasse kann nicht instanziiert werden");
    }

    // Regex-Patterns
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[0-9\\-\\+\\s\\(\\)]{6,20}$");

    /**
     * Validiert eine E-Mail-Adresse
     */
    public static void validateEmail(String email) throws ValidationException {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("E-Mail darf nicht leer sein");
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException("Ungültige E-Mail-Adresse: " + email);
        }
    }

    /**
     * Validiert eine Telefonnummer
     */
    public static void validatePhone(String phone) throws ValidationException {
        if (phone == null || phone.trim().isEmpty()) {
            throw new ValidationException("Telefonnummer darf nicht leer sein");
        }

        if (!PHONE_PATTERN.matcher(phone).matches()) {
            throw new ValidationException("Ungültige Telefonnummer: " + phone);
        }
    }

    /**
     * Validiert einen Namen
     */
    public static void validateName(String name, String fieldName) throws ValidationException {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException(fieldName + " darf nicht leer sein");
        }

        if (name.length() < 2) {
            throw new ValidationException(fieldName + " muss mindestens 2 Zeichen lang sein");
        }

        if (name.length() > 100) {
            throw new ValidationException(fieldName + " darf maximal 100 Zeichen lang sein");
        }
    }

    /**
     * Validiert einen Preis
     */
    public static void validatePrice(double price) throws ValidationException {
        if (price < 0) {
            throw new ValidationException("Preis darf nicht negativ sein");
        }

        if (price > 10000) {
            throw new ValidationException("Preis darf nicht über 10.000 € liegen");
        }
    }

    /**
     * Validiert eine Kapazität
     */
    public static void validateCapacity(int capacity) throws ValidationException {
        if (capacity < 1) {
            throw new ValidationException("Kapazität muss mindestens 1 sein");
        }

        if (capacity > 1000) {
            throw new ValidationException("Kapazität darf maximal 1000 sein");
        }
    }

    /**
     * Prüft ob ein String nicht null oder leer ist
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Prüft ob ein Objekt nicht null ist
     */
    public static <T> void requireNonNull(T object, String fieldName) throws ValidationException {
        if (object == null) {
            throw new ValidationException(fieldName + " darf nicht null sein");
        }
    }
}