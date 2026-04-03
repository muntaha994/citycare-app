package com.mycompany.citycare;

import com.mycompany.citycare.Route;
import com.mycompany.citycare.TransportService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TransportPanel extends JPanel {

    private final TransportService service;

    private JTable routeTable;
    private JTextField distanceField;
    private JLabel fareLabel;
    private JButton estimateBtn;
    private JButton refreshBtn;

    public TransportPanel(TransportService service) {
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

        distanceField = new JTextField(8);
        fareLabel = new JLabel("Estimated fare: -");
        estimateBtn = new JButton("Estimate Fare");
        refreshBtn = new JButton("Refresh");

        estimateBtn.addActionListener(e -> estimateFare());
        refreshBtn.addActionListener(e -> loadData());
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));

        add(new JScrollPane(routeTable), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.add(new JLabel("Distance (km):"));
        bottom.add(distanceField);
        bottom.add(estimateBtn);
        bottom.add(new JSeparator(SwingConstants.VERTICAL));
        bottom.add(refreshBtn);
        bottom.add(fareLabel);

        add(bottom, BorderLayout.SOUTH);
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

    private void estimateFare() {
        Long id = getSelectedRouteId();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Select a route first.");
            return;
        }

        double distance;
        try {
            distance = Double.parseDouble(distanceField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter valid distance.");
            return;
        }

        double fare = service.estimateFare(id, distance);
        fareLabel.setText("Estimated fare: " + fare);
    }
}
