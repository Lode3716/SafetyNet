package com.safetynet.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonDeserialize(builder = Medication.MedicationBuilder.class)

public class Medication {

    private String name;

    @Builder
    public Medication(@JsonProperty("allergie") String name) {
        this.name = name;
    }
}
