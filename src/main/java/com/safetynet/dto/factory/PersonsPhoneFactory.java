package com.safetynet.dto.factory;

import com.safetynet.dto.PersonsPhoneDTO;
import com.safetynet.model.Firestations;

import java.util.ArrayList;
import java.util.List;

public class PersonsPhoneFactory {

    public List<PersonsPhoneDTO> createPersonsPhone(List<Firestations> firestations) {

        List<PersonsPhoneDTO> personsPhoneList = new ArrayList<>();

        firestations
                .stream()
                .forEach(station ->
                {

                    station.getPersonsList()
                            .forEach(persons ->
                            {
                                if(!phoneExist(personsPhoneList, persons.getPhone())) {
                                    PersonsPhoneDTO personsPhoneDTO = new PersonsPhoneDTO();
                                    personsPhoneDTO.setPhone(persons.getPhone());
                                    personsPhoneList.add(personsPhoneDTO);
                                }
                            });

                });
        return personsPhoneList;
    }

    private boolean phoneExist(List<PersonsPhoneDTO> personsPhoneList,String phone)
    {
        return personsPhoneList.stream()
                .anyMatch(search -> phone.equals(search.getPhone()));
    }
}