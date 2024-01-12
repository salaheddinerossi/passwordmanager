package com.example.passwordmanager.service;

import com.example.passwordmanager.dto.request.RegisterRequest;
import com.example.passwordmanager.model.VaultUser;

public interface VaultUserService {

    public void register(RegisterRequest request);

    public VaultUser getVaultUserByID(Long id);

    VaultUser getVaultUserByUsername(String username);

}
