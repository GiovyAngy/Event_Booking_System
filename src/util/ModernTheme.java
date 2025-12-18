package util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Centralized modern theme configuration for the application
 * Provides consistent colors, fonts, and component styling
 */
public class ModernTheme {

    // ========== COLOR PALETTE ==========

    // Primary Colors
    public static final Color PRIMARY = new Color(37, 99, 235); // Modern Blue
    public static final Color PRIMARY_DARK = new Color(29, 78, 216); // Darker Blue
    public static final Color PRIMARY_LIGHT = new Color(96, 165, 250); // Lighter Blue

    // Secondary Colors
    public static final Color SECONDARY = new Color(100, 116, 139); // Slate Gray
    public static final Color SECONDARY_LIGHT = new Color(148, 163, 184);

    // Accent Colors
    public static final Color ACCENT = new Color(16, 185, 129); // Emerald Green
    public static final Color ACCENT_LIGHT = new Color(52, 211, 153);
    public static final Color WARNING = new Color(245, 158, 11); // Amber
    public static final Color ERROR = new Color(239, 68, 68); // Red

    // Background Colors
    public static final Color BACKGROUND = new Color(248, 250, 252); // Light Gray
    public static final Color SURFACE = Color.WHITE;
    public static final Color SURFACE_HOVER = new Color(241, 245, 249);

    // Text Colors
    public static final Color TEXT_PRIMARY = new Color(30, 41, 59);
    public static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    public static final Color TEXT_DISABLED = new Color(203, 213, 225);

    // Border Colors
    public static final Color BORDER = new Color(226, 232, 240);
    public static final Color BORDER_FOCUS = PRIMARY;

    // Table Colors
    public static final Color TABLE_HEADER = new Color(241, 245, 249);
    public static final Color TABLE_ROW_EVEN = Color.WHITE;
    public static final Color TABLE_ROW_ODD = new Color(249, 250, 251);
    public static final Color TABLE_SELECTION = new Color(219, 234, 254);

    // ========== TYPOGRAPHY ==========

    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_SUBHEADER = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 14);

    // ========== SPACING ==========

    public static final int PADDING_SMALL = 8;
    public static final int PADDING_MEDIUM = 12;
    public static final int PADDING_LARGE = 16;
    public static final int PADDING_XL = 24;

    public static final int BORDER_RADIUS = 8;
    public static final int BORDER_RADIUS_LARGE = 12;

    // ========== COMPONENT STYLING METHODS ==========

    /**
     * Creates a styled primary button
     */
    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(FONT_BUTTON);
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMargin(new Insets(PADDING_SMALL, PADDING_LARGE, PADDING_SMALL, PADDING_LARGE));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_DARK);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY);
            }
        });

        return button;
    }

    /**
     * Creates a styled secondary button
     */
    public static JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(FONT_BUTTON);
        button.setForeground(TEXT_PRIMARY);
        button.setBackground(SURFACE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(BORDER, 1));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMargin(new Insets(PADDING_SMALL, PADDING_LARGE, PADDING_SMALL, PADDING_LARGE));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(SURFACE_HOVER);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(SURFACE);
            }
        });

        return button;
    }

    /**
     * Creates a styled accent button (for positive actions)
     */
    public static JButton createAccentButton(String text) {
        JButton button = new JButton(text);
        button.setFont(FONT_BUTTON);
        button.setForeground(Color.WHITE);
        button.setBackground(ACCENT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMargin(new Insets(PADDING_SMALL, PADDING_LARGE, PADDING_SMALL, PADDING_LARGE));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(ACCENT_LIGHT);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(ACCENT);
            }
        });

        return button;
    }

    /**
     * Creates a styled danger button (for delete/cancel actions)
     */
    public static JButton createDangerButton(String text) {
        JButton button = new JButton(text);
        button.setFont(FONT_BUTTON);
        button.setForeground(Color.WHITE);
        button.setBackground(ERROR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMargin(new Insets(PADDING_SMALL, PADDING_LARGE, PADDING_SMALL, PADDING_LARGE));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(220, 38, 38));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(ERROR);
            }
        });

        return button;
    }

    /**
     * Creates a styled panel with padding
     */
    public static JPanel createPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(SURFACE);
        panel.setBorder(new EmptyBorder(PADDING_MEDIUM, PADDING_MEDIUM, PADDING_MEDIUM, PADDING_MEDIUM));
        return panel;
    }

    /**
     * Creates a styled card panel with border
     */
    public static JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(SURFACE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                new EmptyBorder(PADDING_LARGE, PADDING_LARGE, PADDING_LARGE, PADDING_LARGE)));
        return panel;
    }

    /**
     * Styles a table with modern appearance
     */
    public static void styleTable(JTable table) {
        table.setFont(FONT_BODY);
        table.setRowHeight(40);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(TABLE_SELECTION);
        table.setSelectionForeground(TEXT_PRIMARY);

        // Header styling
        table.getTableHeader().setFont(FONT_BUTTON);
        table.getTableHeader().setBackground(TABLE_HEADER);
        table.getTableHeader().setForeground(TEXT_PRIMARY);
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER));

        // Alternating row colors
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? TABLE_ROW_EVEN : TABLE_ROW_ODD);
                }
                setBorder(new EmptyBorder(PADDING_SMALL, PADDING_MEDIUM, PADDING_SMALL, PADDING_MEDIUM));
                return c;
            }
        });
    }

    /**
     * Styles a text field with modern appearance
     */
    public static void styleTextField(JTextField textField) {
        textField.setFont(FONT_BODY);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                new EmptyBorder(PADDING_SMALL, PADDING_MEDIUM, PADDING_SMALL, PADDING_MEDIUM)));

        // Focus border
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_FOCUS, 2),
                        new EmptyBorder(PADDING_SMALL - 1, PADDING_MEDIUM - 1, PADDING_SMALL - 1, PADDING_MEDIUM - 1)));
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER, 1),
                        new EmptyBorder(PADDING_SMALL, PADDING_MEDIUM, PADDING_SMALL, PADDING_MEDIUM)));
            }
        });
    }

    /**
     * Styles a combo box with modern appearance
     */
    public static void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(FONT_BODY);
        comboBox.setBackground(SURFACE);
        comboBox.setBorder(BorderFactory.createLineBorder(BORDER, 1));
    }

    /**
     * Creates a styled label
     */
    public static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_BODY);
        label.setForeground(TEXT_PRIMARY);
        return label;
    }

    /**
     * Creates a styled header label
     */
    public static JLabel createHeaderLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_HEADER);
        label.setForeground(TEXT_PRIMARY);
        return label;
    }

    /**
     * Creates a styled subheader label
     */
    public static JLabel createSubheaderLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_SUBHEADER);
        label.setForeground(TEXT_PRIMARY);
        return label;
    }
}
