package com.safetynet.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.safetynet.dto.MedicalRecordsDTO;

import com.safetynet.model.Medicalrecords;
import com.safetynet.repository.RepositoryService;
import com.safetynet.web.exceptions.BadArgumentsException;

import com.safetynet.web.exceptions.MedicalrecordsNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MedicalRecordsIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    RepositoryService service;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Given a medicalRecords, when POST request, then added medicalRecords return the created status as well as the medicalRecords and check if it exists in BDD")
    public void givenMedicalRecordsDtoAdd_whenPostRequest_thenReturnMedicalRecordDtoAdd() throws Exception {
        List<String> medi = Arrays.asList("doliprane", "paracetamol");
        List<String> aller = Arrays.asList("cacahuette", "milk");
        MedicalRecordsDTO medicalRecordsDTO = new MedicalRecordsDTO();
        medicalRecordsDTO.setFirstName("Laurenzo");
        medicalRecordsDTO.setLastName("Dev");
        medicalRecordsDTO.setBirthdate("01/03/1981");
        medicalRecordsDTO.setAllergies(aller);
        medicalRecordsDTO.setMedications(medi);

        Medicalrecords medicalRecords = new Medicalrecords();
        medicalRecords.setFirstName("Laurenzo");
        medicalRecords.setLastName("Dev");
        medicalRecords.setBirthdate("01/03/1981");
        medicalRecords.setAllergies(aller);
        medicalRecords.setMedications(medi);

        mvc.perform(MockMvcRequestBuilders.post("/medicalRecord")
                .content(asJsonString(medicalRecordsDTO))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").exists());

        assumeTrue(service.getMedicalrecordsRepository().exist(medicalRecords));
    }


    @Test
    @DisplayName("Given null last name, when POST request, then throws a BadArgumentsException")
    public void givenBadMedicalRecordsAdd_whenPostRequest_thenReturnBadArgumentsException() throws Exception {
        List<String> medi = Arrays.asList("doliprane", "paracetamol");
        List<String> aller = Arrays.asList("cacahuette", "milk");
        MedicalRecordsDTO medicalRecordsDTO = new MedicalRecordsDTO();
        medicalRecordsDTO.setFirstName("Laurenzo");
        medicalRecordsDTO.setLastName("");
        medicalRecordsDTO.setBirthdate("01/03/1981");
        medicalRecordsDTO.setAllergies(aller);
        medicalRecordsDTO.setMedications(medi);

        mvc.perform(MockMvcRequestBuilders.post("/medicalRecord")
                .content(asJsonString(medicalRecordsDTO))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadArgumentsException))
                .andExpect(result -> assertEquals("POST : firstName or lastName are not null for create medical records", result.getResolvedException().getMessage()));

    }

    @Test
    @DisplayName("Given MedicalRecords update , when requesting PUT, then requesting update medical record information")
    public void givenMedicalRecordsUpdate_whenPutRequest_thenReturnUpdateMedicalRecords() throws Exception {
        List<String> medi = Arrays.asList("cortisone");
        List<String> aller = Arrays.asList("cacahuette", "milk");
        MedicalRecordsDTO medicalRecordsDTO = new MedicalRecordsDTO();
        medicalRecordsDTO.setFirstName("Zach");
        medicalRecordsDTO.setLastName("Zemicks");
        medicalRecordsDTO.setBirthdate("01/03/1982");
        medicalRecordsDTO.setAllergies(aller);
        medicalRecordsDTO.setMedications(medi);

        mvc.perform(MockMvcRequestBuilders.put("/medicalRecord")
                .content(asJsonString(medicalRecordsDTO))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.birthdate").value("01/03/1982"))
                .andExpect(jsonPath("$.medications[0]").value("cortisone"));
    }

    @Test
    @DisplayName("Given null id medicalrecord upadte, when PUT request, then throws a BadArgumentsException")
    public void givenMedicalRecordNull_whenPutRequest_thenReturnBadArgumentsException() throws Exception {
        MedicalRecordsDTO medicalRecordsDTO = new MedicalRecordsDTO();
        medicalRecordsDTO.setFirstName("");
        medicalRecordsDTO.setLastName("Dev");
        medicalRecordsDTO.setBirthdate("01/03/1982");

        mvc.perform(MockMvcRequestBuilders.put("/medicalRecord")
                .content(asJsonString(medicalRecordsDTO))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadArgumentsException))
                .andExpect(result -> assertEquals("PUT : firstName or lastName are not null for update medical records", result.getResolvedException().getMessage()));

    }

    @Test
    @DisplayName("Given medicalrecord not exist update, when PUT request, throws a medicalrecord not found exception")
    public void givenMedicalRecordNotExist_whenPutRequest_thenReturnMedicalRecordsNotFoundException() throws Exception {
        MedicalRecordsDTO medicalRecordsDTO = new MedicalRecordsDTO();
        medicalRecordsDTO.setFirstName("Jason");
        medicalRecordsDTO.setLastName("Tatame");
        medicalRecordsDTO.setBirthdate("01/03/1972");

        mvc.perform(MockMvcRequestBuilders.put("/medicalRecord")
                .content(asJsonString(medicalRecordsDTO))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MedicalrecordsNotFoundException));
    }

    @Test
    @DisplayName("Given a medicalrecord, when requesting DELETE, then delete medicalrecord return htpp response accepted and check isNot present in BDD")
    public void givenMedicalrecordDTO_whenDeleteRequest_thenReturnHttpAccept() throws Exception {
        List<String> medi = Arrays.asList("cortisone");
        List<String> aller = Arrays.asList("cacahuette", "milk");
        MedicalRecordsDTO medicalRecordsDTO = new MedicalRecordsDTO();
        medicalRecordsDTO.setFirstName("Laurenzo");
        medicalRecordsDTO.setLastName("Dev");
        medicalRecordsDTO.setBirthdate("01/03/1981");
        medicalRecordsDTO.setAllergies(aller);
        medicalRecordsDTO.setMedications(medi);

        medicalRecordsDTO.setAllergies(aller);
        medicalRecordsDTO.setMedications(medi);

        Medicalrecords medicalRecords = new Medicalrecords();
        medicalRecords.setFirstName("Laurenzo");
        medicalRecords.setLastName("Dev");
        medicalRecords.setBirthdate("01/03/1981");
        medicalRecords.setAllergies(aller);
        medicalRecords.setMedications(medi);

        mvc.perform(MockMvcRequestBuilders.delete("/medicalRecord")
                .content(asJsonString(medicalRecordsDTO))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isAccepted());

        assumeFalse(service.getMedicalrecordsRepository().exist(medicalRecords));
    }

    @Test
    @DisplayName("Given null id medicalrecord, when POST request, then throws a BadArgumentsException")
    public void givenMedicalRecordsDTO_whenDeleteRequest_thenReturnBadArgumentsException() throws Exception {
        MedicalRecordsDTO medicalRecordsDTO = new MedicalRecordsDTO();
        medicalRecordsDTO.setFirstName("Zacho");
        medicalRecordsDTO.setLastName("");
        medicalRecordsDTO.setBirthdate("01/03/1982");

        mvc.perform(MockMvcRequestBuilders.delete("/medicalRecord")
                .content(asJsonString(medicalRecordsDTO))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadArgumentsException))
                .andExpect(result -> assertEquals("DELETE : firstName or lastName are not null for update medical records", result.getResolvedException().getMessage()));

    }

    @Test
    @DisplayName("Given medicalrecord not exist , when requesting DELETE, throws a medicalrecord not found exception")
    public void givenMedicalRecordsDTONotExist_whenDeleteRequest_thenReturnMedicalRecordsNotFoundException() throws Exception {
        MedicalRecordsDTO medicalRecordsDTO = new MedicalRecordsDTO();
        medicalRecordsDTO.setFirstName("Jason");
        medicalRecordsDTO.setLastName("Tatame");
        medicalRecordsDTO.setBirthdate("01/03/1972");

        mvc.perform(MockMvcRequestBuilders.delete("/medicalRecord")
                .content(asJsonString(medicalRecordsDTO))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MedicalrecordsNotFoundException));
    }


}
