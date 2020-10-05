package com.safetynet.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.model.Firestations;
import com.safetynet.utils.ParseJSON;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class FirestationsRepository implements BuisnessRepo {

    private ParseJSON json = new ParseJSON();

    @Override
    public void read(byte[] jsonData) {
        List<Firestations> firestationsList = new ArrayList<>();
        try {
            json.parseJsonObject("firestations", jsonData)
                    .elements()
                    .forEachRemaining(s -> {
                        try {
                            ObjectMapper objectMapper = new ObjectMapper();
                            Firestations firestations = objectMapper.treeToValue(s, Firestations.class);
                            firestationsList.add(firestations);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    });

        } catch (IOException e) {
            e.printStackTrace();
        }
        firestationsList.forEach(s -> log.info("Parse " + s));
    }

    @Override
    public void add(byte[] jsonData) {

    }

    @Override
    public void remove(byte[] jsonData) {

    }
}
