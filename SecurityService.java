package com.mycompany.citycare;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SecurityService {

    private final List<SecurityAlert> alerts = new ArrayList<>();
    private final List<GuardContact> guards = new ArrayList<>();

    public SecurityService() {
        // sample guards
        guards.add(new GuardContact("Main Gate Guard", "01234-555555", "Main Gate"));
        guards.add(new GuardContact("Block A Guard", "01234-666666", "Block A"));

        // sample alert
        alerts.add(new SecurityAlert(
                "Test Alert",
                "System initialized",
                "Info"
        ));
    }

    public List<SecurityAlert> getAlerts() {
        return Collections.unmodifiableList(alerts);
    }

    public List<GuardContact> getGuards() {
        return Collections.unmodifiableList(guards);
    }

    public void addAlert(SecurityAlert a) {
        alerts.add(a);
    }

    public void deleteAlertById(long id) {
        alerts.removeIf(a -> a.getId() == id);
    }

    public void addGuard(GuardContact g) {
        guards.add(g);
    }

    public void deleteGuardById(long id) {
        guards.removeIf(g -> g.getId() == id);
    }
}
