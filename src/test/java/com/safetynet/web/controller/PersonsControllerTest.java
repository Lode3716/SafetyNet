package com.safetynet.web.controller;

import lombok.extern.log4j.Log4j2;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
@SpringBootTest
@AutoConfigureMockMvc
class PersonsControllerTest {

    @Autowired
    private MockMvc mvc;


    @Test
    public void given_allist_person() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/persons")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(s -> log.info("Passe le trux {}", s))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(23)));
    }

}