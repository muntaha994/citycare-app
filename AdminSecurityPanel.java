package com.mycompany.citycare;

import com.mycompany.citycare.GuardContact;
import com.mycompany.citycare.SecurityAlert;
import com.mycompany.citycare.SecurityService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminSecurityPanel extends JPanel {

    private final SecurityService service;

    private JTable guardTable;
    private JTable alertTable;

    private JButton refreshBtn;
    private JButton addGuardBtn;
    private JButton deleteGuardBtn;
    private JButton addAlertBtn;
    private JButton deleteAlertBtn;

    public AdminSecurityPanel(SecurityService service) {
        this.service = service;
        initComponents();
        layoutComponents();
        loadData();
    }

    private void initComponents() {
        guardTable = new JTable(new DefaultTableModel(
                new Object[]{"ID", "Name", "Phone", "Post"}, 0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        });

        alertTable = new JTable(new DefaultTableModel(
                new Object[]{"ID", "Title", "Level", "Created At"}, 0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        });

        refreshBtn = new JButton("Refresh");
        addGuardBtn = new JButton("Add Guard");
        deleteGuardBtn = new JButton("Delete Guard");
        addAlertBtn = new JButton("Add Alert");
        deleteAlertBtn = new JButton("Delete Alert");

        refreshBtn.addActionListener(e -> loadData());
        addGuardBtn.addActionListener(e -> addGuard());
        deleteGuardBtn.addActionListener(e -> deleteGuard());
        addAlertBtn.addActionListener(e -> addAlert());
        deleteAlertBtn.addActionListener(e -> deleteAlert());
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
        topButtons.add(addGuardBtn);
        topButtons.add(deleteGuardBtn);

        JPanel bottomButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomButtons.add(addAlertBtn);
        bottomButtons.add(deleteAlertBtn);

        add(topButtons, BorderLayout.NORTH);
        add(split, BorderLayout.CENTER);
        add(bottomButtons, BorderLayout.SOUTH);
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

    private Long getSelectedGuardId() {
        int row = guardTable.getSelectedRow();
        if (row == -1) return null;
        Object v = guardTable.getValueAt(row, 0);
        if (v instanceof Number) return ((Number) v).longValue();
        return Long.parseLong(v.toString());
    }

    private Long getSelectedAlertId() {
        int row = alertTable.getSelectedRow();
        if (row == -1) return null;
        Object v = alertTable.getValueAt(row, 0);
        if (v instanceof Number) return ((Number) v).longValue();
        return Long.parseLong(v.toString());
    }

    private void addGuard() {
        String name = JOptionPane.showInputDialog(this, "Guard name:");
        if (name == null || name.trim().isEmpty()) return;

        String phone = JOptionPane.showInputDialog(this, "Phone:");
        if (phone == null || phone.trim().isEmpty()) return;

        String post = JOptionPane.showInputDialog(this, "Post (location):");
        if (post == null || post.trim().isEmpty()) return;

        service.addGuard(new GuardContact(name, phone, post));
        loadData();
    }

    private void deleteGuard() {
        Long id = getSelectedGuardId();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Select a guard first.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete guard ID " + id + "?",
                "Confirm delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            service.deleteGuardById(id);
            loadData();
        }
    }

    private void addAlert() {
        String title = JOptionPane.showInputDialog(this, "Alert title:");
        if (title == null || title.trim().isEmpty()) return;

        String message = JOptionPane.showInputDialog(this, "Alert message:");
        if (message == null || message.trim().isEmpty()) return;

        String[] levels = {"Info", "Warning", "Critical"};
        String level = (String) JOptionPane.showInputDialog(
                this,
                "Level:",
                "Level",
                JOptionPane.QUESTION_MESSAGE,
                null,
                levels,
                levels[0]
        );
        if (level == null) return;

        service.addAlert(new SecurityAlert(title, message, level));
        loadData();
    }

    private void deleteAlert() {
        Long id = getSelectedAlertId();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Select an alert first.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete alert ID " + id + "?",
                "Confirm delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            service.deleteAlertById(id);
            loadData();
        }
    }
}
