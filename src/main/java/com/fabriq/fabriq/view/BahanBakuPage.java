package com.fabriq.fabriq.view;

import com.fabriq.fabriq.model.BahanBaku;
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

public class BahanBakuPage extends JPanel {

    private final JTextField kodeField = UiFactory.textField();
    private final JTextField namaField = UiFactory.textField();
    private final JTextField stokField = UiFactory.textField();
    private final JTextField hargaField = UiFactory.textField();
    private final JTextField searchField = UiFactory.textField();

    private final JComboBox<String> kategoriBox = new JComboBox<>(new String[]{
        "Kain", "Benang", "Kancing", "Resleting", "Aksesoris", "Lainnya"
    });
    private final JComboBox<String> satuanBox = new JComboBox<>(new String[]{
        "meter", "roll", "kg", "cone", "pcs", "lusin"
    });

    private final JButton tambahButton = UiFactory.actionButton("Tambah", Theme.BLUE);
    private final JButton ubahButton = UiFactory.actionButton("Ubah", Theme.GREEN);
    private final JButton hapusButton = UiFactory.actionButton("Hapus", new Color(200, 60, 60));
    private final JButton bersihkanButton = UiFactory.actionButton("Bersihkan", new Color(110, 120, 140));

    private final DefaultTableModel tableModel = new DefaultTableModel(
        new String[]{"Kode", "Nama Bahan", "Kategori", "Satuan", "Stok", "Harga/Satuan"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return (columnIndex == 4 || columnIndex == 5) ? Double.class : String.class;
        }
    };
    private final JTable table = UiFactory.table(tableModel);

    private final NumberFormat numberFormat =
        NumberFormat.getNumberInstance(Locale.forLanguageTag("id-ID"));

    public BahanBakuPage() {
        numberFormat.setMaximumFractionDigits(2);

        kodeField.setEditable(false);
        kodeField.setBackground(Theme.TABLE_HEADER);

        setLayout(new BorderLayout(0, 20));
        setBackground(Theme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(30, 35, 30, 35));

        JPanel center = new JPanel(new BorderLayout(0, 18));
        center.setOpaque(false);
        center.add(createFormCard(), BorderLayout.NORTH);
        center.add(createTableCard(), BorderLayout.CENTER);

        add(UiFactory.pageHeader("Bahan Baku",
            "Kelola data kain, benang, dan material produksi."), BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
    }

    // ===================== FORM =====================
    private JPanel createFormCard() {
        JPanel card = UiFactory.card(new BorderLayout(0, 12));

        JPanel fields = new JPanel(new GridLayout(2, 3, 16, 10));
        fields.setOpaque(false);
        fields.add(UiFactory.formField("Kode", kodeField));
        fields.add(UiFactory.formField("Nama Bahan", namaField));
        fields.add(UiFactory.formField("Kategori", kategoriBox));
        fields.add(UiFactory.formField("Satuan", satuanBox));
        fields.add(UiFactory.formField("Stok", stokField));
        fields.add(UiFactory.formField("Harga per Satuan (Rp)", hargaField));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttons.setOpaque(false);
        buttons.add(tambahButton);
        buttons.add(ubahButton);
        buttons.add(hapusButton);
        buttons.add(bersihkanButton);

        card.add(UiFactory.label("Form Bahan Baku", Theme.HEADING, Theme.NAVY), BorderLayout.NORTH);
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
        top.add(UiFactory.label("Daftar Bahan Baku", Theme.HEADING, Theme.NAVY), BorderLayout.WEST);
        top.add(searchBox, BorderLayout.EAST);

        table.setAutoCreateRowSorter(true); // klik header kolom untuk mengurutkan
        table.getColumnModel().getColumn(0).setPreferredWidth(70);
        table.getColumnModel().getColumn(1).setPreferredWidth(240);
        table.getColumnModel().getColumn(4).setCellRenderer(new NumberRenderer(false));
        table.getColumnModel().getColumn(5).setCellRenderer(new NumberRenderer(true));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        card.add(top, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);
        return card;
    }

    // Renderer kolom angka (rata kanan, format Indonesia)
    private class NumberRenderer extends DefaultTableCellRenderer {
        private final boolean currency;

        NumberRenderer(boolean currency) {
            this.currency = currency;
            setHorizontalAlignment(SwingConstants.RIGHT);
        }

        @Override
        protected void setValue(Object value) {
            if (value instanceof Number) {
                double v = ((Number) value).doubleValue();
                setText(currency ? "Rp " + numberFormat.format(v) : numberFormat.format(v));
            } else {
                super.setValue(value);
            }
        }
    }

    // ===== Method untuk dipakai Controller: membaca isian form =====
    public String getNama() {
        return namaField.getText().trim();
    }

    public String getKategori() {
        return (String) kategoriBox.getSelectedItem();
    }

    public String getSatuan() {
        return (String) satuanBox.getSelectedItem();
    }

    public String getStokText() {
        return stokField.getText();
    }

    public String getHargaText() {
        return hargaField.getText();
    }

    public String getSearchText() {
        return searchField.getText();
    }

    // Kode bahan baku pada baris yang dipilih, atau null kalau tidak ada yang dipilih
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

    public void fillForm(BahanBaku item) {
        kodeField.setText(item.getKode());
        namaField.setText(item.getNama());
        kategoriBox.setSelectedItem(item.getKategori());
        satuanBox.setSelectedItem(item.getSatuan());
        stokField.setText(plainNumber(item.getStok()));
        hargaField.setText(plainNumber(item.getHarga()));
    }

    public void clearForm() {
        table.clearSelection();
        namaField.setText("");
        stokField.setText("");
        hargaField.setText("");
        kategoriBox.setSelectedIndex(0);
        satuanBox.setSelectedIndex(0);
    }

    public void setTableData(List<BahanBaku> items) {
        tableModel.setRowCount(0);
        for (BahanBaku item : items) {
            tableModel.addRow(new Object[]{
                item.getKode(), item.getNama(), item.getKategori(),
                item.getSatuan(), item.getStok(), item.getHarga()
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
            this, "Hapus bahan baku \"" + nama + "\"?",
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        return choice == JOptionPane.YES_OPTION;
    }
}