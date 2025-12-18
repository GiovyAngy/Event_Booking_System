package view;

import util.ModernTheme;
import model.Customer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Ansicht für die Kunden-Verwaltung
 * Zeigt alle Kunden in einer Tabelle mit Such- und Sortierfunktionen
 */
public class CustomerManagementView extends JPanel {

    private JTable customerTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JButton viewBookingsButton;
    private JTextField searchField;
    private JLabel statusLabel;
    private JLabel totalCustomersLabel;

    /**
     * Konstruktor - Initialisiert die gesamte Ansicht
     */
    public CustomerManagementView() {
        initializeComponents();
        layoutComponents();
        setupTableSorter();
    }

    /**
     * Initialisiert alle GUI-Komponenten
     */
    private void initializeComponents() {
        // Tabellen-Modell
        String[] columns = { "ID", "Vorname", "Nachname", "E-Mail", "Telefon", "Aktive Buchungen" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0 || columnIndex == 5)
                    return Long.class;
                return String.class;
            }
        };

        customerTable = new JTable(tableModel);
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customerTable.setRowHeight(25);
        customerTable.getTableHeader().setReorderingAllowed(false);

        // Spaltenbreiten setzen
        customerTable.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        customerTable.getColumnModel().getColumn(1).setPreferredWidth(120); // Vorname
        customerTable.getColumnModel().getColumn(2).setPreferredWidth(120); // Nachname
        customerTable.getColumnModel().getColumn(3).setPreferredWidth(200); // E-Mail
        customerTable.getColumnModel().getColumn(4).setPreferredWidth(120); // Telefon
        customerTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Buchungen

        // Doppelklick zum Bearbeiten
        customerTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editButton.doClick();
                }
            }
        });

        // Modern styled table
        ModernTheme.styleTable(customerTable);

        // Modern styled buttons
        addButton = ModernTheme.createPrimaryButton("+ Neuer Kunde");
        editButton = ModernTheme.createSecondaryButton("Bearbeiten");
        deleteButton = ModernTheme.createDangerButton("Löschen");
        refreshButton = ModernTheme.createSecondaryButton("Aktualisieren");
        viewBookingsButton = ModernTheme.createAccentButton("Buchungen anzeigen");

        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
        viewBookingsButton.setEnabled(false);

        // Modern styled search field
        searchField = new JTextField(30);
        searchField.setToolTipText("Suche nach Name oder E-Mail");
        ModernTheme.styleTextField(searchField);

        // Modern styled status labels
        statusLabel = ModernTheme.createLabel("Bereit");
        statusLabel.setForeground(ModernTheme.TEXT_SECONDARY);

        totalCustomersLabel = ModernTheme.createSubheaderLabel("Gesamt: 0 Kunden");
    }

    /**
     * Layoutet alle Komponenten im Panel
     */
    private void layoutComponents() {
        setLayout(new BorderLayout(ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_MEDIUM));
        setBackground(ModernTheme.BACKGROUND);
        setBorder(new EmptyBorder(ModernTheme.PADDING_LARGE, ModernTheme.PADDING_LARGE,
                ModernTheme.PADDING_LARGE, ModernTheme.PADDING_LARGE));

        // Modern top panel with search and statistics
        JPanel topPanel = new JPanel(new BorderLayout(ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_SMALL));
        topPanel.setBackground(ModernTheme.SURFACE);

        JPanel searchPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_SMALL));
        searchPanel.setBackground(ModernTheme.SURFACE);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(ModernTheme.BORDER, 1),
                        "Suche",
                        javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                        javax.swing.border.TitledBorder.DEFAULT_POSITION,
                        ModernTheme.FONT_SUBHEADER,
                        ModernTheme.TEXT_PRIMARY),
                new EmptyBorder(ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_MEDIUM,
                        ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_MEDIUM)));
        searchPanel.add(searchField);
        searchPanel.add(refreshButton);

        JPanel statsPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_SMALL));
        statsPanel.setBackground(ModernTheme.SURFACE);
        statsPanel.add(totalCustomersLabel);

        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(statsPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Tabelle mit ScrollPane
        JScrollPane scrollPane = new JScrollPane(customerTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Kunden-Liste"));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // Modern button panel with status
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(ModernTheme.SURFACE);
        bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, ModernTheme.BORDER));

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_MEDIUM));
        buttonPanel.setBackground(ModernTheme.SURFACE);
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewBookingsButton);

        JPanel statusPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_MEDIUM));
        statusPanel.setBackground(ModernTheme.SURFACE);
        statusPanel.add(statusLabel);

        bottomPanel.add(statusPanel, BorderLayout.WEST);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Richtet den Table-Sorter für sortierbare Spalten ein
     */
    private void setupTableSorter() {
        sorter = new TableRowSorter<>(tableModel);
        customerTable.setRowSorter(sorter);
    }

    /**
     * Zeigt Kunden in der Tabelle an
     * 
     * @param customers Liste der anzuzeigenden Kunden
     */
    public void displayCustomers(List<Customer> customers) {
        tableModel.setRowCount(0); // Tabelle leeren

        if (customers == null || customers.isEmpty()) {
            statusLabel.setText("Keine Kunden gefunden");
            totalCustomersLabel.setText("Gesamt: 0 Kunden");
            return;
        }

        for (Customer customer : customers) {
            Object[] row = {
                    customer.getId(),
                    customer.getFirstName(),
                    customer.getLastName(),
                    customer.getEmail(),
                    customer.getPhone(),
                    customer.getActiveBookingsCount()
            };
            tableModel.addRow(row);
        }

        statusLabel.setText(customers.size() + " Kunde(n) angezeigt");
        totalCustomersLabel.setText("Gesamt: " + customers.size() + " Kunden");
    }

    /**
     * Gibt die ausgewählte Kunden-ID zurück
     * 
     * @return Kunden-ID oder null wenn keine Auswahl
     */
    public Long getSelectedCustomerId() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow >= 0) {
            // Konvertiere View-Index zu Model-Index (wichtig bei Sortierung!)
            int modelRow = customerTable.convertRowIndexToModel(selectedRow);
            return (Long) tableModel.getValueAt(modelRow, 0);
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
     * Setzt den Status-Text
     */
    public void setStatus(String status) {
        statusLabel.setText(status);
    }

    /**
     * Filtert die Tabelle basierend auf Suchtext
     */
    public void filterTable(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            // Suche in Vorname, Nachname und E-Mail (Spalten 1, 2, 3)
            RowFilter<DefaultTableModel, Object> filter = RowFilter.regexFilter(
                    "(?i)" + searchText, 1, 2, 3);
            sorter.setRowFilter(filter);
        }
    }

    /**
     * Aktiviert/Deaktiviert Buttons basierend auf Selektion
     */
    private void updateButtonStates() {
        boolean hasSelection = customerTable.getSelectedRow() >= 0;
        editButton.setEnabled(hasSelection);
        deleteButton.setEnabled(hasSelection);
        viewBookingsButton.setEnabled(hasSelection);
    }

    /**
     * Markiert eine Zeile basierend auf Kunden-ID
     */
    public void selectCustomerById(Long customerId) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 0).equals(customerId)) {
                int viewRow = customerTable.convertRowIndexToView(i);
                customerTable.setRowSelectionInterval(viewRow, viewRow);
                customerTable.scrollRectToVisible(customerTable.getCellRect(viewRow, 0, true));
                break;
            }
        }
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
     * Fügt Listener für "Buchungen anzeigen"-Button hinzu
     */
    public void addViewBookingsButtonListener(ActionListener listener) {
        viewBookingsButton.addActionListener(listener);
    }

    /**
     * Fügt Listener für Suchfeld hinzu (Enter-Taste)
     */
    public void addSearchListener(ActionListener listener) {
        searchField.addActionListener(listener);
    }

    /**
     * Fügt Listener für Tabellen-Selektion hinzu
     */
    public void addTableSelectionListener(javax.swing.event.ListSelectionListener listener) {
        customerTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateButtonStates();
                listener.valueChanged(e);
            }
        });
    }

    /**
     * Fügt Key-Listener für Live-Suche hinzu
     */
    public void addSearchKeyListener(java.awt.event.KeyListener listener) {
        searchField.addKeyListener(listener);
    }
}