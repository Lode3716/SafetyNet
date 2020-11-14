package com.safetynet.repository;

import com.safetynet.dao.Database;
import com.safetynet.model.Firestations;
import com.safetynet.model.Persons;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
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
            log.debug("Repository :  create new person : {}", persons.getFirstName());
            database.getPersonsList().add(contrustPersons(persons));
            return Optional.of(persons);
        }
        return Optional.of(persons);
    }

    @Override
    public boolean delete(Persons persons) {
        if (exist(persons)) {
            log.debug("Repository before person delete : {}" + database.getPersonsList().size());

            database.getPersonsList()
                    .stream()
                    .filter(pers -> pers.getFirstName().equals(persons.getFirstName()) && pers.getLastName().equals(persons.getLastName()))
                    .findFirst()
                    .ifPresent(s -> database.getPersonsList().remove(deleteContrustPersons(s)));

            log.debug("Repository before person delete : {}", database.getPersonsList().size());
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
        AtomicReference<Persons> atoPerson= new AtomicReference<>();
        if (exist(persons)) {

            database.getPersonsList().stream()
                    .filter(search -> persons.getLastName().equals(search.getLastName()) && persons.getFirstName().equals(search.getFirstName()))
                    .findFirst()
                    .ifPresent(maj ->
                    {
                        updateContrustPersons(maj,persons);
                        maj.setAddress(persons.getAddress());
                        maj.setCity(persons.getCity());
                        maj.setEmail(persons.getEmail());
                        maj.setPhone(persons.getPhone());
                        maj.setZip(persons.getZip());
                        atoPerson.set(maj);
                    });
            log.info("Repository : personn update  - succes ");
            return Optional.of(atoPerson.get());
        }
        log.debug("Repository : personn not exist - not update");
        return Optional.empty();
    }

    public Optional<Persons> findByElements(String nom, String prenom) {
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
        log.info("FIND bY / {}", atomicPers.get().getLastName());
        return Optional.of(atomicPers.get());
    }

    public Optional<List<Persons>> searchAllName(String nom, String prenom) {
        List<Persons> personsList = new ArrayList<>();
        findByElements(nom, prenom)
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


    /**
     * Create a person with classes
     *
     * @param persons
     * @return person
     */
    private Persons contrustPersons(Persons persons) {

        associatePersonAndMedical(persons);
        associatePersonAndFirestation(persons);
        log.debug("Repository : construct person with parameter Firestation and Medical : {}", persons);
        return persons;

    }

    /**
     * Update to person with classes
     *
     * @param persons to update
     * @return person : update
     */
    private Persons updateContrustPersons(Persons oldPerson,Persons newPerson) {
        deletePersonAndFirestation(oldPerson);
        associatePersonAndFirestation(newPerson);
        log.debug("Repository : update person with parameter Firestation : {}", newPerson);
        return newPerson;

    }



    /**
     * Associates a person and medical for the creation of the class person
     *
     * @param persons
     */
    public void associatePersonAndMedical(Persons persons) {
        database.getMedicalrecordsList()
                .forEach(medicalrecords ->
                {
                    if (persons.getFirstName().equals(medicalrecords.getFirstName()) && persons.getLastName().equals(medicalrecords.getLastName())) {
                        persons.setMedicalrecords(medicalrecords);
                    }
                });
    }

    /**
     * Associates a person and firestation for the creation of the class person
     *
     * @param persons
     */
    public void associatePersonAndFirestation(Persons persons) {
        List<Firestations> listFires = new ArrayList<>();

        database.getFirestationsList()
                .forEach(firestations ->
                        {
                            if (persons.getAddress().equals(firestations.getAddress())) {
                                listFires.add(firestations);
                            }
                        }
                );
        log.debug("Repository person with list Firestation  : {}", listFires.size());
        persons.setFirestations(listFires);

    }

    /**
     * Remove all associations with other
     *
     * @param person remove
     * @return person remove
     */
    private Persons deleteContrustPersons(Persons person) {

        deletePersonAndMedical(person);
        deletePersonAndFirestation(person);


        return person;

    }

    /**
     * Delete the association between a person and medical
     *
     * @param person : delete
     */
    public void deletePersonAndMedical(Persons person) {
        log.debug("Repository before person Medical association delete : {}", database.getMedicalrecordsList().size());

        database.getMedicalrecordsList()
                .stream()
                .filter(medicalrecords -> person.getFirstName().equals(medicalrecords.getFirstName()) && person.getLastName().equals(medicalrecords.getLastName()))
                .findFirst()
                .ifPresent(s -> database.getMedicalrecordsList().remove(s));

        log.debug("Repository after person Medical association delete : {}", database.getMedicalrecordsList().size());

    }

    /**
     * Associates a person and firestation for the creation of the class person
     *
     * @param person : delete
     */
    public void deletePersonAndFirestation(Persons person) {
        database.getFirestationsList()
                .stream()
                .filter(firestations -> person.getAddress().equals(firestations.getAddress()))
                .findFirst()
                .ifPresent(persons -> {
                    log.debug("Repository before person firestation association delete : {}", persons.getPersonsList().size());
                    persons.getPersonsList()
                            .stream()
                            .filter(s -> s.getFirstName().equals(person.getFirstName()) && s.getLastName().equals(person.getLastName()))
                            .findFirst()
                            .ifPresent(s -> persons.getPersonsList().remove(s));

                    log.debug("Repository after person firestation association delete : {}", persons.getPersonsList().size());
                });

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
