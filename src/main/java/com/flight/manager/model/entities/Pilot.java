package com.flight.manager.model.entities;

import jakarta.persistence.*;

@Entity
public class Pilot extends Person {

    private String flightLicense;
    private int yearsExperience;

    public String getFlightLicense() {
        return flightLicense;
    }

    public int getYearsExperience() {
        return yearsExperience;
    }

    public void setFlightLicense(String flightLicense) {
        this.flightLicense = flightLicense;
    }

    public void setYearsExperience(int yearsExperience) {
        this.yearsExperience = yearsExperience;
    }
}
