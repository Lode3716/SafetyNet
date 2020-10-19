package com.safetynet.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MedicalrecordsIntrouvablesException extends RuntimeException {

    public MedicalrecordsIntrouvablesException() {
    }

    public MedicalrecordsIntrouvablesException(String message) {
        super(message);
    }
}
