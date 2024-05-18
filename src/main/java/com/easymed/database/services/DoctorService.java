package com.easymed.database.services;

import com.easymed.utils.DatabaseReadCall;
import com.easymed.utils.DatabaseWriteCall;
import com.easymed.utils.Hash;
import com.easymed.utils.Helpers;

import java.util.HashMap;

/**
 * DoctorService for handling doctor related database operations
 *
 * @author Raju Bepary
 * @since 1.0.0
 */
public class DoctorService {

    /**
     * Attempt to register a user as a doctor
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
    public static DatabaseWriteCall attemptDoctorRegistration(String name, String email, String password, String role, String gender, String bloodGroup, String phone) {
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
        placeholders.put(index++, Helpers.getFormattedPhoneNumber(phone));

        if (gender != null) placeholders.put(index++, gender);
        if (bloodGroup != null) placeholders.put(index, bloodGroup);
        String finalQuery = query + values;

        return new DatabaseWriteCall(finalQuery, placeholders);
    }

    /**
     * Get doctor id by email
     *
     * @param email Email
     *
     * @return DatabaseReadCall
     */
    public static DatabaseReadCall getDoctorIdByEmail(String email) {
        String query = "SELECT id FROM users WHERE email = ?";

        HashMap<Integer, Object> placeholders = new HashMap<>();
        placeholders.put(1, email);

        return new DatabaseReadCall(query, placeholders);
    }

    /**
     * Attempt to register doctor profile
     *
     * @param user_id         user id of the doctor
     * @param bio             bio
     * @param spacialities    spacialities
     * @param fees            doctor fees
     * @param designation     designation
     * @param hospital        hospital
     * @param hospitalAddress Hospital address
     * @param experience      experience
     * @param education       education
     * @param schedule        schedule
     *
     * @return DatabaseWriteCall
     */
    public static DatabaseWriteCall attemptDoctorProfileRegistration(String user_id, String bio, String spacialities, String fees, String designation, String hospital, String hospitalAddress, String experience, String education, String schedule) {
        String query = "INSERT INTO doctors (user_id, bio, spacialities, fees, designation, hospital, hospital_address";
        String values = " VALUES (?, ?, ?, ?, ?, ?, ?";

        if (experience != null) {
            query += ", experience";
            values += ", ?";
        }
        if (education != null) {
            query += ", education";
            values += ", ?";
        }
        if (schedule != null) {
            query += ", schedule";
            values += ", ?";
        }

        query += ")";
        values += ")";
        HashMap<Integer, Object> placeholders = new HashMap<>();
        int index = 1;

        placeholders.put(index++, user_id);
        placeholders.put(index++, bio);
        placeholders.put(index++, spacialities);
        placeholders.put(index++, fees);
        placeholders.put(index++, designation);
        placeholders.put(index++, hospital);
        placeholders.put(index++, hospitalAddress);

        if (experience != null) placeholders.put(index++, experience);
        if (education != null) placeholders.put(index++, education);
        if (schedule != null) placeholders.put(index, schedule);

        String finalQuery = query + values;

        return new DatabaseWriteCall(finalQuery, placeholders);
    }

    /**
     * get doctor information by id
     *
     * @param id doctor id
     *
     * @return DatabaseReadCall
     */
    public static DatabaseReadCall getDoctorInformation(Integer id) {
        String query = "SELECT * FROM users as u JOIN doctors as d ON u.id = d.user_id WHERE u.id = ?";
        HashMap<Integer, Object> placeholders = new HashMap<>();
        placeholders.put(1, id);
        return new DatabaseReadCall(query, placeholders);
    }

    public static DatabaseWriteCall attemptUpdateDoctorInformation(Integer id, String bio, String spacialities, String fees, String designation, String hospital, String hospitalAddress, String experience, String education, String schedule) {
        String query = "UPDATE doctors SET spacialities = ?, fees = ?, designation = ?, hospital = ?, hospital_address = ?";
        HashMap<Integer, Object> placeholders = new HashMap<>();
        int index = 1;
        placeholders.put(index++, spacialities);
        placeholders.put(index++, fees);
        placeholders.put(index++, designation);
        placeholders.put(index++, hospital);
        placeholders.put(index++, hospitalAddress);

        if (bio != null) {
            query += ", bio = ?";
            placeholders.put(index++, bio);
        }
        if (experience != null) {
            query += ", experience = ?";
            placeholders.put(index++, experience);
        }
        if (education != null) {
            query += ", education = ?";
            placeholders.put(index++, education);
        }
        if (schedule != null) {
            query += ", schedule = ?";
            placeholders.put(index++, schedule);
        }
        query += " WHERE user_id = ?";
        placeholders.put(index, id);
        return new DatabaseWriteCall(query, placeholders);
    }

    public static DatabaseReadCall getDoctorsInformation() {
        String query = "SELECT * FROM users as u JOIN doctors as d ON u.id = d.user_id";
        HashMap<Integer, Object> placeholders = new HashMap<>();
        return new DatabaseReadCall(query, placeholders);
    }

    public static DatabaseReadCall searchDocsInformations(String searchText) {
        String query = "SELECT * FROM users as u JOIN doctors as d ON u.id = d.user_id " + "WHERE " + "(name LIKE ? OR " + "email LIKE ? OR " + "hospital LIKE ? OR" + " hospital_address LIKE ? OR " + "spacialities LIKE ?)";

        HashMap<Integer, Object> placeholders = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            placeholders.put(i, "%" + searchText + "%");
        }
        return new DatabaseReadCall(query, placeholders);
    }
}
