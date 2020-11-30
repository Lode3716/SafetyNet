package com.safetynet.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ChildStationDTO {

    private List<PersonsShortDTO> children;
    private List<PersonsShortDTO> parents;

}
