package com.mycompany.citycare;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TransportService {

    private final List<Route> routes = new ArrayList<>();

    public TransportService() {
        routes.add(new Route("Central", "Airport", "Bus", 15.0, 20.0, 3.0));
        routes.add(new Route("Central", "Railway Station", "Bus", 5.0, 15.0, 2.0));
        routes.add(new Route("Central", "University", "Local", 8.0, 18.0, 2.5));
    }

    public List<Route> getRoutes() {
        return Collections.unmodifiableList(routes);
    }

    public void addRoute(Route r) {
        routes.add(r);
    }

    public void deleteRouteById(long id) {
        routes.removeIf(r -> r.getId() == id);
    }

    public Route findById(long id) {
        return routes.stream().filter(r -> r.getId() == id).findFirst().orElse(null);
    }

    public double estimateFare(long routeId, double distance) {
        Route r = findById(routeId);
        if (r == null) return 0.0;
        return r.getBaseFare() + r.getFarePerKm() * distance;
    }
}
