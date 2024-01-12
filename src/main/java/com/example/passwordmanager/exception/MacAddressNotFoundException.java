package com.example.passwordmanager.exception;

public class MacAddressNotFoundException extends RuntimeException{
    public MacAddressNotFoundException(){
        super("mac address not found ");
    }
}
