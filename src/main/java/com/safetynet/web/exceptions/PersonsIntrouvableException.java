package com.safetynet.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.function.Supplier;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PersonsIntrouvableException extends RuntimeException{

    public PersonsIntrouvableException(String s) {
        super(s);
    }
}
