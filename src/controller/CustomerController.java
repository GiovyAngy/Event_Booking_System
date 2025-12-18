package controller;

import exceptions.DatabaseException;
import exceptions.ValidationException;
import model.Booking;
import model.Customer;
import service.BookingService;
import service.CustomerService;
import view.CustomerDialog;
import view.CustomerManagementView;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

/**
 * Controller für die Kunden-Verwaltung
 * Verbindet CustomerManagementView mit CustomerService
 */
public class CustomerController {

    private final CustomerManagementView view;
    private final CustomerService customerService;
    private final BookingService bookingService;

    /**
     * Konstruktor
     * 
     * @param view            Die Customer-Management View
     * @param customerService Der Customer Service
     * @param bookingService  Der Booking Service
     */
    public CustomerController(CustomerManagementView view, CustomerService customerService,
            BookingService bookingService) {
        this.view = view;
        this.customerService = customerService;
        this.bookingService = bookingService;

        initializeEventListeners();
        loadCustomers(); // Initial laden
    }

    /**
     * Initialisiert alle Event-Listener
     */
    private void initializeEventListeners() {
        view.addAddButtonListener(e -> createCustomer());
        view.addEditButtonListener(e -> editCustomer());
        view.addDeleteButtonListener(e -> deleteCustomer());
        view.addRefreshButtonListener(e -> loadCustomers());
        view.addSearchListener(e -> searchCustomers());
        view.addViewBookingsButtonListener(e -> viewCustomerBookings());
        view.addTableSelectionListener(e -> {
        });

        // Live-Suche beim Tippen
        view.addSearchKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                performLiveSearch();
            }
        });
    }

    /**
     * Lädt alle Kunden und zeigt sie in der View an
     */
    public void loadCustomers() {
        try {
            view.setStatus("Lade Kunden...");
            List<Customer> customers = customerService.findAllCustomers();
            view.displayCustomers(customers);
            view.setStatus("Kunden erfolgreich geladen");
        } catch (DatabaseException e) {
            showError("Fehler beim Laden der Kunden: " + e.getMessage());
            view.setStatus("Fehler beim Laden");
        }
    }

    /**
     * Erstellt einen neuen Kunden
     */
    private void createCustomer() {
        // Öffne Dialog
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(view);
        CustomerDialog dialog = new CustomerDialog(parentFrame);
        dialog.setVisible(true);

        // Prüfe ob bestätigt
        Customer newCustomer = dialog.getCustomer();
        if (newCustomer != null) {
            try {
                view.setStatus("Erstelle Kunden...");
                Customer savedCustomer = customerService.createCustomer(
                        newCustomer.getFirstName(),
                        newCustomer.getLastName(),
                        newCustomer.getEmail(),
                        newCustomer.getPhone());
                showSuccess("Kunde '" + savedCustomer.getFullName() + "' erfolgreich erstellt!");
                loadCustomers(); // Aktualisiere Liste

                // Wähle den neuen Kunden aus
                view.selectCustomerById(savedCustomer.getId());
            } catch (DatabaseException e) {
                showError("Fehler beim Erstellen des Kunden: " + e.getMessage());
            } catch (ValidationException e) {
                showWarning("Validierungsfehler: " + e.getMessage());
            }
        }
    }

    /**
     * Bearbeitet einen bestehenden Kunden
     */
    private void editCustomer() {
        Long customerId = view.getSelectedCustomerId();
        if (customerId == null) {
            showWarning("Bitte wählen Sie einen Kunden aus");
            return;
        }

        try {
            // Lade Kunde
            Customer customer = customerService.findCustomerById(customerId);

            // Öffne Dialog mit vorhandenen Daten
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(view);
            CustomerDialog dialog = new CustomerDialog(parentFrame, customer);
            dialog.setVisible(true);

            // Prüfe ob bestätigt
            if (dialog.isConfirmed()) {
                view.setStatus("Aktualisiere Kunden...");
                customerService.updateCustomer(customer);
                showSuccess("Kunde erfolgreich aktualisiert!");
                loadCustomers();

                // Wähle den aktualisierten Kunden wieder aus
                view.selectCustomerById(customerId);
            }
        } catch (DatabaseException e) {
            showError("Fehler beim Bearbeiten des Kunden: " + e.getMessage());
        } catch (ValidationException e) {
            showWarning("Validierungsfehler: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            showError("Kunde nicht gefunden: " + e.getMessage());
        }
    }

    /**
     * Löscht einen Kunden nach Bestätigung
     */
    private void deleteCustomer() {
        Long customerId = view.getSelectedCustomerId();
        if (customerId == null) {
            showWarning("Bitte wählen Sie einen Kunden aus");
            return;
        }

        try {
            // Lade Kunde für Anzeige und Prüfung
            Customer customer = customerService.findCustomerById(customerId);

            // Prüfe ob Kunde aktive Buchungen hat
            long activeBookings = customer.getActiveBookingsCount();
            if (activeBookings > 0) {
                showWarning(
                        "Dieser Kunde hat noch " + activeBookings + " aktive Buchung(en) " +
                                "und kann nicht gelöscht werden.\n\n" +
                                "Bitte stornieren Sie zuerst alle Buchungen.");
                return;
            }

            // Bestätigungsdialog
            int confirm = JOptionPane.showConfirmDialog(
                    view,
                    "Möchten Sie den Kunden '" + customer.getFullName() + "' wirklich löschen?\n\n" +
                            "E-Mail: " + customer.getEmail(),
                    "Kunde löschen",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                view.setStatus("Lösche Kunden...");
                customerService.deleteCustomer(customerId);
                showSuccess("Kunde erfolgreich gelöscht!");
                loadCustomers();
            }
        } catch (DatabaseException e) {
            showError("Fehler beim Löschen des Kunden: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            showError("Kunde nicht gefunden: " + e.getMessage());
        }
    }

    /**
     * Sucht Kunden nach Namen oder E-Mail
     */
    private void searchCustomers() {
        String searchTerm = view.getSearchText();

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            loadCustomers(); // Zeige alle
            return;
        }

        try {
            view.setStatus("Suche Kunden...");
            List<Customer> customers = customerService.searchCustomersByName(searchTerm);
            view.displayCustomers(customers);

            if (customers.isEmpty()) {
                view.setStatus("Keine Kunden gefunden für: " + searchTerm);
            } else {
                view.setStatus(customers.size() + " Kunde(n) gefunden");
            }
        } catch (DatabaseException e) {
            showError("Fehler bei der Suche: " + e.getMessage());
            view.setStatus("Suchfehler");
        }
    }

    /**
     * Führt eine Live-Suche durch (filtert die Tabelle)
     */
    private void performLiveSearch() {
        String searchText = view.getSearchText();
        view.filterTable(searchText);
    }

    /**
     * Zeigt die Buchungen eines Kunden an
     */
    private void viewCustomerBookings() {
        Long customerId = view.getSelectedCustomerId();
        if (customerId == null) {
            showWarning("Bitte wählen Sie einen Kunden aus");
            return;
        }

        try {
            // Lade Kunde und Buchungen
            Customer customer = customerService.findCustomerById(customerId);
            List<Booking> bookings = bookingService.findBookingsByCustomer(customerId);

            // Erstelle Nachricht
            StringBuilder message = new StringBuilder();
            message.append("Buchungen von: ").append(customer.getFullName()).append("\n");
            message.append("E-Mail: ").append(customer.getEmail()).append("\n");
            message.append("─────────────────────────────────────────────\n\n");

            if (bookings.isEmpty()) {
                message.append("Keine Buchungen vorhanden.");
            } else {
                message.append("Gesamt: ").append(bookings.size()).append(" Buchung(en)\n\n");

                int nr = 1;
                for (Booking booking : bookings) {
                    message.append(nr++).append(". ");
                    message.append(booking.getEvent().getName());
                    message.append("\n   Sitzplatz: ").append(booking.getSeat().getFullLabel());
                    message.append("\n   Status: ").append(booking.getStatus().getDisplayName());
                    message.append("\n   Preis: ").append(String.format("%.2f €", booking.getPrice()));
                    message.append("\n   Datum: ").append(booking.getBookingDate().format(
                            java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
                    message.append("\n\n");
                }
            }

            // Zeige Dialog mit Scroll-Möglichkeit
            JTextArea textArea = new JTextArea(message.toString());
            textArea.setEditable(false);
            textArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new java.awt.Dimension(500, 400));

            JOptionPane.showMessageDialog(
                    view,
                    scrollPane,
                    "Buchungsübersicht - " + customer.getFullName(),
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (DatabaseException e) {
            showError("Fehler beim Laden der Buchungen: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            showError("Kunde nicht gefunden: " + e.getMessage());
        }
    }

    /**
     * Lädt nur Kunden mit aktiven Buchungen
     */
    public void loadCustomersWithBookings() {
        try {
            view.setStatus("Lade Kunden mit Buchungen...");
            List<Customer> customers = customerService.findCustomersWithActiveBookings();
            view.displayCustomers(customers);
            view.setStatus(customers.size() + " Kunde(n) mit Buchungen");
        } catch (DatabaseException e) {
            showError("Fehler beim Laden: " + e.getMessage());
        }
    }

    /**
     * Lädt nur Kunden ohne Buchungen
     */
    public void loadCustomersWithoutBookings() {
        try {
            view.setStatus("Lade Kunden ohne Buchungen...");
            List<Customer> customers = customerService.findCustomersWithActiveBookings();
            view.displayCustomers(customers);
            view.setStatus(customers.size() + " Kunde(n) ohne Buchungen");
        } catch (DatabaseException e) {
            showError("Fehler beim Laden: " + e.getMessage());
        }
    }

    /**
     * Zeigt Top-Kunden nach Buchungsanzahl
     */
    public void showTopCustomers(int limit) {
        try {
            view.setStatus("Lade Top-Kunden...");
            List<Customer> topCustomers = customerService.findTopCustomers(limit);
            view.displayCustomers(topCustomers);
            view.setStatus("Top " + limit + " Kunden angezeigt");
        } catch (DatabaseException e) {
            showError("Fehler beim Laden: " + e.getMessage());
        }
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