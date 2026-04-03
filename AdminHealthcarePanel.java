package com.mycompany.citycare;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminHealthcarePanel extends JPanel {

    private final HealthcareService healthcareService;

    private JTable bloodTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton deleteButton;
    private JButton refreshButton;

    public AdminHealthcarePanel(HealthcareService healthcareService) {
        this.healthcareService = healthcareService;
        initComponents();
        layoutComponents();
        loadBloodEntries();
    }

    private void initComponents() {
        String[] cols = {"ID", "Blood Group", "Units Available"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        bloodTable = new JTable(tableModel);
        bloodTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        addButton = new JButton("Add Blood");
        deleteButton = new JButton("Delete Selected");
        refreshButton = new JButton("Refresh");

        addButton.addActionListener(e -> addBlood());
        deleteButton.addActionListener(e -> deleteSelected());
        refreshButton.addActionListener(e -> loadBloodEntries());
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(5, 5));
        add(new JScrollPane(bloodTable), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(addButton);
        bottom.add(deleteButton);
        bottom.add(refreshButton);

        add(bottom, BorderLayout.SOUTH);
    }

    private void loadBloodEntries() {
        tableModel.setRowCount(0);
        List<BloodBankEntry> entries = healthcareService.getAllBloodEntries();
        for (BloodBankEntry e : entries) {
            tableModel.addRow(new Object[]{
                    e.getId(),
                    e.getBloodGroup(),
                    e.getUnitsAvailable()
            });
        }
    }

    private void addBlood() {
        String group = JOptionPane.showInputDialog(this, "Enter blood group (e.g. A+, O-):");
        if (group == null || group.trim().isEmpty()) return;

        String unitsStr = JOptionPane.showInputDialog(this, "Enter units available:");
        if (unitsStr == null) return;

        int units;
        try {
            units = Integer.parseInt(unitsStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid units.");
            return;
        }
        if (units <= 0) {
            JOptionPane.showMessageDialog(this, "Units must be positive.");
            return;
        }

        healthcareService.addBloodEntry(group.trim(), units);
        loadBloodEntries();
    }

    private void deleteSelected() {
        int row = bloodTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a row to delete.");
            return;
        }
        long id = (long) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete blood entry ID " + id + "?",
                "Confirm",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            healthcareService.deleteBloodEntry(id);
            loadBloodEntries();
        }
    }
}
