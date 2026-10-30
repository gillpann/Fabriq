package com.fabriq.fabriq.service;

import com.fabriq.fabriq.model.Pengguna;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PenggunaService {

    private final List<Pengguna> data = new ArrayList<>();

    public PenggunaService() {
        // Data dummy awal. Password disimpan polos HANYA untuk tahap dummy.
        // Saat pakai database, password wajib di-hash (misalnya BCrypt).
        data.add(new Pengguna("USR-001", "admin", "Administrator", "Admin", "Aktif", "admin123"));
        data.add(new Pengguna("USR-002", "sari", "Sari Wulandari", "Produksi", "Aktif", "sari123"));
        data.add(new Pengguna("USR-003", "budi", "Budi Santoso", "Produksi", "Aktif", "budi123"));
        data.add(new Pengguna("USR-004", "rina", "Rina Marlina", "Produksi", "Aktif", "rina123"));
        data.add(new Pengguna("USR-005", "andi", "Andi Pratama", "Quality Control", "Aktif", "andi123"));
        data.add(new Pengguna("USR-006", "dewi", "Dewi Lestari", "Penjualan", "Aktif", "dewi123"));
        data.add(new Pengguna("USR-007", "rudi", "Rudi Hartono", "Produksi", "Nonaktif", "rudi123"));
    }

    public List<Pengguna> getAll() {
        return new ArrayList<>(data);
    }

    // Cari di ID, username, nama, role, dan status
    public List<Pengguna> search(String keyword) {
        String key = keyword.trim().toLowerCase();
        if (key.isEmpty()) {
            return getAll();
        }

        List<Pengguna> result = new ArrayList<>();
        for (Pengguna item : data) {
            if (item.getKode().toLowerCase().contains(key)
                    || item.getUsername().toLowerCase().contains(key)
                    || item.getNamaLengkap().toLowerCase().contains(key)
                    || item.getRole().toLowerCase().contains(key)
                    || item.getStatus().toLowerCase().contains(key)) {
                result.add(item);
            }
        }
        return result;
    }

    public Optional<Pengguna> findByKode(String kode) {
        for (Pengguna item : data) {
            if (item.getKode().equals(kode)) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    // Username dianggap sama walau beda huruf besar/kecil. exceptKode = akun yang sedang diedit.
    public boolean isUsernameTaken(String username, String exceptKode) {
        for (Pengguna item : data) {
            if (!item.getKode().equals(exceptKode)
                    && item.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    // Dipakai login: cocokkan username dan password (status dicek oleh AuthService)
    public Optional<Pengguna> findByCredentials(String username, String password) {
        for (Pengguna item : data) {
            if (item.getUsername().equalsIgnoreCase(username)
                    && item.getPassword().equals(password)) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    // Nama lengkap pengguna aktif dengan role tertentu (dipakai pilihan Penanggung Jawab)
    public List<String> getActiveNamesByRole(String role) {
        List<String> names = new ArrayList<>();
        for (Pengguna item : data) {
            if (item.isAktif() && item.getRole().equals(role)) {
                names.add(item.getNamaLengkap());
            }
        }
        return names;
    }

    public String generateKode() {
        int max = 0;
        for (Pengguna item : data) {
            try {
                max = Math.max(max, Integer.parseInt(item.getKode().substring(4)));
            } catch (Exception ignored) {
            }
        }
        return String.format("USR-%03d", max + 1);
    }

    public void add(Pengguna item) {
        data.add(item);
    }

    public void update(Pengguna updated) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getKode().equals(updated.getKode())) {
                data.set(i, updated);
                return;
            }
        }
    }

    public void delete(String kode) {
        data.removeIf(item -> item.getKode().equals(kode));
    }
}