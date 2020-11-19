package com.safetynet.service;

import com.googlecode.jmapper.JMapper;
import com.safetynet.dto.MedicalRecordsDTO;
import com.safetynet.model.Medicalrecords;
import com.safetynet.repository.RepositoryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class MedicalRecordsService implements IMedicalRecordsService {

    @Autowired
    RepositoryService repositorMedicalRecords;

    @Autowired
    JMapper<MedicalRecordsDTO, Medicalrecords> medicalRecordsMapper;

    @Autowired
    JMapper<Medicalrecords, MedicalRecordsDTO> medicalRecordsUnMapper;

    @Override
    public List<MedicalRecordsDTO> findAll() {
        List<MedicalRecordsDTO> list = new ArrayList<>();

        repositorMedicalRecords.getMedicalrecordsRepository().findAll().forEach(medeicals ->
        {
            list.add(medicalRecordsMapper.getDestination(medeicals));
        });
        log.debug("Service : Retourne la liste contruite de medical Records DTO {}", list.size());
        return list;
    }

    @Override
    public Optional<Medicalrecords> add(MedicalRecordsDTO medicalRecordsDTO) {
        return repositorMedicalRecords.getMedicalrecordsRepository()
                .add(medicalRecordsUnMapper.getDestination(medicalRecordsDTO));
    }

    @Override
    public Optional<Medicalrecords> update(MedicalRecordsDTO medicalRecordsDTO) {
        return repositorMedicalRecords.getMedicalrecordsRepository().update(medicalRecordsUnMapper.getDestination(medicalRecordsDTO));
    }

    @Override
    public Boolean delete(MedicalRecordsDTO medicalRecordsDTO) {
        return repositorMedicalRecords.getMedicalrecordsRepository().delete(medicalRecordsUnMapper.getDestination(medicalRecordsDTO));
    }
}