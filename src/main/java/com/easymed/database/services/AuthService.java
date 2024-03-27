package com.easymed.database.services;

import com.easymed.utils.DatabaseReadCall;
import com.easymed.utils.Hash;

import java.util.HashMap;

/**
 * AuthService for handling authentication
 *
 * @author Raju Bepary
 * @since 1.0.0
 */
public class AuthService {

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


}
