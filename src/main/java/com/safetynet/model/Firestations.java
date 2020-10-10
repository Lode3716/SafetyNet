package com.safetynet.model;

import lombok.Data;

import java.util.List;

@Data
public class Firestations {

    private String station;
    private String address;
    private List<Persons> personsList;


}
