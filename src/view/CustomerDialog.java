package view;

import util.ModernTheme;
import model.Customer;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog für das Erstellen und Bearbeiten von Kunden
 */
public class CustomerDialog extends JDialog {

    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JTextField phoneField;

    private Customer customer;
    private boolean confirmed = false;

    /**
     * Konstruktor für neuen Kunden
     */
    public CustomerDialog(Frame owner) {
        this(owner, null);
    }

    /**
     * Konstruktor für bestehenden Kunden (Bearbeiten)
     */
    public CustomerDialog(Frame owner, Customer customer) {
        super(owner, customer == null ? "Neuen Kunden anlegen" : "Kunde bearbeiten", true);
        this.customer = customer;

        initializeComponents();
        layoutComponents();

        if (customer != null) {
            fillFormWithCustomerData();
        }

        setSize(400, 300);
        setLocationRelativeTo(owner);
    }

    /**
     * Initialisiert alle Komponenten
     */
    private void initializeComponents() {
        firstNameField = new JTextField(20);
        lastNameField = new JTextField(20);
        emailField = new JTextField(20);
        phoneField = new JTextField(20);
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

        // Vorname
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Vorname:*"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(firstNameField, gbc);

        // Nachname
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Nachname:*"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(lastNameField, gbc);

        // E-Mail
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("E-Mail:*"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(emailField, gbc);

        // Telefon
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Telefon:*"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(phoneField, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Info-Label
        JLabel infoLabel = new JLabel("* Pflichtfelder");
        infoLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
        add(infoLabel, BorderLayout.NORTH);

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
     * Füllt das Formular mit Daten eines bestehenden Kunden
     */
    private void fillFormWithCustomerData() {
        firstNameField.setText(customer.getFirstName());
        lastNameField.setText(customer.getLastName());
        emailField.setText(customer.getEmail());
        phoneField.setText(customer.getPhone());
    }

    /**
     * Speichern-Button Aktion
     */
    private void onSave() {
        // Validierung
        if (firstNameField.getText().trim().isEmpty()) {
            showError("Vorname darf nicht leer sein");
            return;
        }

        if (lastNameField.getText().trim().isEmpty()) {
            showError("Nachname darf nicht leer sein");
            return;
        }

        if (emailField.getText().trim().isEmpty()) {
            showError("E-Mail darf nicht leer sein");
            return;
        }

        if (!emailField.getText().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            showError("Ungültige E-Mail-Adresse");
            return;
        }

        if (phoneField.getText().trim().isEmpty()) {
            showError("Telefonnummer darf nicht leer sein");
            return;
        }

        // Kunde erstellen oder aktualisieren
        if (customer == null) {
            customer = new Customer();
        }

        customer.setFirstName(firstNameField.getText().trim());
        customer.setLastName(lastNameField.getText().trim());
        customer.setEmail(emailField.getText().trim());
        customer.setPhone(phoneField.getText().trim());

        confirmed = true;
        dispose();
    }

    /**
     * Abbrechen-Button Aktion
     */
    private void onCancel() {
        confirmed = false;
        dispose();
    }

    /**
     * Gibt den Kunden zurück (null wenn abgebrochen)
     */
    public Customer getCustomer() {
        return confirmed ? customer : null;
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