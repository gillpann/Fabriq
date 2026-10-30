package com.fabriq.fabriq.model;

import java.time.LocalDate;

public class Produksi {

    public static final String STATUS_DIRENCANAKAN = "Direncanakan";
    public static final String STATUS_BERJALAN = "Berjalan";
    public static final String STATUS_SELESAI = "Selesai";
    public static final String STATUS_DIBATALKAN = "Dibatalkan";

    public static final String[] STATUSES = {
        STATUS_DIRENCANAKAN, STATUS_BERJALAN, STATUS_SELESAI, STATUS_DIBATALKAN
    };

    private final String kode;
    private final String produk;
    private final int jumlah;
    private final LocalDate mulai;
    private final LocalDate target;
    private final String penanggungJawab;
    private final String status;

    public Produksi(String kode, String produk, int jumlah, LocalDate mulai,
                    LocalDate target, String penanggungJawab, String status) {
        this.kode = kode;
        this.produk = produk;
        this.jumlah = jumlah;
        this.mulai = mulai;
        this.target = target;
        this.penanggungJawab = penanggungJawab;
        this.status = status;
    }

    public String getKode() {
        return kode;
    }

    public String getProduk() {
        return produk;
    }

    public int getJumlah() {
        return jumlah;
    }

    public LocalDate getMulai() {
        return mulai;
    }

    public LocalDate getTarget() {
        return target;
    }

    public String getPenanggungJawab() {
        return penanggungJawab;
    }

    public String getStatus() {
        return status;
    }

    public boolean isSelesai() {
        return STATUS_SELESAI.equals(status);
    }

    public boolean isBerjalan() {
        return STATUS_BERJALAN.equals(status);
    }
}