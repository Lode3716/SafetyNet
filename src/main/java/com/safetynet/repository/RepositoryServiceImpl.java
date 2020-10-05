package com.safetynet.repository;


import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Getter
@Log4j2
public class RepositoryServiceImpl implements RepositoryService {

    @Override
    public FirestationsRepository getFirestationsRepository() {
        return new FirestationsRepository();
    }

    @Override
    public MedicalrecordsRepository getMedicalrecordsRepository() {
        return new MedicalrecordsRepository();
    }

    @Override
    public PersonsRepository getPersonsRepository() {
        return new PersonsRepository();
    }
}
