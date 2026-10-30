package com.fabriq.fabriq.controller;

import com.fabriq.fabriq.model.Penjualan;
import com.fabriq.fabriq.model.Produk;
import com.fabriq.fabriq.service.PenjualanService;
import com.fabriq.fabriq.service.ProdukService;
import com.fabriq.fabriq.util.DateUtil;
import com.fabriq.fabriq.util.MoneyUtil;
import com.fabriq.fabriq.view.PenjualanPage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PenjualanController {

    private final PenjualanPage view;
    private final PenjualanService service;
    private final ProdukService produkService;

    public PenjualanController(PenjualanPage view, PenjualanService service,
                               ProdukService produkService) {
        this.view = view;
        this.service = service;
        this.produkService = produkService;

        view.addSimpanListener(e -> handleSimpan());
        view.addBatalkanListener(e -> handleBatalkan());
        view.addBersihkanListener(e -> resetForm());
        view.addProdukListener(this::updatePreview);
        view.addJumlahListener(this::updatePreview);
        view.addSearchListener(this::refreshTable);
        view.addSelectionListener(this::handleSelection);

        refresh();
    }

    // Dipanggil setiap halaman Penjualan dibuka, supaya pilihan produk dan stok selalu terbaru
    public void refresh() {
        refreshTable();
        resetForm();
    }

    private void refreshTable() {
        view.setTableData(service.search(view.getSearchText()));
    }

    private void resetForm() {
        List<String> options = new ArrayList<>();
        for (Produk produk : produkService.getSellable()) {
            options.add(produk.getKode() + " - " + produk.getNama());
        }

        view.setProdukOptions(options);
        view.clearForm();
        view.setKode(service.generateKode());
        updatePreview();
    }

    // Menampilkan stok, harga satuan, dan total sesuai produk dan jumlah yang diisi
    private void updatePreview() {
        if (view.getSelectedKode() != null) {
            return; // sedang melihat transaksi lama, jangan timpa
        }

        Optional<Produk> found = selectedProduk();
        if (found.isEmpty()) {
            view.setStokInfo("-");
            view.setHargaInfo("-");
            view.setTotalInfo("-");
            return;
        }

        Produk produk = found.get();
        view.setStokInfo(produk.getStok() + " pcs");
        view.setHargaInfo(MoneyUtil.format(produk.getHarga()));

        Integer jumlah = parseInt(view.getJumlahText());
        if (jumlah != null && jumlah > 0) {
            view.setTotalInfo(MoneyUtil.format(produk.getHarga() * jumlah));
        } else {
            view.setTotalInfo("-");
        }
    }

    // Pilihan produk berbentuk "PR-001 - Nama Produk"
    private Optional<Produk> selectedProduk() {
        String option = view.getProduk();
        if (option == null) {
            return Optional.empty();
        }

        int separator = option.indexOf(" - ");
        String kode = separator < 0 ? option : option.substring(0, separator);
        return produkService.findByKode(kode);
    }

    // Baris dipilih -> tampilkan detail transaksinya
    private void handleSelection() {
        String kode = view.getSelectedKode();
        if (kode == null) {
            return;
        }
        service.findByKode(kode).ifPresent(view::fillForm);
    }

    private void handleSimpan() {
        if (view.getSelectedKode() != null) {
            view.showWarning("Kamu sedang melihat transaksi lama.\n"
                + "Klik Bersihkan dulu untuk membuat transaksi baru.");
            return;
        }

        Penjualan item = readInput(service.generateKode());
        if (item == null) {
            return;
        }

        // Kurangi stok produk. Jika gagal, transaksi tidak disimpan.
        if (!produkService.adjustStock(item.getProdukKode(), -item.getJumlah())) {
            view.showWarning("Stok produk tidak mencukupi.");
            return;
        }

        service.add(item);
        refreshTable();
        resetForm();
        view.showInfo("Penjualan " + item.getKode() + " berhasil dicatat.\n"
            + "Total: " + MoneyUtil.format(item.getTotal()));
    }

    private void handleBatalkan() {
        String kode = view.getSelectedKode();
        if (kode == null) {
            view.showWarning("Pilih dulu transaksi di tabel yang ingin dibatalkan.");
            return;
        }

        Optional<Penjualan> found = service.findByKode(kode);
        if (found.isEmpty()) {
            return;
        }
        Penjualan item = found.get();

        if (!item.isSelesai()) {
            view.showWarning("Transaksi " + kode + " sudah dibatalkan sebelumnya.");
            return;
        }

        boolean yes = view.confirm(
            "Batalkan transaksi " + kode + "?\n"
                + "Stok " + item.getJumlah() + " pcs " + item.getProdukNama()
                + " akan dikembalikan.",
            "Konfirmasi Pembatalan");
        if (!yes) {
            return;
        }

        service.update(item.batalkan());
        boolean restored = produkService.adjustStock(item.getProdukKode(), item.getJumlah());

        refreshTable();
        resetForm();
        if (restored) {
            view.showInfo("Transaksi " + kode + " dibatalkan dan stok dikembalikan.");
        } else {
            view.showInfo("Transaksi " + kode + " dibatalkan.\n"
                + "Stok tidak dikembalikan karena produknya sudah dihapus.");
        }
    }

    // Validasi isian form. Mengembalikan null (dan menampilkan peringatan) jika tidak valid.
    private Penjualan readInput(String kode) {
        String pelanggan = view.getPelanggan();
        if (pelanggan.isEmpty()) {
            view.showWarning("Nama pelanggan wajib diisi.");
            return null;
        }

        Optional<Produk> found = selectedProduk();
        if (found.isEmpty()) {
            view.showWarning("Belum ada produk yang bisa dijual.\n"
                + "Produk harus berstatus Aktif dan stoknya lebih dari 0 (cek menu Produk).");
            return null;
        }
        Produk produk = found.get();

        // Data produk bisa berubah sejak form dibuka, jadi dicek ulang
        if (!produk.isAktif() || produk.getStok() <= 0) {
            view.showWarning("Produk " + produk.getNama() + " sudah tidak bisa dijual.\n"
                + "Klik Bersihkan untuk memuat ulang pilihan produk.");
            return null;
        }

        LocalDate tanggal = DateUtil.parse(view.getTanggalText());
        if (tanggal == null) {
            view.showWarning("Tanggal tidak valid.\nGunakan format dd-MM-yyyy, contoh: 25-10-2026");
            return null;
        }

        if (tanggal.isAfter(LocalDate.now())) {
            view.showWarning("Tanggal penjualan tidak boleh melewati hari ini.");
            return null;
        }

        Integer jumlah = parseInt(view.getJumlahText());
        if (jumlah == null || jumlah <= 0) {
            view.showWarning("Jumlah harus bilangan bulat lebih dari 0.\nContoh: 10");
            return null;
        }

        if (jumlah > produk.getStok()) {
            view.showWarning("Stok " + produk.getNama() + " hanya tersisa "
                + produk.getStok() + " pcs.");
            return null;
        }

        return new Penjualan(kode, tanggal, pelanggan, produk.getKode(), produk.getNama(),
            jumlah, produk.getHarga(), Penjualan.STATUS_SELESAI);
    }

    private Integer parseInt(String text) {
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}