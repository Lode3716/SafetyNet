package com.safetynet.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.dao.UtilsDao;
import com.safetynet.model.Medicalrecords;
import com.safetynet.utils.ParseJSON;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@Repository
public class MedicalrecordsRepository implements BuisnessRepo<Medicalrecords> {

    private ParseJSON json = new ParseJSON();
    private byte[] jsonData = new byte[0];
    @Getter
    private List<Medicalrecords> medicalrecordsList;


    public MedicalrecordsRepository() {
        this.medicalrecordsList = LoadListInit.getMedicalrecordsList();
    }

    @Override
    public List findAllInit() {
        List<Medicalrecords> medicalrecordsList = new ArrayList<>();
        try {
            jsonData = Files.readAllBytes(Paths.get(UtilsDao.FILE_NAME));
            json.parseJsonObject("medicalrecords", jsonData)
                    .elements()
                    .forEachRemaining(s ->
                    {
                        try {
                            ObjectMapper objectMapper = new ObjectMapper();
                            Medicalrecords medicalrecords = objectMapper.treeToValue(s, Medicalrecords.class);
                            medicalrecordsList.add(medicalrecords);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return medicalrecordsList;
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
