package com.flight.manager.model.entities;

import jakarta.persistence.*;

@Entity
public class CrewMember extends Person {

    private String role;
    private int yearsExperience;

    public String getRole() {
        return role;
    }

    public int getYearsExperience() {
        return yearsExperience;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setYearsExperience(int yearsExperience) {
        this.yearsExperience = yearsExperience;
    }
}
