package com.safetynet.web.controller;

import com.googlecode.jmapper.JMapper;
import com.safetynet.dto.*;
import com.safetynet.dto.factory.*;
import com.safetynet.model.Firestations;
import com.safetynet.model.Persons;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
@RestController
public class FirestationsController {

    @Autowired
    RepositoryService repositorFirestations;

    @Autowired
    ServiceFactory serviceFactory;

    @Autowired
    JMapper<FirestationsDTO, Firestations> firestationsMapper;

    @Autowired
    JMapper<Firestations, FirestationsDTO> firestationsUnMapper;

    @GetMapping(value = "firestation/all")
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

    @GetMapping(value = "firestation")
    public PersonsBelongFirestationDTO personsBelongFireStation(@RequestParam String stationNumber) {
        log.info("personsBelongFireStation  : " + stationNumber);
        List<Firestations> station = repositorFirestations
                .getFirestationsRepository()
                .personsBelongFirestation(stationNumber);

        return serviceFactory.getPersonalBelongFirestationFactory().createPersonFirestation(station);

    }

    @GetMapping(value = "phoneAlert")
    public List<PersonsPhoneDTO> phoneAlerte(@RequestParam String firestation_number) {
        log.info("phoneAlerte  : " + firestation_number);
        List<Firestations> station = repositorFirestations
                .getFirestationsRepository()
                .personsBelongFirestation(firestation_number);
        return serviceFactory.getPersonsPhoneFactory().createPersonsPhone(station);

    }

    @GetMapping(value = "childAlert")
    public ChildStationDTO childAlertStation(@RequestParam String address) {
        log.info("childAlertStation : " + address);
        List<Persons> persons = repositorFirestations
                .getFirestationsRepository()
                .personsAdress(address);
        return serviceFactory.getChildStationFactory().createChildStationDTO(persons);

    }

    @GetMapping(value = "fire")
    public List<PersonsMedicalStationDTO> fireAdress(@RequestParam String address) {
        log.info("fireAdress : " + address);
        List<Persons> persons = repositorFirestations
                .getFirestationsRepository()
                .personsAdress(address);
        return serviceFactory.getPersonsMedicalsStationFactory().createPersonsMedicals(persons);

    }

    @GetMapping(value = "flood/")
    public Map<String, List<PersonsMedicalsDTO>> floodStation(@RequestParam List<String> stations) {
        log.info("floodStation : " + stations);
        Map<String, List<PersonsMedicalsDTO>> retour = new HashMap<>();
        stations.forEach(stat ->
        {
            log.info(stat);
            repositorFirestations
                    .getFirestationsRepository()
                    .personsBelongFirestation(stat)
                    .forEach(pers ->
            {
                    List<PersonsMedicalsDTO> personeMedicalsList = serviceFactory.getPersonsMedicalsFactory().createPersonsMedicals(pers.getPersonsList(),stat);
                    StringBuilder sb=new StringBuilder();
                    sb.append("Station ")
                            .append(stat)
                            .append(" / ")
                            .append(pers.getAddress());
                    retour.put(sb.toString(), personeMedicalsList);

            });
        });
        return retour;
    }


}
