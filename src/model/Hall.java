package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Repräsentiert einen Veranstaltungssaal (Hall).
 */
public class Hall {

    private Long id;
    private String name;
    private int capacity;
    private List<Seat> seats = new ArrayList<>();

    public Hall(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }
    public Hall(){

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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    // ================= Methoden =================
    /**
     * Fügt einen Sitzplatz zur Hall hinzu.
     * @param seat Sitzplatz
     */
    public void addSeat(Seat seat) {
        seats.add(seat);
        seat.setHall(this);
    }

    /**
     * Generiert Sitzplätze für die Hall.
     * Beispiel: generateSeats(3, 10) erzeugt 3 Reihen mit je 10 Plätzen.
     * @param rows Anzahl der Reihen
     * @param seatsPerRow Anzahl der Plätze pro Reihe
     */
    public void generateSeats(int rows, int seatsPerRow) {
        seats.clear();
        for (int r = 1; r <= rows; r++) {
            String rowLabel = String.valueOf((char) ('A' + r - 1)); // Reihen A, B, C, ...
            for (int n = 1; n <= seatsPerRow; n++) {
                Seat seat = new Seat();
                seat.setRow(rowLabel);
                seat.setNumber(n);
                seat.setHall(this);
                seats.add(seat);
            }
        }
        this.capacity = rows * seatsPerRow;
    }
}
