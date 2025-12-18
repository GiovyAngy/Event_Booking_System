package view;

import util.ModernTheme;
import model.Customer;
import model.Event;
import model.Seat;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Dialog zur Erstellung einer neuen Buchung.
 * Modal-Fenster, das die Auswahl von Kunde und Sitzplatz ermöglicht.
 */
public class BookingDialog extends JDialog {

    private final Event event;
    private final List<Customer> customers;
    private final List<Seat> seats;

    private JComboBox<Customer> customerComboBox;
    private JComboBox<Seat> seatComboBox;
    private boolean confirmed = false;

    public BookingDialog(Frame owner, Event event, List<Customer> customers, List<Seat> seats) {
        super(owner, "Neue Buchung erstellen", true);
        this.event = event;
        this.customers = customers;
        this.seats = seats;

        initializeComponents();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initializeComponents() {
        setLayout(new BorderLayout(10, 10));

        // Header Panel
        JPanel headerPanel = new JPanel(new GridLayout(3, 1));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.add(new JLabel("Event: " + event.getName()));
        headerPanel.add(new JLabel("Datum: " + event.getDateTime().toString().replace("T", " "))); // Simple format
        headerPanel.add(new JLabel("Saal: " + (event.getHall() != null ? event.getHall().getName() : "-")));
        add(headerPanel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Customer Selection
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Kunde:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        customerComboBox = new JComboBox<>(customers.toArray(new Customer[0]));
        formPanel.add(customerComboBox, gbc);

        // Seat Selection
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Sitzplatz:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        seatComboBox = new JComboBox<>(seats.toArray(new Seat[0]));
        formPanel.add(seatComboBox, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Modern styled buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = ModernTheme.createPrimaryButton("Buchen");
        JButton cancelButton = ModernTheme.createSecondaryButton("Abbrechen");

        okButton.addActionListener(e -> {
            confirmed = true;
            dispose();
        });

        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(cancelButton);
        buttonPanel.add(okButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Customer getSelectedCustomer() {
        return (Customer) customerComboBox.getSelectedItem();
    }

    public Seat getSelectedSeat() {
        return (Seat) seatComboBox.getSelectedItem();
    }
}
