package com.safetynet.model;

import lombok.Data;

@Data
public class Firestations {

    private String station;
    private String address;

    public Firestations() {
    }

    public Firestations(String station, String address) {
        this.station = station;
        this.address = address;
    }
}
