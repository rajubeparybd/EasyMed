package com.easymed.database.services;

import com.easymed.utils.DatabaseWriteCall;

import java.util.HashMap;

/**
 * PrescriptionService for handling prescription related database operations
 *
 * @author Asif Ahammed
 * @since 1.0.0
 */
public class IllnessService {
    /**
     * AttemptIllness for insert illness data
     *
     * @param user_id          Integer
     * @param prescriptions_id Integer
     * @param name             String
     * @param symptoms_date    String
     *
     * @return DatabaseWriteCall
     */
    public static DatabaseWriteCall AttemptIllness(Integer user_id, Integer prescriptions_id, String name, String symptoms_date) {
        String query = "INSERT INTO illness (user_id, prescriptions_id, name, symptoms_date) VALUES (?,?,?,?)";

        HashMap<Integer, Object> placeholders = new HashMap<>();
        int index = 1;

        placeholders.put(index++, user_id);
        placeholders.put(index++, prescriptions_id);
        placeholders.put(index++, name);
        placeholders.put(index, symptoms_date);

        return new DatabaseWriteCall(query, placeholders);
    }
}

