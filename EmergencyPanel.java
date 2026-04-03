package com.mycompany.citycare;

import com.mycompany.citycare.EmergencyRequest;
import com.mycompany.citycare.EmergencyService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class EmergencyPanel extends JPanel {

    private final EmergencyService service;

    private JTextField nameField;
    private JTextField contactField;
    private JComboBox<String> typeBox;
    private JTextField locationField;
    private JTextArea descriptionArea;
    private JTable table;
    private DefaultTableModel model;

    public EmergencyPanel(EmergencyService service) {
        this.service = service;
        initComponents();
        layoutComponents();
    }

    private void initComponents() {
        nameField = new JTextField(15);
        contactField = new JTextField(12);
        locationField = new JTextField(20);

        typeBox = new JComboBox<>(new String[]{
                "Ambulance",
                "Fire service",
                "Police",
                "Gas/Electricity emergency"
        });

        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        String[] cols = { "ID", "Name", "Contact", "Type", "Location", "Description", "Status" };
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        form.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        form.add(new JLabel("Contact:"), gbc);
        gbc.gridx = 1;
        form.add(contactField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        form.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        form.add(typeBox, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        form.add(new JLabel("Location:"), gbc);
        gbc.gridx = 1;
        form.add(locationField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        form.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        form.add(new JScrollPane(descriptionArea), gbc);

        JButton submitBtn = new JButton("Send Emergency Request");
        JButton refreshBtn = new JButton("Refresh");
        submitBtn.setActionCommand("submit");
        refreshBtn.setActionCommand("refresh");
        submitBtn.addActionListener(this::onButtonClicked);
        refreshBtn.addActionListener(this::onButtonClicked);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(submitBtn);
        btnPanel.add(refreshBtn);

        JPanel top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        top.add(btnPanel, BorderLayout.SOUTH);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void onButtonClicked(ActionEvent e) {
        String cmd = e.getActionCommand();
        if ("submit".equals(cmd)) {
            handleSubmit();
        } else if ("refresh".equals(cmd)) {
            reloadTable();
        }
    }

    private void handleSubmit() {
        String name = nameField.getText().trim();
        String contact = contactField.getText().trim();
        String type = (String) typeBox.getSelectedItem();
        String location = locationField.getText().trim();
        String desc = descriptionArea.getText().trim();

        if (name.isEmpty() || contact.isEmpty() || location.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please fill in name, contact, and location.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        EmergencyRequest r = service.addRequest(name, contact, type, location, desc);

        nameField.setText("");
        contactField.setText("");
        locationField.setText("");
        descriptionArea.setText("");

        model.addRow(new Object[]{
                r.getId(),
                r.getCitizenName(),
                r.getContact(),
                r.getType(),
                r.getLocation(),
                r.getDescription(),
                r.getStatus()
        });

        JOptionPane.showMessageDialog(
                this,
                "Emergency request sent. ID: " + r.getId(),
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void reloadTable() {
        model.setRowCount(0);
        for (EmergencyRequest r : service.getAllRequests()) {
            model.addRow(new Object[]{
                    r.getId(),
                    r.getCitizenName(),
                    r.getContact(),
                    r.getType(),
                    r.getLocation(),
                    r.getDescription(),
                    r.getStatus()
            });
        }
    }
}
