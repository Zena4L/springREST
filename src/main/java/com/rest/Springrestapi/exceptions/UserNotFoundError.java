package com.rest.Springrestapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserNotFoundError extends RuntimeException {
    public UserNotFoundError(String message) {
        super(message);
    }
}
