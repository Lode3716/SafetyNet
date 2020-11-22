package com.safetynet.service;

import com.googlecode.jmapper.JMapper;
import com.safetynet.dto.MedicalRecordsDTO;
import com.safetynet.model.Firestations;
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
        log.debug("Service : all list medical Records DTO {}", list.size());
        return list;
    }

    @Override
    public Optional<MedicalRecordsDTO> add(MedicalRecordsDTO medicalRecordsDTO) {
        Medicalrecords medical= repositorMedicalRecords.getMedicalrecordsRepository()
                .add(medicalRecordsUnMapper.getDestination(medicalRecordsDTO)).get();
        return Optional.of(medicalRecordsMapper.getDestination(medical));
    }

    @Override
    public Optional<MedicalRecordsDTO> update(MedicalRecordsDTO medicalRecordsDTO)
    {
        Optional<Medicalrecords> medical= repositorMedicalRecords.getMedicalrecordsRepository().update(medicalRecordsUnMapper.getDestination(medicalRecordsDTO));

        if (medical.isPresent()) {
            log.info("Service : Update medical record - Success {}", medical.get());
            return Optional.of(medicalRecordsMapper.getDestination(medical.get()));
        }else{
            return Optional.empty();
        }
    }

    @Override
    public Boolean delete(MedicalRecordsDTO medicalRecordsDTO) {
        return repositorMedicalRecords.getMedicalrecordsRepository().delete(medicalRecordsUnMapper.getDestination(medicalRecordsDTO));
    }
}