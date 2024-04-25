package com.easymed.database.services;

import com.easymed.utils.DatabaseReadCall;

import java.util.HashMap;

/**
 * UserService for handling users information
 *
 * @author Asif Ahammed
 * @since 1.0.0
 */
public class UserService {

    /**
     * get users information
     *
     * @param role user role
     * @return users
     */

    public static DatabaseReadCall getUsersInfo(String role) {

        String query = "SELECT * FROM  users WHERE role = ?";
        HashMap<Integer, Object> placeholders = new HashMap<>();
        placeholders.put(1, role);

        return new DatabaseReadCall(query, placeholders);
    }

    /**
     * get patient information
     *
     * @param role       user role
     * @param searchText Search Query
     * @return users
     */
    public static DatabaseReadCall searchUsersInfo(String role, String searchText) {

        String query = "SELECT * FROM users " +
                "WHERE " +
                "role = ? AND " +
                "(name LIKE ? OR " +
                "email LIKE ? OR " +
                "id LIKE ? OR " +
                "dob LIKE ? OR " +
                "gender LIKE ? OR " +
                "phone LIKE ? OR " +
                "blood_group LIKE ? OR " +
                "JSON_EXTRACT(address, '$.address') LIKE ? OR " +
                "JSON_EXTRACT(address, '$.city') LIKE ? OR " +
                "JSON_EXTRACT(address, '$.zip') LIKE ?)";

        HashMap<Integer, Object> placeholders = new HashMap<>();
        placeholders.put(1, role);
        for (int i = 2; i <= 11; i++) {
            placeholders.put(i, "%" + searchText + "%");
        }

        return new DatabaseReadCall(query, placeholders);
    }
}
