package com.mycompany.citycare;

import com.mycompany.citycare.GuardContact;
import com.mycompany.citycare.SecurityAlert;
import com.mycompany.citycare.SecurityService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SecurityPanel extends JPanel {

    private final SecurityService service;

    private JTable guardTable;
    private JTable alertTable;
    private JButton refreshBtn;
    private JButton showAlertBtn;

    public SecurityPanel(SecurityService service) {
        this.service = service;
        initComponents();
        layoutComponents();
        loadData();
    }

    private void initComponents() {
        guardTable = new JTable(new DefaultTableModel(
                new Object[]{"ID", "Name", "Phone", "Post"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });

        alertTable = new JTable(new DefaultTableModel(
                new Object[]{"ID", "Title", "Level", "Created At"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });

        refreshBtn = new JButton("Refresh");
        showAlertBtn = new JButton("Show Selected Alert");

        refreshBtn.addActionListener(e -> loadData());
        showAlertBtn.addActionListener(e -> showSelectedAlert());
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));

        JSplitPane split = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(guardTable),
                new JScrollPane(alertTable)
        );
        split.setResizeWeight(0.4);

        JPanel topButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topButtons.add(refreshBtn);
        topButtons.add(showAlertBtn);

        add(topButtons, BorderLayout.NORTH);
        add(split, BorderLayout.CENTER);
    }

    private void loadData() {
        DefaultTableModel gModel = (DefaultTableModel) guardTable.getModel();
        DefaultTableModel aModel = (DefaultTableModel) alertTable.getModel();

        gModel.setRowCount(0);
        for (GuardContact g : service.getGuards()) {
            gModel.addRow(new Object[]{
                    g.getId(),
                    g.getName(),
                    g.getPhone(),
                    g.getPost()
            });
        }

        aModel.setRowCount(0);
        for (SecurityAlert a : service.getAlerts()) {
            aModel.addRow(new Object[]{
                    a.getId(),
                    a.getTitle(),
                    a.getLevel(),
                    a.getCreatedAt()
            });
        }
    }

    private Long getSelectedAlertId() {
        int row = alertTable.getSelectedRow();
        if (row == -1) return null;
        Object v = alertTable.getValueAt(row, 0);
        if (v instanceof Number) return ((Number) v).longValue();
        return Long.parseLong(v.toString());
    }

    private void showSelectedAlert() {
        Long id = getSelectedAlertId();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Select an alert first.");
            return;
        }

        SecurityAlert selected = service.getAlerts().stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElse(null);
        if (selected == null) return;

        int type = JOptionPane.INFORMATION_MESSAGE;
        if ("Warning".equalsIgnoreCase(selected.getLevel())) {
            type = JOptionPane.WARNING_MESSAGE;
        } else if ("Critical".equalsIgnoreCase(selected.getLevel())) {
            type = JOptionPane.ERROR_MESSAGE;
        }

        JOptionPane.showMessageDialog(
                this,
                selected.getMessage(),
                selected.getTitle(),
                type
        );
    }
}
