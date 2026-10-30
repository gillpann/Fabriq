package com.fabriq.fabriq.util;

import javax.swing.table.DefaultTableModel;
import com.fabriq.fabriq.util.Theme;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;

public final class UiFactory {

    private UiFactory() {
    }

    public static JLabel label(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    public static JTextField textField() {
        JTextField field = new JTextField();
        styleInput(field);
        return field;
    }

    public static JPasswordField passwordField() {
        JPasswordField field = new JPasswordField();
        styleInput(field);
        return field;
    }

    private static void styleInput(JTextComponent field) {
        field.setFont(Theme.BODY);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.INPUT_BORDER),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
    }

    public static JButton button(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setOpaque(true);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    // Panel dengan susunan atas-ke-bawah, warna latar, dan padding
    public static JPanel verticalPanel(Color background, int top, int left, int bottom, int right) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(background);
        panel.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
        return panel;
    }
    
        // Judul halaman + subjudul
    public static JPanel pageHeader(String title, String subtitle) {
        JPanel header = verticalPanel(Theme.BACKGROUND, 0, 0, 0, 0);
        header.add(label(title, Theme.TITLE, Theme.NAVY));
        header.add(Box.createVerticalStrut(6));
        header.add(label(subtitle, Theme.BODY, Color.GRAY));
        return header;
    }

    // Kartu putih dengan border dan padding
    public static JPanel card(LayoutManager layout) {
        JPanel card = new JPanel(layout);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER),
            BorderFactory.createEmptyBorder(16, 20, 16, 20)
        ));
        return card;
    }

    // Label kecil di atas komponen input
    public static JPanel formField(String labelText, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout(0, 4));
        panel.setOpaque(false);
        panel.add(label(labelText, Theme.SMALL.deriveFont(Font.BOLD), Theme.TEXT),
            BorderLayout.NORTH);
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    // Tombol ukuran tetap untuk baris aksi (Tambah, Ubah, Hapus, ...)
    public static JButton actionButton(String text, Color color) {
        JButton button = button(text, color);
        button.setPreferredSize(new Dimension(110, 36));
        return button;
    }

    // Tabel dengan gaya Fabriq
    public static JTable table(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(34);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setForeground(Theme.TEXT);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(235, 238, 245));
        table.setSelectionBackground(new Color(225, 234, 252));
        table.setSelectionForeground(Theme.TEXT);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.getTableHeader().setFont(Theme.LABEL);
        table.getTableHeader().setBackground(Theme.TABLE_HEADER);
        table.getTableHeader().setForeground(Theme.TEXT);
        table.getTableHeader().setReorderingAllowed(false);
        return table;
    }
}