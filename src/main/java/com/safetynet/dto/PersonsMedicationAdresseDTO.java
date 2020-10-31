package com.safetynet.dto;

import lombok.Data;

import java.util.List;

@Data
public class PersonsMedicationAdresseDTO {

    private String lastName;
    private String adress;
    private Integer age;
    private String email;
    private List<String> medications;
    private List<String> allergies;
}
