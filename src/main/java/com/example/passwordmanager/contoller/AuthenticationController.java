package com.example.passwordmanager.contoller;

import com.example.passwordmanager.dto.reponse.JwtResponse;
import com.example.passwordmanager.dto.request.LoginRequest;
import com.example.passwordmanager.model.VaultUser;
import com.example.passwordmanager.secutiry.JwtTokenUtil;
import com.example.passwordmanager.service.MacAddressService;
import com.example.passwordmanager.service.VaultUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    VaultUserService vaultUserService;

    @Autowired
    MacAddressService macAddressService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request ){
        try {

            VaultUser user = vaultUserService.getVaultUserByUsername(loginRequest.getUsername());
            if(!macAddressService.isMacAddressAllowed(request,user)){
               return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Mac address is not allowed");
            }
            authenticate(loginRequest.getUsername(), loginRequest.getPassword());
            final String token = jwtTokenUtil.generateToken(loginRequest.getUsername(), "ROLE");
            return ResponseEntity.ok(new JwtResponse(token,user.getId(), user.getUsername()));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void authenticate(String email, String password) throws Exception {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }

}
