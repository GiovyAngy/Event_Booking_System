import controller.MainController;

import javax.swing.*;

/**
 * Hauptklasse der Anwendung
 * Startet die GUI und initialisiert den Haupt-Controller
 */
public class Main {

    public static void main(String[] args) {
        // Setze Look and Feel auf System-Standard
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Look and Feel konnte nicht gesetzt werden: " + e.getMessage());
        }

        // Starte GUI im Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // Erstelle und zeige die Hauptanwendung
                new MainController();

                System.out.println("Event Booking System gestartet!");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                        null,
                        "Fehler beim Starten der Anwendung:\n" + e.getMessage(),
                        "Startfehler",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

    }
}