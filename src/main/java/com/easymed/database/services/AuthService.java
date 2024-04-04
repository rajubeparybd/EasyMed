package com.easymed.database.services;

import com.easymed.utils.DatabaseReadCall;
import com.easymed.utils.DatabaseWriteCall;
import com.easymed.utils.Hash;

import java.util.HashMap;

/**
 * AuthService for handling authentication
 *
 * @author Raju Bepary
 * @since 1.0.0
 */
public class AuthService {

    /*
     * The code sent to the user's email to reset the password
     */
    public static String forgotPasswordCode = null;

    /**
     * Attempt to log in a user with the given email and password
     *
     * @param email    User's email
     * @param password User's password
     *
     * @return DatabaseReadCall
     */
    public static DatabaseReadCall attemptLogin(String email, String password) {
        String hashedPassword = Hash.make(password);
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";

        HashMap<Integer, Object> placeholders = new HashMap<>();
        placeholders.put(1, email);
        placeholders.put(2, hashedPassword);

        return new DatabaseReadCall(query, placeholders);
    }


    public static DatabaseWriteCall attemptRegistration(String name, String email, String password, String role) {
        String query = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";
        String hashedPassword = Hash.make(password);

        HashMap<Integer, Object> placeholders = new HashMap<>();
        placeholders.put(1, name);
        placeholders.put(2, email);
        placeholders.put(3, hashedPassword);
        placeholders.put(4, role);

        return new DatabaseWriteCall(query, placeholders);
    }

    /**
     * Check if a user exists with the given email
     *
     * @param email User's email
     *
     * @return Boolean
     */
    public static DatabaseReadCall checkUserExistsByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        HashMap<Integer, Object> placeholders = new HashMap<>();

        placeholders.put(1, email);

        return new DatabaseReadCall(query, placeholders);
    }

    /**
     * Send a code to the user's email to reset the password
     *
     * @param email User's email
     *
     * @return DatabaseWriteCall
     */
    public static DatabaseWriteCall sendForgetPasswordCode(String email) {
        String query = "INSERT INTO forget_password (email, code) VALUES (?, ?)";
        forgotPasswordCode = String.valueOf((int) (Math.random() * 900000) + 100000);

        HashMap<Integer, Object> placeholders = new HashMap<>();
        placeholders.put(1, email);
        placeholders.put(2, forgotPasswordCode);

        return new DatabaseWriteCall(query, placeholders);
    }


    /**
     * Check if the code sent to the user's email matches the code in the database
     *
     * @param email User's email
     * @param code  Code sent to the user's email
     *
     * @return DatabaseReadCall
     */
    public static DatabaseReadCall checkForgetPasswordCode(String email, String code) {
        String query = "SELECT * FROM forget_password WHERE email = ? AND code = ?";
        HashMap<Integer, Object> placeholders = new HashMap<>();
        placeholders.put(1, email);
        placeholders.put(2, code);

        return new DatabaseReadCall(query, placeholders);
    }


    /**
     * Delete the code sent to the user's email
     *
     * @param email User's email
     *
     * @return DatabaseWriteCall
     */
    public static DatabaseWriteCall deleteForgetPasswordCode(String email) {
        String query = "DELETE FROM forget_password WHERE email = ?";
        HashMap<Integer, Object> placeholders = new HashMap<>();
        placeholders.put(1, email);

        return new DatabaseWriteCall(query, placeholders);
    }


    /**
     * Change the user's password
     *
     * @param email    User's email
     * @param password User's new password
     *
     * @return DatabaseWriteCall
     */
    public static DatabaseWriteCall changePassword(String email, String password) {
        String query = "UPDATE users SET password = ? WHERE email = ?";
        String hashedPassword = Hash.make(password);

        HashMap<Integer, Object> placeholders = new HashMap<>();
        placeholders.put(1, hashedPassword);
        placeholders.put(2, email);

        return new DatabaseWriteCall(query, placeholders);
    }

    public static DatabaseWriteCall deleteForgetPasswordAllCode() {
        String query = "DELETE FROM forget_password";
        return new DatabaseWriteCall(query, new HashMap<>());
    }
}
