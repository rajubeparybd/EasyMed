package com.easymed.database.services;

import com.easymed.utils.DatabaseReadCall;
import com.easymed.utils.DatabaseWriteCall;
import com.easymed.utils.Hash;

import java.util.HashMap;

/**
 * PatientService for handling patient related database operations
 *
 * @author Raju Bepary
 * @since 1.0.0
 */
public class PatientService {

    /**
     * Attempt to register a user as a patient
     *
     * @param name       Name
     * @param email      Email
     * @param password   Password
     * @param role       Role
     * @param gender     Gender
     * @param bloodGroup Blood group
     * @param phone      Phone number
     *
     * @return DatabaseWriteCall
     */
    public static DatabaseWriteCall attemptPatientRegistration(String name, String email, String password, String role, String gender, String bloodGroup, String phone) {
        String query = "INSERT INTO users (name, email, password, role, phone";
        String values = " VALUES (?, ?, ?, ?, ?";

        if (gender != null) {
            query += ", gender";
            values += ", ?";
        }
        if (bloodGroup != null) {
            query += ", blood_group";
            values += ", ?";
        }

        query += ")";
        values += ")";
        String hashedPassword = Hash.make(password);
        HashMap<Integer, Object> placeholders = new HashMap<>();
        int index = 1;

        placeholders.put(index++, name);
        placeholders.put(index++, email);
        placeholders.put(index++, hashedPassword);
        placeholders.put(index++, role);
        placeholders.put(index++, phone);

        if (gender != null) placeholders.put(index++, gender);
        if (bloodGroup != null) placeholders.put(index, bloodGroup);
        String finalQuery = query + values;

        return new DatabaseWriteCall(finalQuery, placeholders);
    }

    /**
     * Get patient id by email
     *
     * @param email Email
     *
     * @return DatabaseReadCall
     */
    public static DatabaseReadCall getPatientIdByEmail(String email) {
        String query = "SELECT id FROM users WHERE email = ?";

        HashMap<Integer, Object> placeholders = new HashMap<>();
        placeholders.put(1, email);

        return new DatabaseReadCall(query, placeholders);
    }
}
