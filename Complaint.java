package com.mycompany.citycare;

import java.time.LocalDateTime;

public class Complaint {

    private static long nextId = 1;

    private final long id;
    private String citizenName;
    private String contact;
    private String category;
    private String description;
    private LocalDateTime createdAt;
    private String status;

    public Complaint(String citizenName, String contact,
                     String category, String description) {
        this.id = nextId++;
        this.citizenName = citizenName;
        this.contact = contact;
        this.category = category;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.status = "Open";
    }

    public Complaint(long id, String citizenName, String contact,
                     String category, String description,
                     LocalDateTime createdAt, String status) {
        this.id = id;
        this.citizenName = citizenName;
        this.contact = contact;
        this.category = category;
        this.description = description;
        this.createdAt = createdAt;
        this.status = status;
        if (id >= nextId) {
            nextId = id + 1;
        }
    }

    public static void updateNextId(long next) {
        if (next > nextId) {
            nextId = next;
        }
    }

    public long getId() {
        return id;
    }

    public String getCitizenName() {
        return citizenName;
    }

    public String getContact() {
        return contact;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setCitizenName(String citizenName) {
        this.citizenName = citizenName;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
