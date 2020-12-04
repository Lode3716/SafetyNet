package com.safetynet.dto;

import com.googlecode.jmapper.annotations.JGlobalMap;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JGlobalMap
public class PersonsDto {

    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String zip;
    private String phone;
    private String email;
}
