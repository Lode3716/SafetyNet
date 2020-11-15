package com.safetynet.web.controller;

import com.safetynet.dto.*;
import com.safetynet.service.FirestationService;
import com.safetynet.web.exceptions.BadArgumentsException;
import com.safetynet.web.exceptions.FirestationNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * New Firestation to add
     *
     * @param firestation to save
     * @return ResponseEntity<FirestationsDTO> firestation return when is create,if arguments to search is not good : BadArgumentsException
     */
    @PostMapping(value = "firestation")
    public ResponseEntity<FirestationsDTO> addFirestations(@RequestBody FirestationsDTO firestation) {
        log.debug("POST : create firestation : {}", firestation);
        AtomicReference<ResponseEntity> rep = new AtomicReference<>();

        if (firestation.getStation().isBlank() || firestation.getAddress().isBlank()) {
            throw new BadArgumentsException("POST : Station or adress are not null for create Firestation");
        }
        firestationService.add(firestation)
                .ifPresent(retour ->
                {
                    log.info("POST: create Firestation - SUCCESS");
                    rep.set(ResponseEntity.status(HttpStatus.CREATED).body(retour));
                });
        return rep.get();
    }


    @DeleteMapping(value = "firestation")
    public ResponseEntity<Void> deleteFirestation(@RequestBody FirestationsDTO firestation) {
        log.debug("DELETE : firestation : {}", firestation);
        if (firestation.getStation().isBlank() || firestation.getAddress().isBlank()) {
            throw new BadArgumentsException("DELETE : Station or adress are not null for create Firestation");
        }

        Boolean retour = firestationService.delete(firestation);
        if (!retour) {
            log.error("Delete firestation not found : {}", firestation);
            throw new FirestationNotFoundException("The firestation with the name " + firestation.getStation() + " could not be found.");
        }
        log.info("Delete : firestation response : SUCCES");
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
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
