package com.example.passwordmanager.dto.reponse;

import lombok.Data;

@Data
public class KeyInfoExportRequest {

    private String name;

    private String username;

    private String password;

    private String iv;

    private String salt;

}
