package com.mycompany.citycare;

public class User {

    public enum Role { ADMIN, CITIZEN }

    private String username;
    private String password;
    private Role role;

    private String fullName;
    private String address;
    private String phone;              // expect 11 digits
    private boolean emailNotifications;
    private boolean smsNotifications;

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullName = username;
        this.address = "";
        this.phone = "";
        this.emailNotifications = true;
        this.smsNotifications = false;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isEmailNotifications() {
        return emailNotifications;
    }

    public boolean isSmsNotifications() {
        return smsNotifications;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmailNotifications(boolean emailNotifications) {
        this.emailNotifications = emailNotifications;
    }

    public void setSmsNotifications(boolean smsNotifications) {
        this.smsNotifications = smsNotifications;
    }
}
