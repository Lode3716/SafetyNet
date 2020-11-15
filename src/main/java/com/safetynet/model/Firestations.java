package com.safetynet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class Firestations implements Serializable {

    private String station;
    private String address;
    @JsonIgnore
    private List<Persons> personsList;


    public Firestations(String station, String address) {
        this.station = station;
        this.address = address;
    }
}
