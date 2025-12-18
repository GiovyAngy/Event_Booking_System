package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Repräsentiert ein Event.
 */
public class Event {

    private Long id;
    private String name;
    private String description;
    private LocalDateTime dateTime;
    private String category;
    private double basePrice;
    private Hall hall;
    private int bookedCount; // Transient field for display
    private List<Booking> bookings = new ArrayList<>();

    public Event() {
    }

    public Event(String name, String description, LocalDateTime dateTime, String category, double basePrice,
            Hall hall) {
        this.name = name;
        this.description = description;
        this.dateTime = dateTime;
        this.category = category;
        this.basePrice = basePrice;
        this.hall = hall;
    }

    // ================= Getter und Setter =================
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    // ================= Methoden =================
    /**
     * Gibt die Anzahl der verfügbaren Sitzplätze zurück.
     * 
     * @return Anzahl verfügbarer Plätze
     */
    public int getAvailableSeats() {
        if (hall == null) {
            return 0;
        }
        // Use capacity and the pre-fetched bookedCount
        return hall.getCapacity() - bookedCount;
    }

    public int getBookedCount() {
        return bookedCount;
    }

    public void setBookedCount(int bookedCount) {
        this.bookedCount = bookedCount;
    }

    /**
     * Fügt eine neue Buchung zum Event hinzu.
     * 
     * @param booking neue Buchung
     */
    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", dateTime=" + dateTime +
                ", category='" + category + '\'' +
                ", basePrice=" + basePrice +
                ", hall=" + hall +
                ", bookings=" + bookings +
                '}';
    }
}
