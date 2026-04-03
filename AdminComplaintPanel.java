package com.mycompany.citycare;

import com.mycompany.citycare.Complaint;

import com.mycompany.citycare.ComplaintService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminComplaintPanel extends JPanel {

    private final ComplaintService complaintService;
    private JTable table;
    private DefaultTableModel model;
    private JButton refreshBtn;
    private JButton deleteBtn;
    private JButton setInProgressBtn;
    private JButton setClosedBtn;

    public AdminComplaintPanel(ComplaintService service) {
        this.complaintService = service;
        initComponents();
        layoutComponents();
        loadComplaints();
    }

    private void initComponents() {
        String[] cols = { "ID", "Name", "Contact", "Category", "Description", "Status" };
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        refreshBtn = new JButton("Refresh");
        deleteBtn = new JButton("Delete");
        setInProgressBtn = new JButton("Mark In Progress");
        setClosedBtn = new JButton("Mark Closed");

        refreshBtn.addActionListener(e -> loadComplaints());
        deleteBtn.addActionListener(e -> deleteSelected());
        setInProgressBtn.addActionListener(e -> updateStatusSelected("In progress"));
        setClosedBtn.addActionListener(e -> updateStatusSelected("Closed"));
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(refreshBtn);
        buttons.add(setInProgressBtn);
        buttons.add(setClosedBtn);
        buttons.add(deleteBtn);

        add(buttons, BorderLayout.SOUTH);
    }

    private void loadComplaints() {
        model.setRowCount(0);
        for (Complaint c : complaintService.getAllComplaints()) {
            model.addRow(new Object[]{
                    c.getId(),
                    c.getCitizenName(),
                    c.getContact(),
                    c.getCategory(),
                    c.getDescription(),
                    c.getStatus()
            });
        }
    }

    private Long getSelectedId() {
        int row = table.getSelectedRow();
        if (row == -1) return null;
        Object val = model.getValueAt(row, 0);
        if (val instanceof Number) {
            return ((Number) val).longValue();
        }
        return Long.parseLong(val.toString());
    }

    private void deleteSelected() {
        Long id = getSelectedId();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Select a complaint first.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete complaint ID " + id + "?",
                "Confirm delete",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            complaintService.deleteById(id);
            loadComplaints();
        }
    }

    private void updateStatusSelected(String status) {
        Long id = getSelectedId();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Select a complaint first.");
            return;
        }
        complaintService.updateStatus(id, status);
        loadComplaints();
    }
}
