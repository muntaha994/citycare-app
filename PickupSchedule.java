package com.mycompany.citycare;

import java.time.LocalDate;

public class PickupSchedule {
    private static long nextId = 1;

    private final long id;
    private String area;
    private String dayOfWeek;   // e.g., Monday
    private LocalDate nextDate;

    public PickupSchedule(String area, String dayOfWeek, LocalDate nextDate) {
        this.id = nextId++;
        this.area = area;
        this.dayOfWeek = dayOfWeek;
        this.nextDate = nextDate;
    }

    public long getId() { return id; }
    public String getArea() { return area; }
    public String getDayOfWeek() { return dayOfWeek; }
    public LocalDate getNextDate() { return nextDate; }

    public void setArea(String area) { this.area = area; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public void setNextDate(LocalDate nextDate) { this.nextDate = nextDate; }
}
