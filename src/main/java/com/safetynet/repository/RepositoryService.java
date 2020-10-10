package com.safetynet.repository;

import org.springframework.beans.factory.annotation.Autowired;


public interface RepositoryService {

    FirestationsRepository getFirestationsRepository();
    MedicalrecordsRepository getMedicalrecordsRepository();
    PersonsRepository getPersonsRepository();
}
