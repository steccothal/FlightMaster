package com.flight.manager.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "passports")
public class Passport {

    @Id
    @Size(min = 8, max = 8)
    private String number;

    @Column(nullable = false)
    private String nationality;

    @OneToOne
    @JoinColumn(name = "person_id", nullable = true)
    private Person person;

    public String getNumber() {
        return number;
    }

    public String getNationality() {
        return nationality;
    }

    public Person getPerson() {
        return person;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
