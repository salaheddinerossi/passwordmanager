package com.example.passwordmanager.dto.request;


import lombok.Data;

@Data
public class RegisterRequest {

    private String username;

    private String password;

    private String paraphrase;

}
