package com.mycompany.citycare;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HealthcarePanel extends JPanel {

    private final HealthcareService healthcareService;

    private JTable bloodTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;
    private JButton bookButton;

    public HealthcarePanel(HealthcareService healthcareService) {
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

        refreshButton = new JButton("Refresh");
        bookButton = new JButton("Book Blood");

        refreshButton.addActionListener(e -> loadBloodEntries());
        bookButton.addActionListener(e -> bookSelectedBlood());
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(5, 5));
        add(new JScrollPane(bloodTable), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(refreshButton);
        bottom.add(bookButton);

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

    private void bookSelectedBlood() {
        int row = bloodTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a blood entry to book.");
            return;
        }
        long id = (long) tableModel.getValueAt(row, 0);
        String unitsStr = JOptionPane.showInputDialog(this, "Enter units to book:");
        if (unitsStr == null) return;
        int units;
        try {
            units = Integer.parseInt(unitsStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number.");
            return;
        }

        boolean ok = healthcareService.bookBlood(id, units);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Booking successful.");
        } else {
            JOptionPane.showMessageDialog(this, "Booking failed (not enough units or invalid).");
        }
        loadBloodEntries();
    }
}
