package controller;

import exceptions.DatabaseException;
import exceptions.InvalidBookingStatusTransitionException;
import model.*;
import service.BookingService;
import service.EventService;
import view.BookingView;
import view.BookingDialog;

import javax.swing.*;
import java.util.List;

/**
 * Controller für die Buchungsverwaltung
 * Verbindet Model, View und Service nach MVC-Pattern
 */
public class BookingController {

    private final BookingService bookingService;
    private final EventService eventService;
    private final service.CustomerService customerService;
    private final service.HallService hallService;
    private final BookingView view;

    public BookingController(BookingView view, BookingService bookingService, EventService eventService,
            service.CustomerService customerService, service.HallService hallService) {
        this.view = view;
        this.bookingService = bookingService;
        this.eventService = eventService;
        this.customerService = customerService;
        this.hallService = hallService;

        initializeEventListeners();
        loadEvents();
    }

    /**
     * Initialisiert Event-Listener für die View
     */
    private void initializeEventListeners() {
        // Event-Selection Listener
        view.addEventSelectionListener(e -> loadEventDetails());

        // Buchung erstellen Button
        view.addCreateBookingListener(e -> createBooking());

        // Buchung bestätigen Button
        view.addConfirmBookingListener(e -> confirmBooking());

        // Buchung stornieren Button
        view.addCancelBookingListener(e -> cancelBooking());

        // Refresh Button
        view.addRefreshListener(e -> refreshData());
    }

    /**
     * Lädt alle verfügbaren Events
     */
    public void loadEvents() {
        try {
            List<Event> events = eventService.findUpcomingEvents();
            view.displayEvents(events);
            // Wähle automatisch das erste Event aus, damit die Sitze geladen werden
            view.selectFirstEvent();
        } catch (DatabaseException e) {
            showError("Fehler beim Laden der Events: " + e.getMessage());
        }
    }

    /**
     * Lädt Details eines ausgewählten Events
     */
    /**
     * Lädt Details eines ausgewählten Events
     */
    private void loadEventDetails() {
        Event selectedEvent = view.getSelectedEvent();
        if (selectedEvent != null) {
            try {
                List<Booking> bookings = bookingService.findBookingsByEvent(selectedEvent.getId());
                view.displayBookings(bookings);
                view.updateAvailableSeatsLabel(selectedEvent.getAvailableSeats());

            } catch (DatabaseException e) {
                showError("Fehler beim Laden der Buchungen: " + e.getMessage());
            }
        }
    }

    /**
     * Erstellt eine neue Buchung
     */
    private void createBooking() {
        try {
            Event event = view.getSelectedEvent();
            if (event == null) {
                showWarning("Bitte wählen Sie zuerst ein Event aus der oberen Tabelle aus.");
                return;
            }

            // Lade Daten für den Dialog
            List<Customer> customers = customerService.findAllCustomers();
            List<Seat> seats = hallService.getSeatsForHall(event.getHall().getId());

            // Öffne Dialog
            BookingDialog dialog = view.openBookingDialog(event, customers, seats);

            if (dialog.isConfirmed()) {
                Customer customer = dialog.getSelectedCustomer();
                Seat seat = dialog.getSelectedSeat();

                if (customer != null && seat != null) {
                    // Erstelle Buchung
                    Booking booking = bookingService.createBooking(customer, event, seat);

                    // Füge Observer hinzu für Status-Änderungen
                    booking.addObserver((b, oldStatus, newStatus) -> {
                        view.updateStatusInTable(b.getId(), newStatus);
                        showInfo("Buchungsstatus geändert: " + oldStatus + " → " + newStatus);
                    });

                    showSuccess("Buchung erfolgreich erstellt!");
                    refreshData();
                }
            }

        } catch (DatabaseException | InvalidBookingStatusTransitionException e) {
            showError("Fehler beim Erstellen der Buchung: " + e.getMessage());
        } catch (IllegalStateException e) {
            showWarning(e.getMessage());
        }
    }

    /**
     * Bestätigt eine ausgewählte Buchung
     */
    private void confirmBooking() {
        Booking selectedBooking = view.getSelectedBooking();
        if (selectedBooking == null) {
            showWarning("Bitte wählen Sie eine Buchung aus");
            return;
        }

        try {
            bookingService.confirmBooking(selectedBooking.getId());
            showSuccess("Buchung bestätigt!");
            refreshData();
        } catch (DatabaseException | InvalidBookingStatusTransitionException e) {
            showError("Fehler beim Bestätigen der Buchung: " + e.getMessage());
        }
    }

    /**
     * Storniert eine ausgewählte Buchung
     */
    private void cancelBooking() {
        Booking selectedBooking = view.getSelectedBooking();
        if (selectedBooking == null) {
            showWarning("Bitte wählen Sie eine Buchung aus");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                null,
                "Möchten Sie diese Buchung wirklich stornieren?",
                "Bestätigung",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                bookingService.cancelBooking(selectedBooking.getId());
                showSuccess("Buchung storniert!");
                refreshData();
            } catch (DatabaseException | InvalidBookingStatusTransitionException e) {
                showError("Fehler beim Stornieren der Buchung: " + e.getMessage());
            }
        }
    }

    /**
     * Aktualisiert alle Daten in der View
     */
    private void refreshData() {
        loadEvents();
        loadEvents();
        // loadCustomers(); - Loading on demand now
        loadEventDetails(); // Reloads details if event is selected
    }

    /**
     * Zeigt eine Fehlermeldung
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Fehler", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Zeigt eine Warnung
     */
    private void showWarning(String message) {
        JOptionPane.showMessageDialog(null, message, "Warnung", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Zeigt eine Erfolgsmeldung
     */
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(null, message, "Erfolg", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Zeigt eine Info-Meldung
     */
    private void showInfo(String message) {
        JOptionPane.showMessageDialog(null, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
}