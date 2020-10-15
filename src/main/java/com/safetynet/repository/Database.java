package com.safetynet.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.dao.UtilsDao;
import com.safetynet.model.Firestations;
import com.safetynet.model.Medicalrecords;
import com.safetynet.model.Persons;
import com.safetynet.utils.ParseJSON;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Component
public class Database {

    @Getter
    private List<Persons> personsList;
    @Getter
    public List<Medicalrecords> medicalrecordsList;
    @Getter
    private List<Firestations> firestationsList;

    private ParseJSON json = new ParseJSON();
    private byte[] jsonData = new byte[0];


    @PostConstruct
    public void init() {
        personsList = findAllInitPersonne();
        medicalrecordsList = findAllInitMedicalrecords();
        firestationsList = findAllInitFirestation();
        log.info("Size medicalrecordsList : " + medicalrecordsList.size() + " / personsList : " + personsList.size() + " / firestationsList :" + firestationsList.size());
        contrustPersonsList();
    }

    public List<Persons> findAllInitPersonne() {
        List<Persons> persons2 = new ArrayList<>();
        try {
            jsonData = Files.readAllBytes(Paths.get(UtilsDao.FILE_NAME));
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
        return persons2;
    }

    public List<Firestations> findAllInitFirestation() {
        List<Firestations> firestationsList = new ArrayList<>();
        try {
            jsonData = Files.readAllBytes(Paths.get(UtilsDao.FILE_NAME));
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
        return firestationsList;
    }
    public List<Medicalrecords> findAllInitMedicalrecords() {
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

    private List<Persons> contrustPersonsList() {
        return personsList.stream()
                .parallel()
                .filter(persons -> {
                    medicalrecordsList.forEach(medicalrecords ->
                    {
                        if (persons.getFirstName().equals(medicalrecords.getFirstName()) && persons.getLastName().equals(medicalrecords.getLastName())) {
                            persons.setMedicalrecords(medicalrecords);
                        }
                    });
                    firestationsList.forEach(firestations ->
                            {
                                if (persons.getAddress().equals(firestations.getAddress())) {
                                    persons.setFirestations(firestations);
                                }
                            }
                    );
                    return true;
                })
                .distinct()
                .collect(Collectors.toList());
    }

}
