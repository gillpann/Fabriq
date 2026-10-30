package com.fabriq.fabriq.service;

import com.fabriq.fabriq.model.ActivityLog;
import com.fabriq.fabriq.model.DashboardSummary;

import java.util.List;

public class DashboardService {

    private final BahanBakuService bahanBakuService;
    private final ProdukService produkService;
    private final ProduksiService produksiService;
    private final PenjualanService penjualanService;

    public DashboardService(BahanBakuService bahanBakuService,
                            ProdukService produkService,
                            ProduksiService produksiService,
                            PenjualanService penjualanService) {
        this.bahanBakuService = bahanBakuService;
        this.produkService = produkService;
        this.produksiService = produksiService;
        this.penjualanService = penjualanService;
    }

    public DashboardSummary getSummary() {
        // Semua angka ringkasan sekarang berasal dari data asli
        return new DashboardSummary(
            bahanBakuService.count(),
            produkService.countActive(),
            produksiService.countBerjalan(),
            penjualanService.totalBulanIni());
    }

    public List<ActivityLog> getRecentActivities() {
        return List.of(
            new ActivityLog("09:15", "Stok kain katun masuk 200 m", "Bahan Baku", "Selesai"),
            new ActivityLog("10:02", "Produksi kemeja batch #014 dimulai", "Produksi", "Berjalan"),
            new ActivityLog("11:30", "QC batch #013 lolos inspeksi", "Quality Control", "Selesai"),
            new ActivityLog("13:45", "Penjualan 50 pcs kaos ke Toko Maju", "Penjualan", "Selesai"),
            new ActivityLog("14:20", "Permintaan bahan benang polyester", "Bahan Baku", "Menunggu")
        );
    }
}