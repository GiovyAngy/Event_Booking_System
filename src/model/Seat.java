package model;

/**
 * Repräsentiert einen Sitzplatz in einer Hall.
 */
public class Seat {

    private Long id;
    private String row;
    private int number;
    private Hall hall;

    public Seat(String row, int number, Hall hall) {
        this.row = row;
        this.number = number;
        this.hall = hall;
    }

    public Seat() {

    }

    // ================= Getter und Setter =================
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }

    // ================= Methoden =================
    /**
     * Gibt die vollständige Sitzbezeichnung zurück, z.B. "Reihe A Platz 5".
     * 
     * @return Sitzbezeichnung
     */
    public String getFullLabel() {
        return "Reihe " + row + " Platz " + number;
    }

    @Override
    public String toString() {
        return getFullLabel();
    }
}
