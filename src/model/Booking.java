package model;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * Repräsentiert eine Buchung für ein Event.
 */
public class Booking {

    private Long id;
    private Customer customer;
    private Event event;
    private Seat seat;
    private BookingStatus status;
    private LocalDateTime bookingDate;
    private double price;
    private List<BookingObserver> observers = new ArrayList<>();

    // ================= Standard-Konstruktor =================
    public Booking() {
        this.bookingDate = LocalDateTime.now();
        this.status = BookingStatus.AVAILABLE;
    }

    // ================= Konstruktor mit Parametern =================
    public Booking(Customer customer, Event event, Seat seat, double price) {
        this.customer = customer;
        this.event = event;
        this.seat = seat;
        this.price = price;
        this.bookingDate = LocalDateTime.now();
        this.status = BookingStatus.AVAILABLE;
        this.observers = new ArrayList<>();
    }

    // ================= Getter und Setter =================
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }

    public Seat getSeat() { return seat; }
    public void setSeat(Seat seat) { this.seat = seat; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }

    public LocalDateTime getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDateTime bookingDate) { this.bookingDate = bookingDate; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public List<BookingObserver> getObservers() { return observers; }

    // ================= Observer-Methoden =================
    public void addObserver(BookingObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers(BookingStatus oldStatus, BookingStatus newStatus) {
        for (BookingObserver observer : observers) {
            observer.onStatusChanged(this, oldStatus, newStatus);
        }
    }

    // ================= Buchungsaktionen =================
    public void reserve() {
        if (status.canTransitionTo(BookingStatus.RESERVED)) {
            BookingStatus oldStatus = this.status;
            this.status = BookingStatus.RESERVED;
            notifyObservers(oldStatus, this.status);
        } else {
            throw new IllegalStateException("Statuswechsel nicht erlaubt: " + status + " -> RESERVED");
        }
    }

    public void confirm() {
        if (status.canTransitionTo(BookingStatus.CONFIRMED)) {
            BookingStatus oldStatus = this.status;
            this.status = BookingStatus.CONFIRMED;
            notifyObservers(oldStatus, this.status);
        } else {
            throw new IllegalStateException("Statuswechsel nicht erlaubt: " + status + " -> CONFIRMED");
        }
    }

    public void cancel() {
        if (status.canTransitionTo(BookingStatus.CANCELLED)) {
            BookingStatus oldStatus = this.status;
            this.status = BookingStatus.CANCELLED;
            notifyObservers(oldStatus, this.status);
        } else {
            throw new IllegalStateException("Statuswechsel nicht erlaubt: " + status + " -> CANCELLED");
        }
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", customer=" + customer +
                ", event=" + event +
                ", seat=" + seat +
                ", status=" + status +
                ", bookingDate=" + bookingDate +
                ", price=" + price +
                ", observers=" + observers +
                '}';
    }
}
