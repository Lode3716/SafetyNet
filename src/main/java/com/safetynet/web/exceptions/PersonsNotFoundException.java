package com.safetynet.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PersonsNotFoundException extends RuntimeException {

    public PersonsNotFoundException() {
    }

    public PersonsNotFoundException(String message) {
        super(message);
    }
}
