package com.mycompany.citycare;

import java.io.*;
import java.util.*;

public class UserService {

    private final Map<String, User> users = new HashMap<>();
    private final String userFilePath = "users.csv";

    public UserService() {
        if (!loadFromFile()) {
            createDefaultUsers();
            saveToFile();
        }
    }

    private void createDefaultUsers() {
        addUser(new User("admin", "admin123", User.Role.ADMIN));
        addUser(new User("user", "user123", User.Role.CITIZEN));
    }

    public User authenticate(String username, String password) {
        User u = users.get(username);
        if (u != null && u.getPassword().equals(password)) {
            return u;
        }
        return null;
    }

    public boolean usernameExists(String username) {
        return users.containsKey(username);
    }

    public void addUser(User user) {
        users.put(user.getUsername(), user);
        saveToFile();
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public List<String> getAllUsernames() {
        return new ArrayList<>(users.keySet());
    }

    // username;password;role;fullName;address;phone;emailNotif;smsNotif
    private boolean loadFromFile() {
        File f = new File(userFilePath);
        if (!f.exists()) {
            return false;
        }
        users.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";", -1);
                if (parts.length < 7) continue;

                String username = parts[0];
                String password = parts[1];
                User.Role role = User.Role.valueOf(parts[2]);

                User u = new User(username, password, role);
                u.setFullName(parts[3]);
                u.setAddress(parts[4]);
                u.setPhone(parts[5]);
                u.setEmailNotifications(Boolean.parseBoolean(parts[6]));
                if (parts.length > 7) {
                    u.setSmsNotifications(Boolean.parseBoolean(parts[7]));
                }

                users.put(username, u);
            }
            return true;
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void saveToFile() {
        try (PrintWriter out = new PrintWriter(new FileWriter(userFilePath))) {
            for (User u : users.values()) {
                out.print(u.getUsername());
                out.print(";");
                out.print(u.getPassword());
                out.print(";");
                out.print(u.getRole().name());
                out.print(";");
                out.print(nullSafe(u.getFullName()));
                out.print(";");
                out.print(nullSafe(u.getAddress()));
                out.print(";");
                out.print(nullSafe(u.getPhone()));
                out.print(";");
                out.print(Boolean.toString(u.isEmailNotifications()));
                out.print(";");
                out.print(Boolean.toString(u.isSmsNotifications()));
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
