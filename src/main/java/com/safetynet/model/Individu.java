package com.safetynet.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Individu {

    private Integer idIdentity;
    private String lastName;
    private String firstName;
    private LocalDate birthdate;

}
