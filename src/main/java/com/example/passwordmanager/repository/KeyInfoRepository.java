package com.example.passwordmanager.repository;

import com.example.passwordmanager.model.KeyInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeyInfoRepository extends JpaRepository<KeyInfo,Long> {
}
