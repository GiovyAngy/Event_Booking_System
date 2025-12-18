package controller;

import exceptions.DatabaseException;

import model.Event;
import model.Hall;
import service.EventService;
import service.HallService;
import view.EventDialog;
import view.EventManagementView;

import javax.swing.*;
import java.util.List;

/**
 * Controller für die Event-Verwaltung
 * Verbindet EventManagementView mit EventService
 */
public class EventController {

    private final EventManagementView view;
    private final EventService eventService;
    private final HallService hallService;

    /**
     * Konstruktor
     * 
     * @param view         Die Event-Management View
     * @param eventService Der Event Service
     * @param hallService  Der Hall Service
     */
    public EventController(EventManagementView view, EventService eventService, HallService hallService) {
        this.view = view;
        this.eventService = eventService;
        this.hallService = hallService;

        initializeEventListeners();
        loadEvents(); // Initial laden
    }

    /**
     * Initialisiert alle Event-Listener
     */
    private void initializeEventListeners() {
        view.addAddButtonListener(e -> createEvent());
        view.addEditButtonListener(e -> editEvent());
        view.addDeleteButtonListener(e -> deleteEvent());
        view.addRefreshButtonListener(e -> loadEvents());
        view.addSearchListener(e -> searchEvents());
        view.addCategoryFilterListener(e -> filterByCategory());
    }

    /**
     * Lädt alle Events und zeigt sie in der View an
     */
    public void loadEvents() {
        try {
            view.setStatus("Lade Events...");
            List<Event> events = eventService.findUpcomingEvents();
            view.displayEvents(events);
            view.setStatus("Events erfolgreich geladen");
        } catch (DatabaseException e) {
            showError("Fehler beim Laden der Events: " + e.getMessage());
            view.setStatus("Fehler beim Laden");
        }
    }

    /**
     * Erstellt ein neues Event
     */
    private void createEvent() {
        try {
            // Lade verfügbare Säle
            List<Hall> halls = hallService.findAllHalls();

            if (halls.isEmpty()) {
                showWarning("Bitte erstellen Sie zunächst einen Saal");
                return;
            }

            // Öffne Dialog
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(view);
            EventDialog dialog = new EventDialog(parentFrame, halls);
            dialog.setVisible(true);

            // Prüfe ob bestätigt
            Event newEvent = dialog.getEvent();
            if (newEvent != null) {
                view.setStatus("Erstelle Event...");
                eventService.createEvent(newEvent);
                showSuccess("Event '" + newEvent.getName() + "' erfolgreich erstellt!");
                loadEvents(); // Aktualisiere Liste
            }
        } catch (DatabaseException e) {
            showError("Fehler beim Erstellen des Events: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            showWarning("Validierungsfehler: " + e.getMessage());
        }
    }

    /**
     * Bearbeitet ein bestehendes Event
     */
    private void editEvent() {
        Long eventId = view.getSelectedEventId();
        if (eventId == null) {
            showWarning("Bitte wählen Sie ein Event aus");
            return;
        }

        try {
            // Lade Event und Säle
            Event event = eventService.findEventById(eventId);
            List<Hall> halls = hallService.findAllHalls();

            // Öffne Dialog mit vorhandenen Daten
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(view);
            EventDialog dialog = new EventDialog(parentFrame, event, halls);
            dialog.setVisible(true);

            // Prüfe ob bestätigt
            if (dialog.isConfirmed()) {
                view.setStatus("Aktualisiere Event...");
                eventService.updateEvent(event);
                showSuccess("Event erfolgreich aktualisiert!");
                loadEvents();
            }
        } catch (DatabaseException e) {
            showError("Fehler beim Bearbeiten des Events: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            showError("Event nicht gefunden: " + e.getMessage());
        }
    }

    /**
     * Löscht ein Event nach Bestätigung
     */
    private void deleteEvent() {
        Long eventId = view.getSelectedEventId();
        if (eventId == null) {
            showWarning("Bitte wählen Sie ein Event aus");
            return;
        }

        try {
            // Lade Event für Anzeige im Bestätigungsdialog
            Event event = eventService.findEventById(eventId);

            // Bestätigungsdialog
            int confirm = JOptionPane.showConfirmDialog(
                    view,
                    "Möchten Sie das Event '" + event.getName() + "' wirklich löschen?\n\n" +
                            "ACHTUNG: Alle zugehörigen Buchungen werden ebenfalls gelöscht!",
                    "Event löschen",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                view.setStatus("Lösche Event...");
                eventService.deleteEvent(eventId);
                showSuccess("Event erfolgreich gelöscht!");
                loadEvents();
            }
        } catch (DatabaseException e) {
            showError("Fehler beim Löschen des Events: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            showError("Event nicht gefunden: " + e.getMessage());
        }
    }

    /**
     * Sucht Events nach Namen
     */
    private void searchEvents() {
        String searchTerm = view.getSearchText();

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            loadEvents(); // Zeige alle
            return;
        }

        try {
            view.setStatus("Suche Events...");
            List<Event> events = eventService.searchEventsByName(searchTerm);
            view.displayEvents(events);

            if (events.isEmpty()) {
                view.setStatus("Keine Events gefunden für: " + searchTerm);
            } else {
                view.setStatus(events.size() + " Event(s) gefunden");
            }
        } catch (DatabaseException e) {
            showError("Fehler bei der Suche: " + e.getMessage());
            view.setStatus("Suchfehler");
        }
    }

    /**
     * Filtert Events nach ausgewählter Kategorie
     */
    private void filterByCategory() {
        String category = view.getSelectedCategory();

        try {
            view.setStatus("Filtere Events...");
            List<Event> events;

            if (category == null) {
                // "Alle Kategorien" ausgewählt
                events = eventService.findUpcomingEvents();
            } else {
                events = eventService.findEventsByCategory(category);
            }

            view.displayEvents(events);

            if (category == null) {
                view.setStatus("Alle Events angezeigt");
            } else {
                view.setStatus(events.size() + " Event(s) in Kategorie '" + category + "'");
            }
        } catch (DatabaseException e) {
            showError("Fehler beim Filtern: " + e.getMessage());
            view.setStatus("Filterfehler");
        }
    }

    /**
     * Lädt alle Events (inkl. vergangene)
     */
    public void loadAllEvents() {
        try {
            view.setStatus("Lade alle Events...");
            List<Event> events = eventService.findAllEvents();
            view.displayEvents(events);
            view.setStatus("Alle Events geladen");
        } catch (DatabaseException e) {
            showError("Fehler beim Laden: " + e.getMessage());
        }
    }

    /**
     * Lädt nur zukünftige Events
     */
    public void loadUpcomingEvents() {
        loadEvents(); // Verwendet bereits findUpcomingEvents
    }

    // ========== Hilfsmethoden für Dialoge ==========

    /**
     * Zeigt eine Fehlermeldung
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(
                view,
                message,
                "Fehler",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Zeigt eine Warnung
     */
    private void showWarning(String message) {
        JOptionPane.showMessageDialog(
                view,
                message,
                "Warnung",
                JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Zeigt eine Erfolgsmeldung
     */
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(
                view,
                message,
                "Erfolg",
                JOptionPane.INFORMATION_MESSAGE);
    }

}