package com.fabriq.fabriq.service;

import com.fabriq.fabriq.model.Produksi;
import com.fabriq.fabriq.util.DateUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProduksiService {

    private final List<Produksi> data = new ArrayList<>();

    public ProduksiService() {
        // Data dummy awal. Nanti diganti data dari database.
        data.add(new Produksi("PRD-001", "Kemeja Formal Pria", 200,
            LocalDate.of(2026, 10, 5), LocalDate.of(2026, 10, 20),
            "Sari Wulandari", Produksi.STATUS_SELESAI));
        data.add(new Produksi("PRD-002", "Kaos Polos Premium", 500,
            LocalDate.of(2026, 10, 12), LocalDate.of(2026, 10, 28),
            "Sari Wulandari", Produksi.STATUS_BERJALAN));
        data.add(new Produksi("PRD-003", "Celana Chino Slim Fit", 150,
            LocalDate.of(2026, 10, 20), LocalDate.of(2026, 11, 5),
            "Budi Santoso", Produksi.STATUS_BERJALAN));
        data.add(new Produksi("PRD-004", "Jaket Denim Oversize", 80,
            LocalDate.of(2026, 10, 25), LocalDate.of(2026, 11, 10),
            "Rina Marlina", Produksi.STATUS_DIRENCANAKAN));
        data.add(new Produksi("PRD-005", "Dress Linen Wanita", 60,
            LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 10),
            "Rina Marlina", Produksi.STATUS_DIBATALKAN));
    }

    // Daftar dummy. Nanti diambil dari data Pengguna di database.
    public List<String> getPenanggungJawabList() {
        return List.of("Sari Wulandari", "Budi Santoso", "Rina Marlina");
    }

    public List<Produksi> getAll() {
        return new ArrayList<>(data);
    }

    // Jumlah batch berstatus Berjalan (dipakai kartu Dashboard)
    public int countBerjalan() {
        int total = 0;
        for (Produksi item : data) {
            if (item.isBerjalan()) {
                total++;
            }
        }
        return total;
    }

    // Cari di no. batch, produk, penanggung jawab, status, dan tanggal (dd-MM-yyyy)
    public List<Produksi> search(String keyword) {
        String key = keyword.trim().toLowerCase();
        if (key.isEmpty()) {
            return getAll();
        }

        List<Produksi> result = new ArrayList<>();
        for (Produksi item : data) {
            if (item.getKode().toLowerCase().contains(key)
                    || item.getProduk().toLowerCase().contains(key)
                    || item.getPenanggungJawab().toLowerCase().contains(key)
                    || item.getStatus().toLowerCase().contains(key)
                    || DateUtil.format(item.getMulai()).contains(key)
                    || DateUtil.format(item.getTarget()).contains(key)) {
                result.add(item);
            }
        }
        return result;
    }

    public Optional<Produksi> findByKode(String kode) {
        for (Produksi item : data) {
            if (item.getKode().equals(kode)) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    public String generateKode() {
        int max = 0;
        for (Produksi item : data) {
            try {
                max = Math.max(max, Integer.parseInt(item.getKode().substring(4)));
            } catch (Exception ignored) {
            }
        }
        return String.format("PRD-%03d", max + 1);
    }

    public void add(Produksi item) {
        data.add(item);
    }

    public void update(Produksi updated) {
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
    
        // Batch yang boleh diperiksa QC: berstatus Berjalan atau Selesai
    public List<Produksi> getInspectableBatches() {
        List<Produksi> result = new ArrayList<>();
        for (Produksi item : data) {
            if (item.isBerjalan() || item.isSelesai()) {
                result.add(item);
            }
        }
        return result;
    }
}