package com.safetynet.web.controller;

import com.safetynet.dto.*;
import com.safetynet.service.FirestationService;
import com.safetynet.web.exceptions.FirestationsIntrouvablesException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
@RestController
public class FirestationsController {

    @Autowired
    FirestationService firestationService;

    @GetMapping(value = "firestation/all")
    public List<FirestationsDTO> readAllFirestations() {
        log.info("GET list all firestation.");
        log.debug("Get all firestation : {}", firestationService.findAll().size());
        return firestationService.findAll();
    }

    @PostMapping(value = "firestation")
    public ResponseEntity<Void> addFirestations(@RequestBody FirestationsDTO firestation) {
        log.info("POST add firestation : {}", firestation);
        AtomicReference<ResponseEntity> rep = new AtomicReference<>();

        firestationService.add(firestation)
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
        log.debug("Post add firestation : {}, for firestation : {}", rep.get(), firestation);
        return rep.get();
    }

    @DeleteMapping(value = "firestation")
    public ResponseEntity<Void> deleteFirestation(@RequestBody FirestationsDTO oldFirestation) {
        log.info("DELETE firestation : {}", oldFirestation);
        Boolean retour = firestationService.delete(oldFirestation);
        if (!retour) {
            log.error("Delete firestation not found : {}", oldFirestation);
            throw new FirestationsIntrouvablesException("La station se nommant " + oldFirestation.getStation() + "est introuvable.");
        }
        log.debug("Delete FIFrestation response ok : {}", oldFirestation);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "firestation")
    public ResponseEntity<Void> updateFirestation(@RequestBody FirestationsDTO firestation) {
        log.info("PUT update firestation : {}", firestation);
        AtomicReference<ResponseEntity> rep = new AtomicReference<>();
        firestationService.update(firestation)
                .ifPresentOrElse(retour ->
                {
                    rep.set(ResponseEntity.ok().build());
                }, () ->
                {
                    rep.set(ResponseEntity.noContent().build());
                });

        return rep.get();
    }

    @GetMapping(value = "firestation")
    public PersonsBelongFirestationDTO personsBelongFireStation(@RequestParam String stationNumber) {
        log.info("GET search list person for number station and count by age : {}" + stationNumber);
        return firestationService.getPersonsBelongFireStation(stationNumber);

    }

    @GetMapping(value = "phoneAlert")
    public List<PersonsPhoneDTO> phoneAlerte(@RequestParam String stationNumber) {
        log.info("GET search person phone for number station : {}", stationNumber);
        return firestationService.getPhoneAlerte(stationNumber);

    }

    @GetMapping(value = "childAlert")
    public ChildStationDTO childAlertStation(@RequestParam String address) {
        log.info("GET search lis child for adress and list person of family : {} : ", address);
        return firestationService.getChildAlertStation(address);

    }

    @GetMapping(value = "fire")
    public List<PersonsMedicalStationDTO> fireAdress(@RequestParam String address) {
        log.info("GET list person live at adress and casern station : {}", address);
        return firestationService.getFireAdress(address);

    }

    @GetMapping(value = "flood/")
    public Map<FirestationsDTO, List<PersonsMedicalsDTO>> floodStation(@RequestParam List<String> stations) {
        log.info("GET list person family by numero station : {}", stations);
        return firestationService.getFloodStation(stations);
    }

}
