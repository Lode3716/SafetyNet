package com.safetynet.web.controller;

import com.safetynet.model.Persons;
import com.safetynet.repository.RepositoryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
@RestController
public class PersonsController {

    @Autowired
    RepositoryService repositorPersons;

    @GetMapping(value = "persons")
    public List<Persons> readAllpersons() {
        return repositorPersons.getPersonsRepository().findAll();
    }

    @PostMapping(value = "/persons")
    public ResponseEntity<Void> addPersons(@RequestBody Persons persons) {
        AtomicReference<ResponseEntity> rep = new AtomicReference<>();

        repositorPersons.getPersonsRepository().add(persons)
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

}
