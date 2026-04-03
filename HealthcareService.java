package com.mycompany.citycare;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HealthcareService {

    // ---- Blood bank ----
    private final List<BloodBankEntry> bloodEntries = new ArrayList<>();
    private long nextBloodId = 1;
    private final File bloodFile = new File("bloodbank.dat");

    public HealthcareService() {
        loadBloodFromFile();
    }

    // ADMIN: add a blood entry
    public synchronized BloodBankEntry addBloodEntry(String bloodGroup, int units) {
        BloodBankEntry entry = new BloodBankEntry(nextBloodId++, bloodGroup, units);
        bloodEntries.add(entry);
        saveBloodToFile();
        return entry;
    }

    // ADMIN: delete a blood entry by id
    public synchronized void deleteBloodEntry(long id) {
        bloodEntries.removeIf(e -> e.getId() == id);
        saveBloodToFile();
    }

    // USER: book blood units; returns true if successful
    public synchronized boolean bookBlood(long id, int unitsToBook) {
        for (BloodBankEntry e : bloodEntries) {
            if (e.getId() == id) {
                if (unitsToBook <= 0) {
                    return false;
                }
                if (e.getUnitsAvailable() >= unitsToBook) {
                    e.setUnitsAvailable(e.getUnitsAvailable() - unitsToBook);
                    saveBloodToFile();
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public synchronized List<BloodBankEntry> getAllBloodEntries() {
        return new ArrayList<>(bloodEntries);
    }

    // ---- file persistence ----

    @SuppressWarnings("unchecked")
    private void loadBloodFromFile() {
        if (!bloodFile.exists()) {
            return;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(bloodFile))) {
            Object obj = in.readObject();
            if (obj instanceof List) {
                bloodEntries.clear();
                bloodEntries.addAll((List<BloodBankEntry>) obj);
                nextBloodId = bloodEntries.stream()
                        .mapToLong(BloodBankEntry::getId)
                        .max()
                        .orElse(0L) + 1;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveBloodToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(bloodFile))) {
            out.writeObject(new ArrayList<>(bloodEntries));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
