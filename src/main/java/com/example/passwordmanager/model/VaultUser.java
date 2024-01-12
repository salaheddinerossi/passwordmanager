package com.example.passwordmanager.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class VaultUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String username;

    private String password;

    private String paraphrase;

    @OneToMany(mappedBy = "vaultUser")
    List<KeyInfo> keyInfos;


}
