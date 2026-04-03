package com.mycompany.citycare;

public class GuardContact {
    private static long nextId = 1;

    private final long id;
    private String name;
    private String phone;
    private String post;   // Gate, Block A, etc.

    public GuardContact(String name, String phone, String post) {
        this.id = nextId++;
        this.name = name;
        this.phone = phone;
        this.post = post;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getPost() { return post; }

    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setPost(String post) { this.post = post; }
}
