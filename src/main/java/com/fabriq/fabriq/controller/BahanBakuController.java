package com.fabriq.fabriq.controller;

import com.fabriq.fabriq.model.BahanBaku;
import com.fabriq.fabriq.service.BahanBakuService;
import com.fabriq.fabriq.view.BahanBakuPage;

import java.util.Optional;

public class BahanBakuController {

    private final BahanBakuPage view;
    private final BahanBakuService service;

    public BahanBakuController(BahanBakuPage view, BahanBakuService service) {
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

        BahanBaku item = readInput(service.generateKode());
        if (item == null) {
            return;
        }

        service.add(item);
        refreshTable();
        resetForm();
        view.showInfo("Bahan baku berhasil ditambahkan.");
    }

    private void handleUbah() {
        String kode = view.getSelectedKode();
        if (kode == null) {
            view.showWarning("Pilih dulu data di tabel yang ingin diubah.");
            return;
        }

        BahanBaku item = readInput(kode);
        if (item == null) {
            return;
        }

        service.update(item);
        refreshTable();
        resetForm();
        view.showInfo("Data bahan baku berhasil diubah.");
    }

    private void handleHapus() {
        String kode = view.getSelectedKode();
        if (kode == null) {
            view.showWarning("Pilih dulu data di tabel yang ingin dihapus.");
            return;
        }

        Optional<BahanBaku> item = service.findByKode(kode);
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
    private BahanBaku readInput(String kode) {
        String nama = view.getNama();
        if (nama.isEmpty()) {
            view.showWarning("Nama bahan wajib diisi.");
            return null;
        }

        Double stok = parseNumber(view.getStokText());
        if (stok == null) {
            view.showWarning("Stok harus berupa angka 0 atau lebih.\nContoh: 150 atau 12.5");
            return null;
        }

        Double harga = parseNumber(view.getHargaText());
        if (harga == null) {
            view.showWarning("Harga harus berupa angka 0 atau lebih.\nContoh: 45000");
            return null;
        }

        return new BahanBaku(kode, nama, view.getKategori(), view.getSatuan(), stok, harga);
    }

    private Double parseNumber(String text) {
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