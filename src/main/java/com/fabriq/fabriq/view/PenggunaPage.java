package com.fabriq.fabriq.view;

import com.fabriq.fabriq.model.Pengguna;
import com.fabriq.fabriq.util.Theme;
import com.fabriq.fabriq.util.UiFactory;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class PenggunaPage extends JPanel {

    private final JTextField kodeField = UiFactory.textField();
    private final JTextField usernameField = UiFactory.textField();
    private final JTextField namaField = UiFactory.textField();
    private final JPasswordField passwordField = UiFactory.passwordField();
    private final JTextField searchField = UiFactory.textField();

    private final JLabel passwordLabel =
        UiFactory.label("Password", Theme.SMALL.deriveFont(Font.BOLD), Theme.TEXT);

    private final JComboBox<String> roleBox = new JComboBox<>(Pengguna.ROLES);
    private final JComboBox<String> statusBox = new JComboBox<>(Pengguna.STATUSES);

    private final JButton tambahButton = UiFactory.actionButton("Tambah", Theme.BLUE);
    private final JButton ubahButton = UiFactory.actionButton("Ubah", Theme.GREEN);
    private final JButton hapusButton = UiFactory.actionButton("Hapus", new Color(200, 60, 60));
    private final JButton bersihkanButton = UiFactory.actionButton("Bersihkan", new Color(110, 120, 140));

    private final DefaultTableModel tableModel = new DefaultTableModel(
        new String[]{"ID", "Username", "Nama Lengkap", "Role", "Status"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = UiFactory.table(tableModel);

    public PenggunaPage() {
        kodeField.setEditable(false);
        kodeField.setBackground(Theme.TABLE_HEADER);

        setLayout(new BorderLayout(0, 20));
        setBackground(Theme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(30, 35, 30, 35));

        JPanel center = new JPanel(new BorderLayout(0, 18));
        center.setOpaque(false);
        center.add(createFormCard(), BorderLayout.NORTH);
        center.add(createTableCard(), BorderLayout.CENTER);

        add(UiFactory.pageHeader("Pengguna",
            "Kelola akun pengguna dan peran akses aplikasi Fabriq."), BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
    }

    // ===================== FORM =====================
    private JPanel createFormCard() {
        JPanel card = UiFactory.card(new BorderLayout(0, 12));

        // Field password dibuat manual karena labelnya berubah (tambah vs ubah)
        JPanel passwordPanel = new JPanel(new BorderLayout(0, 4));
        passwordPanel.setOpaque(false);
        passwordPanel.add(passwordLabel, BorderLayout.NORTH);
        passwordPanel.add(passwordField, BorderLayout.CENTER);

        JPanel fields = new JPanel(new GridLayout(2, 3, 16, 10));
        fields.setOpaque(false);
        fields.add(UiFactory.formField("ID", kodeField));
        fields.add(UiFactory.formField("Username", usernameField));
        fields.add(UiFactory.formField("Nama Lengkap", namaField));
        fields.add(UiFactory.formField("Role", roleBox));
        fields.add(passwordPanel);
        fields.add(UiFactory.formField("Status", statusBox));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttons.setOpaque(false);
        buttons.add(tambahButton);
        buttons.add(ubahButton);
        buttons.add(hapusButton);
        buttons.add(bersihkanButton);

        card.add(UiFactory.label("Form Pengguna", Theme.HEADING, Theme.NAVY), BorderLayout.NORTH);
        card.add(fields, BorderLayout.CENTER);
        card.add(buttons, BorderLayout.SOUTH);
        return card;
    }

    // ===================== TABEL =====================
    private JPanel createTableCard() {
        JPanel card = UiFactory.card(new BorderLayout(0, 12));

        searchField.setPreferredSize(new Dimension(220, 34));
        JPanel searchBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        searchBox.setOpaque(false);
        searchBox.add(UiFactory.label("Cari:", Theme.BODY, Theme.TEXT));
        searchBox.add(searchField);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(UiFactory.label("Daftar Pengguna", Theme.HEADING, Theme.NAVY), BorderLayout.WEST);
        top.add(searchBox, BorderLayout.EAST);

        table.setAutoCreateRowSorter(true); // klik header kolom untuk mengurutkan
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);
        table.getColumnModel().getColumn(4).setCellRenderer(new StatusRenderer());

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        card.add(top, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);
        return card;
    }

    // Renderer kolom status: Aktif hijau, Nonaktif merah
    private static class StatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
            setFont(getFont().deriveFont(Font.BOLD));

            if (!isSelected) {
                setForeground(Pengguna.STATUS_AKTIF.equals(value)
                    ? Theme.GREEN : new Color(200, 60, 60));
            }
            return c;
        }
    }

    // ===== Method untuk dipakai Controller: membaca isian form =====
    public String getUsername() {
        return usernameField.getText().trim();
    }

    public String getNama() {
        return namaField.getText().trim();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public String getRole() {
        return (String) roleBox.getSelectedItem();
    }

    public String getStatus() {
        return (String) statusBox.getSelectedItem();
    }

    public String getSearchText() {
        return searchField.getText();
    }

    // ID pada baris yang dipilih, atau null kalau tidak ada yang dipilih
    public String getSelectedKode() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            return null;
        }
        return (String) tableModel.getValueAt(table.convertRowIndexToModel(viewRow), 0);
    }

    // ===== Method untuk dipakai Controller: mengubah tampilan =====
    public void setKode(String kode) {
        kodeField.setText(kode);
    }

    public void fillForm(Pengguna item) {
        kodeField.setText(item.getKode());
        usernameField.setText(item.getUsername());
        namaField.setText(item.getNamaLengkap());
        roleBox.setSelectedItem(item.getRole());
        statusBox.setSelectedItem(item.getStatus());
        passwordField.setText(""); // password tidak pernah ditampilkan
        passwordLabel.setText("Password baru (opsional)");
    }

    public void clearForm() {
        table.clearSelection();
        usernameField.setText("");
        namaField.setText("");
        passwordField.setText("");
        passwordLabel.setText("Password");
        roleBox.setSelectedIndex(0);
        statusBox.setSelectedIndex(0);
    }

    public void setTableData(List<Pengguna> items) {
        tableModel.setRowCount(0);
        for (Pengguna item : items) {
            tableModel.addRow(new Object[]{
                item.getKode(), item.getUsername(), item.getNamaLengkap(),
                item.getRole(), item.getStatus()
            });
        }
    }

    // ===== Method untuk dipakai Controller: pasang listener =====
    public void addTambahListener(ActionListener listener) {
        tambahButton.addActionListener(listener);
    }

    public void addUbahListener(ActionListener listener) {
        ubahButton.addActionListener(listener);
    }

    public void addHapusListener(ActionListener listener) {
        hapusButton.addActionListener(listener);
    }

    public void addBersihkanListener(ActionListener listener) {
        bersihkanButton.addActionListener(listener);
    }

    public void addSearchListener(Runnable onChange) {
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { onChange.run(); }
            @Override public void removeUpdate(DocumentEvent e) { onChange.run(); }
            @Override public void changedUpdate(DocumentEvent e) { onChange.run(); }
        });
    }

    public void addSelectionListener(Runnable onSelect) {
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                onSelect.run();
            }
        });
    }

    // ===== Dialog =====
    public void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Validasi", JOptionPane.WARNING_MESSAGE);
    }

    public void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Fabriq", JOptionPane.INFORMATION_MESSAGE);
    }

    public boolean confirmDelete(String username) {
        int choice = JOptionPane.showConfirmDialog(
            this, "Hapus pengguna \"" + username + "\"?",
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        return choice == JOptionPane.YES_OPTION;
    }
}