package com.safetynet.repository;

import com.safetynet.dao.Database;
import com.safetynet.model.Medicalrecords;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Log4j2
@Repository
public class MedicalrecordsRepository implements BuisnessRepo<Medicalrecords> {

    @Autowired
    Database database;

    @Getter
    public List<Medicalrecords> medicalrecordsList;

    public MedicalrecordsRepository() {
        this.medicalrecordsList =database.getMedicalrecordsList();
    }

    @Override
    public List<Medicalrecords> findAll() {
        return getMedicalrecordsList();
    }

    @Override
    public Optional<Medicalrecords> add(Medicalrecords medicalrecords) {
        return Optional.empty();
    }

    @Override
    public boolean delete(Medicalrecords medicalrecords) {
        return false;
    }

    @Override
    public boolean exist(Medicalrecords objet) {
        return false;
    }

    @Override
    public Optional<Medicalrecords> update(Medicalrecords objet) {
        return Optional.empty();
    }
}
