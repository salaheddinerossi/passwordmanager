package com.example.passwordmanager.exception;

public class KeyInfoNotFoundException extends RuntimeException{
    public KeyInfoNotFoundException(){
        super("Key info not found");
    }
}
