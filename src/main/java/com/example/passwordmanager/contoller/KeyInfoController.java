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
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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

    @GetMapping("/mac")
    public String getMacAddress(HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        System.out.println(ipAddress);
        return resolveMacAddress(ipAddress);
    }


    private String resolveMacAddress(String ipAddress) {
        try {
            String command = "arp -a";
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith(ipAddress)) {
                    String[] parts = line.trim().split("\\s+");
                    if (parts.length >= 3 && isValidMacAddress(parts[1])) {
                        return parts[1];
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
        return "MAC Address Not Found";
    }

    private boolean isValidMacAddress(String macAddress) {
        return macAddress.matches("[0-9A-Fa-f]{2}(-[0-9A-Fa-f]{2}){5}"); // Adjusted for MAC addresses separated by hyphens
    }
}
