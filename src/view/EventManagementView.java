package view;

import util.ModernTheme;
import model.Event;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Ansicht für die Event-Verwaltung
 * Zeigt alle Events in einer Tabelle mit Such- und Filterfunktionen
 */
public class EventManagementView extends JPanel {

    private JTable eventTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JTextField searchField;
    private JComboBox<String> categoryFilter;
    private JLabel statusLabel;

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    /**
     * Konstruktor - Initialisiert die gesamte Ansicht
     */
    public EventManagementView() {
        initializeComponents();
        layoutComponents();
    }

    /**
     * Initialisiert alle GUI-Komponenten
     */
    private void initializeComponents() {
        // Tabellen-Modell mit nicht-editierbaren Zellen
        String[] columns = { "ID", "Name", "Kategorie", "Datum/Zeit", "Saal", "Preis (€)", "Verfügbar" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Keine Zelle ist direkt editierbar
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0)
                    return Long.class;
                if (columnIndex == 6)
                    return Integer.class;
                return String.class;
            }
        };

        eventTable = new JTable(tableModel);
        eventTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        eventTable.setRowHeight(25);
        eventTable.getTableHeader().setReorderingAllowed(false);

        // Spaltenbreiten setzen
        eventTable.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        eventTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Name
        eventTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Kategorie
        eventTable.getColumnModel().getColumn(3).setPreferredWidth(150); // Datum
        eventTable.getColumnModel().getColumn(4).setPreferredWidth(120); // Saal
        eventTable.getColumnModel().getColumn(5).setPreferredWidth(80); // Preis
        eventTable.getColumnModel().getColumn(6).setPreferredWidth(80); // Verfügbar

        // Modern styled table
        ModernTheme.styleTable(eventTable);

        // Modern styled buttons
        addButton = ModernTheme.createPrimaryButton("+ Neues Event");
        editButton = ModernTheme.createSecondaryButton("Bearbeiten");
        deleteButton = ModernTheme.createDangerButton("Löschen");
        refreshButton = ModernTheme.createSecondaryButton("Aktualisieren");

        // Modern styled search field
        searchField = new JTextField(20);
        searchField.setToolTipText("Suche nach Event-Namen");
        ModernTheme.styleTextField(searchField);

        // Modern styled category filter
        categoryFilter = new JComboBox<>(new String[] {
                "Alle Kategorien", "Musik", "Kino", "Theater", "Comedy", "Sport", "Sonstiges"
        });
        categoryFilter.setPreferredSize(new Dimension(150, 30));
        ModernTheme.styleComboBox(categoryFilter);

        // Status-Label
        statusLabel = ModernTheme.createLabel("Bereit");
        statusLabel.setForeground(ModernTheme.TEXT_SECONDARY);
    }

    /**
     * Layoutet alle Komponenten im Panel
     */
    private void layoutComponents() {
        setLayout(new BorderLayout(ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_MEDIUM));
        setBackground(ModernTheme.BACKGROUND);
        setBorder(new EmptyBorder(ModernTheme.PADDING_LARGE, ModernTheme.PADDING_LARGE,
                ModernTheme.PADDING_LARGE, ModernTheme.PADDING_LARGE));

        // Modern top panel with search and filter
        JPanel topPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_SMALL));
        topPanel.setBackground(ModernTheme.SURFACE);
        topPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(ModernTheme.BORDER, 1),
                        "Filter & Suche",
                        javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                        javax.swing.border.TitledBorder.DEFAULT_POSITION,
                        ModernTheme.FONT_SUBHEADER,
                        ModernTheme.TEXT_PRIMARY),
                new EmptyBorder(ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_MEDIUM,
                        ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_MEDIUM)));

        topPanel.add(ModernTheme.createLabel("Suche:"));
        topPanel.add(searchField);
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(ModernTheme.createLabel("Kategorie:"));
        topPanel.add(categoryFilter);
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(refreshButton);

        add(topPanel, BorderLayout.NORTH);

        // Tabelle mit ScrollPane
        JScrollPane scrollPane = new JScrollPane(eventTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Event-Liste"));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // Modern button panel with status label
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(ModernTheme.SURFACE);
        bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, ModernTheme.BORDER));

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_MEDIUM));
        buttonPanel.setBackground(ModernTheme.SURFACE);
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        JPanel statusPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_MEDIUM));
        statusPanel.setBackground(ModernTheme.SURFACE);
        statusPanel.add(statusLabel);

        bottomPanel.add(statusPanel, BorderLayout.WEST);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Zeigt Events in der Tabelle an
     * 
     * @param events Liste der anzuzeigenden Events
     */
    public void displayEvents(List<Event> events) {
        tableModel.setRowCount(0); // Tabelle leeren

        if (events == null || events.isEmpty()) {
            statusLabel.setText("Keine Events gefunden");
            return;
        }

        for (Event event : events) {
            Object[] row = {
                    event.getId(),
                    event.getName(),
                    event.getCategory(),
                    event.getDateTime().format(dateFormatter),
                    event.getHall() != null ? event.getHall().getName() : "-",
                    String.format("%.2f", event.getBasePrice()),
                    event.getAvailableSeats()
            };
            tableModel.addRow(row);
        }

        statusLabel.setText(events.size() + " Event(s) gefunden");
    }

    /**
     * Gibt die ausgewählte Event-ID zurück
     * 
     * @return Event-ID oder null wenn keine Auswahl
     */
    public Long getSelectedEventId() {
        int selectedRow = eventTable.getSelectedRow();
        if (selectedRow >= 0) {
            return (Long) tableModel.getValueAt(selectedRow, 0);
        }
        return null;
    }

    /**
     * Gibt den Suchtext zurück
     */
    public String getSearchText() {
        return searchField.getText().trim();
    }

    /**
     * Gibt die ausgewählte Kategorie zurück
     * 
     * @return Kategorie oder null für "Alle"
     */
    public String getSelectedCategory() {
        String category = (String) categoryFilter.getSelectedItem();
        return "Alle Kategorien".equals(category) ? null : category;
    }

    /**
     * Setzt den Status-Text
     */
    public void setStatus(String status) {
        statusLabel.setText(status);
    }

    /**
     * Aktiviert/Deaktiviert Buttons basierend auf Selektion
     */
    private void updateButtonStates() {
        boolean hasSelection = eventTable.getSelectedRow() >= 0;
        editButton.setEnabled(hasSelection);
        deleteButton.setEnabled(hasSelection);
    }

    // ========== Event-Listener ==========

    /**
     * Fügt Listener für "Hinzufügen"-Button hinzu
     */
    public void addAddButtonListener(ActionListener listener) {
        addButton.addActionListener(listener);
    }

    /**
     * Fügt Listener für "Bearbeiten"-Button hinzu
     */
    public void addEditButtonListener(ActionListener listener) {
        editButton.addActionListener(listener);
    }

    /**
     * Fügt Listener für "Löschen"-Button hinzu
     */
    public void addDeleteButtonListener(ActionListener listener) {
        deleteButton.addActionListener(listener);
    }

    /**
     * Fügt Listener für "Aktualisieren"-Button hinzu
     */
    public void addRefreshButtonListener(ActionListener listener) {
        refreshButton.addActionListener(listener);
    }

    /**
     * Fügt Listener für Suchfeld hinzu (Enter-Taste)
     */
    public void addSearchListener(ActionListener listener) {
        searchField.addActionListener(listener);
    }

    /**
     * Fügt Listener für Kategorie-Filter hinzu
     */
    public void addCategoryFilterListener(ActionListener listener) {
        categoryFilter.addActionListener(listener);
    }

    /**
     * Fügt Listener für Tabellen-Selektion hinzu
     */
    public void addTableSelectionListener(javax.swing.event.ListSelectionListener listener) {
        eventTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateButtonStates();
                listener.valueChanged(e);
            }
        });
    }
}