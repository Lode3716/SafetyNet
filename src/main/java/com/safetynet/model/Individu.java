package com.safetynet.model;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString()
@EqualsAndHashCode(of = {"firstName", "lastName"})
public class Individu implements Serializable {

    private String firstName;
    private String lastName;

}
