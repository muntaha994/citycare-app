package com.mycompany.citycare;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ComplaintService {

    private final List<Complaint> complaints = new ArrayList<>();
    private final String complaintFilePath = "complaints.csv";

    public ComplaintService() {
        loadFromFile();
    }

    public Complaint addComplaint(String name, String contact,
                                  String category, String description) {
        Complaint c = new Complaint(name, contact, category, description);
        complaints.add(c);
        saveToFile();
        return c;
    }

    public List<Complaint> getAllComplaints() {
        return Collections.unmodifiableList(complaints);
    }

    public List<Complaint> findByCategory(String category) {
        return complaints.stream()
                .filter(c -> c.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    public Complaint findById(long id) {
        return complaints.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void updateStatus(long id, String status) {
        Complaint c = findById(id);
        if (c != null) {
            c.setStatus(status);
            saveToFile();
        }
    }

    public void deleteById(long id) {
        boolean removed = complaints.removeIf(c -> c.getId() == id);
        if (removed) {
            saveToFile();
        }
    }

    private void loadFromFile() {
        File f = new File(complaintFilePath);
        if (!f.exists()) {
            return;
        }
        complaints.clear();
        long maxId = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                // id;name;contact;category;description;createdAt;status
                String[] parts = line.split(";", -1);
                if (parts.length < 7) continue;

                long id = Long.parseLong(parts[0]);
                String name = parts[1];
                String contact = parts[2];
                String category = parts[3];
                String description = parts[4];
                LocalDateTime createdAt = LocalDateTime.parse(parts[5]);
                String status = parts[6];

                Complaint c = new Complaint(id, name, contact, category, description, createdAt, status);
                complaints.add(c);
                if (id > maxId) {
                    maxId = id;
                }
            }
            Complaint.updateNextId(maxId + 1);
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
        }
    }

    private void saveToFile() {
        try (PrintWriter out = new PrintWriter(new FileWriter(complaintFilePath))) {
            for (Complaint c : complaints) {
                out.print(c.getId());
                out.print(";");
                out.print(nullSafe(c.getCitizenName()));
                out.print(";");
                out.print(nullSafe(c.getContact()));
                out.print(";");
                out.print(nullSafe(c.getCategory()));
                out.print(";");
                out.print(nullSafe(c.getDescription()));
                out.print(";");
                out.print(c.getCreatedAt());
                out.print(";");
                out.print(nullSafe(c.getStatus()));
                out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String nullSafe(String s) {
        return s == null ? "" : s.replace(";", ",");
    }
}
