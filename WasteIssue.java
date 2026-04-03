package com.mycompany.citycare;

import java.time.LocalDateTime;

public class WasteIssue {
    private static long nextId = 1;

    private final long id;
    private String citizenName;
    private String contact;
    private String location;
    private String type;      // Missed pickup, Overflowing bin, Dumping, etc.
    private String description;
    private LocalDateTime createdAt;
    private String status;    // Open, In progress, Resolved

    public WasteIssue(String citizenName, String contact,
                      String location, String type, String description) {
        this.id = nextId++;
        this.citizenName = citizenName;
        this.contact = contact;
        this.location = location;
        this.type = type;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.status = "Open";
    }

    public long getId() { return id; }
    public String getCitizenName() { return citizenName; }
    public String getContact() { return contact; }
    public String getLocation() { return location; }
    public String getType() { return type; }
    public String getDescription() { return description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
