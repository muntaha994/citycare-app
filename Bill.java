package com.mycompany.citycare;

import java.io.Serializable;

public class Bill implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    private String username;     // owner of this bill
    private String description;
    private double amount;
    private double paid;
    private boolean completed;

    public Bill(long id, String username, String description,
                double amount, double paid, boolean completed) {
        this.id = id;
        this.username = username;
        this.description = description;
        this.amount = amount;
        this.paid = paid;
        this.completed = completed;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public double getPaid() {
        return paid;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setPaid(double paid) {
        this.paid = paid;
        if (this.paid >= this.amount) {
            this.completed = true;
            this.paid = this.amount;
        }
    }

    public double getDue() {
        return amount - paid;
    }
}
