package com.safetynet.service;

import com.safetynet.dto.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IFirestationService {

    List<FirestationsDTO> findAll();

    /**
     * Add firestation and Map Firestation DTO
     *
     * @param firestation
     * @return FirestationDTO
     */
    Optional<FirestationsDTO> add(FirestationsDTO firestation);

    Optional<FirestationsDTO> update(FirestationsDTO firestationDTO);

    boolean delete(FirestationsDTO firestationsDTO);

    Optional<PersonsBelongFirestationDTO> getPersonsBelongFireStation(String stationNumber);

    /**
     * List of the telephone numbers of residents served by the fire station
     * @param stationNumber
     * @return list phone if not phone return Optional.empty
     */
    Optional<List<PersonsPhoneDTO>> getPhoneAlerte(String firestation_number);

    /**
     * list of children (anyone aged 18 or under) living at that address and list parents
     * @param address
     * @return Optional ChildStationDto if not child return Optional.empty
     */
    Optional<ChildStationDTO> getChildAlertStation(String address);

    List<PersonsMedicalStationDTO> getFireAdress(String address);

    Map<FirestationsDTO, List<PersonsMedicalsDTO>> getFloodStation(List<String> stations);
}
