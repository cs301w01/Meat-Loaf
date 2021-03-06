package com.cs301w01.meatload.authentication.controllers;

import com.cs301w01.meatload.authentication.model.Password;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * This class handles the both the creation and comparison of passwords.
 * The passwords are both salted and encrypted using SHA-256.
 * 
 * @author Derek Dowling
 */
public class PasswordManager {

    //this will generate 20 bytes of salt
    private static int numberOfSaltBytes = 20;
    private static String hashingAlgorithm = "SHA-256";

    public static Password generateNewPassword(String password) {

        String salt = getSalt();

        String pwd = hash(password + salt);

        return new Password(salt, pwd);
    }

    /**
     * Performs a SHA-256 hash on the password + salt combo. The resulting string is what is stored within
     * the database as the encrypted user password.
     * @param saltedPw
     * @return
     */
    private static String hash(String saltedPw) {

        try {

            MessageDigest md = MessageDigest.getInstance(hashingAlgorithm);

            byte[] hash = md.digest(saltedPw.getBytes());

            return byteArrayToHex(hash);

        } catch (NoSuchAlgorithmException e) {
            System.out.print("Error ");
            e.printStackTrace();
        }

        return null;

    }

    /**
     * Creates a randomly generated password salt. This is used to make it tougher to crack the password by
     * adding a randomly generated sequence that is unique for each password.
     * @return String salt
     */
    private static String getSalt() {

        Random r = new SecureRandom();
        byte[] salt = new byte[numberOfSaltBytes];
        r.nextBytes(salt);

        return new String(salt);

    }

    /**
     * Used within the PasswordManager to assemble an encrypted password from user input
     * and the corresponding username's salt value.
     *
     * @param password
     * @param salt
     * @return String encryped password combo.
     */
    public static String generatePasswordWithSalt(String password, String salt) {

        String pwd = hash(password + salt);

        return pwd;
    }

    /**
     * Compares the user's
     * @param encryptedPW
     * @param salt
     * @param inputPW
     * @return boolean
     */
    public static boolean comparePasswords(String encryptedPW, String salt, String inputPW) {

        String encryptedCombo = generatePasswordWithSalt(inputPW, salt);

        if(encryptedCombo.equals(encryptedPW))
            return true;
        else
            return false;

    }
    
    /**
     * This method is used to convert randomly generated byte values into strings
     * so that we can perform string operators on the encrypted passwords and store
     * the password as a String or VARCHAR in the database system.
     * 
     * @param barray
     * @return
     */
    private static String byteArrayToHex(byte[] barray) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < barray.length; i++) {
            String hex = Integer.toHexString(0xff & barray[i]);
            if (hex.length() == 1) sb.append('0');
            sb.append(hex);
        }
        return sb.toString();
    }
}
