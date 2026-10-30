package com.fabriq.fabriq.controller;

import com.fabriq.fabriq.model.Produk;
import com.fabriq.fabriq.service.ProdukService;
import com.fabriq.fabriq.view.ProdukPage;

import java.util.Optional;

public class ProdukController {

    private final ProdukPage view;
    private final ProdukService service;

    public ProdukController(ProdukPage view, ProdukService service) {
        this.view = view;
        this.service = service;

        view.addTambahListener(e -> handleTambah());
        view.addUbahListener(e -> handleUbah());
        view.addHapusListener(e -> handleHapus());
        view.addBersihkanListener(e -> resetForm());
        view.addSearchListener(this::refreshTable);
        view.addSelectionListener(this::handleSelection);

        refreshTable();
        resetForm();
    }

    private void refreshTable() {
        view.setTableData(service.search(view.getSearchText()));
    }

    private void resetForm() {
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
                + "Klik Bersihkan dulu untuk menambah data baru.");
            return;
        }

        Produk item = readInput(service.generateKode());
        if (item == null) {
            return;
        }

        service.add(item);
        refreshTable();
        resetForm();
        view.showInfo("Produk berhasil ditambahkan.");
    }

    private void handleUbah() {
        String kode = view.getSelectedKode();
        if (kode == null) {
            view.showWarning("Pilih dulu data di tabel yang ingin diubah.");
            return;
        }

        Produk item = readInput(kode);
        if (item == null) {
            return;
        }

        service.update(item);
        refreshTable();
        resetForm();
        view.showInfo("Data produk berhasil diubah.");
    }

    private void handleHapus() {
        String kode = view.getSelectedKode();
        if (kode == null) {
            view.showWarning("Pilih dulu data di tabel yang ingin dihapus.");
            return;
        }

        Optional<Produk> item = service.findByKode(kode);
        if (item.isEmpty()) {
            return;
        }

        if (view.confirmDelete(item.get().getNama())) {
            service.delete(kode);
            refreshTable();
            resetForm();
        }
    }

    // Validasi isian form. Mengembalikan null (dan menampilkan peringatan) jika tidak valid.
    private Produk readInput(String kode) {
        String nama = view.getNama();
        if (nama.isEmpty()) {
            view.showWarning("Nama produk wajib diisi.");
            return null;
        }

        String warna = view.getWarna();
        if (warna.isEmpty()) {
            view.showWarning("Warna wajib diisi.");
            return null;
        }

        Integer stok = parseStok(view.getStokText());
        if (stok == null) {
            view.showWarning("Stok harus berupa bilangan bulat 0 atau lebih.\nContoh: 50");
            return null;
        }

        Double harga = parseHarga(view.getHargaText());
        if (harga == null) {
            view.showWarning("Harga harus berupa angka 0 atau lebih.\nContoh: 185000");
            return null;
        }

        return new Produk(kode, nama, view.getKategori(), view.getUkuran(),
            warna, stok, harga, view.getStatus());
    }

    private Integer parseStok(String text) {
        try {
            int value = Integer.parseInt(text.trim());
            return value < 0 ? null : value;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Double parseHarga(String text) {
        try {
            double value = Double.parseDouble(text.trim().replace(',', '.'));
            if (Double.isNaN(value) || Double.isInfinite(value) || value < 0) {
                return null;
            }
            return value;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}