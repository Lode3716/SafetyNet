package com.safetynet.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MedicalrecordsNotFoundException extends RuntimeException {

    public MedicalrecordsNotFoundException() {
    }

    public MedicalrecordsNotFoundException(String message) {
        super(message);
    }
}
