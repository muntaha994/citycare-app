package com.mycompany.citycare;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JDialog {

    private final UserService userService;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField fullNameField;
    private JTextField addressField;
    private JTextField phoneField;
    private JComboBox<String> roleBox;
    private JPasswordField adminPassField;
    private JButton registerButton;
    private JButton cancelButton;

    public RegisterFrame(Window owner, UserService userService) {
        super(owner, "User Registration", ModalityType.APPLICATION_MODAL);
        this.userService = userService;
        initComponents();
        layoutComponents();
        initListeners();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        confirmPasswordField = new JPasswordField(15);
        fullNameField = new JTextField(20);
        addressField = new JTextField(25);
        phoneField = new JTextField(11);

        roleBox = new JComboBox<>(new String[]{"CITIZEN", "ADMIN"});
        adminPassField = new JPasswordField(10);

        registerButton = new JButton("Register");
        cancelButton = new JButton("Cancel");
    }

    private void layoutComponents() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        int y = 0;

        gbc.gridx = 0; gbc.gridy = y;
        form.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        form.add(usernameField, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        form.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        form.add(passwordField, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        form.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1;
        form.add(confirmPasswordField, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        form.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        form.add(fullNameField, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        form.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        form.add(addressField, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        form.add(new JLabel("Phone (11 digits):"), gbc);
        gbc.gridx = 1;
        form.add(phoneField, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        form.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        form.add(roleBox, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        form.add(new JLabel("Admin Pass :"), gbc);
        gbc.gridx = 1;
        form.add(adminPassField, gbc);
        y++;

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(registerButton);
        buttons.add(cancelButton);

        getContentPane().setLayout(new BorderLayout(10, 10));
        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }

    private void initListeners() {
        registerButton.addActionListener(e -> doRegister());
        cancelButton.addActionListener(e -> dispose());
        getRootPane().setDefaultButton(registerButton);
    }

    private void doRegister() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirm = new String(confirmPasswordField.getPassword());
        String fullName = fullNameField.getText().trim();
        String address = addressField.getText().trim();
        String phone = phoneField.getText().trim();
        String roleStr = (String) roleBox.getSelectedItem();
        String adminPass = new String(adminPassField.getPassword());

        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and passwords are required.");
            return;
        }
        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.");
            return;
        }
        if (userService.usernameExists(username)) {
            JOptionPane.showMessageDialog(this, "Username already exists.");
            return;
        }
        if (!phone.matches("\\d{11}")) {
            JOptionPane.showMessageDialog(this, "Phone must be exactly 11 digits.");
            return;
        }

        User.Role role = User.Role.valueOf(roleStr);
        if (role == User.Role.ADMIN) {
            if (!"RNNS".equals(adminPass)) {
                JOptionPane.showMessageDialog(this, "Invalid admin pass.");
                return;
            }
        }

        User user = new User(username, password, role);
        user.setFullName(fullName.isEmpty() ? username : fullName);
        user.setAddress(address);
        user.setPhone(phone);

        userService.addUser(user);

        JOptionPane.showMessageDialog(this, "Registration successful.");
        dispose();
    }
}
