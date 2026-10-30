package com.fabriq.fabriq.model;

public class Pengguna {

    public static final String STATUS_AKTIF = "Aktif";
    public static final String STATUS_NONAKTIF = "Nonaktif";

    public static final String[] ROLES = {"Admin", "Produksi", "Quality Control", "Penjualan"};
    public static final String[] STATUSES = {STATUS_AKTIF, STATUS_NONAKTIF};

    private final String kode;
    private final String username;
    private final String namaLengkap;
    private final String role;
    private final String status;
    private final String password;

    public Pengguna(String kode, String username, String namaLengkap,
                    String role, String status, String password) {
        this.kode = kode;
        this.username = username;
        this.namaLengkap = namaLengkap;
        this.role = role;
        this.status = status;
        this.password = password;
    }

    public String getKode() {
        return kode;
    }

    public String getUsername() {
        return username;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public String getRole() {
        return role;
    }

    public String getStatus() {
        return status;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAktif() {
        return STATUS_AKTIF.equals(status);
    }
}