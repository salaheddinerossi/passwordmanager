package com.example.passwordmanager.dto.reponse;


import lombok.Data;

@Data
public class KeyInfoResponse {

    private Long id;

    private String name;

    private String username;

    private String DecodedPassword;

}
