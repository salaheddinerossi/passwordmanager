package com.example.passwordmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(VaultUserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handelVaultUserNotFoundException(VaultUserNotFoundException e){
        return e.getMessage();
    }

    @ExceptionHandler(KeyInfoNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handelKeyInfoNotFoundException(KeyInfoNotFoundException e){
        return e.getMessage();
    }

    @ExceptionHandler(MacAddressNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handelMacAddressNotFoundException(MacAddressNotFoundException e){
        return e.getMessage();
    }


}
