package com.fabriq.fabriq.view;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DashboardFrame extends JFrame {

    private final SidebarPanel sidebar = new SidebarPanel();
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);
    private final Set<String> pages = new HashSet<>();
    private final Map<String, Runnable> onShowActions = new HashMap<>();

    public DashboardFrame() {
        setTitle("Fabriq | Dashboard");
        setSize(1100, 680);
        setMinimumSize(new Dimension(1000, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        add(sidebar, BorderLayout.WEST);
        add(cardPanel, BorderLayout.CENTER);
    }

    public SidebarPanel getSidebar() {
        return sidebar;
    }

    // Daftarkan halaman baru ke area konten
    public void addPage(String name, JPanel page) {
        pages.add(name);
        cardPanel.add(page, name);
    }

    // Aksi yang dijalankan setiap kali halaman ini ditampilkan (misal: refresh data)
    public void setOnShow(String name, Runnable action) {
        onShowActions.put(name, action);
    }

    public boolean hasPage(String name) {
        return pages.contains(name);
    }

    public void showPage(String name) {
        Runnable action = onShowActions.get(name);
        if (action != null) {
            action.run();
        }
        cardLayout.show(cardPanel, name);
        sidebar.setActive(name);
    }

    public boolean confirm(String message, String title) {
        int choice = JOptionPane.showConfirmDialog(
            this, message, title, JOptionPane.YES_NO_OPTION);
        return choice == JOptionPane.YES_OPTION;
    }

    public void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Fabriq", JOptionPane.INFORMATION_MESSAGE);
    }
}