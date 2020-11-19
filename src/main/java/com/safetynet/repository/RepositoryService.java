package com.safetynet.repository;


public interface RepositoryService {

    IFirestationsRepository getFirestationsRepository();

    IMedicalRecordsRepository getMedicalrecordsRepository();

    IPersonsRepository getPersonsRepository();
}
