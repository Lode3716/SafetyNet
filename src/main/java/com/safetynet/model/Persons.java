package com.safetynet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.googlecode.jmapper.annotations.JMap;
import com.safetynet.dto.FirestationsDTO;
import com.safetynet.dto.PersonsBelongFirestationDTO;
import com.safetynet.dto.PersonsDto;
import com.safetynet.dto.PersonsFirestationDTO;
import lombok.Data;
import lombok.ToString;

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