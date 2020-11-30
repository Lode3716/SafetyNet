package com.safetynet.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PersonsMedicalsDTO {

    private String lastName;
    private String phone;
    private Integer age;
    private List<String> medications;
    private List<String> allergies;
}
