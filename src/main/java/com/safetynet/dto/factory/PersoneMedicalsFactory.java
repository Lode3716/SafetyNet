package com.safetynet.dto.factory;

import com.safetynet.dto.PersonsMedicalsDTO;
import com.safetynet.dto.UtilsDTO;
import com.safetynet.model.Persons;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class PersoneMedicalsFactory {

    public List<PersonsMedicalsDTO> createPersonsMedicals(List<Persons> personsLive) {

        List<PersonsMedicalsDTO> personsMedicalsList = new ArrayList<>();

        UtilsDTO utilsDTO = new UtilsDTO();

        personsLive
                .forEach(persons ->
                {
                    PersonsMedicalsDTO personsMedicalsDTO = new PersonsMedicalsDTO();
                    personsMedicalsDTO.setStation(persons.getFirestations().getStation());
                    personsMedicalsDTO.setLastName(persons.getLastName());
                    personsMedicalsDTO.setPhone(persons.getPhone());
                    personsMedicalsDTO.setAllergies(persons.getMedicalrecords().getAllergies());
                    personsMedicalsDTO.setMedications(persons.getMedicalrecords().getMedications());
                    utilsDTO.calculAge(persons.getMedicalrecords().getBirthdate())
                            .ifPresentOrElse(age -> personsMedicalsDTO.setAge(age), () -> personsMedicalsDTO.setAge(0));
                    personsMedicalsList.add(personsMedicalsDTO);

                });
        return personsMedicalsList;
    }

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
    }
}