package com.safetynet.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.dto.PersonsDto;
import com.safetynet.model.Persons;
import com.safetynet.repository.RepositoryService;
import com.safetynet.web.exceptions.BadArgumentsException;
import com.safetynet.web.exceptions.PersonsNotFoundException;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;


@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class PersonsControllerIT {

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
    public void given_allist_person() throws Exception {
        mvc.perform(get("/persons")
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(23)));
    }

    @Test
    @DisplayName("Given a personDTO, when POST request, then added personDTO return the created status as well as the person and check if it exists in BDD")
    public void givenPersonDtoAdd_whenPostRequest_thenReturnPersonDtoAdd() throws Exception {
        PersonsDto personsDto = new PersonsDto("Dupond", "Dupont", "Moulinsard", "City", "ZIP", "09-08-07-05", "dupont@email.com");

        mvc.perform(MockMvcRequestBuilders.post("/person")
                .content(asJsonString(personsDto))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").exists());

        Optional<Persons> persons=service.getPersonsRepository().findByElements(personsDto.getLastName(), personsDto.getFirstName());
        assumeTrue(persons.isPresent(),"La valeur n'a pas été trouvé en BDD");
    }

    @Test
    @DisplayName("Given null id of personDTO, when requesting POST, throws a BadArgumentsException")
    public void givenBadIdPersons_whenPostRequest_thenReturnBadArgumentsException() throws Exception {
        PersonsDto personsDto = new PersonsDto("Dupond", "", "Moulinsard", "City", "ZIP", "09-08-07-05", "dupont@email.com");

        mvc.perform(MockMvcRequestBuilders.post("/person")
                .content(asJsonString(personsDto))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadArgumentsException))
                .andExpect(result -> assertEquals("POST : firstName and lastName are not null for create personDTO", result.getResolvedException().getMessage()));
    }

    @Test
    @DisplayName("Given a personDTO, when requesting DELETE, then delete person return htpp response accepted and check isNot present in BDD")
    public void givenPersonDto_whenDeleterequest_thenReturnHttpAccept() throws Exception {
        PersonsDto personsDto = new PersonsDto("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");

        mvc.perform(MockMvcRequestBuilders.delete("/person")
                .content(asJsonString(personsDto))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isAccepted());

        Optional<Persons> persons=service.getPersonsRepository().findByElements(personsDto.getLastName(), personsDto.getFirstName());
        assumeFalse(persons.isPresent());
    }

    @Test
    @DisplayName("Given null id of personDTO, when requesting DELETE, throws a BadArgumentsException")
    public void givenBadIdPersons_whenDeleteRequest_thenReturnBadArgumentsException() throws Exception {
        PersonsDto personsDto = new PersonsDto("", "Boyd", "Moulinsard", "City", "ZIP", "09-08-07-05", "dupont@email.com");

        mvc.perform(MockMvcRequestBuilders.delete("/person")
                .content(asJsonString(personsDto))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadArgumentsException))
                .andExpect(result -> assertEquals("DELETE : firstName and lastName are not null for create personDTO", result.getResolvedException().getMessage()));
    }

    @Test
    @DisplayName("Given person not exist , when requesting DELETE, throws a PersonsNotFoundException")
    public void givenPersonNotExist_whenDeleteRequest_thenReturnPersonsNotFoundException() throws Exception {
        PersonsDto personsDto = new PersonsDto("Johnna", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");

        mvc.perform(MockMvcRequestBuilders.delete("/person")
                .content(asJsonString(personsDto))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof PersonsNotFoundException));
    }

    @Test
    @DisplayName("Given person update , when requesting PUT, then requesting Persons update and the status http is OK")
    public void givenPersonUpdate_whenPutRequest_thenReturnUpdatePersonn() throws Exception {
        PersonsDto personsDto = new PersonsDto("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-9999", "jaboyd@email.com");

        mvc.perform(MockMvcRequestBuilders.put("/person")
                .content(asJsonString(personsDto))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Boyd"))
                .andExpect(jsonPath("$.address").value("1509 Culver St"))
                .andExpect(jsonPath("$.city").value("Culver"))
                .andExpect(jsonPath("$.zip").value("97451"))
                .andExpect(jsonPath("$.phone").value("841-874-9999"))
                .andExpect(jsonPath("$.email").value("jaboyd@email.com"));

    }

    @Test
    @DisplayName("Given person not exist , when requesting PUT, then throws a PersonsNotFoundException")
    public void givenPersonNotExist_whenPutReques_thenReturnNotFoundPersonn() throws Exception {
        PersonsDto personsDto = new PersonsDto("Johnny", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-9999", "jaboyd@email.com");

        mvc.perform(MockMvcRequestBuilders.put("/person")
                .content(asJsonString(personsDto))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof PersonsNotFoundException));

    }

    @Test
    @DisplayName("Given null id of personDTO, when requesting PUT, then throws a BadArgumentsException")
    public void givenBadIdPersons_whenPutRequest_thenReturnBadArgumentsException() throws Exception {
        PersonsDto personsDto = new PersonsDto("", "", "Moulinsard", "City", "ZIP", "09-08-07-05", "dupont@email.com");

        mvc.perform(MockMvcRequestBuilders.put("/person")
                .content(asJsonString(personsDto))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadArgumentsException))
                .andExpect(result -> assertEquals("UPDATE : firstName and lastName are not null for create personDTO", result.getResolvedException().getMessage()));
    }


    @Test
    @DisplayName("Given param FirstName and LastName exist, when requesting GET, then requesting a list PersonsMedicationAdresseDTO")
    public void givenFirstNameAndLastName_whenGetRequest_thenReturnListPersonsMedicationAdresseDTO() throws Exception {
        mvc.perform(get("/personInfo")
                .queryParam("firstName","Ron")
                .queryParam("lastName","Peters")
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)));
    }

    @Test
    @DisplayName("Given param FirstName and LastName not exist, when requesting GET, then throws a PersonsNotFoundException")
    public void givenFirstNameAndLastNameNotExist_whenGetRequest_thenReturnNotFoundPersonn() throws Exception {
        mvc.perform(get("/personInfo")
                .queryParam("firstName","Lorenzo")
                .queryParam("lastName","DeLaRamasse")
                .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof PersonsNotFoundException));
    }

    @Test
    @DisplayName("Given param null FirstName and LastName, when requesting GET, then throws a BadArgumentsException")
    public void givenFirstNameAndLastNameNull_whenGetRequest_thenReturnBadArgumentsException() throws Exception {
        mvc.perform(get("/personInfo")
                .queryParam("firstName","")
                .queryParam("lastName","")
                .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadArgumentsException));
    }

    @Test
    @DisplayName("Given param city, when requesting GET, then requesting a List the email addresses of all the inhabitants of the city. ")
    public void givenCity_whenGetRequest_thenReturnListPersonsEmail() throws Exception {
        mvc.perform(get("/communityEmail")
                .queryParam("city","Culver")
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(15)));
    }

    @Test
    @DisplayName("Given param city not exist, when requesting GET, then throws a PersonsNotFoundException")
    public void givenCityNotExist_whenGetRequest_thenReturnNotFoundPersonn() throws Exception {
        mvc.perform(get("/communityEmail")
                .queryParam("city","Royan")
                .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof PersonsNotFoundException));
    }

    @Test
    @DisplayName("Given param null city, when requesting GET, then throws a BadArgumentsException")
    public void givenCityNull_whenGetRequest_thenReturnBadArgumentsException() throws Exception {
        mvc.perform(get("/communityEmail")
                .queryParam("city","")
                .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadArgumentsException));
    }
}