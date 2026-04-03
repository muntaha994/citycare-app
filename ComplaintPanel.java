package com.mycompany.citycare;

import com.mycompany.citycare.Complaint;
import com.mycompany.citycare.ComplaintService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ComplaintPanel extends JPanel {

    private final ComplaintService service;

    private JTextField nameField;
    private JTextField contactField;
    private JComboBox<String> categoryBox;
    private JTextArea descriptionArea;
    private JTable complaintTable;
    private DefaultTableModel tableModel;

    public ComplaintPanel(ComplaintService service) {
        this.service = service;
        initComponents();
        layoutComponents();
    }

    private void initComponents() {
        nameField = new JTextField(20);
        contactField = new JTextField(15);

        categoryBox = new JComboBox<>(new String[]{
                "Garbage issues",
                "Street light problems",
                "Drain/Plumbing issues",
                "Apartment-related issues",
                "General service complaints"
        });

        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        String[] columns = {
                "ID", "Name", "Contact", "Category", "Description", "Status"
        };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        complaintTable = new JTable(tableModel);
        complaintTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
        form.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        form.add(categoryBox, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        form.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        form.add(new JScrollPane(descriptionArea), gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton submitBtn = new JButton("Submit Complaint");
        JButton refreshBtn = new JButton("Refresh");
        buttonPanel.add(submitBtn);
        buttonPanel.add(refreshBtn);

        submitBtn.setActionCommand("submit");
        refreshBtn.setActionCommand("refresh");
        submitBtn.addActionListener(this::onButtonClicked);
        refreshBtn.addActionListener(this::onButtonClicked);

        JPanel top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        top.add(buttonPanel, BorderLayout.SOUTH);

        JScrollPane tableScroll = new JScrollPane(complaintTable);

        add(top, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);
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
        String category = (String) categoryBox.getSelectedItem();
        String desc = descriptionArea.getText().trim();

        if (name.isEmpty() || contact.isEmpty() || desc.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please fill in name, contact, and description.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Complaint c = service.addComplaint(name, contact, category, desc);

        nameField.setText("");
        contactField.setText("");
        descriptionArea.setText("");

        tableModel.addRow(new Object[]{
                c.getId(),
                c.getCitizenName(),
                c.getContact(),
                c.getCategory(),
                c.getDescription(),
                c.getStatus()
        });

        JOptionPane.showMessageDialog(
                this,
                "Complaint submitted. ID: " + c.getId(),
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void reloadTable() {
        tableModel.setRowCount(0);
        for (Complaint c : service.getAllComplaints()) {
            tableModel.addRow(new Object[]{
                    c.getId(),
                    c.getCitizenName(),
                    c.getContact(),
                    c.getCategory(),
                    c.getDescription(),
                    c.getStatus()
            });
        }
    }
}
