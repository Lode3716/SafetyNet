package com.safetynet.dto;

import com.googlecode.jmapper.annotations.JGlobalMap;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JGlobalMap
public class MedicalRecordsDTO {

    private String firstName;
    private String lastName;
    private String birthdate;
    private List<String> medications;
    private List<String> allergies;
}
