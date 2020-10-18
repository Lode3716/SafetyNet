package com.safetynet.repository;


public interface RepositoryService {

    FirestationsRepository getFirestationsRepository();

    MedicalRecordsRepository getMedicalrecordsRepository();

    PersonsRepository getPersonsRepository();
}
