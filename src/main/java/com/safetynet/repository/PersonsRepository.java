package com.safetynet.repository;

import com.safetynet.dao.Database;
import com.safetynet.model.Firestations;
import com.safetynet.model.Persons;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
@Repository
public class PersonsRepository implements BuisnessRepo<Persons> {

    @Autowired
    Database database;

    @Autowired
    RepositoryService repositoryService;


    @Override
    public List<Persons> findAll() {
        return database.getPersonsList();
    }

    @Override
    public Optional<Persons> add(Persons persons) {
        if (exist(persons) == Boolean.FALSE) {
            database.getPersonsList().add(contrustPersons(persons));
            return Optional.of(persons);
        }


        return Optional.empty();
    }

    @Override
    public boolean delete(Persons persons) {
        if (exist(persons)) {
            database.getPersonsList().remove(persons);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public boolean exist(Persons persons) {
        return database.getPersonsList()
                .stream()
                .anyMatch(search -> persons.getLastName().equals(search.getLastName()) && persons.getFirstName().equals(search.getFirstName()));
    }

    @Override
    public Optional<Persons> update(Persons persons) {
        if (exist(persons)) {
            database.getPersonsList().stream()
                    .filter(search -> persons.getLastName().equals(search.getLastName()) && persons.getFirstName().equals(search.getFirstName()))
                    .findFirst()
                    .ifPresent(maj ->
                    {
                        maj.setAddress(persons.getAddress());
                        maj.setCity(persons.getCity());
                        maj.setEmail(persons.getEmail());
                        maj.setPhone(persons.getPhone());
                        maj.setZip(persons.getZip());
                    });
            return Optional.of(contrustPersons(persons));
        }
        return Optional.empty();
    }

    public Optional<Persons> finByElements(String nom, String prenom) {
        log.info("finby elements : " + nom + " / " + prenom);
        AtomicReference<Persons> atomicPers = new AtomicReference<>();
        database.getPersonsList().stream()
                .filter(search -> nom.equals(search.getLastName()) && prenom.equals(search.getFirstName()))
                .findFirst()
                .ifPresent(searchPers ->
                {
                    atomicPers.set(searchPers);
                });
        if (atomicPers.get() == null) {
            return Optional.empty();
        }
        return Optional.of(atomicPers.get());
    }


    private Persons contrustPersons(Persons persons) {

        database.getMedicalrecordsList()
                .forEach(medicalrecords ->
        {
            if (persons.getFirstName().equals(medicalrecords.getFirstName()) && persons.getLastName().equals(medicalrecords.getLastName())) {
                persons.setMedicalrecords(medicalrecords);
            }
        });
        List<Firestations> listFires=new ArrayList<>();
        database.getFirestationsList()
                .forEach(firestations ->
                        {
                            if (persons.getAddress().equals(firestations.getAddress())) {
                                listFires.add(firestations);
                            }
                        }
                );
        persons.setFirestations(listFires);
        log.debug("PersonRepository : Construit les associations Persons: " + persons);
        return persons;

    }
}
