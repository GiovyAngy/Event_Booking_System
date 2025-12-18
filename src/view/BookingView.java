package view;

import util.ModernTheme;
import model.*;
import model.Event;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Hauptansicht für die Buchungsverwaltung
 * Implementiert mit Java Swing
 */
public class BookingView extends JFrame {

    private JTable eventTable;
    private JTable bookingTable;
    private DefaultTableModel eventTableModel;
    private DefaultTableModel bookingTableModel;

    private JButton createBookingButton;
    private JButton confirmBookingButton;
    private JButton cancelBookingButton;
    private JButton refreshButton;

    private JLabel availableSeatsLabel;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public BookingView() {
        initializeFrame();
        initializeComponents();
        layoutComponents();
    }

    private void initializeFrame() {
        setTitle("Event Booking System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        getContentPane().setBackground(ModernTheme.BACKGROUND);
    }

    private void initializeComponents() {
        // Tabelle Events
        String[] eventColumns = { "ID", "Name", "Kategorie", "Datum/Zeit", "Saal", "Preis", "Verfügbare Plätze" };
        eventTableModel = new DefaultTableModel(eventColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        eventTable = new JTable(eventTableModel);
        eventTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Tabelle Buchungen
        String[] bookingColumns = { "ID", "Kunde", "Sitzplatz", "Status", "Buchungsdatum", "Preis" };
        bookingTableModel = new DefaultTableModel(bookingColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookingTable = new JTable(bookingTableModel);
        bookingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Modern styled tables
        ModernTheme.styleTable(eventTable);
        ModernTheme.styleTable(bookingTable);

        // Modern styled buttons
        createBookingButton = ModernTheme.createPrimaryButton("+ Neue Buchung");
        confirmBookingButton = ModernTheme.createAccentButton("Buchung bestätigen");
        cancelBookingButton = ModernTheme.createDangerButton("Buchung stornieren");
        refreshButton = ModernTheme.createSecondaryButton("Aktualisieren");

        // Labels & ComboBoxes
        availableSeatsLabel = ModernTheme.createSubheaderLabel("Verfügbare Plätze: -");

    }

    private void layoutComponents() {
        setLayout(new BorderLayout(ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_MEDIUM));

        JPanel topPanel = new JPanel();
        topPanel.setBackground(ModernTheme.SURFACE);
        topPanel.setBorder(new EmptyBorder(ModernTheme.PADDING_LARGE, ModernTheme.PADDING_LARGE,
                ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_LARGE));
        JLabel titleLabel = ModernTheme.createHeaderLabel("Event Booking System");
        topPanel.add(titleLabel);
        add(topPanel, BorderLayout.NORTH);

        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        mainSplitPane.setDividerLocation(300);

        mainSplitPane.setTopComponent(createEventPanel());
        mainSplitPane.setBottomComponent(createBookingPanel());

        add(mainSplitPane, BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createEventPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ModernTheme.SURFACE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(ModernTheme.BORDER, 1),
                        "Verfügbare Events",
                        javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                        javax.swing.border.TitledBorder.DEFAULT_POSITION,
                        ModernTheme.FONT_SUBHEADER,
                        ModernTheme.TEXT_PRIMARY),
                new EmptyBorder(ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_MEDIUM,
                        ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_MEDIUM)));

        JScrollPane scrollPane = new JScrollPane(eventTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.add(availableSeatsLabel);
        panel.add(infoPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createBookingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ModernTheme.SURFACE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(ModernTheme.BORDER, 1),
                        "Buchungen",
                        javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                        javax.swing.border.TitledBorder.DEFAULT_POSITION,
                        ModernTheme.FONT_SUBHEADER,
                        ModernTheme.TEXT_PRIMARY),
                new EmptyBorder(ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_MEDIUM,
                        ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_MEDIUM)));

        JScrollPane scrollPane = new JScrollPane(bookingTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Inputs entfernt, da jetzt über Dialog
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_MEDIUM));
        panel.setBackground(ModernTheme.SURFACE);
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, ModernTheme.BORDER));
        panel.add(createBookingButton);
        panel.add(confirmBookingButton);
        panel.add(cancelBookingButton);
        panel.add(refreshButton);
        return panel;
    }

    // ================= Methoden zum Anzeigen von Daten =================

    private java.util.List<Event> currentEvents;
    private java.util.List<Booking> currentBookings;

    // ================= Methoden zum Anzeigen von Daten =================

    public void displayEvents(List<Event> events) {
        this.currentEvents = events;
        eventTableModel.setRowCount(0); // pulisce la tabella

        if (events == null || events.isEmpty()) {
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        for (Event event : events) {
            String hallName = event.getHall() != null ? event.getHall().getName() : "-";
            String category = event.getCategory() != null ? event.getCategory() : "-";

            Object[] row = {
                    event.getId(),
                    event.getName(),
                    category,
                    event.getDateTime() != null ? event.getDateTime().format(formatter) : "-",
                    hallName,
                    String.format("%.2f €", event.getBasePrice()),
                    event.getAvailableSeats()
            };
            eventTableModel.addRow(row);
        }
    }

    public void displayBookings(List<Booking> bookings) {
        this.currentBookings = bookings;
        bookingTableModel.setRowCount(0);
        if (bookings == null)
            return;

        for (Booking booking : bookings) {
            Object[] row = {
                    booking.getId(),
                    booking.getCustomer() != null ? booking.getCustomer().getFullName() : "null",
                    booking.getSeat() != null ? booking.getSeat().getFullLabel() : "null",
                    booking.getStatus() != null ? booking.getStatus().getDisplayName() : "null",
                    booking.getBookingDate() != null ? booking.getBookingDate().format(dateFormatter) : "-",
                    String.format("%.2f €", booking.getPrice())
            };
            bookingTableModel.addRow(row);
        }
    }

    public void updateAvailableSeatsLabel(int availableSeats) {
        availableSeatsLabel.setText("Verfügbare Plätze: " + availableSeats);
    }

    public Event getSelectedEvent() {
        int selectedRow = eventTable.getSelectedRow();
        if (selectedRow >= 0 && currentEvents != null && selectedRow < currentEvents.size()) {
            return currentEvents.get(selectedRow);
        }
        return null;
    }

    public Booking getSelectedBooking() {
        int selectedRow = bookingTable.getSelectedRow();
        if (selectedRow >= 0 && currentBookings != null && selectedRow < currentBookings.size()) {
            return currentBookings.get(selectedRow);
        }
        return null;
    }

    public void selectFirstEvent() {
        if (eventTable.getRowCount() > 0) {
            eventTable.setRowSelectionInterval(0, 0);
        }
    }

    /**
     * Öffnet den Dialog für eine neue Buchung
     */
    public BookingDialog openBookingDialog(Event event, List<Customer> customers, List<Seat> seats) {
        BookingDialog dialog = new BookingDialog(this, event, customers, seats);
        dialog.setVisible(true);
        return dialog;
    }

    // ================= Event-Listener =================

    public void addEventSelectionListener(ActionListener listener) {
        eventTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting())
                listener.actionPerformed(null);
        });
    }

    public void addCreateBookingListener(ActionListener listener) {
        createBookingButton.addActionListener(listener);
    }

    public void addConfirmBookingListener(ActionListener listener) {
        confirmBookingButton.addActionListener(listener);
    }

    public void addCancelBookingListener(ActionListener listener) {
        cancelBookingButton.addActionListener(listener);
    }

    public void addRefreshListener(ActionListener listener) {
        refreshButton.addActionListener(listener);
    }

    // ================= Hilfsmethoden =================

    public void updateStatusInTable(Long bookingId, BookingStatus newStatus) {
        for (int i = 0; i < bookingTableModel.getRowCount(); i++) {
            Object idValue = bookingTableModel.getValueAt(i, 0);
            if (idValue != null && bookingId != null && idValue.equals(bookingId)) {
                bookingTableModel.setValueAt(newStatus.getDisplayName(), i, 3);
                break;
            }
        }
    }
}
