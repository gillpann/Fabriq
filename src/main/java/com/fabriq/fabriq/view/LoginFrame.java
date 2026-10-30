package com.fabriq.fabriq.view;

import com.fabriq.fabriq.util.Theme;
import com.fabriq.fabriq.util.UiFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {

    private final JTextField usernameField = UiFactory.textField();
    private final JPasswordField passwordField = UiFactory.passwordField();
    private final JButton loginButton = UiFactory.button("LOGIN", Theme.BLUE);

    public LoginFrame() {
        setTitle("Fabriq | Integrated Fashion Manufacturing System");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.add(createBrandPanel());
        mainPanel.add(createFormPanel());
        add(mainPanel);

        getRootPane().setDefaultButton(loginButton); // tombol Enter = klik Login
    }

    // Panel kiri: identitas Fabriq
    private JPanel createBrandPanel() {
        JPanel panel = UiFactory.verticalPanel(Theme.NAVY, 90, 40, 40, 40);

        panel.add(UiFactory.label("FABRIQ",
            new Font("SansSerif", Font.BOLD, 36), Color.WHITE));
        panel.add(Box.createVerticalStrut(12));
        panel.add(UiFactory.label("<html>INTEGRATED FASHION<br>MANUFACTURING SYSTEM</html>",
            Theme.BODY.deriveFont(15f), new Color(190, 205, 230)));
        panel.add(Box.createVerticalStrut(45));
        panel.add(UiFactory.label(
            "<html>Manage materials, production,<br>"
            + "quality control, and sales<br>"
            + "in one integrated application.</html>",
            Theme.BODY, Color.WHITE));

        return panel;
    }

    // Panel kanan: form login
    private JPanel createFormPanel() {
        JPanel panel = UiFactory.verticalPanel(Theme.BACKGROUND, 75, 55, 50, 55);

        JLabel footer = UiFactory.label("FABRIQ © 2026",
            Theme.BODY.deriveFont(11f), Color.GRAY);
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(UiFactory.label("Welcome Back!", Theme.TITLE, Theme.NAVY));
        panel.add(Box.createVerticalStrut(8));
        panel.add(UiFactory.label("Silakan masuk ke akun Anda", Theme.BODY, Color.GRAY));
        panel.add(Box.createVerticalStrut(35));
        panel.add(UiFactory.label("Username", Theme.LABEL, Theme.TEXT));
        panel.add(Box.createVerticalStrut(8));
        panel.add(usernameField);
        panel.add(Box.createVerticalStrut(20));
        panel.add(UiFactory.label("Password", Theme.LABEL, Theme.TEXT));
        panel.add(Box.createVerticalStrut(8));
        panel.add(passwordField);
        panel.add(Box.createVerticalStrut(28));
        panel.add(loginButton);
        panel.add(Box.createVerticalGlue());
        panel.add(footer);

        return panel;
    }

    // ===== Method untuk dipakai Controller =====
    public String getUsername() {
        return usernameField.getText().trim();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public void clearPassword() {
        passwordField.setText("");
    }

    public void addLoginListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    public void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Validasi Login", JOptionPane.WARNING_MESSAGE);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Login Gagal", JOptionPane.ERROR_MESSAGE);
    }
}