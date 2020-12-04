package com.safetynet.dto.factory;

import com.safetynet.dto.PersonsMedicationAdresseDTO;
import com.safetynet.dto.UtilsDTO;
import com.safetynet.model.Persons;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class PersonsMedicationAdresseFactory {

    public PersonsMedicationAdresseDTO createPersonMedical(Persons persons) {
        UtilsDTO utilsDTO = new UtilsDTO();
        PersonsMedicationAdresseDTO personsMedicationAdresseDTO = new PersonsMedicationAdresseDTO();
        personsMedicationAdresseDTO.setLastName(persons.getLastName());
        personsMedicationAdresseDTO.setAdress(persons.getAddress());
        personsMedicationAdresseDTO.setEmail(persons.getEmail());
        personsMedicationAdresseDTO.setAllergies(persons.getMedicalrecords().getAllergies());
        personsMedicationAdresseDTO.setMedications(persons.getMedicalrecords().getMedications());
        utilsDTO.calculAge(persons.getMedicalrecords().getBirthdate())
                .ifPresentOrElse(age -> personsMedicationAdresseDTO.setAge(age), () -> personsMedicationAdresseDTO.setAge(0));
        log.debug("Création d'un PersonsMedicationAdresseDTO : " + personsMedicationAdresseDTO);
        return personsMedicationAdresseDTO;

    }
}
