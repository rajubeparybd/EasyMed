package com.easymed.database.services;

import com.easymed.utils.DatabaseReadCall;
import com.easymed.utils.DatabaseWriteCall;
import com.easymed.utils.Hash;
import com.easymed.utils.Helpers;
import org.json.JSONObject;

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
     *
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
     *
     * @return users
     */
    public static DatabaseReadCall searchUsersInfo(String role, String searchText) {

        String query = "SELECT * FROM users " + "WHERE " + "role = ? AND " + "(name LIKE ? OR " + "email LIKE ? OR " + "id LIKE ? OR " + "dob LIKE ? OR " + "gender LIKE ? OR " + "phone LIKE ? OR " + "blood_group LIKE ? OR " + "JSON_EXTRACT(address, '$.address') LIKE ? OR " + "JSON_EXTRACT(address, '$.city') LIKE ? OR " + "JSON_EXTRACT(address, '$.zip') LIKE ?)";

        HashMap<Integer, Object> placeholders = new HashMap<>();
        placeholders.put(1, role);
        for (int i = 2; i <= 11; i++) {
            placeholders.put(i, "%" + searchText + "%");
        }
        return new DatabaseReadCall(query, placeholders);
    }

    /**
     * get user information by id
     *
     * @param userId user id
     * @param email  user email
     *
     * @return user information
     */
    public static DatabaseReadCall getUserInfo(Integer userId, String email) {
        String query = "SELECT * FROM users WHERE id = ? AND email = ? ";
        HashMap<Integer, Object> placeholders = new HashMap<>();
        placeholders.put(1, userId);
        placeholders.put(2, email);
        return new DatabaseReadCall(query, placeholders);
    }

    public static DatabaseWriteCall attemptUpdateUserInformation(Integer user_id, String user_email, String name, String email, String password, String phone, String address, String city, String zip, String gender, String bloodGroup, String dob, String profilePath) {

        String query = "UPDATE users SET name = ?, email = ?";

        JSONObject full_address = new JSONObject();
        if (address != null) full_address.put("address", address);
        if (city != null) full_address.put("city", city);
        if (zip != null) full_address.put("zip", Integer.parseInt(zip));

        String hashedPass = Hash.make(password);

        if (password != null) query += ", password = ?";
        if (phone != null) query += ", phone = ?";
        if (full_address.toString() != null) query += ", address = ?";
        if (gender != null) query += ", gender = ?";
        if (bloodGroup != null) query += ", blood_group = ?";
        if (dob != null) query += ", dob = ?";
        if (profilePath != null) query += ", picture = ?";
        query += " WHERE id = ? AND email = ?";

        HashMap<Integer, Object> placeholders = new HashMap<>();
        int index = 1;
        placeholders.put(index++, name);
        placeholders.put(index++, email);
        if (password != null) placeholders.put(index++, hashedPass);
        if (phone != null) placeholders.put(index++, Helpers.getFormattedPhoneNumber(phone));
        if (address != null) placeholders.put(index++, full_address.toString());
        if (gender != null) placeholders.put(index++, gender);
        if (bloodGroup != null) placeholders.put(index++, bloodGroup);
        if (dob != null) placeholders.put(index++, dob);
        if (profilePath != null) placeholders.put(index++, profilePath);
        placeholders.put(index++, user_id);
        placeholders.put(index, user_email);

        return new DatabaseWriteCall(query, placeholders);
    }
}
