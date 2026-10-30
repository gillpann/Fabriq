package com.fabriq.fabriq.service;

public class AppServices {

    private final PenggunaService penggunaService = new PenggunaService();
    private final BahanBakuService bahanBakuService = new BahanBakuService();
    private final ProdukService produkService = new ProdukService();
    private final ProduksiService produksiService = new ProduksiService();
    private final QualityControlService qualityControlService = new QualityControlService();
    private final PenjualanService penjualanService = new PenjualanService();

    private final AuthService authService = new AuthService(penggunaService);
    private final DashboardService dashboardService = new DashboardService(
        bahanBakuService, produkService, produksiService, penjualanService);

    public PenggunaService getPenggunaService() {
        return penggunaService;
    }

    public BahanBakuService getBahanBakuService() {
        return bahanBakuService;
    }

    public ProdukService getProdukService() {
        return produkService;
    }

    public ProduksiService getProduksiService() {
        return produksiService;
    }

    public QualityControlService getQualityControlService() {
        return qualityControlService;
    }

    public PenjualanService getPenjualanService() {
        return penjualanService;
    }

    public AuthService getAuthService() {
        return authService;
    }

    public DashboardService getDashboardService() {
        return dashboardService;
    }
}