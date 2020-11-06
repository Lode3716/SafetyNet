package com.safetynet.dto.factory;

import com.safetynet.dto.PersonsFirestationDTO;

public class PersonsFirestationFactory {

    public PersonsFirestationDTO creatPersonsFirestation(String firstName, String lastName, String adress, String phone) {
        return new PersonsFirestationDTO(firstName, lastName, adress, phone);
    }
}
