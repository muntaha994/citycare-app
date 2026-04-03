package com.mycompany.citycare;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WasteService {

    private final List<PickupSchedule> schedules = new ArrayList<>();
    private final List<WasteIssue> issues = new ArrayList<>();

    public WasteService() {
        schedules.add(new PickupSchedule("Central Area", "Monday", LocalDate.now().plusDays(1)));
        schedules.add(new PickupSchedule("West Zone", "Wednesday", LocalDate.now().plusDays(3)));
        schedules.add(new PickupSchedule("East Zone", "Friday", LocalDate.now().plusDays(5)));
    }

    public List<PickupSchedule> getSchedules() {
        return Collections.unmodifiableList(schedules);
    }

    public List<WasteIssue> getIssues() {
        return Collections.unmodifiableList(issues);
    }

    public void addSchedule(PickupSchedule s) { schedules.add(s); }
    public void deleteScheduleById(long id) { schedules.removeIf(s -> s.getId() == id); }

    public WasteIssue addIssue(WasteIssue i) {
        issues.add(i);
        return i;
    }

    public WasteIssue findIssueById(long id) {
        return issues.stream().filter(i -> i.getId() == id).findFirst().orElse(null);
    }

    public void updateIssueStatus(long id, String status) {
        WasteIssue i = findIssueById(id);
        if (i != null) i.setStatus(status);
    }

    public void deleteIssueById(long id) {
        issues.removeIf(i -> i.getId() == id);
    }
}
