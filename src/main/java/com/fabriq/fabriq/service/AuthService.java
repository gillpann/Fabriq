package com.fabriq.fabriq.service;

import com.fabriq.fabriq.model.Pengguna;
import com.fabriq.fabriq.model.User;

import java.util.Optional;

public class AuthService {

    public enum Status {
        SUCCESS, WRONG_CREDENTIALS, INACTIVE
    }

    public static class Result {
        private final Status status;
        private final User user;

        private Result(Status status, User user) {
            this.status = status;
            this.user = user;
        }

        public Status getStatus() {
            return status;
        }

        public User getUser() {
            return user;
        }
    }

    private final PenggunaService penggunaService;

    public AuthService(PenggunaService penggunaService) {
        this.penggunaService = penggunaService;
    }

    public Result authenticate(String username, String password) {
        Optional<Pengguna> found = penggunaService.findByCredentials(username, password);

        if (found.isEmpty()) {
            return new Result(Status.WRONG_CREDENTIALS, null);
        }

        Pengguna pengguna = found.get();
        if (!pengguna.isAktif()) {
            return new Result(Status.INACTIVE, null);
        }

        return new Result(Status.SUCCESS,
            new User(pengguna.getUsername(), pengguna.getNamaLengkap(), pengguna.getRole()));
    }
}