package com.fabriq.fabriq.model;

public class BahanBaku {

    private final String kode;
    private final String nama;
    private final String kategori;
    private final String satuan;
    private final double stok;
    private final double harga;

    public BahanBaku(String kode, String nama, String kategori,
                     String satuan, double stok, double harga) {
        this.kode = kode;
        this.nama = nama;
        this.kategori = kategori;
        this.satuan = satuan;
        this.stok = stok;
        this.harga = harga;
    }

    public String getKode() {
        return kode;
    }

    public String getNama() {
        return nama;
    }

    public String getKategori() {
        return kategori;
    }

    public String getSatuan() {
        return satuan;
    }

    public double getStok() {
        return stok;
    }

    public double getHarga() {
        return harga;
    }
}