package com.safetynet.dto.factory;

import com.safetynet.dto.PersonsShortDTO;

public class PersonShortFactory {

    public PersonsShortDTO createPersonsShort(String firstName, String lastName, Integer age) {
        return new PersonsShortDTO(firstName, lastName, age);
    }
}
