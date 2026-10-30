package com.fabriq.fabriq.view;

import com.fabriq.fabriq.model.Penjualan;
import com.fabriq.fabriq.util.DateUtil;
import com.fabriq.fabriq.util.MoneyUtil;
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

public class PenjualanPage extends JPanel {

    private final JTextField kodeField = UiFactory.textField();
    private final JTextField tanggalField = UiFactory.textField();
    private final JTextField pelangganField = UiFactory.textField();
    private final JTextField stokField = UiFactory.textField();
    private final JTextField hargaField = UiFactory.textField();
    private final JTextField jumlahField = UiFactory.textField();
    private final JTextField totalField = UiFactory.textField();
    private final JTextField searchField = UiFactory.textField();

    private final DefaultComboBoxModel<String> produkModel = new DefaultComboBoxModel<>();
    private final JComboBox<String> produkBox = new JComboBox<>(produkModel);

    private final JButton simpanButton = UiFactory.actionButton("Simpan", Theme.BLUE);
    private final JButton batalkanButton = UiFactory.actionButton("Batalkan Transaksi", new Color(200, 60, 60));
    private final JButton bersihkanButton = UiFactory.actionButton("Bersihkan", new Color(110, 120, 140));

    private final DefaultTableModel tableModel = new DefaultTableModel(
        new String[]{"No. Penjualan", "Tanggal", "Pelanggan", "Produk",
                     "Jumlah", "Harga Satuan", "Total", "Status"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 1) {
                return LocalDate.class;
            }
            if (columnIndex == 4) {
                return Integer.class;
            }
            if (columnIndex == 5 || columnIndex == 6) {
                return Double.class;
            }
            return String.class;
        }
    };
    private final JTable table = UiFactory.table(tableModel);

    public PenjualanPage() {
        for (JTextField field : new JTextField[]{kodeField, stokField, hargaField, totalField}) {
            field.setEditable(false);
            field.setBackground(Theme.TABLE_HEADER);
        }
        batalkanButton.setPreferredSize(new Dimension(170, 36));

        setLayout(new BorderLayout(0, 20));
        setBackground(Theme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(30, 35, 30, 35));

        JPanel center = new JPanel(new BorderLayout(0, 18));
        center.setOpaque(false);
        center.add(createFormCard(), BorderLayout.NORTH);
        center.add(createTableCard(), BorderLayout.CENTER);

        add(UiFactory.pageHeader("Penjualan",
            "Catat transaksi penjualan produk. Stok produk berkurang otomatis."), BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
    }

    // ===================== FORM =====================
    private JPanel createFormCard() {
        JPanel card = UiFactory.card(new BorderLayout(0, 12));

        JPanel fields = new JPanel(new GridLayout(2, 4, 16, 10));
        fields.setOpaque(false);
        fields.add(UiFactory.formField("No. Penjualan", kodeField));
        fields.add(UiFactory.formField("Tgl (dd-MM-yyyy)", tanggalField));
        fields.add(UiFactory.formField("Pelanggan", pelangganField));
        fields.add(UiFactory.formField("Produk", produkBox));
        fields.add(UiFactory.formField("Stok Tersedia", stokField));
        fields.add(UiFactory.formField("Harga Satuan", hargaField));
        fields.add(UiFactory.formField("Jumlah (pcs)", jumlahField));
        fields.add(UiFactory.formField("Total", totalField));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttons.setOpaque(false);
        buttons.add(simpanButton);
        buttons.add(batalkanButton);
        buttons.add(bersihkanButton);

        card.add(UiFactory.label("Form Penjualan", Theme.HEADING, Theme.NAVY), BorderLayout.NORTH);
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
        top.add(UiFactory.label("Riwayat Penjualan", Theme.HEADING, Theme.NAVY), BorderLayout.WEST);
        top.add(searchBox, BorderLayout.EAST);

        table.setAutoCreateRowSorter(true); // klik header kolom untuk mengurutkan
        table.getColumnModel().getColumn(0).setPreferredWidth(95);
        table.getColumnModel().getColumn(1).setCellRenderer(new DateRenderer());
        table.getColumnModel().getColumn(2).setPreferredWidth(130);
        table.getColumnModel().getColumn(3).setPreferredWidth(170);
        table.getColumnModel().getColumn(4).setPreferredWidth(60);
        table.getColumnModel().getColumn(5).setCellRenderer(new MoneyRenderer());
        table.getColumnModel().getColumn(6).setCellRenderer(new MoneyRenderer());
        table.getColumnModel().getColumn(7).setCellRenderer(new StatusRenderer());

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

    // Renderer kolom uang (rata kanan, format Rupiah)
    private static class MoneyRenderer extends DefaultTableCellRenderer {
        MoneyRenderer() {
            setHorizontalAlignment(SwingConstants.RIGHT);
        }

        @Override
        protected void setValue(Object value) {
            if (value instanceof Number) {
                setText(MoneyUtil.format(((Number) value).doubleValue()));
            } else {
                super.setValue(value);
            }
        }
    }

    // Renderer kolom status: Selesai hijau, Dibatalkan merah
    private static class StatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
            setFont(getFont().deriveFont(Font.BOLD));

            if (!isSelected) {
                setForeground(Penjualan.STATUS_SELESAI.equals(value)
                    ? Theme.GREEN : new Color(200, 60, 60));
            }
            return c;
        }
    }

    // ===== Method untuk dipakai Controller: membaca isian form =====
    public String getTanggalText() {
        return tanggalField.getText();
    }

    public String getPelanggan() {
        return pelangganField.getText().trim();
    }

    public String getProduk() {
        return (String) produkBox.getSelectedItem(); // null jika pilihan kosong
    }

    public String getJumlahText() {
        return jumlahField.getText();
    }

    public String getSearchText() {
        return searchField.getText();
    }

    // No. penjualan pada baris yang dipilih, atau null kalau tidak ada yang dipilih
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

    public void setProdukOptions(List<String> options) {
        produkModel.removeAllElements();
        produkModel.addAll(options);
    }

    public void setStokInfo(String text) {
        stokField.setText(text);
    }

    public void setHargaInfo(String text) {
        hargaField.setText(text);
    }

    public void setTotalInfo(String text) {
        totalField.setText(text);
    }

    // Menampilkan data transaksi yang dipilih (harga dan total memakai nilai saat transaksi)
    public void fillForm(Penjualan item) {
        kodeField.setText(item.getKode());
        tanggalField.setText(DateUtil.format(item.getTanggal()));
        pelangganField.setText(item.getPelanggan());

        String option = item.getProdukKode() + " - " + item.getProdukNama();
        if (produkModel.getIndexOf(option) < 0) {
            produkModel.addElement(option);
        }
        produkBox.setSelectedItem(option);

        stokField.setText("-");
        hargaField.setText(MoneyUtil.format(item.getHarga()));
        jumlahField.setText(String.valueOf(item.getJumlah()));
        totalField.setText(MoneyUtil.format(item.getTotal()));
    }

    public void clearForm() {
        table.clearSelection();
        tanggalField.setText(DateUtil.format(LocalDate.now()));
        pelangganField.setText("");
        jumlahField.setText("");
        if (produkBox.getItemCount() > 0) {
            produkBox.setSelectedIndex(0);
        }
        stokField.setText("-");
        hargaField.setText("-");
        totalField.setText("-");
    }

    public void setTableData(List<Penjualan> items) {
        tableModel.setRowCount(0);
        for (Penjualan item : items) {
            tableModel.addRow(new Object[]{
                item.getKode(), item.getTanggal(), item.getPelanggan(), item.getProdukNama(),
                item.getJumlah(), item.getHarga(), item.getTotal(), item.getStatus()
            });
        }
    }

    // ===== Method untuk dipakai Controller: pasang listener =====
    public void addSimpanListener(ActionListener listener) {
        simpanButton.addActionListener(listener);
    }

    public void addBatalkanListener(ActionListener listener) {
        batalkanButton.addActionListener(listener);
    }

    public void addBersihkanListener(ActionListener listener) {
        bersihkanButton.addActionListener(listener);
    }

    public void addProdukListener(Runnable onChange) {
        produkBox.addActionListener(e -> onChange.run());
    }

    public void addJumlahListener(Runnable onChange) {
        jumlahField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { onChange.run(); }
            @Override public void removeUpdate(DocumentEvent e) { onChange.run(); }
            @Override public void changedUpdate(DocumentEvent e) { onChange.run(); }
        });
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

    public boolean confirm(String message, String title) {
        int choice = JOptionPane.showConfirmDialog(
            this, message, title, JOptionPane.YES_NO_OPTION);
        return choice == JOptionPane.YES_OPTION;
    }
}