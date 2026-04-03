package com.mycompany.citycare;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BillPanel extends JPanel {

    private final BillingService billingService;
    private final User currentUser;

    private JTable billTable;
    private DefaultTableModel tableModel;
    private JLabel totalAmountLabel;
    private JLabel totalPaidLabel;
    private JLabel totalDueLabel;
    private JButton refreshButton;
    private JButton payButton;

    public BillPanel(BillingService billingService, User currentUser) {
        this.billingService = billingService;
        this.currentUser = currentUser;
        initComponents();
        layoutComponents();
        loadBills();
    }

    private void initComponents() {
        String[] cols = {"ID", "Description", "Amount", "Paid", "Due", "Completed"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        billTable = new JTable(tableModel);
        billTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        totalAmountLabel = new JLabel("Total: 0.00");
        totalPaidLabel   = new JLabel("Paid: 0.00");
        totalDueLabel    = new JLabel("Due: 0.00");

        refreshButton = new JButton("Refresh");
        payButton     = new JButton("Pay Selected");

        refreshButton.addActionListener(e -> loadBills());
        payButton.addActionListener(e -> paySelectedBill());
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(5, 5));
        add(new JScrollPane(billTable), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        JPanel totals = new JPanel(new FlowLayout(FlowLayout.LEFT));
        totals.add(totalAmountLabel);
        totals.add(totalPaidLabel);
        totals.add(totalDueLabel);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(refreshButton);
        buttons.add(payButton);

        bottom.add(totals, BorderLayout.WEST);
        bottom.add(buttons, BorderLayout.EAST);

        add(bottom, BorderLayout.SOUTH);
    }

    private void loadBills() {
        tableModel.setRowCount(0);
        List<Bill> myBills = billingService.getBillsForUser(currentUser.getUsername());
        for (Bill b : myBills) {
            tableModel.addRow(new Object[]{
                    b.getId(),
                    b.getDescription(),
                    b.getAmount(),
                    b.getPaid(),
                    b.getDue(),
                    b.isCompleted()
            });
        }
        double total = billingService.getTotalAmountForUser(currentUser.getUsername());
        double paid  = billingService.getTotalPaidForUser(currentUser.getUsername());
        double due   = billingService.getTotalDueForUser(currentUser.getUsername());
        totalAmountLabel.setText(String.format("Total: %.2f", total));
        totalPaidLabel.setText(String.format("Paid: %.2f", paid));
        totalDueLabel.setText(String.format("Due: %.2f", due));
    }

    private void paySelectedBill() {
        int row = billTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a bill to pay.");
            return;
        }
        long id = (long) tableModel.getValueAt(row, 0);
        String amountStr = JOptionPane.showInputDialog(this, "Enter amount to pay:");
        if (amountStr == null) return;
        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount.");
            return;
        }
        billingService.payBill(id, amount);
        loadBills();
    }
}
