package com.safetynet.dto.factory;

import com.safetynet.dto.PersonsEmailDTO;

public class PersonEmailFactory {

    public PersonsEmailDTO create(String email) {
        return new PersonsEmailDTO(email);
    }
}
