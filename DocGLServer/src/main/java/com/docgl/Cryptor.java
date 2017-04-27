package com.docgl;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Cryptor  {
    private byte[] getKey(){
        final byte[] finalKey = new byte[16];
        int i = 0;
        try {
            for(byte b : "sovy2017".getBytes("UTF-8"))
                finalKey[i++%16] ^= b;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return finalKey;
    }
    public String encrypt(String text){
        String encriptedtText = null;
        final Cipher encryptCipher;
        try {
            encryptCipher = Cipher.getInstance("AES");
            encryptCipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(getKey(), "AES"));
            encriptedtText = new String(Hex.encodeHex(encryptCipher.doFinal(text.getBytes("UTF-8"))));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encriptedtText;
    }

    public String decrypt(String text){
        String decriptedText = null;
        final Cipher decryptCipher;
        try {
            decryptCipher = Cipher.getInstance("AES");
            decryptCipher.init(Cipher.DECRYPT_MODE,new SecretKeySpec(getKey(), "AES"));
            decriptedText = new String(decryptCipher.doFinal(Hex.decodeHex(text.toCharArray())));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (DecoderException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return decriptedText;
    }
}