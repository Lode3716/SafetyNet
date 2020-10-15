package com.safetynet.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString()
public class Individu {


    private String firstName;
    private String lastName;

    public Individu() {
    }

    public Individu(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
