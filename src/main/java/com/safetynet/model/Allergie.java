package com.safetynet.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class Allergie {

    private String name;

    public Allergie(@JsonProperty("allergie") String name) {
        this.name = name;
    }
}
