package com.mycompany.citycare;

public class Route {
    private static long nextId = 1;

    private final long id;
    private String from;
    private String to;
    private String mode;      // Bus, Train, Local
    private double distanceKm;
    private double baseFare;
    private double farePerKm;

    public Route(String from, String to, String mode,
                 double distanceKm, double baseFare, double farePerKm) {
        this.id = nextId++;
        this.from = from;
        this.to = to;
        this.mode = mode;
        this.distanceKm = distanceKm;
        this.baseFare = baseFare;
        this.farePerKm = farePerKm;
    }

    public long getId() { return id; }
    public String getFrom() { return from; }
    public String getTo() { return to; }
    public String getMode() { return mode; }
    public double getDistanceKm() { return distanceKm; }
    public double getBaseFare() { return baseFare; }
    public double getFarePerKm() { return farePerKm; }

    public void setFrom(String from) { this.from = from; }
    public void setTo(String to) { this.to = to; }
    public void setMode(String mode) { this.mode = mode; }
    public void setDistanceKm(double distanceKm) { this.distanceKm = distanceKm; }
    public void setBaseFare(double baseFare) { this.baseFare = baseFare; }
    public void setFarePerKm(double farePerKm) { this.farePerKm = farePerKm; }
}
