package com.safetynet.repository;

import com.safetynet.model.Firestations;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Log4j2
@Repository
public class FirestationsRepository implements BuisnessRepo<Firestations> {

    @Getter
    private List<Firestations> firestationsList;

    @Autowired
    Database database;

    public FirestationsRepository() {
    }


    @Override
    public List<Firestations> findAll() {
        return database.getFirestationsList();
    }

    @Override
    public Optional<Firestations> add(Firestations firestations) {
        return Optional.empty();
    }

    @Override
    public boolean delete(Firestations firestations) {
        return false;
    }

    @Override
    public boolean exist(Firestations objet) {
        return false;
    }

    @Override
    public Optional<Firestations> update(Firestations objet) {
        return Optional.empty();
    }
}
