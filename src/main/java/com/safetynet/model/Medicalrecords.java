package com.safetynet.model;

import lombok.Data;

import java.util.List;

@Data
public class Medicalrecords{

    private Individu individu;
    private List<Medication> medications;
    private List<Allergie> allergies;


}
