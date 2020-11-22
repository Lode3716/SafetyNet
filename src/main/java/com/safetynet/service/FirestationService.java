package com.safetynet.service;

import com.googlecode.jmapper.JMapper;
import com.safetynet.dto.*;
import com.safetynet.dto.factory.ServiceFactory;
import com.safetynet.model.Firestations;
import com.safetynet.model.Persons;
import com.safetynet.repository.RepositoryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Log4j2
@Service
public class FirestationService implements IFirestationService {

    @Autowired
    RepositoryService repositorFirestations;

    @Autowired
    ServiceFactory serviceFactory;

    @Autowired
    JMapper<FirestationsDTO, Firestations> firestationsMapper;

    @Autowired
    JMapper<Firestations, FirestationsDTO> firestationsUnMapper;

    @Override
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


    @Override
    public Optional<FirestationsDTO> add(FirestationsDTO firestation) {
        Optional<Firestations> fire = repositorFirestations.getFirestationsRepository()
                .add(firestationsUnMapper.getDestination(firestation));
        return Optional.of(firestationsMapper.getDestination(fire.get()));
    }

    @Override
    public Optional<FirestationsDTO> update(FirestationsDTO firestationDTO) {
        Optional<Firestations> firestation = repositorFirestations.getFirestationsRepository()
                .update(firestationsUnMapper.getDestination(firestationDTO));
        if (firestation.isPresent()) {
            return Optional.of(firestationsMapper.getDestination(firestation.get()));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public boolean delete(FirestationsDTO firestationsDTO) {
        return repositorFirestations.getFirestationsRepository().delete(firestationsUnMapper.getDestination(firestationsDTO));
    }

    @Override
    public Optional<PersonsBelongFirestationDTO> getPersonsBelongFireStation(String stationNumber) {
        List<Firestations> station = repositorFirestations
                .getFirestationsRepository()
                .personsBelongFirestation(stationNumber);
        log.info("Service : Number station for create persons {}", station.size());
        if (station.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(serviceFactory.getPersonalBelongFirestationFactory().createPersonFirestation(station));

    }

    @Override
    public Optional<List<PersonsPhoneDTO>> getPhoneAlerte(String firestation_number) {
        List<Firestations> station = repositorFirestations
                .getFirestationsRepository()
                .personsBelongFirestation(firestation_number);
        log.info("Service : Number station for create list phones {}", station.size());
        if (station.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(serviceFactory.getPersonsPhoneFactory().createPersonsPhone(station));
    }

    @Override
    public Optional<ChildStationDTO> getChildAlertStation(String address) {
        List<Persons> persons = repositorFirestations
                .getFirestationsRepository()
                .personsAdress(address);
        log.info("Service : Adress station child live and parents list {}", persons.size());
        if (persons.size() == 0 || serviceFactory.getChildStationFactory().createChildStationDTO(persons).isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(serviceFactory.getChildStationFactory().createChildStationDTO(persons).get());
    }

    @Override
    public Optional<List<PersonsMedicalStationDTO>> getFireAdress(String address) {
        List<Persons> persons = repositorFirestations
                .getFirestationsRepository()
                .personsAdress(address);
        log.info("Service : Adress station list persons lives {}", persons.size());
        if (persons.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(serviceFactory.getPersonsMedicalsStationFactory().createPersonsMedicals(persons));
    }

    @Override
    public Optional<Map<FirestationsDTO, List<PersonsMedicalsDTO>>> getFloodStation(List<String> stations) {
        Map<FirestationsDTO, List<PersonsMedicalsDTO>> retour = new HashMap<>();
        stations.forEach(stat ->
        {
            repositorFirestations
                    .getFirestationsRepository()
                    .personsBelongFirestation(stat)
                    .forEach(firestation ->
                    {
                        List<PersonsMedicalsDTO> personeMedicalsList = serviceFactory.getPersonsMedicalsFactory().createPersonsMedicals(firestation.getPersonsList(), stat);
                        if (personeMedicalsList.size() > 0) {
                            retour.put(firestationsMapper.getDestination(firestation), personeMedicalsList);
                        }
                    });
        });
        if (retour.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(retour);
    }

}