package com.example.passwordmanager.serviceImpl;

import com.example.passwordmanager.dto.reponse.KeyInfoExportRequest;
import com.example.passwordmanager.dto.reponse.KeyInfoResponse;
import com.example.passwordmanager.dto.request.KeyInfoRequest;
import com.example.passwordmanager.exception.KeyInfoNotFoundException;
import com.example.passwordmanager.model.KeyInfo;
import com.example.passwordmanager.model.VaultUser;
import com.example.passwordmanager.repository.KeyInfoRepository;
import com.example.passwordmanager.service.KeyInfoService;
import com.example.passwordmanager.util.EncryptionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class KeyInfoServiceImpl implements KeyInfoService {

    @Autowired
    CryptoService cryptoService;

    @Autowired
    KeyInfoRepository keyInfoRepository;


    @Override
    public List<KeyInfoResponse> getKeyInfosByUser(VaultUser vaultUser) throws Exception {

        return this.extractKeyInfosResponse(vaultUser.getKeyInfos(),vaultUser.getParaphrase());

    }

    @Override
    public void addKeyInfo(KeyInfoRequest keyInfoRequest, VaultUser vaultUser) throws Exception {
        KeyInfo keyInfo = new KeyInfo();

        EncryptionResult encryptionResult = cryptoService.encrypt(keyInfoRequest.getPassword(), vaultUser.getParaphrase());
        keyInfo.setPassword(encryptionResult.getEncryptedData());
        keyInfo.setIv(encryptionResult.getIv());
        keyInfo.setSalt(encryptionResult.getSalt());
        keyInfo.setName(keyInfoRequest.getName());
        keyInfo.setUsername(keyInfoRequest.getUsername());
        keyInfo.setVaultUser(vaultUser);

        keyInfoRepository.save(keyInfo);
    }

    @Override
    public void addKeyInfoEncoded(KeyInfoExportRequest keyInfoRequest, VaultUser vaultUser) throws Exception {
        KeyInfo keyInfo = new KeyInfo();

        keyInfo.setPassword(keyInfoRequest.getPassword());
        keyInfo.setIv(keyInfoRequest.getIv());
        keyInfo.setSalt(keyInfoRequest.getSalt());
        keyInfo.setName(keyInfoRequest.getName());
        keyInfo.setUsername(keyInfoRequest.getUsername());
        keyInfo.setVaultUser(vaultUser);

        keyInfoRepository.save(keyInfo);

    }


    @Override
    @Transactional
    public void deleteKeyInfoById(Long id) {

        System.out.println(id);

        keyInfoRepository.deleteById(id);

    }

    @Override
    public void updateKeyInfo(Long id, KeyInfoRequest keyInfoRequest) throws Exception {
        KeyInfo keyInfo = this.getKeyInfoById(id);

        EncryptionResult encryptionResult = cryptoService.encrypt(keyInfoRequest.getPassword(), keyInfo.getVaultUser().getParaphrase());

        keyInfo.setName(keyInfoRequest.getName());
        keyInfo.setUsername(keyInfoRequest.getUsername());
        keyInfo.setPassword(encryptionResult.getEncryptedData());
        keyInfo.setIv(encryptionResult.getIv());
        keyInfo.setSalt(encryptionResult.getSalt());

        keyInfoRepository.save(keyInfo);
    }

    @Override
    public KeyInfo getKeyInfoById(Long id) {
        return keyInfoRepository.findById(id).orElseThrow(
                KeyInfoNotFoundException::new
        );
    }

    @Override
    public List<KeyInfoExportRequest> getEncodedKeyInfos(VaultUser vaultUser) {


        return this.extractKeyInfosResponseEncoded(vaultUser.getKeyInfos());
    }

    public List<KeyInfoResponse> extractKeyInfosResponse(List<KeyInfo> keyInfos, String paraphrase) throws Exception {
        List<KeyInfoResponse> keyInfoResponses = new ArrayList<>();
        for (KeyInfo keyInfo : keyInfos) {
            KeyInfoResponse keyInfoResponse = new KeyInfoResponse();
            keyInfoResponse.setName(keyInfo.getName());
            keyInfoResponse.setUsername(keyInfo.getUsername());
            keyInfoResponse.setId(keyInfo.getId());
            keyInfoResponse.setPassword(
                    cryptoService.decrypt(keyInfo.getPassword(), keyInfo.getIv(),keyInfo.getSalt(), paraphrase));
            keyInfoResponses.add(keyInfoResponse);
        }
        return keyInfoResponses;
    }

    public List<KeyInfoExportRequest> extractKeyInfosResponseEncoded(List<KeyInfo> keyInfos)  {
        List<KeyInfoExportRequest> keyInfoRequests = new ArrayList<>();
        for (KeyInfo keyInfo : keyInfos) {
            KeyInfoExportRequest keyInfoRequest = new KeyInfoExportRequest();

            keyInfoRequest.setName(keyInfo.getName());
            keyInfoRequest.setUsername(keyInfo.getUsername());
            keyInfoRequest.setPassword(keyInfo.getPassword());
            keyInfoRequest.setIv(keyInfo.getIv());
            keyInfoRequest.setSalt(keyInfo.getSalt());

            keyInfoRequests.add(keyInfoRequest);
        }
        return keyInfoRequests;
    }
}
