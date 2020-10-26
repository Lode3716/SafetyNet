package com.safetynet.dto;

import lombok.extern.log4j.Log4j2;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Log4j2
public class UtilsDTO {

    private static DateTimeFormatter format = DateTimeFormatter.ofPattern("d/MM/yyyy");
    private static DateTimeFormatter formatYear = DateTimeFormatter.ofPattern("yyyy");

    public Optional<Integer> calculAge(String birthday) {
        try {
            return Optional.of(Period.between(LocalDate.parse(birthday, format), LocalDate.now())
                    .getYears());

        } catch (DateTimeException ex) {
            log.error("La date pour le calcul de l'age est invalide, elle sera générée sur la base de l'année . / error :" + ex.getMessage());
            return genereDate(birthday);
        }
    }

    public Optional<Boolean> adultOrNot(String birthday) {
        Boolean control = calculAge(birthday).isPresent();
        if (control) {
            return Optional.of(calculAge(birthday).get() > 19);
        }

        return Optional.empty();
    }

    private Optional<Integer> genereDate(String birthday)
    {
        Integer retour=LocalDate.now().getYear()-LocalDate.parse(birthday, formatYear).getYear();
       return Optional.of(retour);
    }
}


