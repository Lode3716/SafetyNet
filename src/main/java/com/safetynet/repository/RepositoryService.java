package com.safetynet.repository;

import com.safetynet.model.Persons;

public interface RepositoryService {

    FirestationsRepository getFirestationsRepository();
    MedicalrecordsRepository getMedicalrecordsRepository();
    PersonsRepository getPersonsRepository();
}
