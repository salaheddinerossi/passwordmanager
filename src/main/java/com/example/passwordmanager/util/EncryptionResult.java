package com.example.passwordmanager.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EncryptionResult {
    private final String encryptedData;
    private final String iv;
    private final String salt;



}
