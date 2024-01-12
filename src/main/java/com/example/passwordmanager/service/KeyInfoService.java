package com.example.passwordmanager.service;

import com.example.passwordmanager.dto.reponse.KeyInfoExportRequest;
import com.example.passwordmanager.dto.reponse.KeyInfoResponse;
import com.example.passwordmanager.dto.request.KeyInfoRequest;
import com.example.passwordmanager.model.KeyInfo;
import com.example.passwordmanager.model.VaultUser;

import java.util.List;

public interface KeyInfoService {

    List<KeyInfoResponse> getKeyInfosByUser(VaultUser vaultUser) throws Exception;

    void addKeyInfo(KeyInfoRequest keyInfoRequest,VaultUser vaultUser) throws Exception;

    void addKeyInfoEncoded(KeyInfoExportRequest keyInfoRequest, VaultUser vaultUser) throws Exception;


    void deleteKeyInfoById(Long id);

    void updateKeyInfo(Long id, KeyInfoRequest keyInfoRequest) throws Exception;

    KeyInfo getKeyInfoById(Long id);

    List<KeyInfoExportRequest> getEncodedKeyInfos(VaultUser vaultUser);


}
