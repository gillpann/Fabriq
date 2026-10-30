package com.fabriq.fabriq.service;

import com.fabriq.fabriq.model.Penjualan;
import com.fabriq.fabriq.util.DateUtil;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PenjualanService {

    private final List<Penjualan> data = new ArrayList<>();

    public PenjualanService() {
        // Data dummy awal (riwayat). Nanti diganti data dari database.
        data.add(new Penjualan("PJ-001", LocalDate.of(2026, 10, 18), "Toko Maju",
            "PR-001", "Kemeja Formal Pria", 30, 185000, Penjualan.STATUS_SELESAI));
        data.add(new Penjualan("PJ-002", LocalDate.of(2026, 10, 22), "Toko Maju",
            "PR-002", "Kaos Polos Premium", 50, 85000, Penjualan.STATUS_SELESAI));
        data.add(new Penjualan("PJ-003", LocalDate.of(2026, 10, 25), "CV Busana Jaya",
            "PR-003", "Celana Chino Slim Fit", 20, 225000, Penjualan.STATUS_SELESAI));
        data.add(new Penjualan("PJ-004", LocalDate.of(2026, 10, 26), "Butik Anggun",
            "PR-004", "Jaket Denim Oversize", 5, 350000, Penjualan.STATUS_DIBATALKAN));
    }

    public List<Penjualan> getAll() {
        return new ArrayList<>(data);
    }

    // Total penjualan berstatus Selesai pada bulan berjalan (dipakai kartu Dashboard)
    public long totalBulanIni() {
        YearMonth now = YearMonth.now();
        double sum = 0;
        for (Penjualan item : data) {
            if (item.isSelesai() && YearMonth.from(item.getTanggal()).equals(now)) {
                sum += item.getTotal();
            }
        }
        return Math.round(sum);
    }

    // Cari di no. penjualan, pelanggan, produk, status, dan tanggal (dd-MM-yyyy)
    public List<Penjualan> search(String keyword) {
        String key = keyword.trim().toLowerCase();
        if (key.isEmpty()) {
            return getAll();
        }

        List<Penjualan> result = new ArrayList<>();
        for (Penjualan item : data) {
            if (item.getKode().toLowerCase().contains(key)
                    || item.getPelanggan().toLowerCase().contains(key)
                    || item.getProdukNama().toLowerCase().contains(key)
                    || item.getStatus().toLowerCase().contains(key)
                    || DateUtil.format(item.getTanggal()).contains(key)) {
                result.add(item);
            }
        }
        return result;
    }

    public Optional<Penjualan> findByKode(String kode) {
        for (Penjualan item : data) {
            if (item.getKode().equals(kode)) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    public String generateKode() {
        int max = 0;
        for (Penjualan item : data) {
            try {
                max = Math.max(max, Integer.parseInt(item.getKode().substring(3)));
            } catch (Exception ignored) {
            }
        }
        return String.format("PJ-%03d", max + 1);
    }

    public void add(Penjualan item) {
        data.add(item);
    }

    public void update(Penjualan updated) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getKode().equals(updated.getKode())) {
                data.set(i, updated);
                return;
            }
        }
    }
}