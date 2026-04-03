package com.mycompany.citycare;

import com.mycompany.citycare.Route;
import com.mycompany.citycare.TransportService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminTransportPanel extends JPanel {

    private final TransportService service;

    private JTable routeTable;
    private JButton refreshBtn;
    private JButton addBtn;
    private JButton deleteBtn;

    public AdminTransportPanel(TransportService service) {
        this.service = service;
        initComponents();
        layoutComponents();
        loadData();
    }

    private void initComponents() {
        routeTable = new JTable(new DefaultTableModel(
                new Object[]{"ID", "From", "To", "Mode", "Distance (km)", "Base Fare", "Fare/km"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });
        routeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        refreshBtn = new JButton("Refresh");
        addBtn = new JButton("Add Route");
        deleteBtn = new JButton("Delete Route");

        refreshBtn.addActionListener(e -> loadData());
        addBtn.addActionListener(e -> addRoute());
        deleteBtn.addActionListener(e -> deleteRoute());
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));
        add(new JScrollPane(routeTable), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(refreshBtn);
        buttons.add(addBtn);
        buttons.add(deleteBtn);

        add(buttons, BorderLayout.SOUTH);
    }

    private void loadData() {
        DefaultTableModel model = (DefaultTableModel) routeTable.getModel();
        model.setRowCount(0);
        for (Route r : service.getRoutes()) {
            model.addRow(new Object[]{
                    r.getId(),
                    r.getFrom(),
                    r.getTo(),
                    r.getMode(),
                    r.getDistanceKm(),
                    r.getBaseFare(),
                    r.getFarePerKm()
            });
        }
    }

    private Long getSelectedRouteId() {
        int row = routeTable.getSelectedRow();
        if (row == -1) return null;
        Object v = routeTable.getValueAt(row, 0);
        if (v instanceof Number) return ((Number) v).longValue();
        return Long.parseLong(v.toString());
    }

    private void addRoute() {
        String from = JOptionPane.showInputDialog(this, "From:");
        if (from == null || from.trim().isEmpty()) return;

        String to = JOptionPane.showInputDialog(this, "To:");
        if (to == null || to.trim().isEmpty()) return;

        String[] modes = {"Bus", "Train", "Local"};
        String mode = (String) JOptionPane.showInputDialog(
                this,
                "Mode:",
                "Mode",
                JOptionPane.QUESTION_MESSAGE,
                null,
                modes,
                modes[0]
        );
        if (mode == null) return;

        String distanceStr = JOptionPane.showInputDialog(this, "Distance (km):");
        if (distanceStr == null) return;

        String baseFareStr = JOptionPane.showInputDialog(this, "Base fare:");
        if (baseFareStr == null) return;

        String farePerKmStr = JOptionPane.showInputDialog(this, "Fare per km:");
        if (farePerKmStr == null) return;

        try {
            double distance = Double.parseDouble(distanceStr);
            double baseFare = Double.parseDouble(baseFareStr);
            double farePerKm = Double.parseDouble(farePerKmStr);

            Route r = new Route(from, to, mode, distance, baseFare, farePerKm);
            service.addRoute(r);
            loadData();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid numeric values.");
        }
    }

    private void deleteRoute() {
        Long id = getSelectedRouteId();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Select a route first.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete route ID " + id + "?",
                "Confirm delete",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            service.deleteRouteById(id);
            loadData();
        }
    }
}
