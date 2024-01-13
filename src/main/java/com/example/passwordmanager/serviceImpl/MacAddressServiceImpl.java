package com.example.passwordmanager.serviceImpl;


import com.example.passwordmanager.dto.reponse.MacAddressResponse;
import com.example.passwordmanager.exception.MacAddressNotFoundException;
import com.example.passwordmanager.model.MacAddress;
import com.example.passwordmanager.model.VaultUser;
import com.example.passwordmanager.repository.MacAddressRepository;
import com.example.passwordmanager.service.MacAddressService;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

@Service
public class MacAddressServiceImpl implements MacAddressService {

    @Autowired
    MacAddressRepository macAddressRepository;

    @Override
    public Boolean isMacAddressAllowed(HttpServletRequest request,VaultUser vaultUser) {
        String ipAddress = request.getRemoteAddr();

        String localIpAddress;
        if ("127.0.0.1".equals(ipAddress) || "0:0:0:0:0:0:0:1".equals(ipAddress) || ipAddress.equals(this.getLocalIpAddress())) {
            return true;
        }

        return checkIfAddressAllowed(ipAddress,vaultUser);
    }

    @Override
    public void addAllowedAddress(String macAddress, VaultUser vaultUser) {

        MacAddress macAddress1 = new MacAddress();

        macAddress1.setMacAddress(macAddress);
        macAddress1.setVaultUser(vaultUser);

        macAddressRepository.save(macAddress1);

    }

    @Override
    public void deleteAllowedAddress(Long id ) {
        macAddressRepository.deleteById(id);
    }

    @Override
    public Boolean checkIfAddressAllowed(String ipAddress,VaultUser vaultUser) {

        String deviceMacAddress = "\"" + resolveMacAddress(ipAddress).toUpperCase().replace("-", ":") + "\"";
        System.out.println("===================================================");
        System.out.println("deviceMacAddress");
        System.out.println(deviceMacAddress);

        for(MacAddress macAddress : vaultUser.getMacAddresses()){
            System.out.println(macAddress.getMacAddress());
            System.out.println(Objects.equals(macAddress.getMacAddress(), deviceMacAddress));
            if(Objects.equals(macAddress.getMacAddress(), deviceMacAddress)){
                return true;
            }

        }
        return false;
    }

    @Override
    public String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                if (networkInterface.isUp() && !networkInterface.isLoopback()) {
                    Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                    while (inetAddresses.hasMoreElements()) {
                        InetAddress inetAddress = inetAddresses.nextElement();
                        if (inetAddress.isSiteLocalAddress()) {
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Unable to determine local IP address";
    }

    @Override
    public MacAddress getMacAddressById(Long id) {
        return macAddressRepository.findById(id).orElseThrow(
                MacAddressNotFoundException::new
        );
    }

    @Override
    public List<MacAddressResponse> getMacAddressesByUser(VaultUser vaultUser) {

        List<MacAddressResponse> macAddressResponses =new ArrayList<>();

        for (MacAddress macAddress : vaultUser.getMacAddresses()){
            MacAddressResponse macAddressResponse = new MacAddressResponse();

            macAddressResponse.setId(macAddress.getId());
            macAddressResponse.setMacAddress(macAddress.getMacAddress());

            macAddressResponses.add(macAddressResponse);
        }


        return macAddressResponses;
    }


    @Override
    public String resolveMacAddress(String ipAddress) {
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

    @Override
    public boolean isValidMacAddress(String macAddress) {
        return macAddress.matches("[0-9A-Fa-f]{2}(-[0-9A-Fa-f]{2}){5}");
    }



}
