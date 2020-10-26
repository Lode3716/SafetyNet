package com.safetynet.dto;

import lombok.Data;

import java.util.List;

@Data
public class PersonsMedicalsDTO {

    private String Station;
    private String lastName;
    private String phone;
    private Integer age;
    private List<String> medications;
    private List<String> allergies;
}
