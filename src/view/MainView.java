package view;

import util.ModernTheme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Hauptansicht der Anwendung mit Tabbed Pane
 */
public class MainView extends JFrame {

    private JTabbedPane tabbedPane;
    private BookingView bookingView;
    private EventManagementView eventManagementView;
    private CustomerManagementView customerManagementView;
    private ReportView reportView;

    /**
     * Konstruktor
     */
    public MainView() {
        initializeFrame();
        initializeComponents();
        layoutComponents();
    }

    /**
     * Initialisiert das Hauptfenster
     */
    private void initializeFrame() {
        setTitle("Event Booking System - Verwaltungssystem");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 800);
        setLocationRelativeTo(null);

        // Modern background color
        getContentPane().setBackground(ModernTheme.BACKGROUND);

        // Setze Icon (falls vorhanden)
        try {
            setIconImage(new ImageIcon("img/app_icon.png").getImage());
        } catch (Exception e) {
            // Icon nicht gefunden, ignorieren
        }
    }

    /**
     * Initialisiert alle Komponenten
     */
    private void initializeComponents() {
        tabbedPane = new JTabbedPane();

        bookingView = new BookingView();
        eventManagementView = new EventManagementView();
        customerManagementView = new CustomerManagementView();
        reportView = new ReportView();
    }

    /**
     * Layoutet alle Komponenten
     */
    private void layoutComponents() {
        // Menu Bar
        createMenuBar();

        // Helper method to load scaled icons
        java.util.function.BiFunction<String, String, ImageIcon> loadIcon = (path, desc) -> {
            try {
                // Load original image
                ImageIcon icon = new ImageIcon(path);
                Image img = icon.getImage();
                // Scale to a reasonable size for tabs (e.g., 20x20)
                Image newImg = img.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                return new ImageIcon(newImg);
            } catch (Exception e) {
                return null;
            }
        };

        // Tabs hinzufügen
        tabbedPane.addTab("Buchungen", loadIcon.apply("img/icon_booking.png", "Buchungen"),
                bookingView.getContentPane());
        tabbedPane.addTab("Events", loadIcon.apply("img/icon_event.png", "Events"), eventManagementView);
        tabbedPane.addTab("Kunden", loadIcon.apply("img/icon_customers.png", "Kunden"), customerManagementView);
        tabbedPane.addTab("Berichte", loadIcon.apply("img/icon_report.png", "Berichte"), reportView);

        // Modern Tab Design
        tabbedPane.setFont(ModernTheme.FONT_BUTTON);
        tabbedPane.setBackground(ModernTheme.SURFACE);
        tabbedPane.setForeground(ModernTheme.TEXT_PRIMARY);
        tabbedPane.setBorder(new EmptyBorder(ModernTheme.PADDING_SMALL,
                ModernTheme.PADDING_SMALL,
                ModernTheme.PADDING_SMALL,
                ModernTheme.PADDING_SMALL));

        add(tabbedPane, BorderLayout.CENTER);

        // Status-Bar am unteren Rand
        createStatusBar();
    }

    /**
     * Erstellt die Menüleiste
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(ModernTheme.SURFACE);
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, ModernTheme.BORDER));

        // Datei-Menü
        JMenu fileMenu = new JMenu("Datei");
        fileMenu.setFont(ModernTheme.FONT_BODY);
        fileMenu.setForeground(ModernTheme.TEXT_PRIMARY);
        fileMenu.setHorizontalAlignment(SwingConstants.LEFT);

        JMenuItem exitItem = new JMenuItem("Beenden");
        exitItem.setFont(ModernTheme.FONT_BODY);
        exitItem.setHorizontalAlignment(SwingConstants.LEFT);
        exitItem.setMargin(
                new Insets(ModernTheme.PADDING_SMALL, -35, ModernTheme.PADDING_SMALL, ModernTheme.PADDING_SMALL));
        exitItem.setIconTextGap(0);
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        // Ansicht-Menü
        JMenu viewMenu = new JMenu("Ansicht");
        viewMenu.setFont(ModernTheme.FONT_BODY);
        viewMenu.setForeground(ModernTheme.TEXT_PRIMARY);
        viewMenu.setHorizontalAlignment(SwingConstants.LEFT);

        JMenuItem refreshItem = new JMenuItem("Aktualisieren");
        refreshItem.setFont(ModernTheme.FONT_BODY);
        refreshItem.setHorizontalAlignment(SwingConstants.LEFT);
        refreshItem.setMargin(
                new Insets(ModernTheme.PADDING_SMALL, -35, ModernTheme.PADDING_SMALL, ModernTheme.PADDING_SMALL));
        refreshItem.setIconTextGap(0);
        refreshItem.addActionListener(e -> refreshAllViews());
        viewMenu.add(refreshItem);

        // Hilfe-Menü
        JMenu helpMenu = new JMenu("Hilfe");
        helpMenu.setFont(ModernTheme.FONT_BODY);
        helpMenu.setForeground(ModernTheme.TEXT_PRIMARY);
        helpMenu.setHorizontalAlignment(SwingConstants.LEFT);

        JMenuItem aboutItem = new JMenuItem("Über");
        aboutItem.setFont(ModernTheme.FONT_BODY);
        aboutItem.setHorizontalAlignment(SwingConstants.LEFT);
        aboutItem.setMargin(
                new Insets(ModernTheme.PADDING_SMALL, -35, ModernTheme.PADDING_SMALL, ModernTheme.PADDING_SMALL));
        aboutItem.setIconTextGap(0);
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    /**
     * Erstellt die Statusleiste
     */
    private void createStatusBar() {
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBar.setBackground(ModernTheme.SURFACE);
        statusBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, ModernTheme.BORDER),
                new EmptyBorder(ModernTheme.PADDING_SMALL, ModernTheme.PADDING_MEDIUM,
                        ModernTheme.PADDING_SMALL, ModernTheme.PADDING_MEDIUM)));
        JLabel statusLabel = ModernTheme.createLabel("Bereit");
        statusLabel.setForeground(ModernTheme.TEXT_SECONDARY);
        statusBar.add(statusLabel);
        add(statusBar, BorderLayout.SOUTH);
    }

    /**
     * Aktualisiert alle Ansichten
     */
    private void refreshAllViews() {
        // Wird vom Controller aufgerufen
    }

    /**
     * Zeigt den Über-Dialog
     */
    private void showAboutDialog() {
        JEditorPane editorPane = new JEditorPane("text/html",
                """
                        <html>
                        <body style="font-family: sans-serif;">
                        <b>Event Booking System</b><br>
                        Version 1.0<br>
                        <br>
                        Ein Java Desktop-Anwendungsprojekt<br>
                        für die Verwaltung von Veranstaltungsbuchungen.<br>
                        <br>
                        Entwickelt mit:<br>
                        - Java 17+<br>
                        - Swing GUI<br>
                        - MySQL JDBC<br>
                        - MVC-Architektur<br>
                        <br>
                        <a href="https://giovyangy.github.io/Lebenlauf/index.html#kontakt">den Programmierer bei Problemen kontaktieren</a>
                        </body>
                        </html>
                        """);

        editorPane.setEditable(false);
        editorPane.setOpaque(false);
        editorPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);

        editorPane.addHyperlinkListener(e -> {
            if (javax.swing.event.HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType())) {
                try {
                    java.awt.Desktop.getDesktop().browse(e.getURL().toURI());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        JOptionPane.showMessageDialog(this, editorPane, "Über", JOptionPane.INFORMATION_MESSAGE);
    }

    // Getter für die Views
    public BookingView getBookingView() {
        return bookingView;
    }

    public EventManagementView getEventManagementView() {
        return eventManagementView;
    }

    public CustomerManagementView getCustomerManagementView() {
        return customerManagementView;
    }

    public ReportView getReportView() {
        return reportView;
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }
}