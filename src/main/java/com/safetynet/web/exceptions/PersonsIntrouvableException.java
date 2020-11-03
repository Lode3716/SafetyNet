package com.safetynet.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PersonsIntrouvableException extends RuntimeException {

    public PersonsIntrouvableException() {
    }

    public PersonsIntrouvableException(String message) {
        super(message);
    }
}
