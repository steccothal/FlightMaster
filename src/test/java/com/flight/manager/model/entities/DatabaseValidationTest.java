package com.flight.manager.model.entities;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.flight.manager.config.JPAUtil;
import com.flight.manager.utils.TestDatabasePopulator;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DatabaseValidationTest {

    @BeforeAll
    void setup() {        
        TestDatabasePopulator.populateDatabase();
    }

    @Test
    void testAllAirplanesPersisted() {
        EntityManager em = JPAUtil.getEntityManager();
        TypedQuery<Airplane> query = em.createQuery("SELECT a FROM Airplane a", Airplane.class);
        List<Airplane> airplanes = query.getResultList();

        em.close();

        assertEquals(2, airplanes.size(), "There should be exactly 2 airplanes in the database");

        boolean hasBoeing737 = airplanes.stream().anyMatch(a -> a.getModel().equals("Boeing 737"));
        boolean hasAirbusA320 = airplanes.stream().anyMatch(a -> a.getModel().equals("Airbus A320"));

        assertTrue(hasBoeing737, "Database must contain a Boeing 737");
        assertTrue(hasAirbusA320, "Database must contain an Airbus A320");
    }

    @Test
    void testAllFlightsPersisted() {
        EntityManager em = JPAUtil.getEntityManager();
        TypedQuery<Flight> query = em.createQuery("SELECT f FROM Flight f", Flight.class);
        List<Flight> flights = query.getResultList();

        em.close();
        
        assertEquals(2, flights.size(), "There should be exactly 2 flights in the database");

        boolean hasFL1234 = flights.stream().anyMatch(f -> f.getFlightCode().equals("FL1234"));
        boolean hasFL5678 = flights.stream().anyMatch(f -> f.getFlightCode().equals("FL5678"));

        assertTrue(hasFL1234, "Database must contain flight FL1234");
        assertTrue(hasFL5678, "Database must contain flight FL5678");
    }

    @Test
    void testAllPassengersPersisted() {
        EntityManager em = JPAUtil.getEntityManager();
        TypedQuery<Passenger> query = em.createQuery("SELECT p FROM Passenger p", Passenger.class);
        List<Passenger> passengers = query.getResultList();

        em.close();

        assertEquals(4, passengers.size(), "There should be exactly 4 passengers in the database");

        boolean hasJohnDoe = passengers.stream().anyMatch(p -> p.getEmail().equals("johndoe@example.com"));
        boolean hasAliceSmith = passengers.stream().anyMatch(p -> p.getEmail().equals("alice.smith@example.com"));
        boolean hasBobJohnson = passengers.stream().anyMatch(p -> p.getEmail().equals("bob.johnson@example.com"));
        boolean hasEmmaBrown = passengers.stream().anyMatch(p -> p.getEmail().equals("emma.brown@example.com"));

        assertTrue(hasJohnDoe, "Database must contain John Doe");
        assertTrue(hasAliceSmith, "Database must contain Alice Smith");
        assertTrue(hasBobJohnson, "Database must contain Bob Johnson");
        assertTrue(hasEmmaBrown, "Database must contain Emma Brown");
    }

    @Test
    void testAllPilotsPersisted() {
        EntityManager em = JPAUtil.getEntityManager();
        TypedQuery<Pilot> query = em.createQuery("SELECT p FROM Pilot p", Pilot.class);
        List<Pilot> pilots = query.getResultList();

        em.close();

        assertEquals(2, pilots.size(), "There should be exactly 2 pilots in the database");

        boolean hasJamesWilson = pilots.stream().anyMatch(p -> p.getEmail().equals("james.wilson@example.com"));
        boolean hasLauraAdams = pilots.stream().anyMatch(p -> p.getEmail().equals("laura.adams@example.com"));

        assertTrue(hasJamesWilson, "Database must contain pilot James Wilson");
        assertTrue(hasLauraAdams, "Database must contain pilot Laura Adams");
    }

    @Test
    void testAllCrewMembersPersisted() {
        EntityManager em = JPAUtil.getEntityManager();
        TypedQuery<CrewMember> query = em.createQuery("SELECT c FROM CrewMember c", CrewMember.class);
        List<CrewMember> crewMembers = query.getResultList();

        em.close();

        assertEquals(2, crewMembers.size(), "There should be exactly 2 crew members in the database");

        boolean hasEmilyJohnson = crewMembers.stream().anyMatch(c -> c.getEmail().equals("emily.johnson@example.com"));
        boolean hasDanielWhite = crewMembers.stream().anyMatch(c -> c.getEmail().equals("daniel.white@example.com"));

        assertTrue(hasEmilyJohnson, "Database must contain crew member Emily Johnson");
        assertTrue(hasDanielWhite, "Database must contain crew member Daniel White");
    }

    @Test
    void testFlightsHavePassengers() {
        EntityManager em = JPAUtil.getEntityManager();
        TypedQuery<Flight> query = em.createQuery("SELECT f FROM Flight f", Flight.class);
        List<Flight> flights = query.getResultList();

        for (Flight flight : flights) {
            assertEquals(2, flight.getPassengers().size(), "Each flight should have exactly 2 passengers");
        }

        em.close();
    }

    @Test
    void testPassengersHavePassports() {
        EntityManager em = JPAUtil.getEntityManager();
        TypedQuery<Passenger> query = em.createQuery("SELECT p FROM Passenger p", Passenger.class);
        List<Passenger> passengers = query.getResultList();

        em.close();

        for (Passenger passenger : passengers) {
            assertNotNull(passenger.getPassport(), "Each passenger must have a passport");
        }
    }

    @Test
    void testPilotsHavePassports() {
        EntityManager em = JPAUtil.getEntityManager();
        TypedQuery<Pilot> query = em.createQuery("SELECT p FROM Pilot p", Pilot.class);
        List<Pilot> pilots = query.getResultList();

        em.close();

        for (Pilot pilot : pilots) {
            assertNotNull(pilot.getPassport(), "Each pilot must have a passport");
        }
    }

    @Test
    void testCrewMembersHavePassports() {
        EntityManager em = JPAUtil.getEntityManager();
        TypedQuery<CrewMember> query = em.createQuery("SELECT c FROM CrewMember c", CrewMember.class);
        List<CrewMember> crewMembers = query.getResultList();

        em.close();

        for (CrewMember crew : crewMembers) {
            assertNotNull(crew.getPassport(), "Each crew member must have a passport");
        }
    }

    @AfterAll
    void tearDown() {
        EntityManager em = JPAUtil.getEntityManager();

        em.getTransaction().begin();
        em.createQuery("DELETE FROM Flight").executeUpdate();
        em.createQuery("DELETE FROM Passport").executeUpdate();
        em.createQuery("DELETE FROM Passenger").executeUpdate();
        em.createQuery("DELETE FROM Pilot").executeUpdate();
        em.createQuery("DELETE FROM CrewMember").executeUpdate();
        em.createQuery("DELETE FROM Airplane").executeUpdate();
        em.getTransaction().commit();

        em.close();
    }
}
