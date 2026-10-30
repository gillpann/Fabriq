package com.fabriq.fabriq.view;

import com.fabriq.fabriq.util.Theme;
import com.fabriq.fabriq.util.UiFactory;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class SidebarPanel extends JPanel {

    private static final String[] MENUS = {
        "Dashboard", "Bahan Baku", "Produk", "Produksi",
        "Quality Control", "Penjualan", "Laporan", "Pengguna"
    };

    private final Map<String, JButton> menuButtons = new LinkedHashMap<>();
    private final JButton logoutButton = createMenuButton("Logout");

    public SidebarPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Theme.NAVY);
        setPreferredSize(new Dimension(240, 0));
        setBorder(BorderFactory.createEmptyBorder(30, 0, 24, 0));

        JLabel logo = UiFactory.label("FABRIQ",
            new Font("SansSerif", Font.BOLD, 28), Color.WHITE);
        logo.setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 0));

        JLabel tagline = UiFactory.label("Fashion Manufacturing",
            Theme.SMALL, new Color(190, 205, 230));
        tagline.setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 0));

        add(logo);
        add(Box.createVerticalStrut(4));
        add(tagline);
        add(Box.createVerticalStrut(35));

        for (String menu : MENUS) {
            JButton button = createMenuButton(menu);
            menuButtons.put(menu, button);
            add(button);
            add(Box.createVerticalStrut(4));
        }

        add(Box.createVerticalGlue());
        logoutButton.setForeground(new Color(255, 160, 160));
        add(logoutButton);

        setActive("Dashboard");
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(Theme.NAVY);
        button.setOpaque(true);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 24));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        button.setPreferredSize(new Dimension(240, 46));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    // ===== Method untuk dipakai Controller =====
    public void setMenuListener(Consumer<String> listener) {
        for (Map.Entry<String, JButton> entry : menuButtons.entrySet()) {
            String name = entry.getKey();
            entry.getValue().addActionListener(e -> listener.accept(name));
        }
    }

    public void setLogoutListener(Runnable listener) {
        logoutButton.addActionListener(e -> listener.run());
    }

    public void setActive(String name) {
        for (Map.Entry<String, JButton> entry : menuButtons.entrySet()) {
            boolean active = entry.getKey().equals(name);
            entry.getValue().setBackground(active ? Theme.BLUE : Theme.NAVY);
            entry.getValue().setFont(
                new Font("SansSerif", active ? Font.BOLD : Font.PLAIN, 14));
        }
    }
}