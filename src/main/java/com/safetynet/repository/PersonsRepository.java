package com.safetynet.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.model.Persons;
import com.safetynet.utils.ParseJSON;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class PersonsRepository implements BuisnessRepo {

    private ParseJSON json = new ParseJSON();

    @Override
    public void read(byte[] jsonData) {
        List<Persons> persons2 = new ArrayList<>();
        try {
            json.parseJsonObject("persons", jsonData)
                    .elements()
                    .forEachRemaining(s -> {
                        try {
                            ObjectMapper objectMapper = new ObjectMapper();
                            Persons persons = objectMapper.treeToValue(s, Persons.class);
                            persons2.add(persons);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        persons2.forEach(s -> log.info("Parse " + s));
    }

    @Override
    public void add(byte[] jsonData) {

    }

    @Override
    public void remove(byte[] jsonData) {

    }
}
