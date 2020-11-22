package com.safetynet.web.controller;

import com.safetynet.dto.MedicalRecordsDTO;
import com.safetynet.model.Medicalrecords;
import com.safetynet.service.IMedicalRecordsService;
import com.safetynet.web.exceptions.BadArgumentsException;
import com.safetynet.web.exceptions.FirestationNotFoundException;
import com.safetynet.web.exceptions.MedicalrecordsNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<MedicalRecordsDTO> addMedicalRecord(@RequestBody MedicalRecordsDTO medicalrecords) {
        log.debug("POST : create MedicalRecords : {}", medicalrecords);
        AtomicReference<ResponseEntity> rep = new AtomicReference<>();

        if (medicalrecords.getFirstName().isBlank() || medicalrecords.getLastName().isBlank()) {
            log.error("POST : firstName or lastName are not null for create medical records");
            throw new BadArgumentsException("POST : firstName or lastName are not null for create medical records");
        }
        medicalRecordsService.add(medicalrecords)
                .ifPresent(retour ->
                {
                    log.info("POST: create medicalRecords - SUCCESS");
                    rep.set(ResponseEntity.status(HttpStatus.CREATED).body(retour));
                });
        return rep.get();
    }

    @PutMapping(value = "medicalRecord")
    public ResponseEntity<MedicalRecordsDTO> updateMedicalrecord(@RequestBody MedicalRecordsDTO medicalRecord) {
        log.debug("PUT : update medical record : " + medicalRecord);

        if (medicalRecord.getFirstName().isBlank() || medicalRecord.getLastName().isBlank()) {
            log.error("PUT : firstName or lastName are not null for update medical records");
            throw new BadArgumentsException("PUT : firstName or lastName are not null for update medical records");
        }
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(medicalRecordsService.update(medicalRecord)
                .orElseThrow(MedicalrecordsNotFoundException::new));
    }

    @DeleteMapping(value = "medicalRecord")
    public ResponseEntity<Void> deletePerson(@RequestBody MedicalRecordsDTO medicalRecord) {
        log.debug("DELETE : medicalRecord :  {}", medicalRecord);

        if (medicalRecord.getFirstName().isBlank() || medicalRecord.getLastName().isBlank()) {
            log.error("DELETE : firstName or lastName are not null for update medical records");
            throw new BadArgumentsException("DELETE : firstName or lastName are not null for update medical records");
        }

        Boolean retour = medicalRecordsService.delete(medicalRecord);
        if (!retour) {
            throw new MedicalrecordsNotFoundException("The person naming himself " + medicalRecord.getLastName() + "is not found.");
        }
        log.info("Delete :of the medical file is carried out");
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
