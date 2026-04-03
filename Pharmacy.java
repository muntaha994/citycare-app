package com.mycompany.citycare;

public class Pharmacy {
    private static long nextId = 1;

    private final long id;
    private String name;
    private String address;
    private String phone;
    private boolean open24h;

    public Pharmacy(String name, String address, String phone, boolean open24h) {
        this.id = nextId++;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.open24h = open24h;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public boolean isOpen24h() { return open24h; }

    public void setName(String name) { this.name = name; }
    public void setAddress(String address) { this.address = address; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setOpen24h(boolean open24h) { this.open24h = open24h; }
}
