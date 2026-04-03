package com.mycompany.citycare;

import javax.swing.*;
import java.awt.*;

public class UserProfilePanel extends JPanel {

    private final User user;
    private final Runnable onLogout; // callback to CityCareApp

    private JTextField usernameField;
    private JTextField roleField;
    private JTextField fullNameField;
    private JTextField addressField;
    private JTextField phoneField;
    private JCheckBox emailNotifCheck;
    private JCheckBox smsNotifCheck;
    private JButton saveBtn;
    private JButton logoutBtn;

    public UserProfilePanel(User user, Runnable onLogout) {
        this.user = user;
        this.onLogout = onLogout;
        initComponents();
        layoutComponents();
        loadFromUser();
    }

    private void initComponents() {
        usernameField = new JTextField(15);
        usernameField.setEditable(false);

        roleField = new JTextField(10);
        roleField.setEditable(false);

        fullNameField = new JTextField(20);
        addressField = new JTextField(25);
        phoneField = new JTextField(15);

        emailNotifCheck = new JCheckBox("Email notifications");
        smsNotifCheck = new JCheckBox("SMS notifications");

        saveBtn = new JButton("Save Profile");
        logoutBtn = new JButton("Logout");

        saveBtn.addActionListener(e -> saveToUser());
        logoutBtn.addActionListener(e -> doLogout());
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        form.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        form.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        form.add(roleField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        form.add(new JLabel("Full name:"), gbc);
        gbc.gridx = 1;
        form.add(fullNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        form.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        form.add(addressField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        form.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        form.add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        form.add(new JLabel("Notifications:"), gbc);
        gbc.gridx = 1;
        JPanel notifPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        notifPanel.add(emailNotifCheck);
        notifPanel.add(smsNotifCheck);
        form.add(notifPanel, gbc);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(saveBtn);
        bottom.add(logoutBtn);

        add(form, BorderLayout.NORTH);
        add(bottom, BorderLayout.SOUTH);
    }

    private void loadFromUser() {
        usernameField.setText(user.getUsername());
        roleField.setText(user.getRole().name());
        fullNameField.setText(user.getFullName());
        addressField.setText(user.getAddress());
        phoneField.setText(user.getPhone());
        emailNotifCheck.setSelected(user.isEmailNotifications());
        smsNotifCheck.setSelected(user.isSmsNotifications());
    }

    private void saveToUser() {
        user.setFullName(fullNameField.getText().trim());
        user.setAddress(addressField.getText().trim());
        user.setPhone(phoneField.getText().trim());
        user.setEmailNotifications(emailNotifCheck.isSelected());
        user.setSmsNotifications(smsNotifCheck.isSelected());

        JOptionPane.showMessageDialog(
                this,
                "Profile updated.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void doLogout() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION
        );
        if (choice == JOptionPane.YES_OPTION && onLogout != null) {
            onLogout.run();
        }
    }
}
