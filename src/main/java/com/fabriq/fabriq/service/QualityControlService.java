package com.fabriq.fabriq.service;

import com.fabriq.fabriq.model.QualityControl;
import com.fabriq.fabriq.util.DateUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QualityControlService {

    private final List<QualityControl> data = new ArrayList<>();

    public QualityControlService() {
        // Data dummy awal. Nanti diganti data dari database.
        data.add(new QualityControl("QC-001", "PRD-001", "Kemeja Formal Pria",
            LocalDate.of(2026, 10, 21), 200, 192, "Andi Pratama",
            QualityControl.HASIL_LOLOS, "8 pcs jahitan lepas, sudah diperbaiki"));
        data.add(new QualityControl("QC-002", "PRD-002", "Kaos Polos Premium",
            LocalDate.of(2026, 10, 20), 100, 91, "Andi Pratama",
            QualityControl.HASIL_PERBAIKAN, "Warna tidak seragam pada 9 pcs"));
        data.add(new QualityControl("QC-003", "PRD-003", "Celana Chino Slim Fit",
            LocalDate.of(2026, 10, 27), 50, 48, "Andi Pratama",
            QualityControl.HASIL_LOLOS, ""));
    }

    public List<QualityControl> getAll() {
        return new ArrayList<>(data);
    }

    // Cari di no. QC, batch, produk, inspektur, hasil, catatan, dan tanggal (dd-MM-yyyy)
    public List<QualityControl> search(String keyword) {
        String key = keyword.trim().toLowerCase();
        if (key.isEmpty()) {
            return getAll();
        }

        List<QualityControl> result = new ArrayList<>();
        for (QualityControl item : data) {
            if (item.getKode().toLowerCase().contains(key)
                    || item.getBatch().toLowerCase().contains(key)
                    || item.getProduk().toLowerCase().contains(key)
                    || item.getInspektur().toLowerCase().contains(key)
                    || item.getHasil().toLowerCase().contains(key)
                    || item.getCatatan().toLowerCase().contains(key)
                    || DateUtil.format(item.getTanggal()).contains(key)) {
                result.add(item);
            }
        }
        return result;
    }

    public Optional<QualityControl> findByKode(String kode) {
        for (QualityControl item : data) {
            if (item.getKode().equals(kode)) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    public String generateKode() {
        int max = 0;
        for (QualityControl item : data) {
            try {
                max = Math.max(max, Integer.parseInt(item.getKode().substring(3)));
            } catch (Exception ignored) {
            }
        }
        return String.format("QC-%03d", max + 1);
    }

    public void add(QualityControl item) {
        data.add(item);
    }

    public void update(QualityControl updated) {
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