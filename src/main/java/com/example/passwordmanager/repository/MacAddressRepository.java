package com.example.passwordmanager.repository;

import com.example.passwordmanager.model.MacAddress;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MacAddressRepository extends JpaRepository<MacAddress,Long> {

}
