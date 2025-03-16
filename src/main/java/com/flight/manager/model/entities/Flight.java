package com.flight.manager.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "flights", indexes = {@Index(name = "idx_flight_code", columnList = "flightCode")})
@NamedQueries({
    @NamedQuery(
        name = Flight.FIND_FLIGHTS_WITH_EMPTY_SEATS,
        query = "SELECT f FROM Flight f WHERE SIZE(f.passengers) < f.airplane.capacity"
    ),
    @NamedQuery(
        name = Flight.FIND_CURRENTLY_FLYING, 
        query = "SELECT f FROM Flight f WHERE f.departureTime <= CURRENT_TIMESTAMP AND f.arrivalTime >= CURRENT_TIMESTAMP"
    )
})

public class Flight {

    public static final String FIND_FLIGHTS_WITH_EMPTY_SEATS = "Flight.findEmptySeats";
    public static final String FIND_CURRENTLY_FLYING = "Flight.findCurrentlyFlying";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @Size(min = 5, max = 10)
    private String flightCode;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private LocalDateTime departureTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private LocalDateTime arrivalTime;

    @ManyToMany
    @JoinTable(
        name = "flight_person",
        joinColumns = @JoinColumn(name = "flight_id"),
        inverseJoinColumns = @JoinColumn(name = "person_id")
    )
    private Set<Person> passengers = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "airplane_id", nullable = false)
    private Airplane airplane;

    @Transient
    public boolean hasAvailableSeats() {
        return passengers.size() < airplane.getCapacity() && departureTime.isAfter(LocalDateTime.now().plusHours(12));
    }

    public Long getId() {
        return id;
    }

    public String getFlightCode() {
        return flightCode;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public Set<Person> getPassengers() {
        return passengers;
    }

    public Airplane getAirplane() {
        return airplane;
    }

    public void setFlightCode(String flightCode) {
        this.flightCode = flightCode;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setAirplane(Airplane airplane) {
        this.airplane = airplane;
    }
}
