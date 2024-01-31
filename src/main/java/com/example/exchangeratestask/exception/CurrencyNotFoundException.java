package com.example.exchangeratestask.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
/**
This class is responsible for calling  the exception
 */
public class CurrencyNotFoundException extends ResponseStatusException {
    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;
    public CurrencyNotFoundException(String numCode) {
        super(STATUS, String.format("Currency with numCode %s not found", numCode));
    }

    /**
     * Method for getting the exception status
     * @return exception status
     */
    @Override
    public HttpStatus getStatus() {
        return STATUS;
    }
}
