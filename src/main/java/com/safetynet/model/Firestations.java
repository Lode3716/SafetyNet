package com.safetynet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Firestations implements Serializable {

    private String station;
    private String address;
    @JsonIgnore
    private List<Persons> personsList;

    public Firestations() {
    }

    public Firestations(String station, String address) {
        this.station = station;
        this.address = address;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Persons> getPersonsList() {
        return personsList;
    }

    public void setPersonsList(List<Persons> personsList) {
        this.personsList = personsList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Firestations)) return false;
        Firestations that = (Firestations) o;
        return getStation().equals(that.getStation()) &&
                getAddress().equals(that.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStation(), getAddress());
    }

    @Override
    public String toString() {
        return "Firestations{" +
                "station='" + station + '\'' +
                ", address='" + address + '\'' +
                ", personsList=" + personsList +
                '}';
    }
}
