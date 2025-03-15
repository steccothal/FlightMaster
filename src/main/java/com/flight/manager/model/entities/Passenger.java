package com.flight.manager.model.entities;

import jakarta.persistence.*;

@Entity
public class Passenger extends Person {

    private String frequentFlyerId;

    public String getFrequentFlyerId() {
        return frequentFlyerId;
    }

    public void setFrequentFlyerId(String frequentFlyerId) {
        this.frequentFlyerId = frequentFlyerId;
    }
}
