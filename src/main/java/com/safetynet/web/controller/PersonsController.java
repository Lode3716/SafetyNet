package com.safetynet.web.controller;

import com.googlecode.jmapper.JMapper;
import com.safetynet.dto.PersonsDto;
import com.safetynet.model.Persons;
import com.safetynet.repository.RepositoryService;
import com.safetynet.web.exceptions.PersonsIntrouvableException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
@RestController
public class PersonsController {

    @Autowired
    RepositoryService repositorPersons;

    @Autowired
    JMapper<PersonsDto, Persons> personMapper;

    @Autowired
    JMapper<Persons, PersonsDto> personUnMapper;

    @GetMapping(value = "persons")
    public List<PersonsDto> readAllpersons() {

        List<PersonsDto> list = new ArrayList<>();
        repositorPersons.getPersonsRepository().findAll().forEach(persons ->
                {
                    list.add(personMapper.getDestination(persons));
                }
        );
        return list;
    }

    @GetMapping(value = "person/{nom}/{prenom}")
    public Persons readPerson(@PathVariable("nom") String nom, @PathVariable("prenom") String prenom) {
        log.info("Entre dans dinbyElements " + nom + " " + prenom);
        return repositorPersons.getPersonsRepository().finByElements(nom, prenom)
                .orElseThrow(() -> new PersonsIntrouvableException("La personne se nommant " + nom + " " + prenom + "est introuvable."));
    }

    @PostMapping(value = "person")
    public ResponseEntity<Void> addPersons(@RequestBody PersonsDto persons) {
        log.info("Passe add person : " + persons);
        AtomicReference<ResponseEntity> rep = new AtomicReference<>();

        repositorPersons.getPersonsRepository().add(personUnMapper.getDestination(persons))
                .ifPresentOrElse(retour ->
                {
                    URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{nom}/{prenom}")
                            .buildAndExpand(persons.getLastName(), persons.getFirstName())
                            .toUri();
                    rep.set(ResponseEntity.created(location).build());
                }, () ->
                {
                    rep.set(ResponseEntity.noContent().build());
                });


        return rep.get();
    }

    @DeleteMapping(value = "person")
    public ResponseEntity<Void> deletePerson(@RequestBody PersonsDto person) {
        log.info("Passe personn delete : " + person);

        Boolean retour = repositorPersons.getPersonsRepository().delete(personUnMapper.getDestination(person));
        if (!retour) {
            throw new PersonsIntrouvableException("La personne se nommant " + person.getLastName() + "est introuvable.");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "person")
    public ResponseEntity<Void> updatePerson(@RequestBody PersonsDto person) {
        log.info("Passe personn update : " + person);
        AtomicReference<ResponseEntity> rep = new AtomicReference<>();
        repositorPersons.getPersonsRepository().update(personUnMapper.getDestination(person))
                .ifPresentOrElse(retour ->
                {
                    URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{nom}/{prenom}")
                            .buildAndExpand(person.getLastName(), person.getFirstName())
                            .toUri();
                    rep.set(ResponseEntity.created(location).build());
                }, () ->
                {
                    rep.set(ResponseEntity.noContent().build());
                });

        return rep.get();
    }

}
