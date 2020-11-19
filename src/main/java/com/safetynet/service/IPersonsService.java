package com.safetynet.service;

import com.safetynet.dto.PersonsDto;
import com.safetynet.dto.PersonsEmailDTO;
import com.safetynet.dto.PersonsMedicationAdresseDTO;
import com.safetynet.model.Persons;

import java.util.List;
import java.util.Optional;

public interface IPersonsService
{
    List<PersonsDto> getReadAllpersons();

    Optional<PersonsDto> addPersons(PersonsDto persons);

    boolean deletePerson(PersonsDto person);

    Optional<PersonsDto> updatePerson(PersonsDto personsDto);

    /**
     * Construct List all the people with the same names
     *
     * @param firstName
     * @param lastName
     * @return list person with sames names
     */
    Optional<List<PersonsMedicationAdresseDTO>> getPersonInfo(String firstName, String lastName);

    /**
     * Construct List the email addresses of all the inhabitants of the city
     *
     * @param city
     * @return List the email
     */
    Optional<List<PersonsEmailDTO>> getPersonsEmail(String city);

    Optional<Persons> getFindPerson(String nom, String prenom);
}
