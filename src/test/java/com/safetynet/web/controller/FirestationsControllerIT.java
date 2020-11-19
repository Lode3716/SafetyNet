package com.safetynet.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.dto.FirestationsDTO;
import com.safetynet.dto.PersonsBelongFirestationDTO;
import com.safetynet.dto.PersonsFirestationDTO;
import com.safetynet.model.Firestations;
import com.safetynet.repository.RepositoryService;
import com.safetynet.web.exceptions.BadArgumentsException;
import com.safetynet.web.exceptions.FirestationNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

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
        FirestationsDTO firestationsDTO = new FirestationsDTO("1", "Château de Moulinsart");
        Firestations firestation = new Firestations("1", "Château de Moulinsart");

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
        FirestationsDTO firestationsDTO = new FirestationsDTO("", "Château de Moulinsart");

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
        FirestationsDTO firestationsDTO = new FirestationsDTO("2", "951 LoneTree Rd");
        Firestations firestation = new Firestations("2", "951 LoneTree Rd");

        mvc.perform(MockMvcRequestBuilders.delete("/firestation")
                .content(asJsonString(firestationsDTO))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isAccepted());

        assumeFalse(service.getFirestationsRepository().exist(firestation));
    }

    @Test
    @DisplayName("Given null id firestation, when POST request, then throws a BadArgumentsException")
    public void givenFirestationDTO_whenDeleteRequest_thenReturnBadArgumentsException() throws Exception {
        FirestationsDTO firestationsDTO = new FirestationsDTO("", "Château de Moulinsart");

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
        FirestationsDTO firestationsDTO = new FirestationsDTO("Dupont", "6 rue Tintin");

        mvc.perform(MockMvcRequestBuilders.delete("/firestation")
                .content(asJsonString(firestationsDTO))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof FirestationNotFoundException));
    }

    @Test
    @DisplayName("Given firestation update , when requesting PUT, then requesting firestation update the station and the status http is OK")
    public void givenFirestationUpdate_whenPutRequest_thenReturnUpdateFirestation() throws Exception {
        Firestations firestation = new Firestations("5", "834 Binoc Ave");

        mvc.perform(MockMvcRequestBuilders.put("/firestation")
                .content(asJsonString(firestation))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.station").value("5"))
                .andExpect(jsonPath("$.address").value("834 Binoc Ave"));
    }

    @Test
    @DisplayName("Given null id firestation upadte, when PUT request, then throws a BadArgumentsException")
    public void givenFirestationNull_whenPutRequest_thenReturnBadArgumentsException() throws Exception {
        FirestationsDTO firestationsDTO = new FirestationsDTO("", "Château de Moulinsart");

        mvc.perform(MockMvcRequestBuilders.put("/firestation")
                .content(asJsonString(firestationsDTO))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadArgumentsException))
                .andExpect(result -> assertEquals("PUT : Station or adress are not null for update firestation", result.getResolvedException().getMessage()));

    }

    @Test
    @DisplayName("Given firestation not exist update, when PUT request, throws a FirestationNotFoundException")
    public void givenFirestationNotExist_whenPutRequest_thenReturnFirestationNotFoundException() throws Exception {
        FirestationsDTO firestationsDTO = new FirestationsDTO("9", "7 rue Tintin");

        mvc.perform(MockMvcRequestBuilders.put("/firestation")
                .content(asJsonString(firestationsDTO))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof FirestationNotFoundException));
    }

    @Test
    @DisplayName("Given id Station , when GET request, then requesting list person for number station and count by age")
    public void givenIdStation_whenGetRequest_thenReturnPersonsBelongFireStation() throws Exception {

        mvc.perform(MockMvcRequestBuilders.get("/firestation")
                .queryParam("stationNumber", "4")
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.listPersons", Matchers.hasSize(4)))
                .andExpect(jsonPath("$.countAdult", Matchers.is(4)))
                .andExpect(jsonPath("$.countChild", Matchers.is(0)))
                .andExpect(jsonPath("$.listPersons[0].firstName", Matchers.is("Lily")));
    }

    @Test
    @DisplayName("Given id Station not exist , when Get request, throws a FirestationNotFoundException")
    public void givenIdStationNotExist_whenGetRequest_thenReturnFirestationNotFoundException() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/firestation")
                .queryParam("stationNumber", "10")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof FirestationNotFoundException));
    }

    @Test
    @DisplayName("Given id Station null , when Get request, then throws a BadArgumentsException")
    public void givenIdStationNull_whenGetRequest_thenReturnBadArgumentsException() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/firestation")
                .queryParam("stationNumber", "")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadArgumentsException))
                .andExpect(result -> assertEquals("GET : Station number is  not null for create list person", result.getResolvedException().getMessage()));
    }

    @Test
    @DisplayName("Given id Station , when GET request, then requesting list of the telephone numbers of residents served by the fire station")
    public void givenIdFirestation_whenGetRequest_thenReturnListPhone() throws Exception {

        mvc.perform(MockMvcRequestBuilders.get("/phoneAlert")
                .queryParam("firestation", "4")
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(4)));
    }

    @Test
    @DisplayName("Given id Station not exist , when Get request, throws a FirestationNotFoundException")
    public void givenIdFirestationNotExist_whenGetRequest_thenReturnFirestationNotFoundException() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/phoneAlert")
                .queryParam("firestation", "10")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof FirestationNotFoundException));
    }

    @Test
    @DisplayName("Given id Station null , when Get request, then throws a BadArgumentsException")
    public void givenIdFirestationNull_whenGetRequest_thenReturnBadArgumentsException() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/phoneAlert")
                .queryParam("firestation", "")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadArgumentsException))
                .andExpect(result -> assertEquals("GET : Station number is  not null for create list phone", result.getResolvedException().getMessage()));
    }

    @Test
    @DisplayName("Given address Station , when GET request, then requesting list of children (anyone aged 18 or under) living at that address and list parents")
    public void givenAddressStation_whenGetRequest_thenReturnListchildAndParents() throws Exception {

        mvc.perform(MockMvcRequestBuilders.get("/childAlert")
                .queryParam("address", "1509 Culver St")
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.children", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.parents", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.children[0].firstName", Matchers.is("Tenley")))
                .andExpect(jsonPath("$.children[0].lastName", Matchers.is("Boyd")))
                .andExpect(jsonPath("$.children[0].age", Matchers.is(8)));
    }

    @Test
    @DisplayName("Given adress Station not exist , when Get request, throws a FirestationNotFoundException")
    public void givenAddressStationNotExist_whenGetRequest_thenReturnFirestationNotFoundException() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/childAlert")
                .queryParam("address", "1508 Culver St")
                .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof FirestationNotFoundException));
    }

    @Test
    @DisplayName("Given adress Station null , when Get request, then throws a BadArgumentsException")
    public void givenAdressNull_whenGetRequest_thenReturnBadArgumentsException() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/childAlert")
                .queryParam("address", "")
                .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadArgumentsException))
                .andExpect(result -> assertEquals("GET : Address station is  not null for create list child", result.getResolvedException().getMessage()));
    }

}