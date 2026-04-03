package com.mycompany.citycare;

import java.time.LocalDateTime;

public class SecurityAlert {
    private static long nextId = 1;

    private final long id;
    private String title;
    private String message;
    private LocalDateTime createdAt;
    private String level;   // Info, Warning, Critical

    public SecurityAlert(String title, String message, String level) {
        this.id = nextId++;
        this.title = title;
        this.message = message;
        this.level = level;
        this.createdAt = LocalDateTime.now();
    }

    public long getId() { return id; }
    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getLevel() { return level; }

    public void setTitle(String title) { this.title = title; }
    public void setMessage(String message) { this.message = message; }
    public void setLevel(String level) { this.level = level; }
}
