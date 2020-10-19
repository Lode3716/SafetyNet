package com.safetynet.dto;

import com.googlecode.jmapper.annotations.JGlobalMap;
import lombok.Data;

@Data
@JGlobalMap
public class FirestationsDTO {

    private String station;
    private String address;
}
