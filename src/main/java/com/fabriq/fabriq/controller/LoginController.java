package com.fabriq.fabriq.controller;

import com.fabriq.fabriq.model.User;
import com.fabriq.fabriq.service.AppServices;
import com.fabriq.fabriq.service.AuthService;
import com.fabriq.fabriq.view.DashboardFrame;
import com.fabriq.fabriq.view.LoginFrame;

public class LoginController {

    private final LoginFrame view;
    private final AppServices services;

    public LoginController(LoginFrame view, AppServices services) {
        this.view = view;
        this.services = services;
        this.view.addLoginListener(e -> handleLogin());
    }

    private void handleLogin() {
        String username = view.getUsername();
        String password = view.getPassword();

        if (username.isEmpty() || password.isEmpty()) {
            view.showWarning("Username dan password wajib diisi.");
            return;
        }

        AuthService.Result result = services.getAuthService().authenticate(username, password);

        if (result.getStatus() == AuthService.Status.SUCCESS) {
            openDashboard(result.getUser());
        } else if (result.getStatus() == AuthService.Status.INACTIVE) {
            view.showError("Akun ini nonaktif. Hubungi administrator.");
            view.clearPassword();
        } else {
            view.showError("Username atau password salah.");
            view.clearPassword();
        }
    }

    private void openDashboard(User user) {
        view.dispose();

        DashboardFrame dashboard = new DashboardFrame();
        new DashboardController(dashboard, user, services);
        dashboard.setVisible(true);
    }
}