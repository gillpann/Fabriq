package com.fabriq.fabriq;

import com.fabriq.fabriq.controller.LoginController;
import com.fabriq.fabriq.service.AppServices;
import com.fabriq.fabriq.view.LoginFrame;

import javax.swing.SwingUtilities;

public class Fabriq {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppServices services = new AppServices(); 

            LoginFrame view = new LoginFrame();
            new LoginController(view, services);
            view.setVisible(true);
        });
    }
}