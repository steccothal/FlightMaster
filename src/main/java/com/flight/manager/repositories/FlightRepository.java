package com.flight.manager.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import java.util.List;

import com.flight.manager.config.JPAUtil;
import com.flight.manager.model.entities.Airplane;
import com.flight.manager.model.entities.Flight;

public class FlightRepository extends GenericRepository<Flight, Long> {

    public FlightRepository() {
        super(Flight.class);
    }

    public List<Flight> findFlightsWithEmptySeats() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Flight> flights = em.createNamedQuery(Flight.FIND_FLIGHTS_WITH_EMPTY_SEATS, Flight.class)
                .getResultList();
        em.close();
        return flights;
    }

    public List<Flight> findCurrentlyFyingFlights() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Flight> flights = em.createNamedQuery(Flight.FIND_CURRENTLY_FLYING, Flight.class)
                .getResultList();
        em.close();
        return flights;
    }

    public List<Flight> findByPassengerCountGreaterThan(int minPassengers) {
        EntityManager em = JPAUtil.getEntityManager();
        List<Flight> flights = em.createQuery(
                "SELECT f FROM Flight f WHERE SIZE(f.passengers) > :minPassengers", Flight.class)
                .setParameter("minPassengers", minPassengers)
                .getResultList();
        em.close();
        return flights;
    }

    public List<Flight> findByAirplaneModel(String model) {
        EntityManager em = JPAUtil.getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Flight> query = cb.createQuery(Flight.class);
        Root<Flight> root = query.from(Flight.class);
        Join<Flight, Airplane> airplaneJoin = root.join("airplane");
        query.select(root).where(cb.equal(airplaneJoin.get("model"), model));
        List<Flight> flights = em.createQuery(query).getResultList();
        em.close();
        return flights;
    }

    public List<Object[]> countFlightsByAirline() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Object[]> results = em.createNativeQuery(
                "SELECT a.airline, COUNT(f.id) FROM flights f " +
                "JOIN airplanes a ON f.airplane_id = a.id GROUP BY a.airline")
                .getResultList();
        em.close();
        return results;
    }
}
