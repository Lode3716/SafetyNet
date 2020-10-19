package com.safetynet.web.controller;

import com.googlecode.jmapper.JMapper;
import com.safetynet.dto.MedicalRecordsDTO;
import com.safetynet.model.Medicalrecords;
import com.safetynet.repository.RepositoryService;
import com.safetynet.web.exceptions.MedicalrecordsIntrouvablesException;
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
public class MedicalRecordsController {

    @Autowired
    RepositoryService repositorMedicalRecords;

    JMapper<MedicalRecordsDTO, Medicalrecords> medicalRecordsMapper = new JMapper<>(MedicalRecordsDTO.class, Medicalrecords.class);
    JMapper<Medicalrecords, MedicalRecordsDTO> medicalRecordsUnMapper = new JMapper<>(Medicalrecords.class, MedicalRecordsDTO.class);

    @GetMapping(value = "medicalRecord")
    public List<MedicalRecordsDTO> readAllMedicalRecords() {

        List<MedicalRecordsDTO> list = new ArrayList<>();

        repositorMedicalRecords.getMedicalrecordsRepository().findAll().forEach(medeicals ->
        {
            list.add(medicalRecordsMapper.getDestination(medeicals));
        });
        return list;
    }

    @PostMapping(value = "medicalRecord")
    public ResponseEntity<Void> addMedicalRecord(@RequestBody MedicalRecordsDTO medicalrecords) {
        log.info("Add medicalrecords : " + medicalrecords);
        AtomicReference<ResponseEntity> rep = new AtomicReference<>();

        repositorMedicalRecords.getMedicalrecordsRepository()
                .add(medicalRecordsUnMapper.getDestination(medicalrecords))
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
        repositorMedicalRecords.getMedicalrecordsRepository().update(medicalRecordsUnMapper.getDestination(medicalRecord))
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
        Boolean retour = repositorMedicalRecords.getMedicalrecordsRepository().delete(medicalRecordsUnMapper.getDestination(medicalRecord));
        if (!retour) {
            throw new MedicalrecordsIntrouvablesException("La personne se nommant " + medicalRecord.getLastName() + "est introuvable.");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
