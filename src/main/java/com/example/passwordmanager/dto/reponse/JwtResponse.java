package com.example.passwordmanager.dto.reponse;

public class JwtResponse {
    private final String token;

    private Long id;

    private String name;



    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JwtResponse(String token, Long id, String name ) {
        this.id=id;
        this.name=name;
        this.token = token;
    }



    public String getToken() {
        return token;
    }
}
