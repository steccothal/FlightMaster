package com.flight.manager.main;

import com.flight.manager.config.JPAUtil;
import com.flight.manager.model.AirplaneStatus;
import com.flight.manager.model.entities.*;
import com.flight.manager.repositories.GenericRepository;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class DatabaseInitializer {

    private static final GenericRepository<Airplane, Long> airplaneRepository = new GenericRepository<>(Airplane.class) {};
    private static final GenericRepository<Flight, Long> flightRepository = new GenericRepository<>(Flight.class) {};
    private static final GenericRepository<Passenger, Long> passengerRepository = new GenericRepository<>(Passenger.class) {};
    private static final GenericRepository<Pilot, Long> pilotRepository = new GenericRepository<>(Pilot.class) {};
    private static final GenericRepository<CrewMember, Long> crewRepository = new GenericRepository<>(CrewMember.class) {};
    private static final GenericRepository<Passport, String> passportRepository = new GenericRepository<>(Passport.class) {};

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(JPAUtil::close));
        DatabaseInitializer.initialize();
        System.out.println("Database populated successfully!");
    }

    public static void initialize() {
        Airplane airplane1 = createAirplane("Boeing 737", "Airways", 180, AirplaneStatus.IN_SERVICE);
        Airplane airplane2 = createAirplane("Airbus A320", "Sky Airlines", 160, AirplaneStatus.IN_SERVICE);
        airplaneRepository.save(airplane1);
        airplaneRepository.save(airplane2);

        Flight flight1 = createFlight("FL1234", LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(3), airplane1);
        Flight flight2 = createFlight("FL5678", LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2).plusHours(4), airplane2);
        flightRepository.save(flight1);
        flightRepository.save(flight2);

        Passenger passenger1 = createPassenger("John", "Doe", "johndoe@example.com", 1977, Calendar.MAY, 23);
        Passenger passenger2 = createPassenger("Alice", "Smith", "alice.smith@example.com", 1985, Calendar.APRIL, 10);
        Passenger passenger3 = createPassenger("Bob", "Johnson", "bob.johnson@example.com", 1990, Calendar.JUNE, 5);
        Passenger passenger4 = createPassenger("Emma", "Brown", "emma.brown@example.com", 2000, Calendar.NOVEMBER, 15);

        passengerRepository.save(passenger1);
        passengerRepository.save(passenger2);
        passengerRepository.save(passenger3);
        passengerRepository.save(passenger4);

        assignPassport(passenger1, "A1234567", "USA");
        assignPassport(passenger2, "B2345678", "UK");
        assignPassport(passenger3, "C3456789", "Canada");
        assignPassport(passenger4, "D4567890", "Germany");

        flight1.getPassengers().add(passenger1);
        flight1.getPassengers().add(passenger2);
        flight2.getPassengers().add(passenger3);
        flight2.getPassengers().add(passenger4);
        flightRepository.update(flight1);
        flightRepository.update(flight2);

        Pilot pilot1 = createPilot("James", "Wilson", "james.wilson@example.com", 1975, Calendar.FEBRUARY, 15, "PL12345", 20);
        Pilot pilot2 = createPilot("Laura", "Adams", "laura.adams@example.com", 1980, Calendar.MARCH, 25, "PL67890", 18);
        Pilot pilot3 = createPilot("Michael", "Scott", "michael.scott@example.com", 1978, Calendar.DECEMBER, 5, "PL23456", 22);
        Pilot pilot4 = createPilot("Sarah", "Parker", "sarah.parker@example.com", 1982, Calendar.JULY, 19, "PL34567", 17);

        pilotRepository.save(pilot1);
        pilotRepository.save(pilot2);
        pilotRepository.save(pilot3);
        pilotRepository.save(pilot4);

        assignPassport(pilot1, "E5678901", "USA");
        assignPassport(pilot2, "F6789012", "UK");
        assignPassport(pilot3, "G7890123", "Canada");
        assignPassport(pilot4, "H8901234", "Germany");

        CrewMember crew1 = createCrewMember("Emily", "Johnson", "emily.johnson@example.com", 1988, Calendar.JANUARY, 10, "Cabin Crew", 10);
        CrewMember crew2 = createCrewMember("Daniel", "White", "daniel.white@example.com", 1992, Calendar.SEPTEMBER, 3, "Cabin Crew", 8);
        CrewMember crew3 = createCrewMember("Sophia", "Martinez", "sophia.martinez@example.com", 1991, Calendar.MAY, 20, "Cabin Crew", 12);
        CrewMember crew4 = createCrewMember("Ethan", "Garcia", "ethan.garcia@example.com", 1993, Calendar.OCTOBER, 8, "Cabin Crew", 9);

        crewRepository.save(crew1);
        crewRepository.save(crew2);
        crewRepository.save(crew3);
        crewRepository.save(crew4);

        assignPassport(crew1, "I9012345", "France");
        assignPassport(crew2, "J0123456", "Italy");
        assignPassport(crew3, "K1234567", "Spain");
        assignPassport(crew4, "L2345678", "Portugal");
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

    private static Passenger createPassenger(String firstName, String lastName, String email, int year, int month, int day) {
        Passenger passenger = new Passenger();
        passenger.setFirstName(firstName);
        passenger.setLastName(lastName);
        passenger.setEmail(email);
        passenger.setBirthDate(createDate(year, month, day));
        return passenger;
    }

    private static Pilot createPilot(String firstName, String lastName, String email, int year, int month, int day, String license, int experience) {
        Pilot pilot = new Pilot();
        pilot.setFirstName(firstName);
        pilot.setLastName(lastName);
        pilot.setEmail(email);
        pilot.setBirthDate(createDate(year, month, day));
        pilot.setFlightLicense(license);
        pilot.setYearsExperience(experience);
        return pilot;
    }

    private static CrewMember createCrewMember(String firstName, String lastName, String email, int year, int month, int day, String role, int experience) {
        CrewMember crew = new CrewMember();
        crew.setFirstName(firstName);
        crew.setLastName(lastName);
        crew.setEmail(email);
        crew.setBirthDate(createDate(year, month, day));
        crew.setRole(role);
        crew.setYearsExperience(experience);
        return crew;
    }

    private static void assignPassport(Person person, String passportNumber, String nationality) {
        Passport passport = new Passport();
        passport.setNumber(passportNumber);
        passport.setNationality(nationality);
        passport.setPerson(person);
        person.setPassport(passport);
        passportRepository.save(passport);
    }

    private static Date createDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, 0, 0, 0);
        return calendar.getTime();
    }
}
