package com.safetynet.dto.factory;

import com.safetynet.dto.PersonsMedicalsDTO;
import com.safetynet.dto.UtilsDTO;
import com.safetynet.model.Persons;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
public class PersonsMedicalsFactory {

    public List<PersonsMedicalsDTO> createPersonsMedicals(List<Persons> personsLive, String nmrStation) {

        List<PersonsMedicalsDTO> personsMedicalsList = new ArrayList<>();

        personsLive
                .forEach(persons ->
                {
                    Optional.ofNullable(persons.getFirestations())
                            .ifPresentOrElse(station ->
                            {
                                station.stream()
                                        .filter(nmr -> nmrStation.equals(nmr.getStation()))
                                        .forEach(stat ->
                                        {
                                            personsMedicalsList.add(createPersonMedical(persons));
                                        });
                            }, () -> personsMedicalsList.add(createPersonMedical(persons)));
                });
        log.debug("Création d'une liste de personsMedicalsList, taille liste : " + personsMedicalsList.size());
        return personsMedicalsList;
    }

    public PersonsMedicalsDTO createPersonMedical(Persons persons) {
        UtilsDTO utilsDTO = new UtilsDTO();
        PersonsMedicalsDTO personsMedicalDTO = new PersonsMedicalsDTO();
        personsMedicalDTO.setLastName(persons.getLastName());
        personsMedicalDTO.setPhone(persons.getPhone());
        utilsDTO.calculAge(persons.getMedicalrecords().getBirthdate())
                .ifPresentOrElse(age -> personsMedicalDTO.setAge(age), () -> personsMedicalDTO.setAge(0));
        personsMedicalDTO.setAllergies(persons.getMedicalrecords().getAllergies());
        personsMedicalDTO.setMedications(persons.getMedicalrecords().getMedications());

        log.debug("Création d'un personsMedicalsDTO : " + personsMedicalDTO);
        return personsMedicalDTO;

    }
}
