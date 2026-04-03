package com.mycompany.citycare;

import java.time.LocalDateTime;

public class Payment {
    private static long nextId = 1;

    private final long id;
    private long billId;
    private String payerName;
    private double amount;
    private LocalDateTime paidAt;
    private String method; // Cash, Card, Mobile, etc.

    public Payment(long billId, String payerName,
                   double amount, String method) {
        this.id = nextId++;
        this.billId = billId;
        this.payerName = payerName;
        this.amount = amount;
        this.method = method;
        this.paidAt = LocalDateTime.now();
    }

    public long getId() { return id; }
    public long getBillId() { return billId; }
    public String getPayerName() { return payerName; }
    public double getAmount() { return amount; }
    public LocalDateTime getPaidAt() { return paidAt; }
    public String getMethod() { return method; }
}
