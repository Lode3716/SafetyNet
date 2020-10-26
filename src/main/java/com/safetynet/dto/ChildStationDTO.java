package com.safetynet.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChildStationDTO {

    private List<PersonsShortDTO> children;
    private List<PersonsShortDTO> parents;

}
