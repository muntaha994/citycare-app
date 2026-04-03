package com.mycompany.citycare;

import javax.swing.*;
import java.awt.*;

public class CityCareApp extends JFrame {

    private JTabbedPane tabbedPane;
    private final User currentUser;

    public CityCareApp(User user,
                       ComplaintService complaintService,
                       EmergencyService emergencyService,
                       HealthcareService healthcareService,
                       BillingService billingService,
                       TransportService transportService,
                       WasteService wasteService,
                       SecurityService securityService) {
        super("CityCare – Smart City Management (" + user.getRole() + ")");
        this.currentUser = user;
        initComponents(complaintService, emergencyService, healthcareService,
                       billingService, transportService, wasteService, securityService);
        layoutComponents();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
    }

    private void initComponents(ComplaintService complaintService,
                                EmergencyService emergencyService,
                                HealthcareService healthcareService,
                                BillingService billingService,
                                TransportService transportService,
                                WasteService wasteService,
                                SecurityService securityService) {
        tabbedPane = new JTabbedPane();

        // citizen tabs
        tabbedPane.addTab("Complaints", new ComplaintPanel(complaintService));
        tabbedPane.addTab("Emergency", new EmergencyPanel(emergencyService));
        tabbedPane.addTab("Healthcare", new HealthcarePanel(healthcareService));
        tabbedPane.addTab("Bills", new BillPanel(billingService, currentUser)); // note: passes currentUser
        tabbedPane.addTab("Transport", new TransportPanel(transportService));
        tabbedPane.addTab("Waste", new WastePanel(wasteService));
        tabbedPane.addTab("Security", new SecurityPanel(securityService));

        // admin-only tabs
        if (currentUser.getRole() == User.Role.ADMIN) {
            tabbedPane.addTab("Admin Complaints", new AdminComplaintPanel(complaintService));
            tabbedPane.addTab("Admin Emergency", new AdminEmergencyPanel(emergencyService));
            tabbedPane.addTab("Admin Healthcare", new AdminHealthcarePanel(healthcareService));
            tabbedPane.addTab("Admin Bills", new AdminBillingPanel(billingService));
            tabbedPane.addTab("Admin Transport", new AdminTransportPanel(transportService));
            tabbedPane.addTab("Admin Waste", new AdminWastePanel(wasteService));
            tabbedPane.addTab("Admin Security", new AdminSecurityPanel(securityService));
        }

        // Profile tab with logout callback
        tabbedPane.addTab("Profile", new UserProfilePanel(currentUser, this::handleLogout));
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);
    }

    private void handleLogout() {
        dispose();

        UserService userService = new UserService();
        LoginFrame login = new LoginFrame(null, userService);
        login.setVisible(true);

        User loggedIn = login.getAuthenticatedUser();
        if (loggedIn == null) {
            System.exit(0);
        }

        ComplaintService complaintService = new ComplaintService();
        EmergencyService emergencyService = new EmergencyService();
        HealthcareService healthcareService = new HealthcareService();
        BillingService billingService = new BillingService();
        TransportService transportService = new TransportService();
        WasteService wasteService = new WasteService();
        SecurityService securityService = new SecurityService();

        CityCareApp app = new CityCareApp(
                loggedIn,
                complaintService,
                emergencyService,
                healthcareService,
                billingService,
                transportService,
                wasteService,
                securityService
        );
        app.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserService userService = new UserService();
            LoginFrame login = new LoginFrame(null, userService);
            login.setVisible(true);

            User loggedIn = login.getAuthenticatedUser();
            if (loggedIn == null) {
                System.exit(0);
            }

            ComplaintService complaintService = new ComplaintService();
            EmergencyService emergencyService = new EmergencyService();
            HealthcareService healthcareService = new HealthcareService();
            BillingService billingService = new BillingService();
            TransportService transportService = new TransportService();
            WasteService wasteService = new WasteService();
            SecurityService securityService = new SecurityService();

            CityCareApp app = new CityCareApp(
                    loggedIn,
                    complaintService,
                    emergencyService,
                    healthcareService,
                    billingService,
                    transportService,
                    wasteService,
                    securityService
            );
            app.setVisible(true);
        });
    }
}
