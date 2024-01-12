package com.example.passwordmanager.contoller;

import com.example.passwordmanager.dto.request.RegisterRequest;
import com.example.passwordmanager.service.VaultUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VaultUserController {

    @Autowired
    VaultUserService vaultUserService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest){

        vaultUserService.register(registerRequest);
        return ResponseEntity.ok("account registered");

    }

}
