package com.mycompany.citycare;

public class Hospital {
    private static long nextId = 1;

    private final long id;
    private String name;
    private String address;
    private String phone;
    private boolean emergencyAvailable;

    public Hospital(String name, String address, String phone, boolean emergencyAvailable) {
        this.id = nextId++;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.emergencyAvailable = emergencyAvailable;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public boolean isEmergencyAvailable() { return emergencyAvailable; }

    public void setName(String name) { this.name = name; }
    public void setAddress(String address) { this.address = address; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmergencyAvailable(boolean emergencyAvailable) {
        this.emergencyAvailable = emergencyAvailable;
    }
}
