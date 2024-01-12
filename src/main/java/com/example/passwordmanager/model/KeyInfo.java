package com.example.passwordmanager.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class KeyInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String username;

    private String password;

    private String iv;

    private String salt;

    @ManyToOne
    @JoinColumn(name = "vault_user_id")
    private VaultUser vaultUser;

}
