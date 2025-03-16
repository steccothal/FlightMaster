package com.flight.manager.model.repositories;

import static org.junit.jupiter.api.Assertions.*;

import com.flight.manager.model.entities.Airplane;
import com.flight.manager.repositories.GenericRepository;
import com.flight.manager.utils.TestWatcher;
import com.flight.manager.model.AirplaneStatus;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

@ExtendWith(TestWatcher.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GenericRepositoryTest {

    private GenericRepository<Airplane, Long> airplaneRepository;

    @BeforeAll
    void setup() {
        airplaneRepository = new GenericRepository<>(Airplane.class) {};
    }

    @Test
    void testSaveAndFindById() {
        Airplane airplane = new Airplane();
        airplane.setModel("Boeing 747");
        airplane.setAirline("Global Airlines");
        airplane.setCapacity(300);
        airplane.setStatus(AirplaneStatus.IN_SERVICE);

        airplaneRepository.save(airplane);
        Airplane retrieved = airplaneRepository.findById(airplane.getId());

        assertNotNull(retrieved, "Saved airplane should be retrievable by ID");
        assertEquals("Boeing 747", retrieved.getModel(), "Airplane model should match");
    }

    @Test
    void testFindAll() {
      Airplane airplane = new Airplane();
      airplane.setModel("Boeing 747");
      airplane.setAirline("Hibernate Airlines");
      airplane.setCapacity(300);
      airplane.setStatus(AirplaneStatus.IN_SERVICE);

      airplaneRepository.save(airplane);
        List<Airplane> airplanes = airplaneRepository.findAll();
        assertTrue(airplanes.size() >= 1, "There should be at least one airplane in the database");
    }

    @Test
    void testUpdate() {
        Airplane airplane = new Airplane();
        airplane.setModel("Boeing 757");
        airplane.setAirline("Airways");
        airplane.setCapacity(250);
        airplane.setStatus(AirplaneStatus.IN_SERVICE);

        airplaneRepository.save(airplane);

        airplane.setCapacity(280);
        airplaneRepository.update(airplane);

        Airplane updated = airplaneRepository.findById(airplane.getId());
        assertEquals(280, updated.getCapacity(), "Updated airplane capacity should be 280");
    }

    @Test
    void testDelete() {
        Airplane airplane = new Airplane();
        airplane.setModel("Boeing 767");
        airplane.setAirline("Test Airlines");
        airplane.setCapacity(180);
        airplane.setStatus(AirplaneStatus.IN_SERVICE);

        airplaneRepository.save(airplane);
        airplaneRepository.delete(airplane.getId());

        Airplane deleted = airplaneRepository.findById(airplane.getId());
        assertNull(deleted, "Deleted airplane should not be retrievable");
    }
}
