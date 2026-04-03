package com.mycompany.citycare;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminBillingPanel extends JPanel {

    private final BillingService billingService;
    private JTable billTable;
    private DefaultTableModel tableModel;
    private JButton addBillButton;
    private JButton deleteBillButton;
    private JButton refreshButton;

    public AdminBillingPanel(BillingService billingService) {
        this.billingService = billingService;
        initComponents();
        layoutComponents();
        loadBills();
    }

    private void initComponents() {
        String[] columns = {"ID", "Username", "Description", "Amount", "Paid", "Due", "Completed"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        billTable = new JTable(tableModel);
        billTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        addBillButton = new JButton("Add Bill");
        deleteBillButton = new JButton("Delete Bill");
        refreshButton = new JButton("Refresh");

        addBillButton.addActionListener(e -> addBill());
        deleteBillButton.addActionListener(e -> deleteSelectedBill());
        refreshButton.addActionListener(e -> loadBills());
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(5, 5));

        add(new JScrollPane(billTable), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(addBillButton);
        bottom.add(deleteBillButton);
        bottom.add(refreshButton);

        add(bottom, BorderLayout.SOUTH);
    }

    private void loadBills() {
        tableModel.setRowCount(0);
        List<Bill> all = billingService.getAllBills();
        for (Bill b : all) {
            Object[] row = {
                    b.getId(),
                    b.getUsername(),
                    b.getDescription(),
                    b.getAmount(),
                    b.getPaid(),
                    b.getDue(),
                    b.isCompleted()
            };
            tableModel.addRow(row);
        }
    }

    private void addBill() {
        String username = JOptionPane.showInputDialog(this, "Enter username for bill:");
        if (username == null || username.trim().isEmpty()) return;

        String desc = JOptionPane.showInputDialog(this, "Enter description:");
        if (desc == null) return;

        String amountStr = JOptionPane.showInputDialog(this, "Enter amount:");
        if (amountStr == null) return;

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount.");
            return;
        }

        billingService.addBill(username.trim(), desc.trim(), amount, 0.0);
        loadBills();
    }

    private void deleteSelectedBill() {
        int row = billTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a bill to delete.");
            return;
        }
        long id = (long) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete bill ID " + id + "?",
                "Confirm",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            billingService.deleteBill(id);
            loadBills();
        }
    }
}
