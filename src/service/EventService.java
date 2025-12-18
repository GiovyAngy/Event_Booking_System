package service;

import dao.EventDAO;
import exceptions.DatabaseException;
import model.Event;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service-Klasse für Event-Geschäftslogik
 */
public class EventService {

    private final EventDAO eventDAO;

    public EventService() {
        this.eventDAO = new EventDAO();
    }

    /**
     * Erstellt ein neues Event
     */
    public Event createEvent(Event event) throws DatabaseException {
        // Validierung
        if (event.getDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Event-Datum darf nicht in der Vergangenheit liegen");
        }

        return eventDAO.save(event);
    }

    /**
     * Aktualisiert ein Event
     */
    public void updateEvent(Event event) throws DatabaseException {
        eventDAO.update(event);
    }

    /**
     * Löscht ein Event
     */
    public void deleteEvent(Long id) throws DatabaseException {
        eventDAO.delete(id);
    }

    /**
     * Findet ein Event nach ID
     */
    public Event findEventById(Long id) throws DatabaseException {
        return eventDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event nicht gefunden"));
    }

    /**
     * Gibt alle Events zurück
     */
    public List<Event> findAllEvents() throws DatabaseException {
        return eventDAO.findAll();
    }

    /**
     * Findet Events nach Kategorie
     */
    public List<Event> findEventsByCategory(String category) throws DatabaseException {
        return eventDAO.findByCategory(category);
    }

    /**
     * Sortiert Events nach Datum (nächste zuerst)
     * Demonstriert Comparator
     */
    public List<Event> sortEventsByDate(List<Event> events) {
        return events.stream()
                .sorted(Comparator.comparing(Event::getDateTime))
                .collect(Collectors.toList());
    }

    /**
     * Sortiert Events nach Namen (alphabetisch)
     */
    public List<Event> sortEventsByName(List<Event> events) {
        return events.stream()
                .sorted(Comparator.comparing(Event::getName))
                .collect(Collectors.toList());
    }

    /**
     * Filtert zukünftige Events
     * Demonstriert Stream API und Lambda
     */
    public List<Event> findUpcomingEvents() throws DatabaseException {
        LocalDateTime now = LocalDateTime.now();
        return eventDAO.findAll().stream()
                .filter(e -> e.getDateTime().isAfter(now))
                .sorted(Comparator.comparing(Event::getDateTime))
                .collect(Collectors.toList());
    }

    /**
     * Filtert Events mit verfügbaren Plätzen
     */
    public List<Event> findEventsWithAvailableSeats() throws DatabaseException {
        return eventDAO.findAll().stream()
                .filter(e -> e.getAvailableSeats() > 0)
                .collect(Collectors.toList());
    }

    /**
     * Sucht Events nach Namen (case-insensitive)
     * Demonstriert Stream API
     */
    public List<Event> searchEventsByName(String searchTerm) throws DatabaseException {
        return eventDAO.findAll().stream()
                .filter(e -> e.getName().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Gruppiert Events nach Kategorie
     * Demonstriert Collectors.groupingBy
     */
    public java.util.Map<String, List<Event>> groupEventsByCategory() throws DatabaseException {
        return eventDAO.findAll().stream()
                .collect(Collectors.groupingBy(Event::getCategory));
    }
}