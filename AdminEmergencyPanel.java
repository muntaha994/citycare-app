package com.mycompany.citycare;

import com.mycompany.citycare.EmergencyRequest;
import com.mycompany.citycare.EmergencyService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminEmergencyPanel extends JPanel {

    private final EmergencyService service;
    private JTable table;
    private DefaultTableModel model;
    private JButton refreshBtn;
    private JButton dispatchedBtn;
    private JButton resolvedBtn;
    private JButton deleteBtn;

    public AdminEmergencyPanel(EmergencyService service) {
        this.service = service;
        initComponents();
        layoutComponents();
        loadData();
    }

    private void initComponents() {
        String[] cols = { "ID", "Name", "Contact", "Type", "Location", "Description", "Status" };
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        refreshBtn = new JButton("Refresh");
        dispatchedBtn = new JButton("Mark Dispatched");
        resolvedBtn = new JButton("Mark Resolved");
        deleteBtn = new JButton("Delete");

        refreshBtn.addActionListener(e -> loadData());
        dispatchedBtn.addActionListener(e -> changeStatus("Dispatched"));
        resolvedBtn.addActionListener(e -> changeStatus("Resolved"));
        deleteBtn.addActionListener(e -> deleteSelected());
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(refreshBtn);
        bottom.add(dispatchedBtn);
        bottom.add(resolvedBtn);
        bottom.add(deleteBtn);

        add(bottom, BorderLayout.SOUTH);
    }

    private void loadData() {
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

    private Long getSelectedId() {
        int row = table.getSelectedRow();
        if (row == -1) return null;
        Object val = model.getValueAt(row, 0);
        if (val instanceof Number) return ((Number) val).longValue();
        return Long.parseLong(val.toString());
    }

    private void changeStatus(String status) {
        Long id = getSelectedId();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Select a request first.");
            return;
        }
        service.updateStatus(id, status);
        loadData();
    }

    private void deleteSelected() {
        Long id = getSelectedId();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Select a request first.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete emergency request ID " + id + "?",
                "Confirm delete",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            service.deleteById(id);
            loadData();
        }
    }
}
