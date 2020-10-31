package com.safetynet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString(callSuper = true)
public class Persons extends Individu implements Serializable {

    private String address;
    private String city;
    private String zip;
    private String phone;
    private String email;
    @JsonIgnore
    private List<Firestations> firestations;
    @JsonIgnore
    private Medicalrecords medicalrecords;


}