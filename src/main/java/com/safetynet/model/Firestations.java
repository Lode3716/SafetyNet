package com.safetynet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Firestations implements Serializable {

    private String station;
    private String address;
    @JsonIgnore
    private List<Persons> personsList;


}
