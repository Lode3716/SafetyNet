package com.safetynet.repository;

import com.safetynet.dao.Database;
import com.safetynet.model.Medicalrecords;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Log4j2
@Repository
public class MedicalRecordsRepository implements IMedicalRecordsRepository {

    @Autowired
    Database database;

    @Override
    public List<Medicalrecords> findAll() {
        return database.getMedicalrecordsList();
    }

    @Override
    public Optional<Medicalrecords> add(Medicalrecords medicalrecords) {
        if (exist(medicalrecords) == Boolean.FALSE) {
            database.getMedicalrecordsList().add(medicalrecords);
        }
        return Optional.of(medicalrecords);
    }

    @Override
    public boolean delete(Medicalrecords medicalrecords) {
        if (exist(medicalrecords)) {
            database.getMedicalrecordsList().remove(medicalrecords);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public boolean exist(Medicalrecords medicalrecords) {
        return database.getMedicalrecordsList()
                .stream()
                .anyMatch(search -> medicalrecords.getLastName().equals(search.getLastName()) && medicalrecords.getFirstName().equals(search.getFirstName()));
    }

    @Override
    public Optional<Medicalrecords> update(Medicalrecords medicalrecords) {
        if (exist(medicalrecords)) {
            database.getMedicalrecordsList()
                    .stream()
                    .filter(search -> medicalrecords.getLastName().equals(search.getLastName()) && medicalrecords.getFirstName().equals(search.getFirstName()))
                    .findFirst()
                    .ifPresent(maj ->
                    {
                        maj.setBirthdate(medicalrecords.getBirthdate());
                        maj.setAllergies(medicalrecords.getAllergies());
                        maj.setMedications(medicalrecords.getMedications());
                    });
            return Optional.of(medicalrecords);
        }
        return Optional.empty();
    }

}
