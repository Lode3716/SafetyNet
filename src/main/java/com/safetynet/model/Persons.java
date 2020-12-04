package com.safetynet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
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