package com.safetynet.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@JsonDeserialize(builder = LocationMap.LocationMapBuilder.class)
@JsonRootName(value = "locationMap")
public class LocationMap implements Serializable {

    private String city;
    private String address;

    public LocationMap() {
    }

    @Builder
    public LocationMap (@JsonProperty("city") String city, @JsonProperty("address") String address) {
        this.city = city;
        this.address = address;
    }
}
