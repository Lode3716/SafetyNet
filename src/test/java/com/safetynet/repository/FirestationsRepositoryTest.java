package com.safetynet.repository;

import com.safetynet.model.Firestations;
import com.safetynet.model.Persons;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Log
@ExtendWith(MockitoExtension.class)
public class FirestationsRepositoryTest {

    @Mock
    private FirestationsRepository firestationsRepository;

    @BeforeEach //instead of @Before have to use @BeforeEach in junit5
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void givenFirestation_shouldPersist_firestationISCorrect() {
        Firestations fire = new Firestations("3", "La rue de la chèvre");

        when(firestationsRepository.add(fire)).thenReturn(Optional.of(fire));

        Optional<Firestations> result = firestationsRepository.add(fire);

        assertThat(result).isEqualTo(Optional.of(fire));

    }

    @Test
    public void givenFirestation_shouldDelete_firestationISDeleteReturnTrue() {
        Firestations fire = new Firestations("2", "Impasse de la fin");

        when(firestationsRepository.delete(any(Firestations.class))).thenReturn(true);

        boolean result = firestationsRepository.delete(fire);

        verify(firestationsRepository).delete(any(Firestations.class));
        assertThat(result).isTrue();

    }

    @Test
    public void givenFirestation_shouldDelete_firestationISNotDeleteReturnFalse() {
        Firestations fire = new Firestations("2", "Impasse de la fin");

        when(firestationsRepository.delete(any(Firestations.class))).thenReturn(false);

        boolean result = firestationsRepository.delete(fire);

        verify(firestationsRepository).delete(any(Firestations.class));
        assertThat(result).isFalse();

    }

    @Test
    public void givenFirestation_shouldFind_firestationExistReturnTrue() {
        Firestations fire = new Firestations("2", "Impasse de la fin");

        when(firestationsRepository.exist(any(Firestations.class))).thenReturn(true);

        boolean result = firestationsRepository.exist(fire);

        verify(firestationsRepository).exist(any(Firestations.class));
        assertThat(result).isTrue();

    }

    @Test
    public void givenFirestation_shouldUpdate_firestationExistReturnFirestatioUpdate() {
        Firestations fire = new Firestations("2", "Impasse de la fin");

        when(firestationsRepository.update(any(Firestations.class))).thenReturn(Optional.of(fire));

        Optional<Firestations> result = firestationsRepository.update(fire);

        verify(firestationsRepository).update(any(Firestations.class));
        assertThat(result).isEqualTo(Optional.of(fire));
    }

    @Test
    public void givenFirestation_shouldUpdate_firestationnNotExistReturnOptionalEmpty() {
        Firestations fire = new Firestations("2", "Impasse de la fin");

        when(firestationsRepository.update(any(Firestations.class))).thenReturn(Optional.empty());

        Optional<Firestations> result = firestationsRepository.update(fire);

        verify(firestationsRepository).update(any(Firestations.class));
        assertThat(result).isEqualTo(Optional.empty());
    }

    @Test
    public void givenIdFirestation_shouldListFireStation_firestationList() {
        Firestations fire = new Firestations("3", "Impasse de la fin");
        Firestations fire1 = new Firestations("3", "Impasse du debut");
        when(firestationsRepository.personsBelongFirestation(any(String.class))).thenReturn(Arrays.asList(fire, fire1));

        List<Firestations> result = firestationsRepository.personsBelongFirestation("3");

        verify(firestationsRepository).personsBelongFirestation(any(String.class));
        assertThat(result).asList();
        assertThat(result).size().isEqualTo(2);
    }

}