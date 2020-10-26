package com.safetynet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PersonsShortDTO {

    private String firstName;
    private String lastName;
    private Integer age;
}
