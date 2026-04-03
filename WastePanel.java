package com.mycompany.citycare;

import com.mycompany.citycare.PickupSchedule;
import com.mycompany.citycare.WasteIssue;
import com.mycompany.citycare.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class WastePanel extends JPanel {

    private final WasteService service;

    private JTable scheduleTable;
    private JTable issueTable;

    private JTextField nameField;
    private JTextField contactField;
    private JTextField locationField;
    private JComboBox<String> typeBox;
    private JTextArea descriptionArea;
    private JButton submitIssueBtn;
    private JButton refreshBtn;

    public WastePanel(WasteService service) {
        this.service = service;
        initComponents();
        layoutComponents();
        loadData();
    }

    private void initComponents() {
        scheduleTable = new JTable(new DefaultTableModel(
                new Object[]{"ID", "Area", "Day", "Next Date"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });

        issueTable = new JTable(new DefaultTableModel(
                new Object[]{"ID", "Name", "Contact", "Location", "Type", "Description", "Status"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });

        nameField = new JTextField(12);
        contactField = new JTextField(10);
        locationField = new JTextField(15);
        typeBox = new JComboBox<>(new String[]{
                "Missed pickup",
                "Overflowing bin",
                "Illegal dumping",
                "Other"
        });
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        submitIssueBtn = new JButton("Report Issue");
        refreshBtn = new JButton("Refresh");

        submitIssueBtn.addActionListener(e -> submitIssue());
        refreshBtn.addActionListener(e -> loadData());
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));

        JSplitPane topBottom = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(scheduleTable),
                new JScrollPane(issueTable)
        );
        topBottom.setResizeWeight(0.4);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
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
        form.add(new JLabel("Location:"), gbc);
        gbc.gridx = 1;
        form.add(locationField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        form.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        form.add(typeBox, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        form.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        form.add(new JScrollPane(descriptionArea), gbc);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(refreshBtn);
        buttons.add(submitIssueBtn);

        JPanel bottomForm = new JPanel(new BorderLayout());
        bottomForm.add(form, BorderLayout.CENTER);
        bottomForm.add(buttons, BorderLayout.SOUTH);

        add(topBottom, BorderLayout.CENTER);
        add(bottomForm, BorderLayout.SOUTH);
    }

    private void loadData() {
        DefaultTableModel sModel = (DefaultTableModel) scheduleTable.getModel();
        DefaultTableModel iModel = (DefaultTableModel) issueTable.getModel();

        sModel.setRowCount(0);
        for (PickupSchedule s : service.getSchedules()) {
            sModel.addRow(new Object[]{
                    s.getId(),
                    s.getArea(),
                    s.getDayOfWeek(),
                    s.getNextDate()
            });
        }

        iModel.setRowCount(0);
        for (WasteIssue w : service.getIssues()) {
            iModel.addRow(new Object[]{
                    w.getId(),
                    w.getCitizenName(),
                    w.getContact(),
                    w.getLocation(),
                    w.getType(),
                    w.getDescription(),
                    w.getStatus()
            });
        }
    }

    private void submitIssue() {
        String name = nameField.getText().trim();
        String contact = contactField.getText().trim();
        String location = locationField.getText().trim();
        String type = (String) typeBox.getSelectedItem();
        String desc = descriptionArea.getText().trim();

        if (name.isEmpty() || contact.isEmpty() || location.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in name, contact, and location.");
            return;
        }

        WasteIssue issue = new WasteIssue(name, contact, location, type, desc);
        service.addIssue(issue);

        nameField.setText("");
        contactField.setText("");
        locationField.setText("");
        descriptionArea.setText("");

        loadData();
        JOptionPane.showMessageDialog(this,
                "Waste issue reported. ID: " + issue.getId());
    }
}
