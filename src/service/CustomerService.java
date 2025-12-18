package service;

import dao.CustomerDAO;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import model.Customer;
import util.Validator;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service-Klasse für Customer-Geschäftslogik
 */
public class CustomerService {

    private final CustomerDAO customerDAO;

    public CustomerService() {
        this.customerDAO = new CustomerDAO();
    }

    /**
     * Erstellt einen neuen Kunden mit Validierung
     */
    public Customer createCustomer(String firstName, String lastName, String email, String phone)
            throws DatabaseException, ValidationException {

        // Validierung
        Validator.validateName(firstName, "Vorname");
        Validator.validateName(lastName, "Nachname");
        Validator.validateEmail(email);
        Validator.validatePhone(phone);

        // Prüfe ob E-Mail bereits existiert
        Optional<Customer> existing = customerDAO.findByEmail(email);
        if (existing.isPresent()) {
            throw new ValidationException("Ein Kunde mit dieser E-Mail existiert bereits");
        }

        // Erstelle Kunden
        Customer customer = new Customer(firstName, lastName, email, phone);
        return customerDAO.save(customer);
    }

    /**
     * Aktualisiert einen Kunden
     */
    public void updateCustomer(Customer customer) throws DatabaseException, ValidationException {
        Validator.validateName(customer.getFirstName(), "Vorname");
        Validator.validateName(customer.getLastName(), "Nachname");
        Validator.validateEmail(customer.getEmail());
        Validator.validatePhone(customer.getPhone());

        customerDAO.update(customer);
    }

    /**
     * Löscht einen Kunden
     */
    public void deleteCustomer(Long id) throws DatabaseException {
        customerDAO.delete(id);
    }

    /**
     * Findet einen Kunden nach ID
     */
    public Customer findCustomerById(Long id) throws DatabaseException {
        return customerDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Kunde nicht gefunden"));
    }

    /**
     * Gibt alle Kunden zurück
     */
    public List<Customer> findAllCustomers() throws DatabaseException {
        return customerDAO.findAll();
    }

    /**
     * Findet einen Kunden nach E-Mail
     */
    public Optional<Customer> findCustomerByEmail(String email) throws DatabaseException {
        return customerDAO.findByEmail(email);
    }

    /**
     * Sortiert Kunden nach Nachnamen
     */
    public List<Customer> sortCustomersByLastName(List<Customer> customers) {
        return customers.stream()
                .sorted(Comparator.comparing(Customer::getLastName)
                        .thenComparing(Customer::getFirstName))
                .collect(Collectors.toList());
    }

    /**
     * Sucht Kunden nach Namen (case-insensitive)
     */
    public List<Customer> searchCustomersByName(String searchTerm) throws DatabaseException {
        String lowerSearch = searchTerm.toLowerCase();

        return customerDAO.findAll().stream()
                .filter(c -> c.getFirstName().toLowerCase().contains(lowerSearch) ||
                        c.getLastName().toLowerCase().contains(lowerSearch))
                .collect(Collectors.toList());
    }

    /**
     * Findet Kunden mit aktiven Buchungen
     */
    public List<Customer> findCustomersWithActiveBookings() throws DatabaseException {
        return customerDAO.findAll().stream()
                .filter(c -> c.getActiveBookingsCount() > 0)
                .collect(Collectors.toList());
    }

    /**
     * Berechnet die Gesamtzahl der Kunden
     */
    public long getTotalCustomerCount() throws DatabaseException {
        return customerDAO.findAll().size();
    }

    /**
     * Findet Top-Kunden nach Anzahl der Buchungen
     */
    public List<Customer> findTopCustomers(int limit) throws DatabaseException {
        return customerDAO.findAll().stream()
                .sorted(Comparator.comparing(Customer::getActiveBookingsCount).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
}