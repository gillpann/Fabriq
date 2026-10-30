package com.fabriq.fabriq.model;

public class DashboardSummary {

    private final int totalBahanBaku;
    private final int totalProduk;
    private final int produksiAktif;
    private final long penjualanBulanIni;

    public DashboardSummary(int totalBahanBaku, int totalProduk,
                            int produksiAktif, long penjualanBulanIni) {
        this.totalBahanBaku = totalBahanBaku;
        this.totalProduk = totalProduk;
        this.produksiAktif = produksiAktif;
        this.penjualanBulanIni = penjualanBulanIni;
    }

    public int getTotalBahanBaku() {
        return totalBahanBaku;
    }

    public int getTotalProduk() {
        return totalProduk;
    }

    public int getProduksiAktif() {
        return produksiAktif;
    }

    public long getPenjualanBulanIni() {
        return penjualanBulanIni;
    }
}