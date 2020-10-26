package com.safetynet.dto;

import com.googlecode.jmapper.annotations.JGlobalMap;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
@Data
@JGlobalMap
public class PersonsBelongFirestationDTO {

    private List<PersonsFirestationDTO> listPersons;
    private int countAdult;
    private int countChild;

}
