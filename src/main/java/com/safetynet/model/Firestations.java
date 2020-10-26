package com.safetynet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.googlecode.jmapper.annotations.JMap;
import com.safetynet.dto.FirestationsDTO;
import com.safetynet.dto.PersonsBelongFirestationDTO;
import lombok.Data;

import java.util.List;

@Data
public class Firestations {

    @JMap(classes = {PersonsBelongFirestationDTO.class, FirestationsDTO.class})
    private String station;
    private String address;
    @JsonIgnore
    private List<Persons> personsList;


}
