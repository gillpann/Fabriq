package com.fabriq.fabriq.model;

public class ActivityLog {

    private final String time;
    private final String description;
    private final String module;
    private final String status;

    public ActivityLog(String time, String description, String module, String status) {
        this.time = time;
        this.description = description;
        this.module = module;
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getModule() {
        return module;
    }

    public String getStatus() {
        return status;
    }
}