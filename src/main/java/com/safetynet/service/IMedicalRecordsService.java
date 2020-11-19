package com.safetynet.service;

import com.safetynet.dto.MedicalRecordsDTO;
import com.safetynet.model.Medicalrecords;

import java.util.List;
import java.util.Optional;

public interface IMedicalRecordsService {

    List<MedicalRecordsDTO> findAll();

    Optional<Medicalrecords> add(MedicalRecordsDTO medicalRecordsDTO);

    Optional<Medicalrecords> update(MedicalRecordsDTO medicalRecordsDTO);

    Boolean delete(MedicalRecordsDTO medicalRecordsDTO);
}
