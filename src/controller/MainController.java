package controller;

import service.*;
import view.*;

import javax.swing.*;

/**
 * Haupt-Controller für die gesamte Anwendung
 * Koordiniert alle Sub-Controller
 */
public class MainController {

    private final MainView mainView;
    private final BookingController bookingController;
    private final EventController eventController;
    private final CustomerController customerController;
    private final ReportController reportController;

    // Services
    private final BookingService bookingService;
    private final EventService eventService;
    private final CustomerService customerService;
    private final HallService hallService;

    /**
     * Konstruktor
     */
    public MainController() {
        // Initialisiere Services
        this.bookingService = new BookingService();
        this.eventService = new EventService();
        this.customerService = new CustomerService();
        this.hallService = new HallService();

        // Initialisiere View
        this.mainView = new MainView();

        // Initialisiere Sub-Controller
        this.bookingController = new BookingController(
                mainView.getBookingView(),
                bookingService,
                eventService,
                customerService,
                hallService);

        this.eventController = new EventController(
                mainView.getEventManagementView(),
                eventService,
                hallService);

        this.customerController = new CustomerController(
                mainView.getCustomerManagementView(),
                customerService,
                bookingService);

        this.reportController = new ReportController(
                mainView.getReportView(),
                bookingService,
                eventService,
                customerService);

        // Initiale Daten laden
        hallService.initializeDefaultSeats(); // Self-healing: Sitze generieren falls fehlend
        loadAllData();

        // Zeige Hauptfenster
        mainView.setVisible(true);
    }

    /**
     * Lädt alle Daten für alle Views
     */
    private void loadAllData() {
        try {
            bookingController.loadEvents();
            eventController.loadEvents();
            customerController.loadCustomers();
            reportController.updateStatistics();
        } catch (Exception e) {
            showError("Fehler beim Laden der Daten: " + e.getMessage());
        }
    }

    /**
     * Aktualisiert alle Views
     */
    public void refreshAll() {
        loadAllData();
    }

    /**
     * Zeigt eine Fehlermeldung
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(
                mainView,
                message,
                "Fehler",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Gibt die Hauptansicht zurück
     */
    public MainView getMainView() {
        return mainView;
    }
}