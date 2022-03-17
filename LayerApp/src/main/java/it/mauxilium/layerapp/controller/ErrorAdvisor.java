package it.mauxilium.layerapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorAdvisor {

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
    public String onIllegalStateEx(IllegalStateException ex) {
        return "Pipeline fails: " + ex.toString();
    }
}
