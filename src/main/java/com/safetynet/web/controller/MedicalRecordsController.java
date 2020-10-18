package com.safetynet.web.controller;

import com.safetynet.model.Firestations;
import com.safetynet.model.Medicalrecords;
import com.safetynet.model.Persons;
import com.safetynet.repository.RepositoryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
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
    RepositoryService repositorMedicalRecords;

    @GetMapping(value = "medicalRecord")
    public List<Medicalrecords> readAllMedicalRecords() {
        return repositorMedicalRecords.getMedicalrecordsRepository().findAll();
    }

    @PostMapping(value = "medicalRecord")
    public ResponseEntity<Void> addMedicalRecord(@RequestBody Medicalrecords medicalrecords) {
        log.info("Add medicalrecords : " + medicalrecords);
        AtomicReference<ResponseEntity> rep = new AtomicReference<>();

        repositorMedicalRecords.getMedicalrecordsRepository()
                .add(medicalrecords)
                .ifPresentOrElse(retour ->
                {
                    URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{prenom}/{nom}")
                            .buildAndExpand(medicalrecords.getFirstName(),medicalrecords.getLastName())
                            .toUri();
                    rep.set(ResponseEntity.created(location).build());
                }, () ->
                {
                    rep.set(ResponseEntity.noContent().build());
                });

        return rep.get();
    }

    @PutMapping(value = "medicalRecord")
    public ResponseEntity<Void> updateMedicalrecord(@RequestBody Medicalrecords medicalRecord) {
        log.info("Update medicalRecord : " + medicalRecord);
        AtomicReference<ResponseEntity> rep = new AtomicReference<>();
        repositorMedicalRecords.getMedicalrecordsRepository().update(medicalRecord)
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
}
