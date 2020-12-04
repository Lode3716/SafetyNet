package com.safetynet.dto;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class UtilsDTOTest {


    private static final UtilsDTO utilsDTO = new UtilsDTO();

    @Test
    public void calculateAge_thenReturnCorrectAge() {
        String birthday = "03/15/2000";

        int age = utilsDTO.calculAge(birthday).get();

        assertThat(age).isEqualTo(20);

    }


    @Test
    public void adultOrNot_returnTrueIfAdult() {
        String birthday = "03/15/2000";

        assertThat(utilsDTO.adultOrNot(birthday).get()).isEqualTo(true);

    }

    @Test
    public void adultOrNot_returnFalseIfChild() {
        String birthday = "04/15/2010";

        assertThat(utilsDTO.adultOrNot(birthday).get()).isEqualTo(false);

    }

    @Test
    public void calculateAge_returnOptionalEmpty() {

        assertThat(utilsDTO.adultOrNot("")).isEqualTo(Optional.empty());
    }

}