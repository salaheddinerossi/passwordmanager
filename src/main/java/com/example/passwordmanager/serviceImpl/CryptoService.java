package com.example.passwordmanager.serviceImpl;

import com.example.passwordmanager.util.EncryptionResult;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.security.SecureRandom;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@Service
public class CryptoService {

    private static final String AES = "AES";
    private static final String AES_GCM_NOPADDING = "AES/GCM/NoPadding";
    private static final int IV_LENGTH_BYTE = 12;
    private static final int TAG_LENGTH_BIT = 128;
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 256;
    private static final String SECRET_KEY_FACTORY_ALGORITHM = "PBKDF2WithHmacSHA256";

    public EncryptionResult encrypt(String plainText, String passphrase) throws Exception {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        byte[] iv = new byte[IV_LENGTH_BYTE];
        new SecureRandom().nextBytes(iv);

        SecretKey key = getSecretKey(passphrase, salt);

        Cipher cipher = Cipher.getInstance(AES_GCM_NOPADDING);
        cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(TAG_LENGTH_BIT, iv));
        byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        String encodedIV = Base64.getEncoder().encodeToString(iv);
        String encodedCipherText = Base64.getEncoder().encodeToString(cipherText);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);

        return new EncryptionResult(encodedCipherText, encodedIV, encodedSalt);
    }

    public String decrypt(String encryptedData, String iv, String salt, String passphrase) throws Exception {
        byte[] ivBytes = Base64.getDecoder().decode(iv);
        byte[] saltBytes = Base64.getDecoder().decode(salt);
        byte[] encryptedDataBytes = Base64.getDecoder().decode(encryptedData);

        SecretKey key = getSecretKey(passphrase, saltBytes);

        Cipher cipher = Cipher.getInstance(AES_GCM_NOPADDING);
        cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(TAG_LENGTH_BIT, ivBytes));

        byte[] plainText = cipher.doFinal(encryptedDataBytes);
        return new String(plainText, StandardCharsets.UTF_8);
    }

    private SecretKey getSecretKey(String passphrase, byte[] salt) throws Exception {
        KeySpec spec = new PBEKeySpec(passphrase.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_FACTORY_ALGORITHM);

        byte[] secret = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(secret, AES);
    }

    private SecretKey getSecretKey(String passphrase) throws Exception {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);

        KeySpec spec = new PBEKeySpec(passphrase.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_FACTORY_ALGORITHM);

        byte[] secret = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(secret, AES);
    }
}
