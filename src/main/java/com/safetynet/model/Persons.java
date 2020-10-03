package com.safetynet.model;

import lombok.*;

@Data
public class Persons extends Individu {

    private String phone;
    private String zip;
    private String email;
    private LocationMap locationMap;


}
