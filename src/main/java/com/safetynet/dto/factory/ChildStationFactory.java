package com.safetynet.dto.factory;

import com.safetynet.dto.ChildStationDTO;
import com.safetynet.dto.PersonsShortDTO;
import com.safetynet.dto.UtilsDTO;
import com.safetynet.model.Persons;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class ChildStationFactory {

    public ChildStationDTO createChildStationDTO(List<Persons> personsLive) {
        ChildStationDTO child = new ChildStationDTO();

        UtilsDTO utilsDTO = new UtilsDTO();
        List<PersonsShortDTO> adultList = new ArrayList<>();
        List<PersonsShortDTO> childList = new ArrayList<>();
        personsLive.forEach(persons ->
                {
                    utilsDTO.calculAge(persons.getMedicalrecords().getBirthdate())
                            .ifPresent(age->
                            {
                                PersonsShortDTO shortPerson = new PersonShortFactory().createPersonsShort(persons.getFirstName(), persons.getLastName(),age);

                                utilsDTO.adultOrNot(persons.getMedicalrecords().getBirthdate())
                                        .ifPresent(util ->
                                        {
                                            if (!util) {
                                                childList.add(shortPerson);
                                            } else {
                                                adultList.add(shortPerson);
                                            }
                                        });
                            });
                }

        );
        if (childList.isEmpty()) {
            return child;
        } else {
            child.setChildren(childList);
            child.setParents(adultList);
            return child;
        }
    }
}
