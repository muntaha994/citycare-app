package com.mycompany.citycare;
import com.mycompany.citycare.PickupSchedule;
import com.mycompany.citycare.PickupSchedule;
import com.mycompany.citycare.WasteIssue;
import com.mycompany.citycare.WasteIssue;
import com.mycompany.citycare.WasteService;
import com.mycompany.citycare.WasteService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class AdminWastePanel extends JPanel {

    private final WasteService service;

    private JTable scheduleTable;
    private JTable issueTable;
    private JButton addScheduleBtn;
    private JButton deleteScheduleBtn;
    private JButton refreshBtn;
    private JButton markInProgressBtn;
    private JButton markResolvedBtn;
    private JButton deleteIssueBtn;

    public AdminWastePanel(WasteService service) {
        this.service = service;
        initComponents();
        layoutComponents();
        loadData();
    }

    private void initComponents() {
        scheduleTable = new JTable(new DefaultTableModel(
                new Object[]{"ID", "Area", "Day", "Next Date"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });

        issueTable = new JTable(new DefaultTableModel(
                new Object[]{"ID", "Name", "Contact", "Location", "Type", "Description", "Status"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });

        addScheduleBtn = new JButton("Add Schedule");
        deleteScheduleBtn = new JButton("Delete Schedule");
        refreshBtn = new JButton("Refresh");
        markInProgressBtn = new JButton("Mark In Progress");
        markResolvedBtn = new JButton("Mark Resolved");
        deleteIssueBtn = new JButton("Delete Issue");

        refreshBtn.addActionListener(e -> loadData());
        addScheduleBtn.addActionListener(e -> addSchedule());
        deleteScheduleBtn.addActionListener(e -> deleteSchedule());
        markInProgressBtn.addActionListener(e -> updateIssueStatus("In progress"));
        markResolvedBtn.addActionListener(e -> updateIssueStatus("Resolved"));
        deleteIssueBtn.addActionListener(e -> deleteIssue());
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));

        JSplitPane split = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(scheduleTable),
                new JScrollPane(issueTable)
        );
        split.setResizeWeight(0.4);

        JPanel topButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topButtons.add(refreshBtn);
        topButtons.add(addScheduleBtn);
        topButtons.add(deleteScheduleBtn);

        JPanel bottomButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomButtons.add(markInProgressBtn);
        bottomButtons.add(markResolvedBtn);
        bottomButtons.add(deleteIssueBtn);

        add(topButtons, BorderLayout.NORTH);
        add(split, BorderLayout.CENTER);
        add(bottomButtons, BorderLayout.SOUTH);
    }

    private void loadData() {
        DefaultTableModel sModel = (DefaultTableModel) scheduleTable.getModel();
        DefaultTableModel iModel = (DefaultTableModel) issueTable.getModel();

        sModel.setRowCount(0);
        for (PickupSchedule s : service.getSchedules()) {
            sModel.addRow(new Object[]{
                    s.getId(),
                    s.getArea(),
                    s.getDayOfWeek(),
                    s.getNextDate()
            });
        }

        iModel.setRowCount(0);
        for (WasteIssue w : service.getIssues()) {
            iModel.addRow(new Object[]{
                    w.getId(),
                    w.getCitizenName(),
                    w.getContact(),
                    w.getLocation(),
                    w.getType(),
                    w.getDescription(),
                    w.getStatus()
            });
        }
    }

    private Long getSelectedScheduleId() {
        int row = scheduleTable.getSelectedRow();
        if (row == -1) return null;
        Object v = scheduleTable.getValueAt(row, 0);
        if (v instanceof Number) return ((Number) v).longValue();
        return Long.parseLong(v.toString());
    }

    private Long getSelectedIssueId() {
        int row = issueTable.getSelectedRow();
        if (row == -1) return null;
        Object v = issueTable.getValueAt(row, 0);
        if (v instanceof Number) return ((Number) v).longValue();
        return Long.parseLong(v.toString());
    }

    private void addSchedule() {
        String area = JOptionPane.showInputDialog(this, "Area:");
        if (area == null || area.trim().isEmpty()) return;

        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        String day = (String) JOptionPane.showInputDialog(
                this,
                "Pickup day:",
                "Day",
                JOptionPane.QUESTION_MESSAGE,
                null,
                days,
                days[0]
        );
        if (day == null) return;

        String daysFromNowStr = JOptionPane.showInputDialog(this, "Next pickup in how many days from today?");
        if (daysFromNowStr == null) return;

        try {
            int d = Integer.parseInt(daysFromNowStr);
            LocalDate nextDate = LocalDate.now().plusDays(d);
            service.addSchedule(new PickupSchedule(area, day, nextDate));
            loadData();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number of days.");
        }
    }

    private void deleteSchedule() {
        Long id = getSelectedScheduleId();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Select a schedule first.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete schedule ID " + id + "?",
                "Confirm delete",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            service.deleteScheduleById(id);
            loadData();
        }
    }

    private void updateIssueStatus(String status) {
        Long id = getSelectedIssueId();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Select an issue first.");
            return;
        }
        service.updateIssueStatus(id, status);
        loadData();
    }

    private void deleteIssue() {
        Long id = getSelectedIssueId();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Select an issue first.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete issue ID " + id + "?",
                "Confirm delete",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            service.deleteIssueById(id);
            loadData();
        }
    }
}
