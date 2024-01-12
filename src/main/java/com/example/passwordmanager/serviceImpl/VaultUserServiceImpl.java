package com.example.passwordmanager.serviceImpl;

import com.example.passwordmanager.dto.request.RegisterRequest;
import com.example.passwordmanager.exception.VaultUserNotFoundException;
import com.example.passwordmanager.model.VaultUser;
import com.example.passwordmanager.repository.VaultUserRepository;
import com.example.passwordmanager.service.VaultUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class VaultUserServiceImpl implements VaultUserService {

    @Autowired
    VaultUserRepository vaultUserRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void register(RegisterRequest request) {

        VaultUser vaultUser = new VaultUser();

        vaultUser.setUsername(request.getUsername());
        vaultUser.setPassword(passwordEncoder.encode(request.getPassword()));
        vaultUser.setParaphrase(request.getParaphrase());

        vaultUserRepository.save(vaultUser);
    }

    @Override
    public VaultUser getVaultUserByID(Long id) {
        return vaultUserRepository.findById(id).orElseThrow(
            VaultUserNotFoundException::new
        );
    }

    @Override
    public VaultUser getVaultUserByUsername(String username) {
        VaultUser vaultUser = vaultUserRepository.findByUsername(username);

        if (vaultUser==null){
            throw new VaultUserNotFoundException();
        }

        return vaultUser;
    }
}
