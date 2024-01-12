package com.example.passwordmanager.contoller;

import com.example.passwordmanager.dto.reponse.MacAddressResponse;
import com.example.passwordmanager.model.MacAddress;
import com.example.passwordmanager.model.VaultUser;
import com.example.passwordmanager.service.MacAddressService;
import com.example.passwordmanager.service.VaultUserService;
import com.example.passwordmanager.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/mac")
public class MacAddressController {

    @Autowired
    VaultUserService vaultUserService;

    @Autowired
    MacAddressService macAddressService;

    @PostMapping("/")
    public ResponseEntity<?> addMacAddress(@RequestBody String macAddress, @AuthenticationPrincipal UserDetails userDetails){

        if(!SecurityUtils.isAuthenticated(userDetails.getAuthorities())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you are not authorized to do this action");
        }

        VaultUser vaultUser = vaultUserService.getVaultUserByUsername(userDetails.getUsername());

        macAddressService.addAllowedAddress(macAddress,vaultUser);

        return ResponseEntity.ok("mac address created");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMacAddress(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails){
        if(!SecurityUtils.isAuthenticated(userDetails.getAuthorities())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you are not authorized to do this action");
        }

        VaultUser vaultUser = vaultUserService.getVaultUserByUsername(userDetails.getUsername());

        MacAddress macAddress = macAddressService.getMacAddressById(id);

        if(!Objects.equals(macAddress.getVaultUser().getId(), vaultUser.getId())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you are not authorized to do this action");
        }

        macAddressService.deleteAllowedAddress(id);

        return ResponseEntity.ok("mac address deleted");
    }

    @GetMapping("/")
    public ResponseEntity<?> getAddressMacByUser( @AuthenticationPrincipal UserDetails userDetails){

        List<MacAddressResponse> macAddressResponses = macAddressService.getMacAddressesByUser(vaultUserService.getVaultUserByUsername(userDetails.getUsername()));
        return ResponseEntity.ok(macAddressResponses);

    }
}
