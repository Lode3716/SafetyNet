package com.safetynet.repository;

import com.safetynet.dao.Database;
import com.safetynet.dto.ChildStationDTO;
import com.safetynet.model.Firestations;
import com.safetynet.model.Persons;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Log4j2
@Repository
public class FirestationsRepository implements BuisnessRepo<Firestations> {


    @Autowired
    Database database;


    @Override
    public List<Firestations> findAll() {
        return database.getFirestationsList();
    }

    @Override
    public Optional<Firestations> add(Firestations firestations) {
        if (exist(firestations) == Boolean.FALSE) {
            database.getFirestationsList().add(firestations);
            return Optional.of(firestations);
        }
        return Optional.empty();
    }

    @Override
    public boolean delete(Firestations firestation) {

        AtomicBoolean retour = new AtomicBoolean(Boolean.FALSE);
        database.getFirestationsList()
                .stream()
                .filter(search -> firestation.getAddress().equals(search.getAddress()) || firestation.getStation().equals(search.getStation()))
                .findFirst()
                .ifPresent(fire ->
                {

                    log.info("Mapping Station supprimer : " + firestation);

                    if (firestation.getAddress().isBlank()) {
                        fire.setAddress("");
                    }
                    if (firestation.getStation().isBlank()) {
                        fire.setStation("");
                    }
                    retour.set(true);
                });

        return retour.get();

    }

    @Override
    public boolean exist(Firestations firestation) {
        return database.getFirestationsList()
                .stream()
                .anyMatch(search -> firestation.getAddress().equals(search.getAddress()) && firestation.getStation().equals(search.getStation()));
    }

    @Override
    public Optional<Firestations> update(Firestations firestation) {

        database.getFirestationsList().stream()
                .filter(search -> firestation.getAddress().equals(search.getAddress()))
                .findFirst()
                .ifPresent(maj ->
                {
                    log.info("Mise à jour de la firestation : " + firestation.getStation());
                    maj.setStation(firestation.getStation());
                });
        return Optional.of(firestation);
    }

    public List<Firestations> personsBelongFirestation(String idSation) {
        return database.getFirestationsList()
                .stream()
                .filter(stattion -> idSation.equals(stattion.getStation()))
                .collect(Collectors.toList());

    }
    public List<Persons> personsAdress(String station) {
        return database.getFirestationsList()
                .stream()
                .filter(stattion -> station.equals(stattion.getAddress()))
                .map(Firestations::getPersonsList)
                .findFirst()
                .get();
    }

}