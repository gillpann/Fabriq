package com.fabriq.fabriq.model;

public class Produk {

    private final String kode;
    private final String nama;
    private final String kategori;
    private final String ukuran;
    private final String warna;
    private final int stok;
    private final double harga;
    private final String status;

    public Produk(String kode, String nama, String kategori, String ukuran,
                  String warna, int stok, double harga, String status) {
        this.kode = kode;
        this.nama = nama;
        this.kategori = kategori;
        this.ukuran = ukuran;
        this.warna = warna;
        this.stok = stok;
        this.harga = harga;
        this.status = status;
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

    public String getUkuran() {
        return ukuran;
    }

    public String getWarna() {
        return warna;
    }

    public int getStok() {
        return stok;
    }

    public double getHarga() {
        return harga;
    }

    public String getStatus() {
        return status;
    }

    public boolean isAktif() {
        return "Aktif".equals(status);
    }
}