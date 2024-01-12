package com.example.passwordmanager.repository;

import com.example.passwordmanager.model.VaultUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VaultUserRepository extends JpaRepository<VaultUser,Long> {
    VaultUser findByUsername(String username);
}
