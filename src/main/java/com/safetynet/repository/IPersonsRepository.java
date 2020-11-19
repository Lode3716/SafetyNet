package com.safetynet.repository;

import com.safetynet.model.Persons;

import java.util.List;
import java.util.Optional;

public interface IPersonsRepository extends BuisnessRepo<Persons> {

    Optional<Persons> findByElements(String nom, String prenom);

    Optional<List<Persons>> searchAllName(String nom, String prenom);

    /**
     * Create a person with associate classes
     * @param persons
     * @return person
     */
    Persons contrustPersons(Persons persons);

    /**
     * Update to person with associate classes
     *
     * @param persons to update
     * @return person : update
     */
    Persons updateContrustPersons(Persons oldPerson,Persons newPerson);

    /**
     * Associates a person and medical for the creation of the class person
     *
     * @param persons
     */
    void associatePersonAndMedical(Persons persons);

    /**
     * Associates a person and firestation for the creation of the class person
     *
     * @param persons
     */
    void associatePersonAndFirestation(Persons persons);

    /**
     * Delete the association between a person and medical
     *
     * @param person : delete
     */
    void deletePersonAndMedical(Persons person);

    /**
     * Associates a person and firestation for the creation of the class person
     *
     * @param person : delete
     */
    void deletePersonAndFirestation(Persons person);

    /**
     * Search all emails of people living in the city
     * @param city
     * @return list e-mail
     */
    Optional<List<String>> searchEmailCity(String city);

    /**
     * Check if mail exist
     * @param personsEmailList
     * @param email
     * @return Ture if mail exist
     */
    boolean emailExist(List<String> personsEmailList, String email);
}
