package com.safetynet.repository;


public interface RepositoryService {

    IFirestationsRepository getFirestationsRepository();

    MedicalRecordsRepository getMedicalrecordsRepository();

    PersonsRepository getPersonsRepository();
}
