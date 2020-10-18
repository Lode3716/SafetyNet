package com.safetynet.web.controller;

import com.safetynet.model.Firestations;
import com.safetynet.repository.RepositoryService;
import com.safetynet.web.exceptions.FirestationsIntrouvablesException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
@RestController
public class FirestationsController {

    @Autowired
    RepositoryService repositorFirestations;


    @GetMapping(value = "firestation")
    public List<Firestations> readAllFirestations() {
        return repositorFirestations.getFirestationsRepository().findAll();
    }

    @PostMapping(value = "firestation")
    public ResponseEntity<Void> addFirestations(@RequestBody Firestations firestation) {
        log.info("Passe add firestation : " + firestation);
        AtomicReference<ResponseEntity> rep = new AtomicReference<>();

        repositorFirestations.getFirestationsRepository()
                .add(firestation)
                .ifPresentOrElse(retour ->
                {
                    URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{station}/{adress}")
                            .buildAndExpand(firestation.getStation(),firestation.getAddress())
                            .toUri();
                    rep.set(ResponseEntity.created(location).build());
                }, () ->
                {
                    rep.set(ResponseEntity.noContent().build());
                });

        return rep.get();
    }

    @DeleteMapping(value = "firestation")
    public ResponseEntity<Void> deleteFirestation(@RequestBody Firestations oldFirestation) {
        log.info("Passe firestation : " + oldFirestation );
        Boolean retour = repositorFirestations.getFirestationsRepository().delete(oldFirestation);
        if (!retour) {
            throw new FirestationsIntrouvablesException("La station se nommant "+oldFirestation.getStation() + "est introuvable.");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "firestation")
    public ResponseEntity<Void> updateFirestation(@RequestBody Firestations firestation) {
        log.info("Passe firestation update : " + firestation);
        AtomicReference<ResponseEntity> rep = new AtomicReference<>();
        repositorFirestations.getFirestationsRepository()
                .update(firestation)
                .ifPresentOrElse(retour ->
                {
                    URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{station}/{adresse}")
                            .buildAndExpand(firestation.getStation(), firestation.getAddress())
                            .toUri();
                    rep.set(ResponseEntity.created(location).build());
                }, () ->
                {
                    rep.set(ResponseEntity.noContent().build());
                });

        return rep.get();
    }
}
