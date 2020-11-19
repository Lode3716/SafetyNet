package com.safetynet.web.controller;

import com.safetynet.dto.MedicalRecordsDTO;
import com.safetynet.service.IMedicalRecordsService;
import com.safetynet.web.exceptions.MedicalrecordsNotFoundException;
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
public class MedicalRecordsController {

    @Autowired
    IMedicalRecordsService medicalRecordsService;

    @GetMapping(value = "medicalRecord")
    public List<MedicalRecordsDTO> readAllMedicalRecords() {
        return medicalRecordsService.findAll();
    }

    @PostMapping(value = "medicalRecord")
    public ResponseEntity<Void> addMedicalRecord(@RequestBody MedicalRecordsDTO medicalrecords) {
        log.info("Add medicalrecords : " + medicalrecords);
        AtomicReference<ResponseEntity> rep = new AtomicReference<>();
        medicalRecordsService.add(medicalrecords)
                .ifPresentOrElse(retour ->
                {
                    URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{prenom}/{nom}")
                            .buildAndExpand(medicalrecords.getFirstName(), medicalrecords.getLastName())
                            .toUri();
                    rep.set(ResponseEntity.created(location).build());
                }, () ->
                {
                    rep.set(ResponseEntity.noContent().build());
                });

        return rep.get();
    }

    @PutMapping(value = "medicalRecord")
    public ResponseEntity<Void> updateMedicalrecord(@RequestBody MedicalRecordsDTO medicalRecord) {
        log.info("Update medicalRecord : " + medicalRecord);
        AtomicReference<ResponseEntity> rep = new AtomicReference<>();
        medicalRecordsService.update(medicalRecord)
                .ifPresentOrElse(retour ->
                {
                    URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{nom}/{prenom}")
                            .buildAndExpand(medicalRecord.getLastName(), medicalRecord.getFirstName())
                            .toUri();
                    rep.set(ResponseEntity.created(location).build());
                }, () ->
                {
                    rep.set(ResponseEntity.noContent().build());
                });

        return rep.get();
    }

    @DeleteMapping(value = "medicalRecord")
    public ResponseEntity<Void> deletePerson(@RequestBody MedicalRecordsDTO medicalRecord) {
        log.info("Delete medicalRecord :  " + medicalRecord);
        Boolean retour = medicalRecordsService.delete(medicalRecord);
        if (!retour) {
            throw new MedicalrecordsNotFoundException("La personne se nommant " + medicalRecord.getLastName() + "est introuvable.");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
