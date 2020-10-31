package com.safetynet.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.model.Firestations;
import com.safetynet.model.Medicalrecords;
import com.safetynet.model.Persons;
import com.safetynet.utils.ParseJSON;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@AllArgsConstructor
@Component
public class Database {

    @Getter
    private static List<Persons> personsList;
    @Getter
    private static List<Medicalrecords> medicalrecordsList;
    @Getter
    private static List<Firestations> firestationsList;

    private ParseJSON json = new ParseJSON();
    private byte[] jsonData = new byte[0];

    public Database() {
    }


    @PostConstruct
    public void init() {
        personsList = findAllInitPersonne();
        medicalrecordsList = findAllInitMedicalrecords();
        firestationsList = findAllInitFirestation();
        contrustPersonsList();
        log.debug("Creation des associations Personns.");
        constructListFireStation();
        log.debug("Creation des associations Firestations.");
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
                            log.error("Une erreur c'est produite lors de la lecture du JSON Personn : "+e.getMessage());
                        }
                    });
        } catch (IOException e) {
            log.error("Une erreur c'est produite lors de la creation de la dataBase Personn : "+e.getMessage());
        }
        log.info("Initialisation de la liste Personn de la Database : "+persons2.size());
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
                            if(!firestationExist(firestationsList,firestations)) {
                                firestationsList.add(firestations);
                            }
                        } catch (JsonProcessingException e) {
                            log.error("Une erreur c'est produite lors de la lecture du JSON Firestation : "+e.getMessage());
                        }
                    });

        } catch (IOException e) {
            log.error("Une erreur c'est produite lors de la creation de la dataBase Firestation : "+e.getMessage());
        }
        log.debug("Initialisation de la liste Firestation de la Database : "+firestationsList.size());
        return firestationsList;
    }

    private boolean firestationExist(List<Firestations> list,Firestations firestation)
    {
        return list.stream()
                .anyMatch(search -> firestation.getAddress().equals(search.getAddress()) && firestation.getStation().equals(search.getStation()));
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
                            log.error("Une erreur c'est produite lors de la lecture du JSON Medicalrecords : "+e.getMessage());
                        }
                    });
        } catch (IOException e) {
            log.error("Initialisation de la liste Medicalrecords de la Database : "+e.getMessage());
        }
        log.debug("Initialisation de la liste Medicalrecords de la Database : "+medicalrecordsList.size());
        return medicalrecordsList;
    }

    private void contrustPersonsList() {
        personsList
                .stream()
                .filter(persons ->
                {
                    medicalrecordsList.forEach(medicalrecords ->
                    {
                        if (persons.getFirstName().equals(medicalrecords.getFirstName()) && persons.getLastName().equals(medicalrecords.getLastName())) {
                            persons.setMedicalrecords(medicalrecords);
                        }
                    });
                    List<Firestations> listFires=new ArrayList<>();
                    firestationsList
                            .forEach(firestations ->
                            {
                                if (persons.getAddress().equals(firestations.getAddress()))
                                {
                                    listFires.add(firestations);
                                }
                            }
                    );
                    persons.setFirestations(listFires);
                    return true;
                })
        .distinct()
        .collect(Collectors.toList());
    }

    private void constructListFireStation() {
        firestationsList.forEach(station ->
                {
                   List<Persons> listPers= personsList.stream()
                            .filter(pers-> pers.getAddress().equals(station.getAddress()))
                            .collect(Collectors.toList());

                    station.setPersonsList(listPers);
                }
        );
    }

}
