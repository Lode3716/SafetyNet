package com.safetynet.repository;

import com.safetynet.dao.Database;
import com.safetynet.model.Firestations;
import com.safetynet.model.Persons;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Log4j2
@Repository
public class FirestationsRepository implements IFirestationsRepository {

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
            constructFirestation(firestations);
            return Optional.of(firestations);
        }
        return Optional.of(firestations);
    }

    @Override
    public boolean delete(Firestations firestation) {
        if (exist(firestation)) {
            log.info("Repository before firestation delete : {}" + database.getFirestationsList().size());

            database.getFirestationsList()
                    .stream()
                    .filter(search -> firestation.getAddress().equals(search.getAddress()) && firestation.getStation().equals(search.getStation()))
                    .findFirst()
                    .ifPresent(fire -> database.getFirestationsList().remove(fire));

            log.info("Repository after firestation delete : {}" + database.getFirestationsList().size());
            log.info("Repository firestation - DELETE");
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public boolean exist(Firestations firestation) {
        return database.getFirestationsList()
                .stream()
                .anyMatch(search -> firestation.getAddress().equals(search.getAddress()) && firestation.getStation().equals(search.getStation()));
    }

    @Override
    public Optional<Firestations> update(Firestations firestation) {
        AtomicReference<Firestations> atoFirestation = new AtomicReference<>();
        database.getFirestationsList().stream()
                .filter(search -> firestation.getAddress().equals(search.getAddress()))
                .findFirst()
                .ifPresent(maj ->
                {
                    log.info("Repository : Mise à jour de la firestation {}", maj.getStation());
                    maj.setStation(firestation.getStation());
                    log.info("Repository : Mise à jour de la firestation {}", firestation.getStation());
                    atoFirestation.set(maj);
                });
        if (Optional.ofNullable(atoFirestation.get()).isPresent()) {
            log.info("Repository : firestation update  - succes ");
            return Optional.of(atoFirestation.get());
        } else {
            log.info("Repository : firestation not exist - not update");
            return Optional.empty();
        }
    }


    @Override
    public List<Firestations> personsBelongFirestation(String idSation) {
        return database.getFirestationsList()
                .stream()
                .filter(stattion -> idSation.equals(stattion.getStation()))
                .collect(Collectors.toList());

    }

    @Override
    public List<Persons> personsAdress(String station) {
        AtomicReference<List<Persons>> atomPersons = new AtomicReference<>();
        database.getFirestationsList()
                .stream()
                .filter(stattion -> station.equals(stattion.getAddress()))
                .forEach(firestations ->
                {
                    Optional.ofNullable(firestations.getPersonsList())
                            .ifPresent(list -> atomPersons.set(list));

                });

        return Optional.ofNullable(atomPersons.get()).orElse(Collections.emptyList());
    }

    @Override
    public void constructFirestation(Firestations firestation) {
        database.getFirestationsList()
                .stream()
                .filter(station -> firestation.getStation().equals(station.getStation()) && firestation.getAddress().equals(station.getAddress()))
                .findFirst()
                .ifPresent(station ->
                {
                    Optional.ofNullable(personsAdress(station.getAddress()))
                            .ifPresent(listPersons -> station.setPersonsList(listPersons));
                    log.debug("Repository after firestation with list person  : {}", station.getPersonsList().size());
                });
    }

}