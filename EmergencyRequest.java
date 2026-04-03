package com.mycompany.citycare;

import java.time.LocalDateTime;

public class EmergencyRequest {
    private static long nextId = 1;

    private final long id;
    private String citizenName;
    private String contact;
    private String type;      // Ambulance, Fire, Police, Gas/Electricity
    private String location;
    private String description;
    private LocalDateTime createdAt;
    private String status;    // New, Dispatched, Resolved

    public EmergencyRequest(String citizenName, String contact,
                            String type, String location, String description) {
        this.id = nextId++;
        this.citizenName = citizenName;
        this.contact = contact;
        this.type = type;
        this.location = location;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.status = "New";
    }

    public long getId() { return id; }
    public String getCitizenName() { return citizenName; }
    public String getContact() { return contact; }
    public String getType() { return type; }
    public String getLocation() { return location; }
    public String getDescription() { return description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
