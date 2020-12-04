package com.safetynet.dto;

import com.googlecode.jmapper.annotations.JGlobalMap;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JGlobalMap
public class FirestationsDTO {

    private String station;
    private String address;
}
