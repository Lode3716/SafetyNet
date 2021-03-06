package com.safetynet.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FirestationNotFoundException extends RuntimeException {

    public FirestationNotFoundException() {
    }

    public FirestationNotFoundException(String message) {
        super(message);
    }
}
