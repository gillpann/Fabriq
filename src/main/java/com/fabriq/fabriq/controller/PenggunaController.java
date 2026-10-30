package com.fabriq.fabriq.controller;

import com.fabriq.fabriq.model.Pengguna;
import com.fabriq.fabriq.model.User;
import com.fabriq.fabriq.service.PenggunaService;
import com.fabriq.fabriq.view.PenggunaPage;

import java.util.Optional;
import java.util.regex.Pattern;

public class PenggunaController {

    private static final String MAIN_ADMIN = "admin";
    private static final Pattern USERNAME_PATTERN = Pattern.compile("[A-Za-z0-9_.]{4,20}");

    private final PenggunaPage view;
    private final PenggunaService service;
    private final String currentUsername; // akun yang sedang login

    public PenggunaController(PenggunaPage view, PenggunaService service, User currentUser) {
        this.view = view;
        this.service = service;
        this.currentUsername = currentUser.getUsername();

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
                + "Klik Bersihkan dulu untuk menambah pengguna baru.");
            return;
        }

        Pengguna item = readInput(service.generateKode(), null);
        if (item == null) {
            return;
        }

        service.add(item);
        refreshTable();
        resetForm();
        view.showInfo("Pengguna berhasil ditambahkan.");
    }

    private void handleUbah() {
        String kode = view.getSelectedKode();
        if (kode == null) {
            view.showWarning("Pilih dulu pengguna di tabel yang ingin diubah.");
            return;
        }

        Optional<Pengguna> existing = service.findByKode(kode);
        if (existing.isEmpty()) {
            return;
        }
        Pengguna old = existing.get();

        Pengguna item = readInput(kode, old);
        if (item == null) {
            return;
        }

        // Admin utama tidak boleh kehilangan akses
        if (old.getUsername().equals(MAIN_ADMIN)
                && !(item.getUsername().equals(MAIN_ADMIN)
                     && "Admin".equals(item.getRole())
                     && item.isAktif())) {
            view.showWarning("Akun admin utama tidak boleh diubah username, role, atau statusnya.");
            return;
        }

        // Akun yang sedang dipakai tidak boleh mengunci dirinya sendiri
        if (old.getUsername().equals(currentUsername)
                && !(item.getUsername().equals(old.getUsername()) && item.isAktif())) {
            view.showWarning("Akun yang sedang kamu pakai tidak boleh dinonaktifkan\n"
                + "atau diganti username-nya.");
            return;
        }

        service.update(item);
        refreshTable();
        resetForm();
        view.showInfo("Data pengguna berhasil diubah.");
    }

    private void handleHapus() {
        String kode = view.getSelectedKode();
        if (kode == null) {
            view.showWarning("Pilih dulu pengguna di tabel yang ingin dihapus.");
            return;
        }

        Optional<Pengguna> existing = service.findByKode(kode);
        if (existing.isEmpty()) {
            return;
        }
        Pengguna old = existing.get();

        if (old.getUsername().equals(MAIN_ADMIN)) {
            view.showWarning("Akun admin utama tidak boleh dihapus.");
            return;
        }

        if (old.getUsername().equals(currentUsername)) {
            view.showWarning("Akun yang sedang kamu pakai tidak boleh dihapus.");
            return;
        }

        if (view.confirmDelete(old.getUsername())) {
            service.delete(kode);
            refreshTable();
            resetForm();
        }
    }

    // Validasi isian form. existing = null saat Tambah.
    // Mengembalikan null (dan menampilkan peringatan) jika tidak valid.
    private Pengguna readInput(String kode, Pengguna existing) {
        String username = view.getUsername();
        String nama = view.getNama();
        String password = view.getPassword();

        if (!USERNAME_PATTERN.matcher(username).matches()) {
            view.showWarning("Username harus 4-20 karakter.\n"
                + "Boleh huruf, angka, titik, dan underscore (tanpa spasi).");
            return null;
        }

        if (service.isUsernameTaken(username, kode)) {
            view.showWarning("Username \"" + username + "\" sudah dipakai pengguna lain.");
            return null;
        }

        if (nama.isEmpty()) {
            view.showWarning("Nama lengkap wajib diisi.");
            return null;
        }

        if (existing == null && password.isEmpty()) {
            view.showWarning("Password wajib diisi.");
            return null;
        }

        if (!password.isEmpty() && password.length() < 6) {
            view.showWarning("Password minimal 6 karakter.");
            return null;
        }

        // Saat Ubah dan password dikosongkan, password lama dipertahankan
        String finalPassword = password.isEmpty() ? existing.getPassword() : password;

        return new Pengguna(kode, username, nama, view.getRole(), view.getStatus(), finalPassword);
    }
}