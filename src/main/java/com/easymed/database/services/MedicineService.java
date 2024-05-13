package com.easymed.database.services;

import com.easymed.utils.DatabaseWriteCall;

import java.util.HashMap;

/**
 * MedicineService for handling Medicine related database operations
 *
 * @author Asif Ahammed
 * @since 1.0.0
 */
public class MedicineService {
    /**
     * AttemptMedicine for insert Medicine data
     *
     * @param user_id          Integer
     * @param prescriptions_id Integer
     * @param name             String
     * @param morning          String
     * @param afternoon        String
     * @param night            String
     *
     * @return DatabaseWriteCall
     */
    public static DatabaseWriteCall AttemptMedicine(Integer user_id, Integer prescriptions_id, String name, Boolean morning, Boolean afternoon, Boolean night) {
        String query = "INSERT INTO medicines (user_id, prescriptions_id, name, morning,afternoon,night) VALUES (?,?,?,?,?,?)";

        HashMap<Integer, Object> placeholders = new HashMap<>();
        int index = 1;

        placeholders.put(index++, user_id);
        placeholders.put(index++, prescriptions_id);
        placeholders.put(index++, name);
        placeholders.put(index++, morning);
        placeholders.put(index++, afternoon);
        placeholders.put(index, night);

        return new DatabaseWriteCall(query, placeholders);
    }
}
