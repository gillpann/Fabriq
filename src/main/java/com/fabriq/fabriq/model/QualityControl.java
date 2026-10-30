package com.fabriq.fabriq.model;

import java.time.LocalDate;

public class QualityControl {

    public static final String HASIL_LOLOS = "Lolos";
    public static final String HASIL_PERBAIKAN = "Perlu Perbaikan";
    public static final String HASIL_DITOLAK = "Ditolak";

    public static final String[] HASILS = {HASIL_LOLOS, HASIL_PERBAIKAN, HASIL_DITOLAK};

    private final String kode;
    private final String batch;
    private final String produk;
    private final LocalDate tanggal;
    private final int diperiksa;
    private final int lolos;
    private final String inspektur;
    private final String hasil;
    private final String catatan;

    public QualityControl(String kode, String batch, String produk, LocalDate tanggal,
                          int diperiksa, int lolos, String inspektur,
                          String hasil, String catatan) {
        this.kode = kode;
        this.batch = batch;
        this.produk = produk;
        this.tanggal = tanggal;
        this.diperiksa = diperiksa;
        this.lolos = lolos;
        this.inspektur = inspektur;
        this.hasil = hasil;
        this.catatan = catatan;
    }

    public String getKode() {
        return kode;
    }

    public String getBatch() {
        return batch;
    }

    public String getProduk() {
        return produk;
    }

    public LocalDate getTanggal() {
        return tanggal;
    }

    public int getDiperiksa() {
        return diperiksa;
    }

    public int getLolos() {
        return lolos;
    }

    public int getCacat() {
        return diperiksa - lolos;
    }

    public String getInspektur() {
        return inspektur;
    }

    public String getHasil() {
        return hasil;
    }

    public String getCatatan() {
        return catatan;
    }
}