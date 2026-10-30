package com.fabriq.fabriq.view;

import com.fabriq.fabriq.model.ActivityLog;
import com.fabriq.fabriq.model.DashboardSummary;
import com.fabriq.fabriq.util.Theme;
import com.fabriq.fabriq.util.UiFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DashboardPage extends JPanel {

    private final JLabel subtitleLabel = UiFactory.label("", Theme.BODY, Color.GRAY);
    private final JLabel bahanBakuValue = new JLabel("-");
    private final JLabel produkValue = new JLabel("-");
    private final JLabel produksiValue = new JLabel("-");
    private final JLabel penjualanValue = new JLabel("-");

    private final DefaultTableModel tableModel = new DefaultTableModel(
        new String[]{"Waktu", "Aktivitas", "Modul", "Status"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public DashboardPage() {
        setLayout(new BorderLayout(0, 24));
        setBackground(Theme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(30, 35, 30, 35));

        JPanel center = new JPanel(new BorderLayout(0, 24));
        center.setOpaque(false);
        center.add(createStatCards(), BorderLayout.NORTH);
        center.add(createActivityPanel(), BorderLayout.CENTER);

        add(createHeader(), BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
    }

    // ===================== HEADER =====================
    private JPanel createHeader() {
        JPanel header = UiFactory.verticalPanel(Theme.BACKGROUND, 0, 0, 0, 0);
        header.add(UiFactory.label("Dashboard", Theme.TITLE, Theme.NAVY));
        header.add(Box.createVerticalStrut(6));
        header.add(subtitleLabel);
        return header;
    }

    // ===================== KARTU RINGKASAN =====================
    private JPanel createStatCards() {
        JPanel cards = new JPanel(new GridLayout(1, 4, 18, 0));
        cards.setOpaque(false);
        cards.setPreferredSize(new Dimension(0, 120));

        cards.add(createStatCard("Total Bahan Baku", bahanBakuValue, "item terdaftar", Theme.BLUE));
        cards.add(createStatCard("Total Produk", produkValue, "produk aktif", Theme.GREEN));
        cards.add(createStatCard("Produksi Aktif", produksiValue, "sedang berjalan", Theme.ORANGE));
        cards.add(createStatCard("Penjualan Bulan Ini", penjualanValue, "total penjualan", Theme.PURPLE));
        return cards;
    }

    private JPanel createStatCard(String title, JLabel valueLabel, String note, Color accent) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(4, 0, 0, 0, accent),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
                BorderFactory.createEmptyBorder(14, 18, 14, 18)
            )
        ));

        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        valueLabel.setForeground(Theme.NAVY);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(UiFactory.label(title, Theme.BODY.deriveFont(13f), Color.GRAY));
        card.add(Box.createVerticalStrut(6));
        card.add(valueLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(UiFactory.label(note, Theme.SMALL, accent));
        return card;
    }

    // ===================== AKTIVITAS TERBARU =====================
    private JPanel createActivityPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER),
            BorderFactory.createEmptyBorder(18, 20, 18, 20)
        ));

        JTable table = new JTable(tableModel);
        table.setRowHeight(36);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setForeground(Theme.TEXT);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(235, 238, 245));
        table.setSelectionBackground(new Color(225, 234, 252));
        table.setSelectionForeground(Theme.TEXT);

        table.getTableHeader().setFont(Theme.LABEL);
        table.getTableHeader().setBackground(Theme.TABLE_HEADER);
        table.getTableHeader().setForeground(Theme.TEXT);
        table.getTableHeader().setReorderingAllowed(false);

        table.getColumnModel().getColumn(0).setPreferredWidth(70);
        table.getColumnModel().getColumn(1).setPreferredWidth(320);
        table.getColumnModel().getColumn(2).setPreferredWidth(130);
        table.getColumnModel().getColumn(3).setPreferredWidth(90);
        table.getColumnModel().getColumn(3).setCellRenderer(new StatusRenderer());

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        panel.add(UiFactory.label("Aktivitas Terbaru", Theme.HEADING, Theme.NAVY), BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    // Renderer kolom status: teks berwarna sesuai status
    private static class StatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
            setFont(getFont().deriveFont(Font.BOLD));

            if (!isSelected) {
                String status = String.valueOf(value);
                if (status.equals("Selesai")) {
                    setForeground(Theme.GREEN);
                } else if (status.equals("Berjalan")) {
                    setForeground(Theme.ORANGE);
                } else {
                    setForeground(Theme.BLUE);
                }
            }
            return c;
        }
    }

    // ===== Method untuk dipakai Controller =====
    public void setUserName(String name) {
        subtitleLabel.setText("Selamat datang, " + name + ". Berikut ringkasan aktivitas Fabriq.");
    }

    public void setSummary(DashboardSummary summary) {
        bahanBakuValue.setText(String.valueOf(summary.getTotalBahanBaku()));
        produkValue.setText(String.valueOf(summary.getTotalProduk()));
        produksiValue.setText(String.valueOf(summary.getProduksiAktif()));
        penjualanValue.setText(formatRupiah(summary.getPenjualanBulanIni()));
    }

    public void setActivities(List<ActivityLog> activities) {
        tableModel.setRowCount(0);
        for (ActivityLog log : activities) {
            tableModel.addRow(new Object[]{
                log.getTime(), log.getDescription(), log.getModule(), log.getStatus()
            });
        }
    }

    private String formatRupiah(long amount) {
        Locale id = Locale.forLanguageTag("id-ID");
        if (amount >= 1_000_000) {
            return "Rp " + String.format(id, "%.1f jt", amount / 1_000_000.0);
        }
        return "Rp " + NumberFormat.getNumberInstance(id).format(amount);
    }
}