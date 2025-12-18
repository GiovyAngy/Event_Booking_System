package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Repräsentiert einen Kunden.
 */
public class Customer {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private int activeBookingsCount; // Transient
    private List<Booking> bookings = new ArrayList<>();

    public Customer(String firstName, String lastName, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;

    }

    public Customer() {

    }

    // ================= Getter und Setter =================
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    // ================= Methoden =================
    /**
     * Gibt den vollständigen Namen des Kunden zurück.
     * 
     * @return vollständiger Name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Gibt die Anzahl der aktiven Buchungen zurück (nicht storniert).
     * 
     * @return Anzahl der aktiven Buchungen
     */
    public long getActiveBookingsCount() {
        return activeBookingsCount;
    }

    public void setActiveBookingsCount(int activeBookingsCount) {
        this.activeBookingsCount = activeBookingsCount;
    }

    /**
     * Fügt eine neue Buchung zur Kundenliste hinzu.
     * 
     * @param booking neue Buchung
     */
    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    @Override
    public String toString() {
        return getFullName();
    }
}
