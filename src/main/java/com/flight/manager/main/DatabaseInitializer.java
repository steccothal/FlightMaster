package com.flight.manager.main;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Calendar;

import com.flight.manager.config.JPAUtil;
import com.flight.manager.model.AirplaneStatus;
import com.flight.manager.model.entities.*;

public class DatabaseInitializer {

    public static void main(String[] args) {
        DatabaseInitializer.initialize();
        System.out.println("Database populated successfully!");
    }

    public static void initialize() {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Airplane airplane1 = createAirplane("Boeing 737", "Airways", 180, AirplaneStatus.IN_SERVICE);
        Airplane airplane2 = createAirplane("Airbus A320", "Sky Airlines", 160, AirplaneStatus.IN_SERVICE);
        em.persist(airplane1);
        em.persist(airplane2);

        Flight flight1 = createFlight("FL1234", LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(3), airplane1);
        Flight flight2 = createFlight("FL5678", LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2).plusHours(4), airplane2);
        em.persist(flight1);
        em.persist(flight2);

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
        flight2.getPassengers().add(passenger3);
        flight2.getPassengers().add(passenger4);

        Pilot pilot1 = createPilotWithPassport("James", "Wilson", "james.wilson@example.com", 1975, Calendar.FEBRUARY, 15, "E5678901", "USA", "PL12345", 20);
        Pilot pilot2 = createPilotWithPassport("Laura", "Adams", "laura.adams@example.com", 1980, Calendar.MARCH, 25, "F6789012", "UK", "PL67890", 18);
        Pilot pilot3 = createPilotWithPassport("Michael", "Scott", "michael.scott@example.com", 1978, Calendar.DECEMBER, 5, "G7890123", "Canada", "PL23456", 22);
        Pilot pilot4 = createPilotWithPassport("Sarah", "Parker", "sarah.parker@example.com", 1982, Calendar.JULY, 19, "H8901234", "Germany", "PL34567", 17);

        em.persist(pilot1);
        em.persist(pilot2);
        em.persist(pilot3);
        em.persist(pilot4);

        CrewMember crew1 = createCrewMemberWithPassport("Emily", "Johnson", "emily.johnson@example.com", 1988, Calendar.JANUARY, 10, "I9012345", "France", "Cabin Crew", 10);
        CrewMember crew2 = createCrewMemberWithPassport("Daniel", "White", "daniel.white@example.com", 1992, Calendar.SEPTEMBER, 3, "J0123456", "Italy", "Cabin Crew", 8);
        CrewMember crew3 = createCrewMemberWithPassport("Sophia", "Martinez", "sophia.martinez@example.com", 1991, Calendar.MAY, 20, "K1234567", "Spain", "Cabin Crew", 12);
        CrewMember crew4 = createCrewMemberWithPassport("Ethan", "Garcia", "ethan.garcia@example.com", 1993, Calendar.OCTOBER, 8, "L2345678", "Portugal", "Cabin Crew", 9);

        em.persist(crew1);
        em.persist(crew2);
        em.persist(crew3);
        em.persist(crew4);

        tx.commit();
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
    
    private static Flight createFlight(String flightCode, LocalDateTime departureTime, LocalDateTime arrivalTime, Airplane airplane) {
        Flight flight = new Flight();
        flight.setFlightCode(flightCode);
        flight.setDepartureTime(departureTime);
        flight.setArrivalTime(arrivalTime);
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
