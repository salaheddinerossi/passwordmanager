package com.example.passwordmanager.contoller;


import com.example.passwordmanager.dto.reponse.JwtResponse;
import com.example.passwordmanager.dto.request.LoginRequest;
import com.example.passwordmanager.model.VaultUser;
import com.example.passwordmanager.secutiry.JwtTokenUtil;
import com.example.passwordmanager.service.VaultUserService;
import org.springframework.beans.factory.annotation.Autowired;
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


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        try {

            final VaultUser user = vaultUserService.getVaultUserByUsername(loginRequest.getUsername());
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
