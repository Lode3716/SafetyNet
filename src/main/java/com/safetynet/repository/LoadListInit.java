package com.safetynet.repository;

import com.safetynet.model.Firestations;
import com.safetynet.model.Medicalrecords;
import com.safetynet.model.Persons;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Component
public class LoadListInit {

    @Getter
    private static List<Persons> personsList;
    @Getter
    public static List<Medicalrecords> medicalrecordsList;
    @Getter
    private static List<Firestations> firestationsList;

    @Autowired
    RepositoryService repositoryService;


    @PostConstruct
    public void init() {
        personsList = repositoryService.getPersonsRepository().findAllInit();
        medicalrecordsList = repositoryService.getMedicalrecordsRepository().findAllInit();
        firestationsList = repositoryService.getFirestationsRepository().findAllInit();
        log.info("Size medicalrecordsList : " + medicalrecordsList.size() + " / personsList : " + personsList.size() + " / firestationsList :" + firestationsList.size());
        contrustPersonsList();
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
