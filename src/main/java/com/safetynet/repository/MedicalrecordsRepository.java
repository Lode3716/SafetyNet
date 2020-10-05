package com.safetynet.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.model.Medicalrecords;
import com.safetynet.utils.ParseJSON;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class MedicalrecordsRepository implements BuisnessRepo {

    private ParseJSON json = new ParseJSON();

    @Override
    public void read(byte[] jsonData) {

        List<Medicalrecords> medicalrecordsList = new ArrayList<>();
        try {
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
        medicalrecordsList.forEach(s -> log.info(s));
    }

    @Override
    public void add(byte[] jsonData) {
    }

    @Override
    public void remove(byte[] jsonData) {

    }
}
