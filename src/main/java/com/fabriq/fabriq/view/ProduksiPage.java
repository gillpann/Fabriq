package com.fabriq.fabriq.view;

import com.fabriq.fabriq.model.Produksi;
import com.fabriq.fabriq.util.DateUtil;
import com.fabriq.fabriq.util.Theme;
import com.fabriq.fabriq.util.UiFactory;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

public class ProduksiPage extends JPanel {

    private final JTextField kodeField = UiFactory.textField();
    private final JTextField jumlahField = UiFactory.textField();
    private final JTextField mulaiField = UiFactory.textField();
    private final JTextField targetField = UiFactory.textField();
    private final JTextField searchField = UiFactory.textField();

    private final DefaultComboBoxModel<String> produkModel = new DefaultComboBoxModel<>();
    private final DefaultComboBoxModel<String> pjModel = new DefaultComboBoxModel<>();
    private final JComboBox<String> produkBox = new JComboBox<>(produkModel);
    private final JComboBox<String> pjBox = new JComboBox<>(pjModel);
    private final JComboBox<String> statusBox = new JComboBox<>(Produksi.STATUSES);

    private final JButton tambahButton = UiFactory.actionButton("Tambah", Theme.BLUE);
    private final JButton ubahButton = UiFactory.actionButton("Ubah", Theme.GREEN);
    private final JButton hapusButton = UiFactory.actionButton("Hapus", new Color(200, 60, 60));
    private final JButton bersihkanButton = UiFactory.actionButton("Bersihkan", new Color(110, 120, 140));

    private final DefaultTableModel tableModel = new DefaultTableModel(
        new String[]{"No. Batch", "Produk", "Jumlah", "Tgl Mulai",
                     "Target Selesai", "Penanggung Jawab", "Status"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 2) {
                return Integer.class;
            }
            if (columnIndex == 3 || columnIndex == 4) {
                return LocalDate.class;
            }
            return String.class;
        }
    };
    private final JTable table = UiFactory.table(tableModel);

    public ProduksiPage() {
        kodeField.setEditable(false);
        kodeField.setBackground(Theme.TABLE_HEADER);

        setLayout(new BorderLayout(0, 20));
        setBackground(Theme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(30, 35, 30, 35));

        JPanel center = new JPanel(new BorderLayout(0, 18));
        center.setOpaque(false);
        center.add(createFormCard(), BorderLayout.NORTH);
        center.add(createTableCard(), BorderLayout.CENTER);

        add(UiFactory.pageHeader("Produksi",
            "Catat dan pantau batch produksi dari rencana sampai selesai."), BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
    }

    // ===================== FORM =====================
    private JPanel createFormCard() {
        JPanel card = UiFactory.card(new BorderLayout(0, 12));

        JPanel kosong = new JPanel();
        kosong.setOpaque(false);

        JPanel fields = new JPanel(new GridLayout(2, 4, 16, 10));
        fields.setOpaque(false);
        fields.add(UiFactory.formField("No. Batch", kodeField));
        fields.add(UiFactory.formField("Produk", produkBox));
        fields.add(UiFactory.formField("Jumlah Target (pcs)", jumlahField));
        fields.add(UiFactory.formField("Status", statusBox));
        fields.add(UiFactory.formField("Tgl Mulai (dd-MM-yyyy)", mulaiField));
        fields.add(UiFactory.formField("Target Selesai (dd-MM-yyyy)", targetField));
        fields.add(UiFactory.formField("Penanggung Jawab", pjBox));
        fields.add(kosong);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttons.setOpaque(false);
        buttons.add(tambahButton);
        buttons.add(ubahButton);
        buttons.add(hapusButton);
        buttons.add(bersihkanButton);

        card.add(UiFactory.label("Form Batch Produksi", Theme.HEADING, Theme.NAVY), BorderLayout.NORTH);
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
        top.add(UiFactory.label("Daftar Batch Produksi", Theme.HEADING, Theme.NAVY), BorderLayout.WEST);
        top.add(searchBox, BorderLayout.EAST);

        table.setAutoCreateRowSorter(true); // klik header kolom untuk mengurutkan
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(170);
        table.getColumnModel().getColumn(2).setPreferredWidth(60);
        table.getColumnModel().getColumn(3).setCellRenderer(new DateRenderer());
        table.getColumnModel().getColumn(4).setCellRenderer(new DateRenderer());
        table.getColumnModel().getColumn(5).setPreferredWidth(130);
        table.getColumnModel().getColumn(6).setCellRenderer(new StatusRenderer());

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        card.add(top, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);
        return card;
    }

    // Renderer kolom tanggal: tampil dd-MM-yyyy
    private static class DateRenderer extends DefaultTableCellRenderer {
        @Override
        protected void setValue(Object value) {
            if (value instanceof LocalDate) {
                setText(DateUtil.format((LocalDate) value));
            } else {
                super.setValue(value);
            }
        }
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
                if (Produksi.STATUS_BERJALAN.equals(status)) {
                    setForeground(Theme.ORANGE);
                } else if (Produksi.STATUS_SELESAI.equals(status)) {
                    setForeground(Theme.GREEN);
                } else if (Produksi.STATUS_DIBATALKAN.equals(status)) {
                    setForeground(new Color(200, 60, 60));
                } else {
                    setForeground(Theme.BLUE);
                }
            }
            return c;
        }
    }

    // ===== Method untuk dipakai Controller: membaca isian form =====
    public String getProduk() {
        return (String) produkBox.getSelectedItem(); // null jika pilihan kosong
    }

    public String getJumlahText() {
        return jumlahField.getText();
    }

    public String getMulaiText() {
        return mulaiField.getText();
    }

    public String getTargetText() {
        return targetField.getText();
    }

    public String getPenanggungJawab() {
        return (String) pjBox.getSelectedItem();
    }

    public String getStatus() {
        return (String) statusBox.getSelectedItem();
    }

    public String getSearchText() {
        return searchField.getText();
    }

    // No. batch pada baris yang dipilih, atau null kalau tidak ada yang dipilih
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

    public void setProdukOptions(List<String> names) {
        produkModel.removeAllElements();
        produkModel.addAll(names);
    }

    public void setPenanggungJawabOptions(List<String> names) {
        pjModel.removeAllElements();
        pjModel.addAll(names);
    }

    public void fillForm(Produksi item) {
        kodeField.setText(item.getKode());
        selectOrAdd(produkModel, produkBox, item.getProduk());
        jumlahField.setText(String.valueOf(item.getJumlah()));
        mulaiField.setText(DateUtil.format(item.getMulai()));
        targetField.setText(DateUtil.format(item.getTarget()));
        selectOrAdd(pjModel, pjBox, item.getPenanggungJawab());
        statusBox.setSelectedItem(item.getStatus());
    }

    // Kalau nilainya tidak ada di pilihan (misal produk sudah Nonaktif), tambahkan dulu
    private void selectOrAdd(DefaultComboBoxModel<String> model, JComboBox<String> box, String value) {
        if (model.getIndexOf(value) < 0) {
            model.addElement(value);
        }
        box.setSelectedItem(value);
    }

    public void clearForm() {
        table.clearSelection();
        jumlahField.setText("");
        mulaiField.setText(DateUtil.format(LocalDate.now()));
        targetField.setText("");
        if (produkBox.getItemCount() > 0) {
            produkBox.setSelectedIndex(0);
        }
        if (pjBox.getItemCount() > 0) {
            pjBox.setSelectedIndex(0);
        }
        statusBox.setSelectedIndex(0);
    }

    public void setTableData(List<Produksi> items) {
        tableModel.setRowCount(0);
        for (Produksi item : items) {
            tableModel.addRow(new Object[]{
                item.getKode(), item.getProduk(), item.getJumlah(), item.getMulai(),
                item.getTarget(), item.getPenanggungJawab(), item.getStatus()
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

    public boolean confirmDelete(String kode) {
        int choice = JOptionPane.showConfirmDialog(
            this, "Hapus batch " + kode + "?",
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        return choice == JOptionPane.YES_OPTION;
    }
}