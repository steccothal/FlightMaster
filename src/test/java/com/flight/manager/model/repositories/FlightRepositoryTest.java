package com.flight.manager.model.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import com.flight.manager.model.entities.Flight;
import com.flight.manager.repositories.FlightRepository;
import com.flight.manager.utils.TestDatabasePopulator;
import com.flight.manager.utils.TestWatcher;

@ExtendWith(TestWatcher.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FlightRepositoryTest {

    private FlightRepository flightRepository;

    @BeforeAll
    void setup() {
        flightRepository = new FlightRepository();
        TestDatabasePopulator.populateDatabase();
    }

    @Test
    void testFindById() {
        Flight flight = flightRepository.findById(1L);
        assertNotNull(flight, "Flight with ID 1 should exist");
        assertEquals("FL1234", flight.getFlightCode(), "Flight ID 1 should have code FL1234");
    }

    @Test
    void testFindAll() {
        List<Flight> flights = flightRepository.findAll();
        assertEquals(3, flights.size(), "There should be exactly 3 flights in the database");
    }

    @Test
    void testFindFilghtsWithemptySeats() {
        List<Flight> emptyFlights = flightRepository.findFlightsWithEmptySeats();
        assertEquals(2, emptyFlights.size(), "There should be 2 flights with empty seats");
    }

    @Test
    void testFindCurrentlyFlyingFlights() {
        List<Flight> flyingFlights = flightRepository.findCurrentlyFyingFlights();
        assertEquals(1,flyingFlights.size(), "There should be 1 currently flying flight initially");
    }

    @Test
    void testFindByPassengerCountGreaterThan() {
        List<Flight> flights = flightRepository.findByPassengerCountGreaterThan(2);
        assertEquals(1, flights.size(), "Two flights should have more than 1 passenger");
    }

    @Test
    void testFindByAirplaneModel() {
        List<Flight> flights = flightRepository.findByAirplaneModel("Boeing 737");
        assertEquals(1, flights.size(), "There should be 1 flight with a Boeing 737");
    }

    @Test
    void testCountFlightsByAirline() {
      List<Object[]> results = flightRepository.countFlightsByAirline();
  
      assertEquals(2, results.size(), "There should be counts for 2 airlines");
  
      for (Object[] row : results) {
          String airline = (String) row[0];
          Long count = ((Number) row[1]).longValue();
  
          if (airline.equals("Airways")) {
              assertEquals(1, count, "Airways should have exactly 1 flight");
          } else if (airline.equals("Sky Airlines")) {
              assertEquals(2, count, "Sky Airlines should have exactly 2 flights");
          } else {
              fail("Unexpected airline in results: " + airline);
          }
      }
  }
}
