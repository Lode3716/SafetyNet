package com.safetynet.web.controller;

import com.googlecode.jmapper.JMapper;
import com.safetynet.dto.FirestationsDTO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
@RestController
public class FirestationsController {

    @Autowired
    RepositoryService repositorFirestations;

    JMapper<FirestationsDTO, Firestations> firestationsMapper = new JMapper<>(FirestationsDTO.class, Firestations.class);
    JMapper<Firestations, FirestationsDTO> firestationsUnMapper = new JMapper<>(Firestations.class, FirestationsDTO.class);

    @GetMapping(value = "firestation")
    public List<FirestationsDTO> readAllFirestations() {
        List<FirestationsDTO> list = new ArrayList<>();
        repositorFirestations.getFirestationsRepository().findAll().forEach(fire ->
                {
                    list.add(firestationsMapper.getDestination(fire));
                }
        );

        return list;
    }

    @PostMapping(value = "firestation")
    public ResponseEntity<Void> addFirestations(@RequestBody FirestationsDTO firestation) {
        log.info("Passe add firestation : " + firestation);
        AtomicReference<ResponseEntity> rep = new AtomicReference<>();

        repositorFirestations.getFirestationsRepository()
                .add(firestationsUnMapper.getDestination(firestation))
                .ifPresentOrElse(retour ->
                {
                    URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{station}/{adress}")
                            .buildAndExpand(firestation.getStation(), firestation.getAddress())
                            .toUri();
                    rep.set(ResponseEntity.created(location).build());
                }, () ->
                {
                    rep.set(ResponseEntity.noContent().build());
                });

        return rep.get();
    }

    @DeleteMapping(value = "firestation")
    public ResponseEntity<Void> deleteFirestation(@RequestBody FirestationsDTO oldFirestation) {
        log.info("Passe firestation : " + oldFirestation);
        Boolean retour = repositorFirestations.getFirestationsRepository().delete(firestationsUnMapper.getDestination(oldFirestation));
        if (!retour) {
            throw new FirestationsIntrouvablesException("La station se nommant " + oldFirestation.getStation() + "est introuvable.");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "firestation")
    public ResponseEntity<Void> updateFirestation(@RequestBody FirestationsDTO firestation) {
        log.info("Passe firestation update : " + firestation);
        AtomicReference<ResponseEntity> rep = new AtomicReference<>();
        repositorFirestations.getFirestationsRepository()
                .update(firestationsUnMapper.getDestination(firestation))
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
