package com.mycompany.citycare;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BillingService {

    private final List<Bill> bills = new ArrayList<>();
    private long nextId = 1;
    private final File file = new File("bills.dat");

    public BillingService() {
        loadFromFile();
    }

    public synchronized Bill addBill(String username,
                                     String description,
                                     double amount,
                                     double paid) {
        Bill bill = new Bill(nextId++, username, description, amount, paid, paid >= amount);
        bills.add(bill);
        saveToFile();
        return bill;
    }

    public synchronized List<Bill> getAllBills() {
        return new ArrayList<>(bills);
    }

    public synchronized List<Bill> getBillsForUser(String username) {
        return bills.stream()
                .filter(b -> b.getUsername().equals(username))
                .collect(Collectors.toList());
    }

    public synchronized void payBill(long id, double amount) {
        for (Bill b : bills) {
            if (b.getId() == id) {
                b.setPaid(b.getPaid() + amount);
                break;
            }
        }
        saveToFile();
    }

    public synchronized void deleteBill(long id) {
        bills.removeIf(b -> b.getId() == id);
        saveToFile();
    }

    public synchronized double getTotalAmountForUser(String username) {
        return bills.stream()
                .filter(b -> b.getUsername().equals(username))
                .mapToDouble(Bill::getAmount)
                .sum();
    }

    public synchronized double getTotalPaidForUser(String username) {
        return bills.stream()
                .filter(b -> b.getUsername().equals(username))
                .mapToDouble(Bill::getPaid)
                .sum();
    }

    public synchronized double getTotalDueForUser(String username) {
        return bills.stream()
                .filter(b -> b.getUsername().equals(username))
                .mapToDouble(Bill::getDue)
                .sum();
    }

    @SuppressWarnings("unchecked")
    private void loadFromFile() {
        if (!file.exists()) {
            return;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = in.readObject();
            if (obj instanceof List) {
                bills.clear();
                bills.addAll((List<Bill>) obj);
                nextId = bills.stream()
                        .mapToLong(Bill::getId)
                        .max()
                        .orElse(0L) + 1;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(new ArrayList<>(bills));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
