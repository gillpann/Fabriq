package com.fabriq.fabriq.controller;

import com.fabriq.fabriq.model.Produksi;
import com.fabriq.fabriq.model.QualityControl;
import com.fabriq.fabriq.service.PenggunaService;
import com.fabriq.fabriq.service.ProduksiService;
import com.fabriq.fabriq.service.QualityControlService;
import com.fabriq.fabriq.util.DateUtil;
import com.fabriq.fabriq.view.QualityControlPage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QualityControlController {

    private final QualityControlPage view;
    private final QualityControlService service;
    private final ProduksiService produksiService;
    private final PenggunaService penggunaService;

    public QualityControlController(QualityControlPage view, QualityControlService service,
                                    ProduksiService produksiService,
                                    PenggunaService penggunaService) {
        this.view = view;
        this.service = service;
        this.produksiService = produksiService;
        this.penggunaService = penggunaService;

        view.addTambahListener(e -> handleTambah());
        view.addUbahListener(e -> handleUbah());
        view.addHapusListener(e -> handleHapus());
        view.addBersihkanListener(e -> resetForm());
        view.addBatchListener(this::syncProduk);
        view.addSearchListener(this::refreshTable);
        view.addSelectionListener(this::handleSelection);

        refresh();
    }

    // Dipanggil setiap halaman QC dibuka, supaya pilihan batch dan inspektur selalu terbaru
    public void refresh() {
        refreshTable();
        resetForm();
    }

    private void refreshTable() {
        view.setTableData(service.search(view.getSearchText()));
    }

    private void resetForm() {
        List<String> batchCodes = new ArrayList<>();
        for (Produksi batch : produksiService.getInspectableBatches()) {
            batchCodes.add(batch.getKode());
        }

        view.setBatchOptions(batchCodes);
        view.setInspekturOptions(penggunaService.getActiveNamesByRole("Quality Control"));
        view.clearForm();
        view.setKode(service.generateKode());
        syncProduk();
    }

    // Nama produk mengikuti batch yang dipilih
    private void syncProduk() {
        String batch = view.getBatch();
        if (batch == null) {
            view.setProduk("");
            return;
        }
        produksiService.findByKode(batch).ifPresent(b -> view.setProduk(b.getProduk()));
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
                + "Klik Bersihkan dulu untuk menambah pemeriksaan baru.");
            return;
        }

        QualityControl item = readInput(service.generateKode());
        if (item == null) {
            return;
        }

        service.add(item);
        refreshTable();
        resetForm();
        view.showInfo("Data pemeriksaan berhasil ditambahkan.");
    }

    private void handleUbah() {
        String kode = view.getSelectedKode();
        if (kode == null) {
            view.showWarning("Pilih dulu data di tabel yang ingin diubah.");
            return;
        }

        QualityControl item = readInput(kode);
        if (item == null) {
            return;
        }

        service.update(item);
        refreshTable();
        resetForm();
        view.showInfo("Data pemeriksaan berhasil diubah.");
    }

    private void handleHapus() {
        String kode = view.getSelectedKode();
        if (kode == null) {
            view.showWarning("Pilih dulu data di tabel yang ingin dihapus.");
            return;
        }

        if (view.confirmDelete(kode)) {
            service.delete(kode);
            refreshTable();
            resetForm();
        }
    }

    // Validasi isian form. Mengembalikan null (dan menampilkan peringatan) jika tidak valid.
    private QualityControl readInput(String kode) {
        String batch = view.getBatch();
        if (batch == null) {
            view.showWarning("Belum ada batch yang bisa diperiksa.\n"
                + "Batch harus berstatus Berjalan atau Selesai di menu Produksi.");
            return null;
        }

        Optional<Produksi> batchData = produksiService.findByKode(batch);
        if (batchData.isEmpty()) {
            view.showWarning("Batch " + batch + " sudah tidak ada di menu Produksi.\nPilih batch lain.");
            return null;
        }
        Produksi produksi = batchData.get();

        String inspektur = view.getInspektur();
        if (inspektur == null) {
            view.showWarning("Belum ada pengguna aktif dengan role Quality Control.\n"
                + "Tambahkan dulu di menu Pengguna.");
            return null;
        }

        LocalDate tanggal = DateUtil.parse(view.getTanggalText());
        if (tanggal == null) {
            view.showWarning("Tanggal tidak valid.\nGunakan format dd-MM-yyyy, contoh: 25-10-2026");
            return null;
        }

        if (tanggal.isBefore(produksi.getMulai())) {
            view.showWarning("Tanggal inspeksi tidak boleh sebelum batch dimulai ("
                + DateUtil.format(produksi.getMulai()) + ").");
            return null;
        }

        if (tanggal.isAfter(LocalDate.now())) {
            view.showWarning("Tanggal inspeksi tidak boleh melewati hari ini.");
            return null;
        }

        Integer diperiksa = parseInt(view.getDiperiksaText());
        if (diperiksa == null || diperiksa <= 0) {
            view.showWarning("Jumlah diperiksa harus bilangan bulat lebih dari 0.\nContoh: 50");
            return null;
        }

        if (diperiksa > produksi.getJumlah()) {
            view.showWarning("Jumlah diperiksa (" + diperiksa + ") melebihi target batch ("
                + produksi.getJumlah() + " pcs).");
            return null;
        }

        Integer lolos = parseInt(view.getLolosText());
        if (lolos == null || lolos < 0) {
            view.showWarning("Jumlah lolos harus bilangan bulat 0 atau lebih.\nContoh: 48");
            return null;
        }

        if (lolos > diperiksa) {
            view.showWarning("Jumlah lolos tidak boleh lebih besar dari jumlah diperiksa.");
            return null;
        }

        return new QualityControl(kode, batch, produksi.getProduk(), tanggal,
            diperiksa, lolos, inspektur, view.getHasil(), view.getCatatan());
    }

    private Integer parseInt(String text) {
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}