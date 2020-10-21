package com.safetynet.dto;

import com.googlecode.jmapper.JMapper;
import com.safetynet.model.Firestations;
import com.safetynet.model.Medicalrecords;
import com.safetynet.model.Persons;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigDTO {

    @Bean
    JMapper<FirestationsDTO, Firestations> firestationsMapper() {
        return new JMapper<>(FirestationsDTO.class, Firestations.class);
    }

    @Bean
    JMapper<Firestations, FirestationsDTO> firestationsUnMapper() {
        return new JMapper<>(Firestations.class, FirestationsDTO.class);
    }

    @Bean
    JMapper<MedicalRecordsDTO, Medicalrecords> medicalRecordsMapper() {
        return new JMapper<>(MedicalRecordsDTO.class, Medicalrecords.class);
    }

    @Bean
    JMapper<Medicalrecords, MedicalRecordsDTO> medicalRecordsUnMapperr() {
        return new JMapper<>(Medicalrecords.class, MedicalRecordsDTO.class);
    }

    @Bean
    JMapper<PersonsDto, Persons> personMapper() {
        return new JMapper<>(PersonsDto.class, Persons.class);
    }

    @Bean
    JMapper<Persons, PersonsDto> personUnMapper() {
        return new JMapper<>(Persons.class, PersonsDto.class);
    }

}
