package com.safetynet.model;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString()
public class Individu implements Serializable {

    private String firstName;
    private String lastName;

}
