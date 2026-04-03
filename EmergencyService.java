package com.mycompany.citycare;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EmergencyService {

    private final List<EmergencyRequest> requests = new ArrayList<>();

    public EmergencyRequest addRequest(String name, String contact,
                                       String type, String location, String description) {
        EmergencyRequest r = new EmergencyRequest(name, contact, type, location, description);
        requests.add(r);
        return r;
    }

    public List<EmergencyRequest> getAllRequests() {
        return Collections.unmodifiableList(requests);
    }

    public EmergencyRequest findById(long id) {
        return requests.stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void updateStatus(long id, String status) {
        EmergencyRequest r = findById(id);
        if (r != null) {
            r.setStatus(status);
        }
    }

    public void deleteById(long id) {
        requests.removeIf(r -> r.getId() == id);
    }
}
