package com.safetynet.repository;


import com.safetynet.dto.MedicalRecordsDTO;
import com.safetynet.web.controller.MedicalRecordsController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


public class MedicalRecordsControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MedicalRecordsController medicalRecordsControllerTest;

    @Test
    public void givenAUser_whenRequestIsMadeToAdd_thenaSouldBeShown() throws Exception {

        /*mockMvc.perform(MockMvcRequestBuilders.post("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)*/

    }
}
