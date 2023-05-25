package com.semtleWebGroup.youtubeclone.domain.auth.util;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES256 {

    public static String alg = "AES/CBC/PKCS5Padding";
    private static final String key = "01234567890123456789012345678901";
    private static final String iv = key.substring(0, 16); // 16byte

    public static String encrypt(String text) {
        byte[] encrypted;
        try {

            Cipher cipher = Cipher.getInstance(alg);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);

            encrypted = cipher.doFinal(text.getBytes("UTF-8"));
        } catch (Exception e){
            throw new IllegalStateException("암호화 에러");
        }
        return Base64.getEncoder().encodeToString(encrypted);
    }


    public static String decrypt(String cipherText) {
        String result;
        try {
            Cipher cipher = Cipher.getInstance(alg);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);

            byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
            byte[] decrypted = cipher.doFinal(decodedBytes);
            result = new String(decrypted, "UTF-8");
        } catch (Exception e){
            throw new IllegalStateException("복호화 에러");
        }
        return result;
    }

}
