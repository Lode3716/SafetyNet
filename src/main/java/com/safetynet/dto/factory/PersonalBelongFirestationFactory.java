package com.safetynet.dto.factory;

import com.safetynet.dto.PersonsBelongFirestationDTO;
import com.safetynet.dto.PersonsFirestationDTO;
import com.safetynet.dto.UtilsDTO;
import com.safetynet.model.Firestations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class PersonalBelongFirestationFactory {

    @Autowired
    ServiceFactory serviceFactory;

    public PersonsBelongFirestationDTO createPersonFirestation(List<Firestations> firestations) {

        PersonsBelongFirestationDTO newPersFirest = new PersonsBelongFirestationDTO();
        List<PersonsFirestationDTO> personsFirestationDTO = new ArrayList<>();

        UtilsDTO utilsDTO = new UtilsDTO();

        AtomicInteger adult = new AtomicInteger();
        AtomicInteger child = new AtomicInteger();

        firestations.forEach(station ->
                {
                    station.getPersonsList().forEach(persons ->
                            {
                                personsFirestationDTO.add(serviceFactory.getPersonsFirestationFactory().creatPersonsFirestation(persons.getFirstName(), persons.getLastName(), persons.getAddress()));

                                utilsDTO.calculAge(persons.getMedicalrecords().getBirthdate())
                                        .ifPresent(age ->
                                        {
                                            if (age > 19) {
                                                adult.incrementAndGet();
                                            } else {
                                                child.incrementAndGet();
                                            }

                                        });
                            });
                });
        newPersFirest.setListPersons(personsFirestationDTO);
        newPersFirest.setCountAdult(adult.get());
        newPersFirest.setCountChild(child.get());

        return newPersFirest;
    }
}
