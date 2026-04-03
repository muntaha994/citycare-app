package com.mycompany.citycare;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JDialog {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton cancelButton;
    private JButton registerButton;

    private final UserService userService;
    private User authenticatedUser;

    public LoginFrame(Frame owner, UserService userService) {
        super(owner, "CityCare Login", true);
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
        loginButton = new JButton("Login");
        cancelButton = new JButton("Cancel");
        registerButton = new JButton("Register");
    }

    private void layoutComponents() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        form.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        form.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        form.add(passwordField, gbc);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(registerButton);
        buttons.add(loginButton);
        buttons.add(cancelButton);

        getContentPane().setLayout(new BorderLayout(10, 10));
        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }

    private void initListeners() {
        loginButton.addActionListener(e -> doLogin());
        cancelButton.addActionListener(e -> {
            authenticatedUser = null;
            dispose();
        });
        registerButton.addActionListener(e -> openRegister());
        getRootPane().setDefaultButton(loginButton);
    }

    private void doLogin() {
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword());

        User u = userService.authenticate(user, pass);
        if (u == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Invalid credentials",
                    "Login failed",
                    JOptionPane.ERROR_MESSAGE
            );
        } else {
            authenticatedUser = u;
            dispose();
        }
    }

    private void openRegister() {
        RegisterFrame reg = new RegisterFrame(this, userService);
        reg.setVisible(true);
    }

    public User getAuthenticatedUser() {
        return authenticatedUser;
    }
}
