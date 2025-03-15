package com.flight.manager.model.entities;

import jakarta.persistence.*;
import java.util.Set;

import com.flight.manager.model.AirplaneStatus;

@Entity
@Table(name = "airplanes")
public class Airplane {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String airline;

    private int capacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AirplaneStatus status;

    @OneToMany(mappedBy = "airplane")
    private Set<Flight> flights;

    public Long getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public String getAirline() {
        return airline;
    }

    public int getCapacity() {
        return capacity;
    }

    public AirplaneStatus getStatus() {
        return status;
    }

    public Set<Flight> getFlights() {
        return flights;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setStatus(AirplaneStatus status) {
        this.status = status;
    }
}
