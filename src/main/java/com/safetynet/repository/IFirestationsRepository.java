package com.safetynet.repository;

import com.safetynet.model.Firestations;
import com.safetynet.model.Persons;

import java.util.List;

public interface IFirestationsRepository extends BuisnessRepo<Firestations>{

    List<Firestations> personsBelongFirestation(String idSation);

    List<Persons> personsAdress(String station);

    void constructFirestation(Firestations firestation);
}
