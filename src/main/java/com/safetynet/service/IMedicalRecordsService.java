package com.safetynet.service;

import com.safetynet.dto.MedicalRecordsDTO;

import java.util.List;
import java.util.Optional;

public interface IMedicalRecordsService {

    List<MedicalRecordsDTO> findAll();

    Optional<MedicalRecordsDTO> add(MedicalRecordsDTO medicalRecordsDTO);

    Optional<MedicalRecordsDTO> update(MedicalRecordsDTO medicalRecordsDTO);

    Boolean delete(MedicalRecordsDTO medicalRecordsDTO);
}
