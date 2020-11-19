package com.safetynet.repository;

import com.safetynet.model.Firestations;
import com.safetynet.model.Persons;

import java.util.List;

public interface IFirestationsRepository extends BuisnessRepo<Firestations>{

    /**
     * List all station by id
     * @param idSation
     * @return List firestation
     */
    List<Firestations> personsBelongFirestation(String idSation);

    /**
     * List person live associate at station
     * @param station
     * @return list person
     */
    List<Persons> personsAdress(String station);


    /**
     * Associates List person living at this address of the fire station
     *
     * @param firestation
     */
    void constructFirestation(Firestations firestation);
}
