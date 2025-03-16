package com.flight.manager.utils;

import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import com.flight.manager.config.JPAUtil;
import com.flight.manager.model.AirplaneStatus;
import com.flight.manager.model.entities.*;

public class TestDatabasePopulator {

    public static final String THREE_PASSENGERS_FLIGHT_CODE = "FL5678";

    public static void populateDatabase() {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();

        Airplane airplane1 = createAirplane("Boeing 737", "Airways", 180, AirplaneStatus.IN_SERVICE);
        Airplane airplane2 = createAirplane("Airbus A320", "Sky Airlines", 160, AirplaneStatus.IN_SERVICE);
        Airplane airplane3 = createAirplane("Pipistrel Virus SW 121", "Sky Airlines", 2, AirplaneStatus.IN_SERVICE);
        em.persist(airplane1);
        em.persist(airplane2);
        em.persist(airplane3);

        Flight flight1 = createFlight("FL1234", LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(3), airplane1);
        Flight flight2 = createFlight(THREE_PASSENGERS_FLIGHT_CODE, LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2).plusHours(4), airplane2);
        Flight flight3 = createFlight("FL9678", LocalDateTime.now().plusDays(9), LocalDateTime.now().plusDays(9).plusHours(4), airplane3);
        em.persist(flight1);
        em.persist(flight2);
        em.persist(flight3);

        Passenger passenger1 = createPassengerWithPassport("John", "Doe", "johndoe@example.com", 1977, Calendar.MAY, 23, "A1234567", "USA");
        Passenger passenger2 = createPassengerWithPassport("Alice", "Smith", "alice.smith@example.com", 1985, Calendar.APRIL, 10, "B2345678", "UK");
        Passenger passenger3 = createPassengerWithPassport("Bob", "Johnson", "bob.johnson@example.com", 1990, Calendar.JUNE, 5, "C3456789", "Canada");
        Passenger passenger4 = createPassengerWithPassport("Emma", "Brown", "emma.brown@example.com", 2000, Calendar.NOVEMBER, 15, "D4567890", "Germany");

        em.persist(passenger1);
        em.persist(passenger2);
        em.persist(passenger3);
        em.persist(passenger4);

        flight1.getPassengers().add(passenger1);
        flight1.getPassengers().add(passenger2);
        flight2.getPassengers().add(passenger1);
        flight2.getPassengers().add(passenger3);
        flight2.getPassengers().add(passenger4);
        flight3.getPassengers().add(passenger1);
        flight3.getPassengers().add(passenger4);

        em.persist(createPilotWithPassport("James", "Wilson", "james.wilson@example.com", 1975, Calendar.FEBRUARY, 15, "E5678901", "USA", "PL12345", 20));
        em.persist(createPilotWithPassport("Laura", "Adams", "laura.adams@example.com", 1980, Calendar.MARCH, 25, "F6789012", "UK", "PL67890", 18));

        em.persist(createCrewMemberWithPassport("Emily", "Johnson", "emily.johnson@example.com", 1988, Calendar.JANUARY, 10, "I9012345", "France", "Cabin Crew", 10));
        em.persist(createCrewMemberWithPassport("Daniel", "White", "daniel.white@example.com", 1992, Calendar.SEPTEMBER, 3, "J0123456", "Italy", "Cabin Crew", 8));

        em.getTransaction().commit();
        em.close();
    }

    private static Airplane createAirplane(String model, String airline, int capacity, AirplaneStatus status) {
        Airplane airplane = new Airplane();
        airplane.setModel(model);
        airplane.setAirline(airline);
        airplane.setCapacity(capacity);
        airplane.setStatus(status);
        return airplane;
    }

    private static Flight createFlight(String code, LocalDateTime departure, LocalDateTime arrival, Airplane airplane) {
        Flight flight = new Flight();
        flight.setFlightCode(code);
        flight.setDepartureTime(departure);
        flight.setArrivalTime(arrival);
        flight.setAirplane(airplane);
        return flight;
    }

    private static Passenger createPassengerWithPassport(String firstName, String lastName, String email, int year, int month, int day, String passportNumber, String nationality) {
        Passenger passenger = new Passenger();
        passenger.setFirstName(firstName);
        passenger.setLastName(lastName);
        passenger.setEmail(email);
        passenger.setBirthDate(createDate(year, month, day));

        Passport passport = new Passport();
        passport.setNumber(passportNumber);
        passport.setNationality(nationality);
        passport.setPerson(passenger);
        passenger.setPassport(passport);

        return passenger;
    }

    private static Pilot createPilotWithPassport(String firstName, String lastName, String email, int year, int month, int day, String passportNumber, String nationality, String license, int experience) {
        Pilot pilot = new Pilot();
        pilot.setFirstName(firstName);
        pilot.setLastName(lastName);
        pilot.setEmail(email);
        pilot.setBirthDate(createDate(year, month, day));
        pilot.setFlightLicense(license);
        pilot.setYearsExperience(experience);

        Passport passport = new Passport();
        passport.setNumber(passportNumber);
        passport.setNationality(nationality);
        passport.setPerson(pilot);
        pilot.setPassport(passport);

        return pilot;
    }

    private static CrewMember createCrewMemberWithPassport(String firstName, String lastName, String email, int year, int month, int day, String passportNumber, String nationality, String role, int experience) {
        CrewMember crew = new CrewMember();
        crew.setFirstName(firstName);
        crew.setLastName(lastName);
        crew.setEmail(email);
        crew.setBirthDate(createDate(year, month, day));
        crew.setRole(role);
        crew.setYearsExperience(experience);

        Passport passport = new Passport();
        passport.setNumber(passportNumber);
        passport.setNationality(nationality);
        passport.setPerson(crew);
        crew.setPassport(passport);

        return crew;
    }

    private static Date createDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, 0, 0, 0);
        return calendar.getTime();
    }
}
