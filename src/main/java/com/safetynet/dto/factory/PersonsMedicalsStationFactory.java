package com.safetynet.dto.factory;

import com.safetynet.dto.PersonsMedicalStationDTO;
import com.safetynet.dto.UtilsDTO;
import com.safetynet.model.Persons;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
public class PersonsMedicalsStationFactory {

    public List<PersonsMedicalStationDTO> createPersonsMedicals(List<Persons> personsLive) {

        List<PersonsMedicalStationDTO> personsMedicalsList = new ArrayList<>();

        personsLive
                .forEach(persons ->
                {
                    Optional.ofNullable(persons.getFirestations())
                            .ifPresentOrElse(station ->
                            {
                                station.forEach(stat ->
                                {
                                    personsMedicalsList.add(createPersonMedical(persons, stat.getStation()));
                                });
                            }, () -> personsMedicalsList.add(createPersonMedical(persons, "")));
                });
        return personsMedicalsList;
    }

    public PersonsMedicalStationDTO createPersonMedical(Persons persons, String station) {
        UtilsDTO utilsDTO = new UtilsDTO();
        PersonsMedicalStationDTO personsMedicalStationDTO = new PersonsMedicalStationDTO();
        personsMedicalStationDTO.setStation(station);
        personsMedicalStationDTO.setLastName(persons.getLastName());
        personsMedicalStationDTO.setPhone(persons.getPhone());
        personsMedicalStationDTO.setAllergies(persons.getMedicalrecords().getAllergies());
        personsMedicalStationDTO.setMedications(persons.getMedicalrecords().getMedications());
        utilsDTO.calculAge(persons.getMedicalrecords().getBirthdate())
                .ifPresentOrElse(age -> personsMedicalStationDTO.setAge(age), () -> personsMedicalStationDTO.setAge(0));
        log.debug("Création d'un personsMedicalsDTO : " + personsMedicalStationDTO);
        return personsMedicalStationDTO;

    }

/*
    public List<PersonsMedicalsDTO> createPersonsMedicals(List<Persons> personsLive, String station) {

        List<PersonsMedicalsDTO> personsMedicalsList = new ArrayList<>();

        UtilsDTO utilsDTO = new UtilsDTO();

        personsLive
                .forEach(persons ->
                {
                    log.info(persons.getFirestations().getAddress() + " / "+persons.getFirestations().getStation());
                    if(persons.getFirestations().getStation().equals(station)) {
                        log.info("Passe : "+persons.getFirestations().getStation());
                        PersonsMedicalsDTO personsMedicalsDTO = new PersonsMedicalsDTO();
                        personsMedicalsDTO.setStation(persons.getFirestations().getStation());
                        personsMedicalsDTO.setLastName(persons.getLastName());
                        personsMedicalsDTO.setPhone(persons.getPhone());
                        personsMedicalsDTO.setAllergies(persons.getMedicalrecords().getAllergies());
                        personsMedicalsDTO.setMedications(persons.getMedicalrecords().getMedications());
                        utilsDTO.calculAge(persons.getMedicalrecords().getBirthdate())
                                .ifPresentOrElse(age -> personsMedicalsDTO.setAge(age), () -> personsMedicalsDTO.setAge(0));
                        personsMedicalsList.add(personsMedicalsDTO);
                    }
                });
        return personsMedicalsList;
    }*/
}