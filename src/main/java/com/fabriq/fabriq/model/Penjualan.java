package com.fabriq.fabriq.model;

import java.time.LocalDate;

public class Penjualan {

    public static final String STATUS_SELESAI = "Selesai";
    public static final String STATUS_DIBATALKAN = "Dibatalkan";

    private final String kode;
    private final LocalDate tanggal;
    private final String pelanggan;
    private final String produkKode;
    private final String produkNama;
    private final int jumlah;
    private final double harga;
    private final String status;

    public Penjualan(String kode, LocalDate tanggal, String pelanggan, String produkKode,
                     String produkNama, int jumlah, double harga, String status) {
        this.kode = kode;
        this.tanggal = tanggal;
        this.pelanggan = pelanggan;
        this.produkKode = produkKode;
        this.produkNama = produkNama;
        this.jumlah = jumlah;
        this.harga = harga;
        this.status = status;
    }

    public String getKode() {
        return kode;
    }

    public LocalDate getTanggal() {
        return tanggal;
    }

    public String getPelanggan() {
        return pelanggan;
    }

    public String getProdukKode() {
        return produkKode;
    }

    public String getProdukNama() {
        return produkNama;
    }

    public int getJumlah() {
        return jumlah;
    }

    public double getHarga() {
        return harga;
    }

    public double getTotal() {
        return jumlah * harga;
    }

    public String getStatus() {
        return status;
    }

    public boolean isSelesai() {
        return STATUS_SELESAI.equals(status);
    }

    // Salinan transaksi ini dengan status Dibatalkan
    public Penjualan batalkan() {
        return new Penjualan(kode, tanggal, pelanggan, produkKode, produkNama,
            jumlah, harga, STATUS_DIBATALKAN);
    }
}