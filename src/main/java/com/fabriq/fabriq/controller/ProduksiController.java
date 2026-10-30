package com.fabriq.fabriq.controller;

import com.fabriq.fabriq.model.Produksi;
import com.fabriq.fabriq.service.ProdukService;
import com.fabriq.fabriq.service.ProduksiService;
import com.fabriq.fabriq.service.PenggunaService;
import com.fabriq.fabriq.util.DateUtil;
import com.fabriq.fabriq.view.ProduksiPage;

import java.time.LocalDate;
import java.util.Optional;

public class ProduksiController {

    private final ProduksiPage view;
    private final ProduksiService service;
    private final ProdukService produkService;
    private final PenggunaService penggunaService;

    public ProduksiController(ProduksiPage view, ProduksiService service,
                              ProdukService produkService, PenggunaService penggunaService) {
        this.view = view;
        this.service = service;
        this.produkService = produkService;
        this.penggunaService = penggunaService;

        view.addTambahListener(e -> handleTambah());
        view.addUbahListener(e -> handleUbah());
        view.addHapusListener(e -> handleHapus());
        view.addBersihkanListener(e -> resetForm());
        view.addSearchListener(this::refreshTable);
        view.addSelectionListener(this::handleSelection);

        refresh();
    }

    // Dipanggil setiap halaman Produksi dibuka, supaya pilihan produk selalu terbaru
    public void refresh() {
        refreshTable();
        resetForm();
    }

    private void refreshTable() {
        view.setTableData(service.search(view.getSearchText()));
    }

    private void resetForm() {
        view.setProdukOptions(produkService.getActiveNames());
        view.setPenanggungJawabOptions(penggunaService.getActiveNamesByRole("Produksi"));
        view.clearForm();
        view.setKode(service.generateKode());
    }

    // Baris dipilih -> isi form dengan datanya
    private void handleSelection() {
        String kode = view.getSelectedKode();
        if (kode == null) {
            return;
        }
        service.findByKode(kode).ifPresent(view::fillForm);
    }

    private void handleTambah() {
        if (view.getSelectedKode() != null) {
            view.showWarning("Kamu sedang memilih data di tabel.\n"
                + "Klik Bersihkan dulu untuk menambah batch baru.");
            return;
        }

        Produksi item = readInput(service.generateKode());
        if (item == null) {
            return;
        }

        if (item.isSelesai()) {
            view.showWarning("Batch baru tidak bisa langsung berstatus Selesai.");
            return;
        }

        service.add(item);
        refreshTable();
        resetForm();
        view.showInfo("Batch produksi berhasil ditambahkan.");
    }

    private void handleUbah() {
        String kode = view.getSelectedKode();
        if (kode == null) {
            view.showWarning("Pilih dulu batch di tabel yang ingin diubah.");
            return;
        }

        if (isLocked(kode)) {
            view.showWarning("Batch yang sudah Selesai terkunci dan tidak bisa diubah.");
            return;
        }

        Produksi item = readInput(kode);
        if (item == null) {
            return;
        }

        service.update(item);
        refreshTable();
        resetForm();
        view.showInfo("Batch produksi berhasil diubah.");
    }

    private void handleHapus() {
        String kode = view.getSelectedKode();
        if (kode == null) {
            view.showWarning("Pilih dulu batch di tabel yang ingin dihapus.");
            return;
        }

        if (isLocked(kode)) {
            view.showWarning("Batch yang sudah Selesai terkunci dan tidak bisa dihapus.");
            return;
        }

        if (view.confirmDelete(kode)) {
            service.delete(kode);
            refreshTable();
            resetForm();
        }
    }

    private boolean isLocked(String kode) {
        Optional<Produksi> existing = service.findByKode(kode);
        return existing.isPresent() && existing.get().isSelesai();
    }

    // Validasi isian form. Mengembalikan null (dan menampilkan peringatan) jika tidak valid.
    private Produksi readInput(String kode) {
        String produk = view.getProduk();
        if (produk == null) {
            view.showWarning("Belum ada produk aktif.\nTambahkan atau aktifkan produk dulu di menu Produk.");
            return null;
        }
        
        if (view.getPenanggungJawab() == null) {
            view.showWarning("Belum ada pengguna aktif dengan role Produksi.\n"
                + "Tambahkan dulu di menu Pengguna.");
            return null;
        }

        Integer jumlah = parseJumlah(view.getJumlahText());
        if (jumlah == null) {
            view.showWarning("Jumlah target harus bilangan bulat lebih dari 0.\nContoh: 100");
            return null;
        }

        LocalDate mulai = DateUtil.parse(view.getMulaiText());
        if (mulai == null) {
            view.showWarning("Tanggal mulai tidak valid.\nGunakan format dd-MM-yyyy, contoh: 25-10-2026");
            return null;
        }

        LocalDate target = DateUtil.parse(view.getTargetText());
        if (target == null) {
            view.showWarning("Target selesai tidak valid.\nGunakan format dd-MM-yyyy, contoh: 10-11-2026");
            return null;
        }

        if (target.isBefore(mulai)) {
            view.showWarning("Target selesai tidak boleh sebelum tanggal mulai.");
            return null;
        }

        return new Produksi(kode, produk, jumlah, mulai, target,
            view.getPenanggungJawab(), view.getStatus());
    }

    private Integer parseJumlah(String text) {
        try {
            int value = Integer.parseInt(text.trim());
            return value <= 0 ? null : value;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}