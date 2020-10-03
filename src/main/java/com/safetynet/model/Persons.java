package com.safetynet.model;

import lombok.*;

@Getter
@ToString(callSuper=true)
@EqualsAndHashCode
public class Persons extends Individu {

    private String phone;
    private String zip;
    private String email;
    private LocationMap locationMap;

    @Builder
    public Persons(String lastName,String firstName,String phone, String zip, String email, LocationMap locationMap) {
        super(lastName,firstName);
        this.phone = phone;
        this.zip = zip;
        this.email = email;
        this.locationMap = locationMap;
    }

}
