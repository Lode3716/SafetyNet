package com.safetynet.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode
@JsonDeserialize(builder = Persons.PersonsBuilder.class)
@JsonRootName(value = "persons")
public class Persons extends Individu implements Serializable {

    private String phone;
    private String zip;
    private String email;
    private String city;
    private String address;

    public Persons() {
    }

    @Builder
    public Persons(@JsonProperty("firstName") String firstName, @JsonProperty("lastName") String lastName, @JsonProperty("phone") String phone, @JsonProperty("zip") String zip, @JsonProperty("email") String email, @JsonProperty("city") String city, @JsonProperty("address") String address) {
        super(firstName, lastName);
        this.phone = phone;
        this.zip = zip;
        this.email = email;
        this.city = city;
        this.address = address;
    }

}