package view;

import util.ModernTheme;
import model.Event;
import model.Hall;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Dialog für das Erstellen und Bearbeiten von Events
 */
public class EventDialog extends JDialog {

    private JTextField nameField;
    private JTextArea descriptionArea;
    private JTextField dateField;
    private JTextField timeField;
    private JComboBox<String> categoryComboBox;
    private JTextField priceField;
    private JComboBox<Hall> hallComboBox;

    private Event event;
    private boolean confirmed = false;

    private static final String[] CATEGORIES = {
            "Musik", "Kino", "Theater", "Comedy", "Sport", "Sonstiges"
    };

    /**
     * Konstruktor für neues Event
     */
    public EventDialog(Frame owner, List<Hall> availableHalls) {
        this(owner, null, availableHalls);
    }

    /**
     * Konstruktor für bestehendes Event (Bearbeiten)
     */
    public EventDialog(Frame owner, Event event, List<Hall> availableHalls) {
        super(owner, event == null ? "Neues Event erstellen" : "Event bearbeiten", true);
        this.event = event;

        initializeComponents(availableHalls);
        layoutComponents();

        if (event != null) {
            fillFormWithEventData();
        }

        setSize(500, 600);
        setLocationRelativeTo(owner);
    }

    /**
     * Initialisiert alle Komponenten
     */
    private void initializeComponents(List<Hall> availableHalls) {
        nameField = new JTextField(30);
        descriptionArea = new JTextArea(5, 30);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        dateField = new JTextField("dd.MM.yyyy", 15);
        timeField = new JTextField("HH:mm", 10);

        categoryComboBox = new JComboBox<>(CATEGORIES);
        priceField = new JTextField("0.00", 10);

        hallComboBox = new JComboBox<>();
        for (Hall hall : availableHalls) {
            hallComboBox.addItem(hall);
        }
    }

    /**
     * Layoutet alle Komponenten
     */
    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));

        // Formular-Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Name:*"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(nameField, gbc);

        // Beschreibung
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Beschreibung:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        formPanel.add(new JScrollPane(descriptionArea), gbc);

        // Datum
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weighty = 0;
        formPanel.add(new JLabel("Datum:*"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(dateField, gbc);

        // Zeit
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Uhrzeit:*"), gbc);
        gbc.gridx = 1;
        formPanel.add(timeField, gbc);

        // Kategorie
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Kategorie:*"), gbc);
        gbc.gridx = 1;
        formPanel.add(categoryComboBox, gbc);

        // Preis
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Basispreis (€):*"), gbc);
        gbc.gridx = 1;
        formPanel.add(priceField, gbc);

        // Saal
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("Saal:*"), gbc);
        gbc.gridx = 1;
        formPanel.add(hallComboBox, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Modern styled buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = ModernTheme.createPrimaryButton("Speichern");
        JButton cancelButton = ModernTheme.createSecondaryButton("Abbrechen");

        saveButton.addActionListener(e -> onSave());
        cancelButton.addActionListener(e -> onCancel());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Füllt das Formular mit Daten eines bestehenden Events
     */
    private void fillFormWithEventData() {
        nameField.setText(event.getName());
        descriptionArea.setText(event.getDescription());

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        dateField.setText(event.getDateTime().format(dateFormatter));
        timeField.setText(event.getDateTime().format(timeFormatter));

        categoryComboBox.setSelectedItem(event.getCategory());
        priceField.setText(String.format("%.2f", event.getBasePrice()));

        // Wähle den richtigen Saal
        for (int i = 0; i < hallComboBox.getItemCount(); i++) {
            if (hallComboBox.getItemAt(i).getId().equals(event.getHall().getId())) {
                hallComboBox.setSelectedIndex(i);
                break;
            }
        }
    }

    /**
     * Speichern-Button Aktion
     */
    private void onSave() {
        try {
            // Validierung
            if (nameField.getText().trim().isEmpty()) {
                showError("Name darf nicht leer sein");
                return;
            }

            if (hallComboBox.getSelectedItem() == null) {
                showError("Bitte wählen Sie einen Saal aus");
                return;
            }

            // Event erstellen oder aktualisieren
            if (event == null) {
                event = new Event();
            }

            event.setName(nameField.getText().trim());
            event.setDescription(descriptionArea.getText().trim());
            event.setCategory((String) categoryComboBox.getSelectedItem());
            event.setHall((Hall) hallComboBox.getSelectedItem());

            // Datum parsen
            String dateTimeString = dateField.getText() + " " + timeField.getText();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            event.setDateTime(LocalDateTime.parse(dateTimeString, formatter));

            // Preis parsen
            double price = Double.parseDouble(priceField.getText().replace(",", "."));
            event.setBasePrice(price);

            confirmed = true;
            dispose();

        } catch (Exception ex) {
            showError("Ungültige Eingabe: " + ex.getMessage());
        }
    }

    /**
     * Abbrechen-Button Aktion
     */
    private void onCancel() {
        confirmed = false;
        dispose();
    }

    /**
     * Gibt das Event zurück (null wenn abgebrochen)
     */
    public Event getEvent() {
        return confirmed ? event : null;
    }

    /**
     * Prüft ob der Dialog bestätigt wurde
     */
    public boolean isConfirmed() {
        return confirmed;
    }

    /**
     * Zeigt eine Fehlermeldung
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Fehler", JOptionPane.ERROR_MESSAGE);
    }
}