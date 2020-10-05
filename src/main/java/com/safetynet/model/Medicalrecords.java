package com.safetynet.model;

import lombok.Data;

import java.util.List;

@Data
public class Medicalrecords {

    private String firstName;
    private String lastName;
    private String birthdate;
    private List<String> medications;
    private List<String> allergies;

    public Medicalrecords() {
    }

    public Medicalrecords(String firstName, String lastName, String birthdate, List<String> medications, List<String> allergies) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.medications = medications;
        this.allergies = allergies;
    }
}
