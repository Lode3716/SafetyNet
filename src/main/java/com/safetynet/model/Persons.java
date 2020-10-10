package com.safetynet.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import java.io.Serializable;

@Data
@ToString(callSuper = true)
@JsonRootName(value = "persons")
public class Persons extends Individu implements Serializable {

    private String address;
    private String city;
    private String zip;
    private String phone;
    private String email;
    @JsonIgnore
    private Firestations firestations;
    @JsonIgnore
    private Medicalrecords medicalrecords;


}