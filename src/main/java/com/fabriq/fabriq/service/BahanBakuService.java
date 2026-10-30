package com.fabriq.fabriq.service;

import com.fabriq.fabriq.model.BahanBaku;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BahanBakuService {

    private final List<BahanBaku> data = new ArrayList<>();

    public BahanBakuService() {
        // Data dummy awal. Nanti diganti data dari database.
        data.add(new BahanBaku("BB-001", "Kain Katun Combed 30s", "Kain", "meter", 250, 45000));
        data.add(new BahanBaku("BB-002", "Kain Denim 12oz", "Kain", "meter", 120, 78000));
        data.add(new BahanBaku("BB-003", "Benang Polyester 40/2", "Benang", "cone", 80, 18500));
        data.add(new BahanBaku("BB-004", "Kancing Baju 18L", "Kancing", "lusin", 60, 12000));
        data.add(new BahanBaku("BB-005", "Resleting 20 cm", "Resleting", "pcs", 300, 3500));
    }

    public List<BahanBaku> getAll() {
        return new ArrayList<>(data);
    }

    public int count() {
        return data.size();
    }

    // Cari di kode, nama, kategori, dan satuan
    public List<BahanBaku> search(String keyword) {
        String key = keyword.trim().toLowerCase();
        if (key.isEmpty()) {
            return getAll();
        }

        List<BahanBaku> result = new ArrayList<>();
        for (BahanBaku item : data) {
            if (item.getKode().toLowerCase().contains(key)
                    || item.getNama().toLowerCase().contains(key)
                    || item.getKategori().toLowerCase().contains(key)
                    || item.getSatuan().toLowerCase().contains(key)) {
                result.add(item);
            }
        }
        return result;
    }

    public Optional<BahanBaku> findByKode(String kode) {
        for (BahanBaku item : data) {
            if (item.getKode().equals(kode)) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    public String generateKode() {
        int max = 0;
        for (BahanBaku item : data) {
            try {
                max = Math.max(max, Integer.parseInt(item.getKode().substring(3)));
            } catch (Exception ignored) {
            }
        }
        return String.format("BB-%03d", max + 1);
    }

    public void add(BahanBaku item) {
        data.add(item);
    }

    public void update(BahanBaku updated) {
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