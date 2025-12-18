package service;

import dao.HallDAO;
import dao.SeatDAO;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import model.Hall;
import model.Seat;
import util.Validator;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service-Klasse für Hall-Geschäftslogik
 * Verwaltet alle Operationen rund um Säle und deren Sitzplätze
 */
public class HallService {

    private final HallDAO hallDAO;
    private final SeatDAO seatDAO;

    /**
     * Konstruktor
     */
    public HallService() {
        this.hallDAO = new HallDAO();
        this.seatDAO = new SeatDAO();
    }

    /**
     * Erstellt einen neuen Saal mit Validierung
     * 
     * @param name     Name des Saals
     * @param capacity Kapazität des Saals
     * @return Der erstellte Saal
     * @throws DatabaseException   Bei Datenbankfehlern
     * @throws ValidationException Bei Validierungsfehlern
     */
    public Hall createHall(String name, int capacity)
            throws DatabaseException, ValidationException {

        // Validierung der Eingaben
        Validator.validateName(name, "Saalname");
        Validator.validateCapacity(capacity);

        // Erstelle Saal
        Hall hall = new Hall(name, capacity);
        return hallDAO.save(hall);
    }

    /**
     * Erstellt einen Saal mit automatischer Sitzplatzgenerierung
     * 
     * @param name        Name des Saals
     * @param rows        Anzahl der Reihen
     * @param seatsPerRow Anzahl der Sitze pro Reihe
     * @return Der erstellte Saal mit Sitzplätzen
     * @throws DatabaseException   Bei Datenbankfehlern
     * @throws ValidationException Bei Validierungsfehlern
     */
    public Hall createHallWithSeats(String name, int rows, int seatsPerRow)
            throws DatabaseException, ValidationException {

        // Validierung
        if (rows <= 0 || rows > 50) {
            throw new ValidationException("Anzahl der Reihen muss zwischen 1 und 50 liegen");
        }
        if (seatsPerRow <= 0 || seatsPerRow > 50) {
            throw new ValidationException("Sitze pro Reihe müssen zwischen 1 und 50 liegen");
        }

        int totalCapacity = rows * seatsPerRow;
        Hall hall = createHall(name, totalCapacity);

        // Generiere Sitzplätze
        generateSeatsForHall(hall, rows, seatsPerRow);

        return hall;
    }

    /**
     * Generiert Sitzplätze für einen Saal
     * 
     * @param hall        Der Saal
     * @param rows        Anzahl der Reihen
     * @param seatsPerRow Sitze pro Reihe
     * @throws DatabaseException Bei Datenbankfehlern
     */
    public void generateSeatsForHall(Hall hall, int rows, int seatsPerRow)
            throws DatabaseException {

        if (hall == null || hall.getId() == null) {
            throw new IllegalArgumentException("Saal muss bereits in der Datenbank gespeichert sein");
        }

        // Lösche existierende Sitzplätze (falls vorhanden)
        List<Seat> existingSeats = seatDAO.findByHall(hall.getId());
        for (Seat seat : existingSeats) {
            seatDAO.delete(seat.getId());
        }

        // Erstelle neue Sitzplätze
        for (int row = 1; row <= rows; row++) {
            for (int number = 1; number <= seatsPerRow; number++) {
                Seat seat = new Seat("Reihe " + row, number, hall);
                Seat savedSeat = seatDAO.save(seat);
                hall.addSeat(savedSeat);
            }
        }

        // Aktualisiere Kapazität
        hall.setCapacity(rows * seatsPerRow);
        hallDAO.update(hall);
    }

    /**
     * Generiert Sitzplätze mit benutzerdefinierten Reihennamen
     * 
     * @param hall        Der Saal
     * @param rowNames    Array mit Reihennamen (z.B. ["A", "B", "C"])
     * @param seatsPerRow Sitze pro Reihe
     * @throws DatabaseException Bei Datenbankfehlern
     */
    public void generateSeatsWithCustomRows(Hall hall, String[] rowNames, int seatsPerRow)
            throws DatabaseException {

        if (hall == null || hall.getId() == null) {
            throw new IllegalArgumentException("Saal muss bereits gespeichert sein");
        }
        if (rowNames == null || rowNames.length == 0) {
            throw new IllegalArgumentException("Reihennamen dürfen nicht leer sein");
        }

        // Lösche existierende Sitzplätze
        List<Seat> existingSeats = seatDAO.findByHall(hall.getId());
        for (Seat seat : existingSeats) {
            seatDAO.delete(seat.getId());
        }

        // Erstelle Sitzplätze mit benutzerdefinierten Reihennamen
        for (String rowName : rowNames) {
            for (int number = 1; number <= seatsPerRow; number++) {
                Seat seat = new Seat("Reihe " + rowName, number, hall);
                Seat savedSeat = seatDAO.save(seat);
                hall.addSeat(savedSeat);
            }
        }

        // Aktualisiere Kapazität
        hall.setCapacity(rowNames.length * seatsPerRow);
        hallDAO.update(hall);
    }

    /**
     * Aktualisiert einen Saal
     * 
     * @param hall Der zu aktualisierende Saal
     * @throws DatabaseException   Bei Datenbankfehlern
     * @throws ValidationException Bei Validierungsfehlern
     */
    public void updateHall(Hall hall) throws DatabaseException, ValidationException {
        // Validierung
        if (hall == null || hall.getId() == null) {
            throw new IllegalArgumentException("Saal und Saal-ID dürfen nicht null sein");
        }

        Validator.validateName(hall.getName(), "Saalname");
        Validator.validateCapacity(hall.getCapacity());

        hallDAO.update(hall);
    }

    /**
     * Löscht einen Saal und alle zugehörigen Sitzplätze
     * 
     * @param id Die ID des zu löschenden Saals
     * @throws DatabaseException Bei Datenbankfehlern
     */
    public void deleteHall(Long id) throws DatabaseException {
        if (id == null) {
            throw new IllegalArgumentException("Saal-ID darf nicht null sein");
        }

        // Sitzplätze werden durch CASCADE in der Datenbank gelöscht
        hallDAO.delete(id);
    }

    /**
     * Findet einen Saal nach ID und lädt alle Sitzplätze
     * 
     * @param id Die Saal-ID
     * @return Der gefundene Saal mit Sitzplätzen
     * @throws DatabaseException Bei Datenbankfehlern
     */
    public Hall findHallById(Long id) throws DatabaseException {
        if (id == null) {
            throw new IllegalArgumentException("Saal-ID darf nicht null sein");
        }

        Hall hall = hallDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Saal mit ID " + id + " nicht gefunden"));

        // Lade Sitzplätze
        List<Seat> seats = seatDAO.findByHall(hall.getId());
        seats.forEach(hall::addSeat);

        return hall;
    }

    /**
     * Gibt alle Säle zurück
     * 
     * @return Liste aller Säle
     * @throws DatabaseException Bei Datenbankfehlern
     */
    public List<Hall> findAllHalls() throws DatabaseException {
        return hallDAO.findAll();
    }

    /**
     * Sortiert Säle nach Namen (alphabetisch)
     * 
     * @param halls Liste der zu sortierenden Säle
     * @return Sortierte Liste
     */
    public List<Hall> sortHallsByName(List<Hall> halls) {
        if (halls == null)
            return List.of();

        return halls.stream()
                .sorted(Comparator.comparing(Hall::getName))
                .collect(Collectors.toList());
    }

    /**
     * Sortiert Säle nach Kapazität (absteigend)
     * 
     * @param halls Liste der zu sortierenden Säle
     * @return Sortierte Liste
     */
    public List<Hall> sortHallsByCapacity(List<Hall> halls) {
        if (halls == null)
            return List.of();

        return halls.stream()
                .sorted(Comparator.comparing(Hall::getCapacity).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Sortiert Säle nach Kapazität (aufsteigend)
     * 
     * @param halls Liste der zu sortierenden Säle
     * @return Sortierte Liste
     */
    public List<Hall> sortHallsByCapacityAsc(List<Hall> halls) {
        if (halls == null)
            return List.of();

        return halls.stream()
                .sorted(Comparator.comparing(Hall::getCapacity))
                .collect(Collectors.toList());
    }

    /**
     * Findet Säle mit Mindestkapazität
     * 
     * @param minCapacity Minimale Kapazität
     * @return Liste der passenden Säle
     * @throws DatabaseException Bei Datenbankfehlern
     */
    public List<Hall> findHallsWithMinCapacity(int minCapacity) throws DatabaseException {
        return hallDAO.findAll().stream()
                .filter(h -> h.getCapacity() >= minCapacity)
                .collect(Collectors.toList());
    }

    /**
     * Findet Säle mit Kapazität in einem Bereich
     * 
     * @param minCapacity Minimale Kapazität
     * @param maxCapacity Maximale Kapazität
     * @return Liste der passenden Säle
     * @throws DatabaseException Bei Datenbankfehlern
     */
    public List<Hall> findHallsByCapacityRange(int minCapacity, int maxCapacity)
            throws DatabaseException {
        return hallDAO.findAll().stream()
                .filter(h -> h.getCapacity() >= minCapacity && h.getCapacity() <= maxCapacity)
                .collect(Collectors.toList());
    }

    /**
     * Sucht Säle nach Namen (case-insensitive)
     * 
     * @param searchTerm Der Suchbegriff
     * @return Liste der gefundenen Säle
     * @throws DatabaseException Bei Datenbankfehlern
     */
    public List<Hall> searchHallsByName(String searchTerm) throws DatabaseException {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return findAllHalls();
        }

        String lowerSearch = searchTerm.toLowerCase();

        return hallDAO.findAll().stream()
                .filter(h -> h.getName().toLowerCase().contains(lowerSearch))
                .collect(Collectors.toList());
    }

    /**
     * Berechnet die Gesamtkapazität aller Säle
     * 
     * @return Gesamtkapazität
     * @throws DatabaseException Bei Datenbankfehlern
     */
    public int getTotalCapacity() throws DatabaseException {
        return hallDAO.findAll().stream()
                .mapToInt(Hall::getCapacity)
                .sum();
    }

    /**
     * Berechnet die durchschnittliche Kapazität der Säle
     * 
     * @return Durchschnittliche Kapazität
     * @throws DatabaseException Bei Datenbankfehlern
     */
    public double getAverageCapacity() throws DatabaseException {
        return hallDAO.findAll().stream()
                .mapToInt(Hall::getCapacity)
                .average()
                .orElse(0.0);
    }

    /**
     * Findet den größten Saal
     * 
     * @return Der Saal mit der höchsten Kapazität
     * @throws DatabaseException Bei Datenbankfehlern
     */
    public Hall findLargestHall() throws DatabaseException {
        return hallDAO.findAll().stream()
                .max(Comparator.comparing(Hall::getCapacity))
                .orElse(null);
    }

    /**
     * Findet den kleinsten Saal
     * 
     * @return Der Saal mit der niedrigsten Kapazität
     * @throws DatabaseException Bei Datenbankfehlern
     */
    public Hall findSmallestHall() throws DatabaseException {
        return hallDAO.findAll().stream()
                .min(Comparator.comparing(Hall::getCapacity))
                .orElse(null);
    }

    /**
     * Gibt alle Sitzplätze eines Saals zurück
     * 
     * @param hallId Die Saal-ID
     * @return Liste der Sitzplätze
     * @throws DatabaseException Bei Datenbankfehlern
     */
    public List<Seat> getSeatsForHall(Long hallId) throws DatabaseException {
        if (hallId == null) {
            throw new IllegalArgumentException("Saal-ID darf nicht null sein");
        }
        return seatDAO.findByHall(hallId);
    }

    /**
     * Zählt die Anzahl der Säle
     * 
     * @return Anzahl der Säle
     * @throws DatabaseException Bei Datenbankfehlern
     */
    public long getHallCount() throws DatabaseException {
        return hallDAO.findAll().size();
    }

    /**
     * Prüft ob ein Saal existiert
     * 
     * @param hallId Die Saal-ID
     * @return true wenn Saal existiert
     * @throws DatabaseException Bei Datenbankfehlern
     */
    public boolean hallExists(Long hallId) throws DatabaseException {
        if (hallId == null)
            return false;
        return hallDAO.findById(hallId).isPresent();
    }

    /**
     * Gruppiert Säle nach Kapazitätsbereichen
     * 
     * @return Map mit Kapazitätsbereich als Key
     * @throws DatabaseException Bei Datenbankfehlern
     */
    public java.util.Map<String, List<Hall>> groupHallsByCapacityRange() throws DatabaseException {
        List<Hall> halls = hallDAO.findAll();

        java.util.Map<String, List<Hall>> grouped = new java.util.HashMap<>();
        grouped.put("Klein (< 50)", halls.stream()
                .filter(h -> h.getCapacity() < 50)
                .collect(Collectors.toList()));
        grouped.put("Mittel (50-150)", halls.stream()
                .filter(h -> h.getCapacity() >= 50 && h.getCapacity() <= 150)
                .collect(Collectors.toList()));
        grouped.put("Groß (> 150)", halls.stream()
                .filter(h -> h.getCapacity() > 150)
                .collect(Collectors.toList()));

        return grouped;
    }

    /**
     * Prüft alle Säle und generiert Sitzplätze, falls keine vorhanden sind.
     * Dies ist eine Hilfsmethode zur Initialisierung oder Reparatur der Datenbank.
     */
    public void initializeDefaultSeats() {
        try {
            List<Hall> halls = findAllHalls();
            for (Hall hall : halls) {
                List<Seat> seats = getSeatsForHall(hall.getId());
                if (seats.isEmpty()) {
                    System.out.println("Initialisiere Sitzplätze für Saal: " + hall.getName());

                    int seatsPerRow = 10;
                    if (hall.getName().contains("VIP")) {
                        seatsPerRow = 5; // Weniger Sitze pro Reihe für VIP
                    } else if (hall.getName().contains("Klein")) {
                        seatsPerRow = 8;
                    }

                    // Berechne Reihen basierend auf Kapazität
                    int capacity = hall.getCapacity();
                    int rows = (int) Math.ceil((double) capacity / seatsPerRow);

                    // Sicherheitscheck: Kapazität anpassen falls nötig oder Rows begrenzen
                    if (rows * seatsPerRow != capacity) {
                        // Wir behalten die konfigurierte Kapazität bei und passen die Generierung an?
                        // Einfacher: Wir nutzen die Logik aus generateSeatsForHall die die Kapazität
                        // updatet.
                        // Aber wir wollen die ursprüngliche Kapazität vielleicht behalten.
                        // Für diesen Fix: Wir generieren passend.
                    }

                    generateSeatsForHall(hall, rows, seatsPerRow);
                    System.out.println("  -> " + (rows * seatsPerRow) + " Sitzplätze generiert.");
                }
            }
        } catch (DatabaseException e) {
            System.err.println("Fehler bei der Sitzplatz-Initialisierung: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
