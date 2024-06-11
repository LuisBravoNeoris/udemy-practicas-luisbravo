package com.udemy.practicas.luisbravo.client.services;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class EncryptionService {

    private final SecretKey secretKey;
    private final IvParameterSpec ivParameterSpec;

    public EncryptionService() throws Exception {
        this.secretKey = KeyGenerator.getInstance("AES").generateKey();
        byte[] iv = new byte[16];
        this.ivParameterSpec = new IvParameterSpec(iv);
    }

    public String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
        byte[] encrypted = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public String decrypt(String encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
        byte[] original = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(original);
    }
    
}