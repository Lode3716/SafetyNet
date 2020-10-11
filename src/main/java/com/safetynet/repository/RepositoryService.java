package com.safetynet.repository;


public interface RepositoryService {

    FirestationsRepository getFirestationsRepository();

    MedicalrecordsRepository getMedicalrecordsRepository();

    PersonsRepository getPersonsRepository();
}
