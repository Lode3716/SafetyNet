package com.safetynet.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.dto.FirestationsDTO;
import com.safetynet.dto.PersonsDto;
import com.safetynet.model.Firestations;
import com.safetynet.model.Persons;
import com.safetynet.repository.RepositoryService;
import com.safetynet.web.exceptions.BadArgumentsException;
import com.safetynet.web.exceptions.FirestationNotFoundException;
import com.safetynet.web.exceptions.PersonsNotFoundException;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class FirestationsControllerIT {


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
    @DisplayName("Given a firestation, when POST request, then added Firestation return the created status as well as the firestation and check if it exists in BDD")
    public void givenFirestationDtoAdd_whenPostRequest_thenReturnFirestationDtoAdd() throws Exception {
        FirestationsDTO firestationsDTO = new FirestationsDTO("1","Château de Moulinsart");
        Firestations firestation = new Firestations("1","Château de Moulinsart");

        mvc.perform(MockMvcRequestBuilders.post("/firestation")
                .content(asJsonString(firestationsDTO))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").exists());

        assumeTrue(service.getFirestationsRepository().exist(firestation));
    }

    @Test
    @DisplayName("Given null id firestation, when POST request, then throws a BadArgumentsException")
    public void givenBadFirestationDtoAdd_whenPostRequest_thenReturnBadArgumentsException() throws Exception {
        FirestationsDTO firestationsDTO = new FirestationsDTO("","Château de Moulinsart");

        mvc.perform(MockMvcRequestBuilders.post("/firestation")
                .content(asJsonString(firestationsDTO))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadArgumentsException))
                .andExpect(result -> assertEquals("POST : Station or adress are not null for create Firestation", result.getResolvedException().getMessage()));

    }

    @Test
    @DisplayName("Given a firestation, when requesting DELETE, then delete firestation return htpp response accepted and check isNot present in BDD")
    public void givenFirestationDTO_whenDeleteRequest_thenReturnHttpAccept() throws Exception {
        FirestationsDTO firestationsDTO = new FirestationsDTO("2","951 LoneTree Rd");
        Firestations firestation = new Firestations("2","951 LoneTree Rd");

        mvc.perform(MockMvcRequestBuilders.delete("/firestation")
                .content(asJsonString(firestationsDTO))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isAccepted());

        assumeFalse(service.getFirestationsRepository().exist(firestation));
    }

    @Test
    @DisplayName("Given null id firestation, when POST request, then throws a BadArgumentsException")
    public void givenFirestationDTO_whenDeleteRequest_thenReturnBadArgumentsException() throws Exception {
        FirestationsDTO firestationsDTO = new FirestationsDTO("","Château de Moulinsart");

        mvc.perform(MockMvcRequestBuilders.delete("/firestation")
                .content(asJsonString(firestationsDTO))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadArgumentsException))
                .andExpect(result -> assertEquals("DELETE : Station or adress are not null for create Firestation", result.getResolvedException().getMessage()));

    }

    @Test
    @DisplayName("Given firestation not exist , when requesting DELETE, throws a FirestationNotFoundException")
    public void givenFirestationNotExist_whenDeleteRequest_thenReturnFirestationNotFoundException() throws Exception {
        FirestationsDTO firestationsDTO = new FirestationsDTO("Dupont","6 rue Tintin");

        mvc.perform(MockMvcRequestBuilders.delete("/firestation")
                .content(asJsonString(firestationsDTO))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof FirestationNotFoundException));
    }
}