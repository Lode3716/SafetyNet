package com.safetynet.dto;

import org.junit.jupiter.api.Test;

import java.time.DateTimeException;

import static org.assertj.core.api.Assertions.*;

class UtilsDTOTest {


    private static final UtilsDTO utilsDTO = new UtilsDTO();

    @Test
    public void calculateAge_thenReturnCorrectAge() {
        String birthday = "03/15/2000";

        int age = utilsDTO.calculAge(birthday).get();

        assertThat(age).isEqualTo(20);

    }

    @Test
    public void calculateAge_thenIllegallDateThrown() {
        String birthday = "15/15/2000";
        Throwable thrown = catchThrowable(() -> utilsDTO.calculAge(birthday));
        assertThat(thrown)
                .isInstanceOf(Exception.class)
                .hasCauseExactlyInstanceOf(DateTimeException.class);
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

}