package com.fabriq.fabriq.controller;

import com.fabriq.fabriq.model.User;
import com.fabriq.fabriq.service.AppServices;
import com.fabriq.fabriq.view.BahanBakuPage;
import com.fabriq.fabriq.view.DashboardFrame;
import com.fabriq.fabriq.view.DashboardPage;
import com.fabriq.fabriq.view.LoginFrame;
import com.fabriq.fabriq.view.PenggunaPage;
import com.fabriq.fabriq.view.ProdukPage;
import com.fabriq.fabriq.view.ProduksiPage;
import com.fabriq.fabriq.view.QualityControlPage;
import com.fabriq.fabriq.view.PenjualanPage;

public class DashboardController {

    private final DashboardFrame view;
    private final AppServices services;
    private final DashboardPage dashboardPage = new DashboardPage();

    public DashboardController(DashboardFrame view, User user, AppServices services) {
        this.view = view;
        this.services = services;

        // Dashboard
        dashboardPage.setUserName(user.getFullName());
        view.addPage("Dashboard", dashboardPage);
        view.setOnShow("Dashboard", this::loadDashboardData);

        // Bahan Baku
        BahanBakuPage bahanBakuPage = new BahanBakuPage();
        new BahanBakuController(bahanBakuPage, services.getBahanBakuService());
        view.addPage("Bahan Baku", bahanBakuPage);

        // Produk
        ProdukPage produkPage = new ProdukPage();
        new ProdukController(produkPage, services.getProdukService());
        view.addPage("Produk", produkPage);

        // Produksi (butuh data Produk dan Pengguna, jadi refresh setiap dibuka)
        ProduksiPage produksiPage = new ProduksiPage();
        ProduksiController produksiController = new ProduksiController(
            produksiPage,
            services.getProduksiService(),
            services.getProdukService(),
            services.getPenggunaService());
        view.addPage("Produksi", produksiPage);
        view.setOnShow("Produksi", produksiController::refresh);

        // Pengguna
        PenggunaPage penggunaPage = new PenggunaPage();
        new PenggunaController(penggunaPage, services.getPenggunaService(), user);
        view.addPage("Pengguna", penggunaPage);
        
        // Quality Control (butuh data Produksi dan Pengguna, jadi refresh setiap dibuka)
        QualityControlPage qcPage = new QualityControlPage();
        QualityControlController qcController = new QualityControlController(
            qcPage,
            services.getQualityControlService(),
            services.getProduksiService(),
            services.getPenggunaService());
        view.addPage("Quality Control", qcPage);
        view.setOnShow("Quality Control", qcController::refresh);
        
        // Penjualan (butuh data Produk, jadi refresh setiap dibuka)
        PenjualanPage penjualanPage = new PenjualanPage();
        PenjualanController penjualanController = new PenjualanController(
            penjualanPage,
            services.getPenjualanService(),
            services.getProdukService());
        view.addPage("Penjualan", penjualanPage);
        view.setOnShow("Penjualan", penjualanController::refresh);

        view.getSidebar().setMenuListener(this::handleMenu);
        view.getSidebar().setLogoutListener(this::handleLogout);

        view.showPage("Dashboard");
    }

    private void loadDashboardData() {
        dashboardPage.setSummary(services.getDashboardService().getSummary());
        dashboardPage.setActivities(services.getDashboardService().getRecentActivities());
    }

    private void handleMenu(String name) {
        if (!view.hasPage(name)) {
            view.showInfo("Halaman " + name + " belum dibuat.");
            return;
        }
        view.showPage(name);
    }

    private void handleLogout() {
        if (view.confirm("Yakin ingin logout?", "Logout")) {
            view.dispose();

            LoginFrame login = new LoginFrame();
            new LoginController(login, services);
            login.setVisible(true);
        }
    }
}