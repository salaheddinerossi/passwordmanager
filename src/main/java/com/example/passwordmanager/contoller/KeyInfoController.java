package com.example.passwordmanager.contoller;

import com.example.passwordmanager.dto.reponse.KeyInfoExportRequest;
import com.example.passwordmanager.dto.reponse.KeyInfoResponse;
import com.example.passwordmanager.dto.request.KeyInfoRequest;
import com.example.passwordmanager.model.KeyInfo;
import com.example.passwordmanager.model.VaultUser;
import com.example.passwordmanager.service.KeyInfoService;
import com.example.passwordmanager.service.VaultUserService;
import com.example.passwordmanager.util.SecurityUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/password")
public class KeyInfoController {

    @Autowired
    VaultUserService vaultUserService;

    @Autowired
    KeyInfoService keyInfoService;

    @GetMapping("/")
    public ResponseEntity<?> getKeyInfosByUser(@AuthenticationPrincipal UserDetails userDetails) throws Exception {

        if(!SecurityUtils.isAuthenticated(userDetails.getAuthorities())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you are not authorized to do this action");
        }

        List<KeyInfoResponse> keyInfoResponses = keyInfoService.getKeyInfosByUser(vaultUserService.getVaultUserByUsername(userDetails.getUsername()));
        return ResponseEntity.ok(keyInfoResponses);

    }

    @PostMapping("/")
    public ResponseEntity<?> addKeyInfo(@RequestBody KeyInfoRequest keyInfoRequest,@AuthenticationPrincipal UserDetails userDetails) throws Exception {

        if(!SecurityUtils.isAuthenticated(userDetails.getAuthorities())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you are not authorized to do this action");
        }

        keyInfoService.addKeyInfo(keyInfoRequest,vaultUserService.getVaultUserByUsername(userDetails.getUsername()));

        return ResponseEntity.ok("Key info added");

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteKeyInfo(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails){

        if(!SecurityUtils.isAuthenticated(userDetails.getAuthorities())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you are not authorized to do this action");
        }

        VaultUser vaultUser = vaultUserService.getVaultUserByUsername(userDetails.getUsername());

        for(KeyInfo keyInfo : vaultUser.getKeyInfos() ){

            if(Objects.equals(keyInfo.getId(), id)){
                keyInfoService.deleteKeyInfoById(id);
                return ResponseEntity.ok("key info deleted");
            }

        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you are not the owner of this keyinfo");

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateKeyInfo(@PathVariable Long id,@RequestBody KeyInfoRequest keyInfoRequest, @AuthenticationPrincipal UserDetails userDetails) throws Exception {
        if(!SecurityUtils.isAuthenticated(userDetails.getAuthorities())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you are not authorized to do this action");
        }

        VaultUser vaultUser = vaultUserService.getVaultUserByUsername(userDetails.getUsername());

        for(KeyInfo keyInfo : vaultUser.getKeyInfos() ){

            if(Objects.equals(keyInfo.getId(), id)){
                keyInfoService.updateKeyInfo(id, keyInfoRequest);
                return ResponseEntity.ok("key info updated");
            }

        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you are not the owner of this key info");

    }

    @PostMapping("/importKeyInfos")
    public ResponseEntity<String> importKeyInfos(@RequestBody List<KeyInfoExportRequest> keyInfoExportRequests, @AuthenticationPrincipal UserDetails userDetails) throws Exception {

        VaultUser vaultUser = vaultUserService.getVaultUserByUsername(userDetails.getUsername());


        for(KeyInfoExportRequest keyInfoExportRequest : keyInfoExportRequests){
            keyInfoService.addKeyInfoEncoded(keyInfoExportRequest,vaultUser);
        }

        return ResponseEntity.ok("Data imported successfully");
    }

    @GetMapping("/exportKeyInfos")
    public ResponseEntity<?> exportKeyInfos(@AuthenticationPrincipal UserDetails userDetails) throws IOException, JsonProcessingException {

        VaultUser vaultUser = vaultUserService.getVaultUserByUsername(userDetails.getUsername());


        List<KeyInfoExportRequest> keyInfoRequests = keyInfoService.getEncodedKeyInfos(vaultUser);


        return  ResponseEntity.ok(keyInfoRequests);
    }

}
