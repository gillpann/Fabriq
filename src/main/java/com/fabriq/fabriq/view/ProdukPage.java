package com.fabriq.fabriq.view;

import com.fabriq.fabriq.model.Produk;
import com.fabriq.fabriq.util.Theme;
import com.fabriq.fabriq.util.UiFactory;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProdukPage extends JPanel {

    private final JTextField kodeField = UiFactory.textField();
    private final JTextField namaField = UiFactory.textField();
    private final JTextField warnaField = UiFactory.textField();
    private final JTextField stokField = UiFactory.textField();
    private final JTextField hargaField = UiFactory.textField();
    private final JTextField searchField = UiFactory.textField();

    private final JComboBox<String> kategoriBox = new JComboBox<>(new String[]{
        "Kemeja", "Kaos", "Celana", "Jaket", "Dress", "Lainnya"
    });
    private final JComboBox<String> ukuranBox = new JComboBox<>(new String[]{
        "S", "M", "L", "XL", "XXL", "All Size"
    });
    private final JComboBox<String> statusBox = new JComboBox<>(new String[]{
        "Aktif", "Nonaktif"
    });

    private final JButton tambahButton = UiFactory.actionButton("Tambah", Theme.BLUE);
    private final JButton ubahButton = UiFactory.actionButton("Ubah", Theme.GREEN);
    private final JButton hapusButton = UiFactory.actionButton("Hapus", new Color(200, 60, 60));
    private final JButton bersihkanButton = UiFactory.actionButton("Bersihkan", new Color(110, 120, 140));

    private final DefaultTableModel tableModel = new DefaultTableModel(
        new String[]{"Kode", "Nama Produk", "Kategori", "Ukuran",
                     "Warna", "Stok", "Harga Jual", "Status"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 5) {
                return Integer.class;
            }
            if (columnIndex == 6) {
                return Double.class;
            }
            return String.class;
        }
    };
    private final JTable table = UiFactory.table(tableModel);

    private final NumberFormat numberFormat =
        NumberFormat.getNumberInstance(Locale.forLanguageTag("id-ID"));

    public ProdukPage() {
        numberFormat.setMaximumFractionDigits(0);

        kodeField.setEditable(false);
        kodeField.setBackground(Theme.TABLE_HEADER);

        setLayout(new BorderLayout(0, 20));
        setBackground(Theme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(30, 35, 30, 35));

        JPanel center = new JPanel(new BorderLayout(0, 18));
        center.setOpaque(false);
        center.add(createFormCard(), BorderLayout.NORTH);
        center.add(createTableCard(), BorderLayout.CENTER);

        add(UiFactory.pageHeader("Produk",
            "Kelola data produk fashion yang diproduksi dan dijual."), BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
    }

    // ===================== FORM =====================
    private JPanel createFormCard() {
        JPanel card = UiFactory.card(new BorderLayout(0, 12));

        JPanel fields = new JPanel(new GridLayout(2, 4, 16, 10));
        fields.setOpaque(false);
        fields.add(UiFactory.formField("Kode", kodeField));
        fields.add(UiFactory.formField("Nama Produk", namaField));
        fields.add(UiFactory.formField("Kategori", kategoriBox));
        fields.add(UiFactory.formField("Ukuran", ukuranBox));
        fields.add(UiFactory.formField("Warna", warnaField));
        fields.add(UiFactory.formField("Stok (pcs)", stokField));
        fields.add(UiFactory.formField("Harga Jual (Rp)", hargaField));
        fields.add(UiFactory.formField("Status", statusBox));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttons.setOpaque(false);
        buttons.add(tambahButton);
        buttons.add(ubahButton);
        buttons.add(hapusButton);
        buttons.add(bersihkanButton);

        card.add(UiFactory.label("Form Produk", Theme.HEADING, Theme.NAVY), BorderLayout.NORTH);
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
        top.add(UiFactory.label("Daftar Produk", Theme.HEADING, Theme.NAVY), BorderLayout.WEST);
        top.add(searchBox, BorderLayout.EAST);

        table.setAutoCreateRowSorter(true); // klik header kolom untuk mengurutkan
        table.getColumnModel().getColumn(0).setPreferredWidth(70);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(3).setPreferredWidth(70);
        table.getColumnModel().getColumn(5).setPreferredWidth(60);
        table.getColumnModel().getColumn(6).setCellRenderer(new HargaRenderer());
        table.getColumnModel().getColumn(7).setCellRenderer(new StatusRenderer());

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        card.add(top, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);
        return card;
    }

    // Renderer kolom harga (rata kanan, format Rupiah)
    private class HargaRenderer extends DefaultTableCellRenderer {
        HargaRenderer() {
            setHorizontalAlignment(SwingConstants.RIGHT);
        }

        @Override
        protected void setValue(Object value) {
            if (value instanceof Number) {
                setText("Rp " + numberFormat.format(((Number) value).doubleValue()));
            } else {
                super.setValue(value);
            }
        }
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
                setForeground("Aktif".equals(value) ? Theme.GREEN : new Color(200, 60, 60));
            }
            return c;
        }
    }

    // ===== Method untuk dipakai Controller: membaca isian form =====
    public String getNama() {
        return namaField.getText().trim();
    }

    public String getKategori() {
        return (String) kategoriBox.getSelectedItem();
    }

    public String getUkuran() {
        return (String) ukuranBox.getSelectedItem();
    }

    public String getWarna() {
        return warnaField.getText().trim();
    }

    public String getStokText() {
        return stokField.getText();
    }

    public String getHargaText() {
        return hargaField.getText();
    }

    public String getStatus() {
        return (String) statusBox.getSelectedItem();
    }

    public String getSearchText() {
        return searchField.getText();
    }

    // Kode produk pada baris yang dipilih, atau null kalau tidak ada yang dipilih
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

    public void fillForm(Produk item) {
        kodeField.setText(item.getKode());
        namaField.setText(item.getNama());
        kategoriBox.setSelectedItem(item.getKategori());
        ukuranBox.setSelectedItem(item.getUkuran());
        warnaField.setText(item.getWarna());
        stokField.setText(String.valueOf(item.getStok()));
        hargaField.setText(plainNumber(item.getHarga()));
        statusBox.setSelectedItem(item.getStatus());
    }

    public void clearForm() {
        table.clearSelection();
        namaField.setText("");
        warnaField.setText("");
        stokField.setText("");
        hargaField.setText("");
        kategoriBox.setSelectedIndex(0);
        ukuranBox.setSelectedIndex(0);
        statusBox.setSelectedIndex(0);
    }

    public void setTableData(List<Produk> items) {
        tableModel.setRowCount(0);
        for (Produk item : items) {
            tableModel.addRow(new Object[]{
                item.getKode(), item.getNama(), item.getKategori(), item.getUkuran(),
                item.getWarna(), item.getStok(), item.getHarga(), item.getStatus()
            });
        }
    }

    private String plainNumber(double value) {
        if (value == Math.floor(value)) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
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

    public boolean confirmDelete(String nama) {
        int choice = JOptionPane.showConfirmDialog(
            this, "Hapus produk \"" + nama + "\"?",
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        return choice == JOptionPane.YES_OPTION;
    }
}