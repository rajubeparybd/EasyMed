package com.easymed.utils;

import io.github.cdimascio.dotenv.Dotenv;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

/**
 * A utility class for encrypting and decrypting data using the AES algorithm
 *
 * @author Raju Bepary
 * @since 1.0.0
 */
public class Hash {

    /*
     * Algorithm to use for encryption and decryption
     * */
    public static final String ALGORITHM = "AES";

    /*
     * Secret key to use for encryption and decryption
     * */
    private static final String secret = Dotenv.load().get("HASH_SECRET");


    /**
     * Encrypts the data using the default secret key and returns the encrypted data
     *
     * @param data Data to encrypt
     *
     * @return Encrypted data
     */
    public static String make(String data) {
        return Hash.make(data, secret);
    }

    /**
     * Encrypts the data using the given key and returns the encrypted data
     *
     * @param data Data to encrypt
     * @param key  Key to use for encryption
     *
     * @return Encrypted data
     */
    public static String make(String data, String key) {
        try {
            byte[] paddedKey = Arrays.copyOf(key.getBytes(), 32);
            SecretKeySpec secretKeySpec = new SecretKeySpec(paddedKey, ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encryptedData = cipher.doFinal(data.getBytes());

            return Base64.getEncoder().encodeToString(encryptedData);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException e) {
            System.out.println("Error encrypting data\n" + e.getMessage());
            return null;
        }
    }

    /**
     * Decrypts the encrypted data using the default secret key and returns the decrypted data
     *
     * @param encryptedData Encrypted data to decrypt
     *
     * @return Decrypted data
     */
    public static String decrypt(String encryptedData) {
        return Hash.decrypt(encryptedData, secret);
    }

    /**
     * Decrypts the encrypted data using the given key and returns the decrypted data
     *
     * @param encryptedData Encrypted data to decrypt
     * @param key           Key to use for decryption
     *
     * @return Decrypted data
     */
    public static String decrypt(String encryptedData, String key) {
        try {
            byte[] paddedKey = Arrays.copyOf(key.getBytes(), 32);
            SecretKeySpec secretKeySpec = new SecretKeySpec(paddedKey, ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(encryptedData));

            return new String(decryptedData);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException e) {
            System.out.println("Error decrypting data\n" + e.getMessage());
            return null;
        }
    }

    /**
     * Checks if the original data matches the encrypted data using the default secret key
     *
     * @param originalData  Original data
     * @param encryptedData Encrypted data
     *
     * @return True if the original data matches the encrypted data, false otherwise
     */
    public static Boolean check(String originalData, String encryptedData) {
        return Hash.check(originalData, encryptedData, secret);
    }

    /**
     * Checks if the original data matches the encrypted data using the given key
     *
     * @param originalData  Original data
     * @param encryptedData Encrypted data
     * @param key           Key to use for decryption
     *
     * @return True if the original data matches the encrypted data, false otherwise
     */
    public static Boolean check(String originalData, String encryptedData, String key) {
        String decryptedPassword = Hash.decrypt(encryptedData, key);
        return originalData.equals(decryptedPassword);
    }
}
