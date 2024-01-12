package com.example.passwordmanager.service;

import com.example.passwordmanager.dto.reponse.MacAddressResponse;
import com.example.passwordmanager.model.MacAddress;
import com.example.passwordmanager.model.VaultUser;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface MacAddressService {


    String resolveMacAddress(String ipAddress);

    boolean isValidMacAddress(String macAddress);

    Boolean isMacAddressAllowed(HttpServletRequest request,VaultUser vaultUser);

    void addAllowedAddress(String macAddress, VaultUser vaultUser);

    void deleteAllowedAddress(Long id);

    Boolean checkIfAddressAllowed(String ipAddress,VaultUser vaultUser);

    String getLocalIpAddress();

    MacAddress getMacAddressById(Long id);

    List<MacAddressResponse> getMacAddressesByUser(VaultUser vaultUser);


}
