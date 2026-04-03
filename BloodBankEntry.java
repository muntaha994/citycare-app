package com.mycompany.citycare;

import java.io.Serializable;

public class BloodBankEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    private String bloodGroup;     // e.g. "A+", "O-"
    private int unitsAvailable;    // number of units/bags

    public BloodBankEntry(long id, String bloodGroup, int unitsAvailable) {
        this.id = id;
        this.bloodGroup = bloodGroup;
        this.unitsAvailable = unitsAvailable;
    }

    public long getId() {
        return id;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public int getUnitsAvailable() {
        return unitsAvailable;
    }

    public void setUnitsAvailable(int unitsAvailable) {
        this.unitsAvailable = unitsAvailable;
    }
}
