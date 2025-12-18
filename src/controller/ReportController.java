package controller;

import exceptions.DatabaseException;
import model.Booking;
import model.Customer;
import model.Event;
import service.BookingService;
import service.CustomerService;
import service.EventService;
import view.ReportView;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller für Berichte und Statistiken
 */
public class ReportController {

    private final ReportView view;
    private final BookingService bookingService;
    private final EventService eventService;
    private final CustomerService customerService;

    public ReportController(ReportView view, BookingService bookingService,
                            EventService eventService, CustomerService customerService) {
        this.view = view;
        this.bookingService = bookingService;
        this.eventService = eventService;
        this.customerService = customerService;

        initializeEventListeners();
    }

    /**
     * Initialisiert Event-Listener
     */
    private void initializeEventListeners() {
        view.addGenerateReportListener(e -> generateReport());
        view.addExportListener(e -> exportReport());
    }

    /**
     * Aktualisiert die Statistiken
     */
    public void updateStatistics() {
        try {
            double totalRevenue = bookingService.calculateTotalRevenue();
            List<Booking> allBookings = bookingService.findActiveBookings();
            int totalBookings = allBookings.size();
            int activeBookings = (int) allBookings.stream()
                    .filter(b -> b.getStatus() != model.BookingStatus.CANCELLED)
                    .count();

            int totalCustomers = (int) customerService.getTotalCustomerCount();
            List<Event> allEvents = eventService.findAllEvents();
            int totalEvents = allEvents.size();
            int upcomingEvents = eventService.findUpcomingEvents().size();

            view.updateStatistics(
                    totalRevenue,
                    totalBookings,
                    activeBookings,
                    totalCustomers,
                    totalEvents,
                    upcomingEvents
            );
        } catch (DatabaseException e) {
            showError("Fehler beim Aktualisieren der Statistiken: " + e.getMessage());
        }
    }

    /**
     * Generiert einen Bericht basierend auf dem ausgewählten Typ
     */
    private void generateReport() {
        String reportType = view.getSelectedReportType();
        view.clearReport();

        try {
            switch (reportType) {
                case "Übersicht" -> generateOverviewReport();
                case "Umsatz nach Event" -> generateRevenueByEventReport();
                case "Top Kunden" -> generateTopCustomersReport();
                case "Buchungen nach Status" -> generateBookingsByStatusReport();
                case "Event-Auslastung" -> generateEventOccupancyReport();
            }
        } catch (DatabaseException e) {
            showError("Fehler beim Erstellen des Berichts: " + e.getMessage());
        }
    }

    /**
     * Erstellt einen Übersichtsbericht
     */
    private void generateOverviewReport() throws DatabaseException {
        StringBuilder report = new StringBuilder();
        report.append("═══════════════════════════════════════════\n");
        report.append("        SYSTEM-ÜBERSICHT\n");
        report.append("═══════════════════════════════════════════\n\n");

        double totalRevenue = bookingService.calculateTotalRevenue();
        List<Booking> allBookings = bookingService.findActiveBookings();
        int totalCustomers = (int) customerService.getTotalCustomerCount();
        List<Event> upcomingEvents = eventService.findUpcomingEvents();

        report.append(String.format("Gesamtumsatz: %.2f €\n", totalRevenue));
        report.append(String.format("Anzahl Buchungen: %d\n", allBookings.size()));
        report.append(String.format("Anzahl Kunden: %d\n", totalCustomers));
        report.append(String.format("Kommende Events: %d\n\n", upcomingEvents.size()));

        // Top 3 Events
        report.append("Top 3 Events nach Buchungen:\n");
        report.append("─────────────────────────────────────────\n");

        Map<Event, Long> eventBookingCount = allBookings.stream()
                .collect(Collectors.groupingBy(Booking::getEvent, Collectors.counting()));

        eventBookingCount.entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .limit(3)
                .forEach(entry -> report.append(String.format("• %s: %d Buchungen\n",
                        entry.getKey().getName(), entry.getValue())));

        view.setReportText(report.toString());
    }

    /**
     * Erstellt einen Umsatzbericht pro Event
     */
    private void generateRevenueByEventReport() throws DatabaseException {
        StringBuilder report = new StringBuilder();
        report.append("═══════════════════════════════════════════\n");
        report.append("      UMSATZ NACH EVENT\n");
        report.append("═══════════════════════════════════════════\n\n");

        List<Event> events = eventService.findAllEvents();

        for (Event event : events) {
            List<Booking> bookings = bookingService.findBookingsByEvent(event.getId());
            double revenue = bookings.stream()
                    .filter(b -> b.getStatus() == model.BookingStatus.CONFIRMED)
                    .mapToDouble(Booking::getPrice)
                    .sum();

            report.append(String.format("%-40s %.2f €\n", event.getName(), revenue));
        }

        view.setReportText(report.toString());
    }

    /**
     * Erstellt einen Top-Kunden Bericht
     */
    private void generateTopCustomersReport() throws DatabaseException {
        StringBuilder report = new StringBuilder();
        report.append("═══════════════════════════════════════════\n");
        report.append("         TOP KUNDEN\n");
        report.append("═══════════════════════════════════════════\n\n");

        List<Customer> topCustomers = customerService.findTopCustomers(10);

        int rank = 1;
        for (Customer customer : topCustomers) {
            report.append(String.format("%2d. %-30s (%d Buchungen)\n",
                    rank++, customer.getFullName(), customer.getActiveBookingsCount()));
        }

        view.setReportText(report.toString());
    }

    /**
     * Erstellt einen Bericht über Buchungen nach Status
     */
    private void generateBookingsByStatusReport() throws DatabaseException {
        StringBuilder report = new StringBuilder();
        report.append("═══════════════════════════════════════════\n");
        report.append("     BUCHUNGEN NACH STATUS\n");
        report.append("═══════════════════════════════════════════\n\n");

        List<Booking> allBookings = bookingService.findActiveBookings();

        Map<model.BookingStatus, Long> statusCount = allBookings.stream()
                .collect(Collectors.groupingBy(Booking::getStatus, Collectors.counting()));

        statusCount.forEach((status, count) ->
                report.append(String.format("%-20s %d Buchungen\n",
                        status.getDisplayName(), count))
        );

        view.setReportText(report.toString());
    }

    /**
     * Erstellt einen Event-Auslastungsbericht
     */
    private void generateEventOccupancyReport() throws DatabaseException {
        StringBuilder report = new StringBuilder();
        report.append("═══════════════════════════════════════════\n");
        report.append("       EVENT-AUSLASTUNG\n");
        report.append("═══════════════════════════════════════════\n\n");

        List<Event> events = eventService.findUpcomingEvents();

        for (Event event : events) {
            int totalSeats = event.getHall().getCapacity();
            int availableSeats = event.getAvailableSeats();
            int bookedSeats = totalSeats - availableSeats;
            double occupancyRate = (bookedSeats * 100.0) / totalSeats;

            report.append(String.format("%-40s\n", event.getName()));
            report.append(String.format("  Gebucht: %d/%d (%.1f%%)\n",
                    bookedSeats, totalSeats, occupancyRate));
            report.append(String.format("  Verfügbar: %d\n\n", availableSeats));
        }

        view.setReportText(report.toString());
    }

    /**
     * Exportiert den aktuellen Bericht in eine Datei
     */
    private void exportReport() {
        String reportText = view.getReportText();
        if (reportText.isEmpty()) {
            showWarning("Bitte erstellen Sie zuerst einen Bericht");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Bericht exportieren");
        fileChooser.setSelectedFile(new java.io.File("bericht.txt"));

        int userSelection = fileChooser.showSaveDialog(view);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try (FileWriter writer = new FileWriter(fileChooser.getSelectedFile())) {
                writer.write(reportText);
                showSuccess("Bericht erfolgreich exportiert!");
            } catch (IOException e) {
                showError("Fehler beim Exportieren: " + e.getMessage());
            }
        }
    }

    // Hilfsmethoden
    private void showError(String message) {
        JOptionPane.showMessageDialog(view, message, "Fehler", JOptionPane.ERROR_MESSAGE);
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(view, message, "Warnung", JOptionPane.WARNING_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(view, message, "Erfolg", JOptionPane.INFORMATION_MESSAGE);
    }
}