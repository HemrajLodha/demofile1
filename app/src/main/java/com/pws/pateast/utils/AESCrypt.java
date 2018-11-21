package com.pws.pateast.utils;


import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by intel on 6/15/2016.
 */
public class AESCrypt {
    /*private static final String SALT = "HPCRF5vgV9x7Sw23QTHEZrk6bRmfB7jx";
    private static final String ALGORITHM = "AES/CBC/PKCS7Padding";

    private final Cipher cipher;
    private final SecretKey key;
    private static AESCrypt aesCrypt;

    private AESCrypt() throws Exception {
        key = getSecreteKey(SALT);
        cipher = Cipher.getInstance(ALGORITHM);
    }

    public static AESCrypt getInstance() throws Exception {
        if (aesCrypt == null)
            aesCrypt = new AESCrypt();
        return aesCrypt;
    }

    public String encryptData(String plainText) throws Exception {
        byte[] iv = generateIV();
        byte[] plain = plainText.getBytes("UTF-8");
        byte[] encrypted = encrypt(plain, iv);
        return Hex.encodeHex(iv) + ":" + Hex.encodeHex(encrypted);
    }

    private byte[] encrypt(byte[] plain, byte[] iv) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, key, readIV(iv));
        byte[] encrypted = cipher.doFinal(plain);
        return encrypted;
    }

    public String decryptData(String encryptedText) throws Exception {
        String[] data = encryptedText.split(":");
        byte[] iv = Hex.decodeHex(data[0]);
        byte[] encrypted = Hex.decodeHex(data[1]);
        byte[] decrypted = decrypt(encrypted, iv);
        return new String(decrypted);
    }

    private byte[] decrypt(byte[] encrypted, byte[] iv) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, key, readIV(iv));
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    private SecretKey getSecreteKey(String secretKey) throws Exception {
        byte[] bytes = secretKey.getBytes("UTF-8");
        SecretKey key = new SecretKeySpec(bytes, "AES");
        return key;
    }

    private AlgorithmParameterSpec readIV(byte[] iv) {
        return new IvParameterSpec(iv);
    }

    private byte[] generateIV() {
        SecureRandom r = new SecureRandom();
        byte[] ivBytes = new byte[16];
        r.nextBytes(ivBytes);
        return ivBytes;
    }*/
}
