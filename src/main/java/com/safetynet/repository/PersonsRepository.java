package com.safetynet.repository;

import com.safetynet.dao.Database;
import com.safetynet.dto.PersonsEmailDTO;
import com.safetynet.dto.PersonsPhoneDTO;
import com.safetynet.model.Firestations;
import com.safetynet.model.Persons;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    public Optional<List<Persons>> searchAllName(String nom, String prenom) {
        List<Persons> personsList = new ArrayList<>();
        finByElements(nom, prenom)
                .ifPresent(persons ->
                {
                    database.getPersonsList()
                            .stream()
                            .filter(search -> persons.getLastName().equals(search.getLastName()))
                            .forEach(searchPers ->
                            {
                                personsList.add(searchPers);
                            });
                });

        if (personsList.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(personsList);
    }


    private Persons contrustPersons(Persons persons) {

        database.getMedicalrecordsList()
                .forEach(medicalrecords ->
                {
                    if (persons.getFirstName().equals(medicalrecords.getFirstName()) && persons.getLastName().equals(medicalrecords.getLastName())) {
                        persons.setMedicalrecords(medicalrecords);
                    }
                });

        List<Firestations> listFires = new ArrayList<>();

        database.getFirestationsList()
                .forEach(firestations ->
                        {
                            log.info("Numero de station : " + firestations.getStation());
                            if (persons.getAddress().equals(firestations.getAddress())) {
                                log.info("Associe personne : " + persons.getFirstName() + " " + persons.getLastName() + " / adress pers :" + persons.getAddress() + " / " + firestations.getAddress());
                                listFires.add(firestations);
                            }
                        }
                );
        persons.setFirestations(listFires);

        log.debug("PersonRepository : Construit les associations Persons: " + persons);
        return persons;

    }

    public Optional<List<String>> searchEmailCity(String city) {
        List<String> personsList = new ArrayList<>();

        database.getPersonsList().stream()
                .filter(search -> city.equals(search.getCity()))
                .forEach(searchPers ->
                {
                    Optional.ofNullable(searchPers.getCity())
                            .ifPresent(mail -> {
                                        if (!emailExist(personsList, searchPers.getEmail())) {
                                            personsList.add(searchPers.getEmail());
                                        }
                                    }
                            );
                });
        if (personsList.isEmpty()) {
            log.debug("PersonRepository : Génère la liste d'email est vide");
            return Optional.empty();
        }
        log.debug("PersonRepository : Génère la liste d'email: " + personsList.size());
        return Optional.of(personsList);

    }

    private boolean emailExist(List<String> personsEmailList, String email) {
        return personsEmailList.stream()
                .anyMatch(search -> email.equals(search));
    }
}
