package com.safetynet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PersonsFirestationDTO {

    private String firstName;
    private String lastName;
    private String address;
    private String phone;


}
