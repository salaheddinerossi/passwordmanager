package com.example.passwordmanager.exception;

public class VaultUserNotFoundException extends RuntimeException{
    public VaultUserNotFoundException(){
        super("vault user not found");
    }
}
