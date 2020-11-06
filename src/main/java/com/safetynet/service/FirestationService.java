package com.safetynet.service;

import com.googlecode.jmapper.JMapper;
import com.safetynet.dto.PersonsPhoneDTO;
import com.safetynet.model.Firestations;
import com.safetynet.dto.FirestationsDTO;
import com.safetynet.dto.PersonsBelongFirestationDTO;
import com.safetynet.dto.factory.ServiceFactory;
import com.safetynet.repository.RepositoryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Log4j2
@Service
public class FirestationService {

    @Autowired
    RepositoryService repositorFirestations;

    @Autowired
    ServiceFactory serviceFactory;

    @Autowired
    JMapper<FirestationsDTO, Firestations> firestationsMapper;

    @Autowired
    JMapper<Firestations, FirestationsDTO> firestationsUnMapper;

    public List<FirestationsDTO> findAll() {
        List<FirestationsDTO> list = new ArrayList<>();
        repositorFirestations.getFirestationsRepository().findAll().forEach(fire ->
                {
                    list.add(firestationsMapper.getDestination(fire));
                }
        );
        log.debug("Service : Retourne la liste contruite de toutes les firestations DTO {}", list.size());
        return list;
    }

    public Optional<Firestations> add(FirestationsDTO firestation) {
        return repositorFirestations.getFirestationsRepository()
                .add(firestationsUnMapper.getDestination(firestation));

    }

    public Optional<Firestations> update(FirestationsDTO firestation) {
        return repositorFirestations.getFirestationsRepository()
                .update(firestationsUnMapper.getDestination(firestation));
    }

    public boolean delete(FirestationsDTO firestationsDTO) {
        return repositorFirestations.getFirestationsRepository().delete(firestationsUnMapper.getDestination(firestationsDTO));
    }

    public PersonsBelongFirestationDTO personsBelongFireStation(String stationNumber) {
        List<Firestations> station = repositorFirestations
                .getFirestationsRepository()
                .personsBelongFirestation(stationNumber);

        return serviceFactory.getPersonalBelongFirestationFactory().createPersonFirestation(station);

    }

    public List<PersonsPhoneDTO> phoneAlerte(String firestation_number) {
        List<Firestations> station = repositorFirestations
                .getFirestationsRepository()
                .personsBelongFirestation(firestation_number);
        return serviceFactory.getPersonsPhoneFactory().createPersonsPhone(station);

    }
}