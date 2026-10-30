package com.fabriq.fabriq.service;

import com.fabriq.fabriq.model.Produk;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProdukService {

    private final List<Produk> data = new ArrayList<>();

    public ProdukService() {
        // Data dummy awal. Nanti diganti data dari database.
        data.add(new Produk("PR-001", "Kemeja Formal Pria", "Kemeja", "M", "Putih", 40, 185000, "Aktif"));
        data.add(new Produk("PR-002", "Kaos Polos Premium", "Kaos", "L", "Hitam", 120, 85000, "Aktif"));
        data.add(new Produk("PR-003", "Celana Chino Slim Fit", "Celana", "L", "Krem", 35, 225000, "Aktif"));
        data.add(new Produk("PR-004", "Jaket Denim Oversize", "Jaket", "XL", "Biru", 18, 350000, "Aktif"));
        data.add(new Produk("PR-005", "Dress Linen Wanita", "Dress", "M", "Sage", 0, 275000, "Nonaktif"));
    }

    public List<Produk> getAll() {
        return new ArrayList<>(data);
    }

    // Jumlah produk berstatus Aktif (dipakai kartu Dashboard)
    public int countActive() {
        int total = 0;
        for (Produk item : data) {
            if (item.isAktif()) {
                total++;
            }
        }
        return total;
    }

    // Cari di kode, nama, kategori, ukuran, warna, dan status
    public List<Produk> search(String keyword) {
        String key = keyword.trim().toLowerCase();
        if (key.isEmpty()) {
            return getAll();
        }

        List<Produk> result = new ArrayList<>();
        for (Produk item : data) {
            if (item.getKode().toLowerCase().contains(key)
                    || item.getNama().toLowerCase().contains(key)
                    || item.getKategori().toLowerCase().contains(key)
                    || item.getUkuran().toLowerCase().contains(key)
                    || item.getWarna().toLowerCase().contains(key)
                    || item.getStatus().toLowerCase().contains(key)) {
                result.add(item);
            }
        }
        return result;
    }

    public Optional<Produk> findByKode(String kode) {
        for (Produk item : data) {
            if (item.getKode().equals(kode)) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    public String generateKode() {
        int max = 0;
        for (Produk item : data) {
            try {
                max = Math.max(max, Integer.parseInt(item.getKode().substring(3)));
            } catch (Exception ignored) {
            }
        }
        return String.format("PR-%03d", max + 1);
    }

    public void add(Produk item) {
        data.add(item);
    }

    public void update(Produk updated) {
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
    
    // Nama produk berstatus Aktif (dipakai pilihan produk di halaman Produksi)
    public List<String> getActiveNames() {
        List<String> names = new ArrayList<>();
        for (Produk item : data) {
            if (item.isAktif()) {
                names.add(item.getNama());
            }
        }
        return names;
    }
    
        // Produk yang boleh dijual: berstatus Aktif dan stok lebih dari 0
    public List<Produk> getSellable() {
        List<Produk> result = new ArrayList<>();
        for (Produk item : data) {
            if (item.isAktif() && item.getStok() > 0) {
                result.add(item);
            }
        }
        return result;
    }

    // Menambah (delta positif) atau mengurangi (delta negatif) stok.
    // Mengembalikan false jika produk tidak ada atau stok akan jadi negatif.
    public boolean adjustStock(String kode, int delta) {
        for (int i = 0; i < data.size(); i++) {
            Produk item = data.get(i);
            if (item.getKode().equals(kode)) {
                int newStock = item.getStok() + delta;
                if (newStock < 0) {
                    return false;
                }
                data.set(i, new Produk(item.getKode(), item.getNama(), item.getKategori(),
                    item.getUkuran(), item.getWarna(), newStock, item.getHarga(), item.getStatus()));
                return true;
            }
        }
        return false;
    }
}