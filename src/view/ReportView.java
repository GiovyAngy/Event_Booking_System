package view;

import util.ModernTheme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Ansicht für Berichte und Statistiken
 */
public class ReportView extends JPanel {

    private JLabel totalRevenueLabel;
    private JLabel totalBookingsLabel;
    private JLabel activeBookingsLabel;
    private JLabel totalCustomersLabel;
    private JLabel totalEventsLabel;
    private JLabel upcomingEventsLabel;

    private JTextArea reportArea;
    private JButton generateReportButton;
    private JButton exportButton;
    private JComboBox<String> reportTypeComboBox;

    /**
     * Konstruktor
     */
    public ReportView() {
        initializeComponents();
        layoutComponents();
    }

    /**
     * Initialisiert alle Komponenten
     */
    private void initializeComponents() {
        // Statistik-Labels
        totalRevenueLabel = createStatLabel("Gesamtumsatz: 0,00 €");
        totalBookingsLabel = createStatLabel("Gesamt-Buchungen: 0");
        activeBookingsLabel = createStatLabel("Aktive Buchungen: 0");
        totalCustomersLabel = createStatLabel("Gesamt-Kunden: 0");
        totalEventsLabel = createStatLabel("Gesamt-Events: 0");
        upcomingEventsLabel = createStatLabel("Kommende Events: 0");

        // Report-Area
        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        // Modern styled buttons
        generateReportButton = ModernTheme.createPrimaryButton("Bericht erstellen");
        exportButton = ModernTheme.createSecondaryButton("Exportieren");

        // Modern styled report type combo box
        reportTypeComboBox = new JComboBox<>(new String[] {
                "Übersicht",
                "Umsatz nach Event",
                "Top Kunden",
                "Buchungen nach Status",
                "Event-Auslastung"
        });
        ModernTheme.styleComboBox(reportTypeComboBox);
    }

    /**
     * Erstellt ein modernes Statistik-Label
     */
    private JLabel createStatLabel(String text) {
        JLabel label = ModernTheme.createSubheaderLabel(text);
        label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ModernTheme.BORDER, 1),
                new EmptyBorder(ModernTheme.PADDING_LARGE, ModernTheme.PADDING_LARGE,
                        ModernTheme.PADDING_LARGE, ModernTheme.PADDING_LARGE)));
        label.setOpaque(true);
        label.setBackground(ModernTheme.SURFACE);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    /**
     * Layoutet alle Komponenten
     */
    private void layoutComponents() {
        setLayout(new BorderLayout(ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_MEDIUM));
        setBackground(ModernTheme.BACKGROUND);
        setBorder(new EmptyBorder(ModernTheme.PADDING_LARGE, ModernTheme.PADDING_LARGE,
                ModernTheme.PADDING_LARGE, ModernTheme.PADDING_LARGE));

        // Statistik-Panel oben
        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        statsPanel.add(totalRevenueLabel);
        statsPanel.add(totalBookingsLabel);
        statsPanel.add(activeBookingsLabel);
        statsPanel.add(totalCustomersLabel);
        statsPanel.add(totalEventsLabel);
        statsPanel.add(upcomingEventsLabel);

        add(statsPanel, BorderLayout.NORTH);

        // Haupt-Panel mit Report
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        // Modern control panel
        JPanel controlPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_SMALL));
        controlPanel.setBackground(ModernTheme.SURFACE);
        controlPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ModernTheme.BORDER, 1),
                new EmptyBorder(ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_MEDIUM,
                        ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_MEDIUM)));
        controlPanel.add(ModernTheme.createLabel("Bericht-Typ:"));
        controlPanel.add(reportTypeComboBox);
        controlPanel.add(generateReportButton);
        controlPanel.add(exportButton);

        mainPanel.add(controlPanel, BorderLayout.NORTH);

        // Report-Area
        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Bericht-Details"));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Aktualisiert die Statistik-Anzeige
     */
    public void updateStatistics(double totalRevenue, int totalBookings, int activeBookings,
            int totalCustomers, int totalEvents, int upcomingEvents) {
        totalRevenueLabel.setText(String.format("Gesamtumsatz: %.2f €", totalRevenue));
        totalBookingsLabel.setText("Gesamt-Buchungen: " + totalBookings);
        activeBookingsLabel.setText("Aktive Buchungen: " + activeBookings);
        totalCustomersLabel.setText("Gesamt-Kunden: " + totalCustomers);
        totalEventsLabel.setText("Gesamt-Events: " + totalEvents);
        upcomingEventsLabel.setText("Kommende Events: " + upcomingEvents);
    }

    /**
     * Setzt den Bericht-Text
     */
    public void setReportText(String text) {
        reportArea.setText(text);
        reportArea.setCaretPosition(0); // Scroll nach oben
    }

    /**
     * Fügt Text zum Bericht hinzu
     */
    public void appendReportText(String text) {
        reportArea.append(text);
    }

    /**
     * Leert den Bericht
     */
    public void clearReport() {
        reportArea.setText("");
    }

    // Event-Listener
    public void addGenerateReportListener(ActionListener listener) {
        generateReportButton.addActionListener(listener);
    }

    public void addExportListener(ActionListener listener) {
        exportButton.addActionListener(listener);
    }

    public void addReportTypeListener(ActionListener listener) {
        reportTypeComboBox.addActionListener(listener);
    }

    public String getSelectedReportType() {
        return (String) reportTypeComboBox.getSelectedItem();
    }

    public String getReportText() {
        return reportArea.getText();
    }
}