package com.safetynet.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PersonsMedicationAdresseDTO {

    private String lastName;
    private String adress;
    private Integer age;
    private String email;
    private List<String> medications;
    private List<String> allergies;
}
