package com.example.passwordmanager.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class MacAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String macAddress;

    @ManyToOne
    @JoinColumn(name = "mac_address_id")
    private VaultUser vaultUser;



}
