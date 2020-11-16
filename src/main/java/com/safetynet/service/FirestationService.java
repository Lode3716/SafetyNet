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

    /**
     * Add firestation and Map Firestation DTO
     *
     * @param firestation
     * @return FirestationDTO
     */
    public Optional<FirestationsDTO> add(FirestationsDTO firestation) {
        Optional<Firestations> fire = repositorFirestations.getFirestationsRepository()
                .add(firestationsUnMapper.getDestination(firestation));
        return Optional.of(firestationsMapper.getDestination(fire.get()));
    }

    public Optional<FirestationsDTO> update(FirestationsDTO firestationDTO) {
        Optional<Firestations> firestation = repositorFirestations.getFirestationsRepository()
                .update(firestationsUnMapper.getDestination(firestationDTO));
        if (firestation.isPresent()) {
            return Optional.of(firestationsMapper.getDestination(firestation.get()));
        } else {
            return Optional.empty();
        }
    }

    public boolean delete(FirestationsDTO firestationsDTO) {
        return repositorFirestations.getFirestationsRepository().delete(firestationsUnMapper.getDestination(firestationsDTO));
    }

    public Optional<PersonsBelongFirestationDTO> getPersonsBelongFireStation(String stationNumber) {
        List<Firestations> station = repositorFirestations
                .getFirestationsRepository()
                .personsBelongFirestation(stationNumber);
        log.info("Service : Number station for create persons {}",station.size());
        if (station.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(serviceFactory.getPersonalBelongFirestationFactory().createPersonFirestation(station));

    }

    public Optional<List<PersonsPhoneDTO>> getPhoneAlerte(String firestation_number) {
        List<Firestations> station = repositorFirestations
                .getFirestationsRepository()
                .personsBelongFirestation(firestation_number);
        log.info("Service : Number station for create list phones {}",station.size());
        if (station.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(serviceFactory.getPersonsPhoneFactory().createPersonsPhone(station));
    }

    public ChildStationDTO getChildAlertStation(String address) {
        List<Persons> persons = repositorFirestations
                .getFirestationsRepository()
                .personsAdress(address);
        return serviceFactory.getChildStationFactory().createChildStationDTO(persons);
    }

    public List<PersonsMedicalStationDTO> getFireAdress(String address) {
        List<Persons> persons = repositorFirestations
                .getFirestationsRepository()
                .personsAdress(address);
        return serviceFactory.getPersonsMedicalsStationFactory().createPersonsMedicals(persons);
    }

    public Map<FirestationsDTO, List<PersonsMedicalsDTO>> getFloodStation(List<String> stations) {
        Map<FirestationsDTO, List<PersonsMedicalsDTO>> retour = new HashMap<>();
        stations.forEach(stat ->
        {
            log.info(stat);
            repositorFirestations
                    .getFirestationsRepository()
                    .personsBelongFirestation(stat)
                    .forEach(firestation ->
                    {
                        List<PersonsMedicalsDTO> personeMedicalsList = serviceFactory.getPersonsMedicalsFactory().createPersonsMedicals(firestation.getPersonsList(), stat);
                        retour.put(firestationsMapper.getDestination(firestation), personeMedicalsList);

                    });
        });
        return retour;
    }

}