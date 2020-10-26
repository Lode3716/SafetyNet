package com.safetynet.dto.factory;

import com.safetynet.dto.PersonsFirestationDTO;

public class PersonsFirestationFactory {

    public PersonsFirestationDTO creatPersonsFirestation(String firstName, String lastName, String adress) {
        return new PersonsFirestationDTO(firstName, lastName, adress);
    }
}
