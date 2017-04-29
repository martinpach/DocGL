package com.docgl;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.docgl.exceptions.CryptorException;
import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;

public class Cryptor  {


    private static final String PASSWORD = "sovy2017";
    private static byte[] FINAL_KEY = new byte[16];

    static {
        int i = 0;
        try {
            for(byte b : PASSWORD.getBytes("UTF-8"))
                FINAL_KEY[i++%16] ^= b;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }}

    public static String encrypt(String text) {
        String encriptedtText = null;
        final Cipher encryptCipher;
        try {
            encryptCipher = Cipher.getInstance("AES");
            encryptCipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(FINAL_KEY, "AES"));
            encriptedtText = new String(Hex.encodeHex(encryptCipher.doFinal(text.getBytes("UTF-8"))));
        } catch (Exception e) {
            throw new CryptorException("An error during encrypting!", e);
        }
        return encriptedtText;
    }

    public static String decrypt(String text)  {
        String decriptedText = null;
        final Cipher decryptCipher;
        try {
            decryptCipher = Cipher.getInstance("AES");
            decryptCipher.init(Cipher.DECRYPT_MODE,new SecretKeySpec(FINAL_KEY, "AES"));
            decriptedText = new String(decryptCipher.doFinal(Hex.decodeHex(text.toCharArray())));
        } catch (Exception e) {
            throw new CryptorException("An error during decryptting!", e);
        }
        return decriptedText;
    }


}