package com.safetynet.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonDeserialize(builder = Allergie.AllergieBuilder.class)
public class Allergie {

    private String name;

    @Builder
    public Allergie(@JsonProperty("allergie") String name) {
        this.name = name;
    }
}
