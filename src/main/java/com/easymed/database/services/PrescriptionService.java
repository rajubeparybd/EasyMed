package com.easymed.database.services;

import com.easymed.utils.DatabaseReadCall;
import com.easymed.utils.DatabaseWriteCall;

import java.util.HashMap;

/**
 * PrescriptionService for handling prescription related database operations
 *
 * @author Asif Ahammed
 * @since 1.0.0
 */
public class PrescriptionService {

    /**
     * AttemptPrescription for insert Prescription data
     *
     * @param patient_id       Integer
     * @param doctor_id        Integer
     * @param appointment_id   Integer
     * @param next_checkupDate String
     *
     * @return DatabaseWriteCall
     */


    public static DatabaseWriteCall AttemptPrescription(Integer patient_id, Integer doctor_id, Integer appointment_id, String next_checkupDate) {
        String query = "INSERT INTO prescriptions (patient_id, doctor_id, appointment_id, next_checkup) VALUES (?,?,?,?)";

        HashMap<Integer, Object> placeholders = new HashMap<>();
        int index = 1;

        placeholders.put(index++, patient_id);
        placeholders.put(index++, doctor_id);
        placeholders.put(index++, appointment_id);
        placeholders.put(index, next_checkupDate);

        return new DatabaseWriteCall(query, placeholders);
    }

    /**
     * getPrescriptionByAppointmentId
     *
     * @param id Integer
     *
     * @return DatabaseReadCall
     */
    public static DatabaseReadCall getPrescriptionByAppointmentId(Integer id) {
        String query = "SELECT * FROM prescriptions WHERE appointment_id = ?";
        HashMap<Integer, Object> placeholders = new HashMap<>();
        placeholders.put(1, id);
        return new DatabaseReadCall(query, placeholders);
    }

    /**
     * getPrescriptionByAppointmentId
     *
     * @param id Integer
     *
     * @return DatabaseReadCall
     */
    public static DatabaseReadCall getPatientPrescription(Integer id) {
        String query = "SELECT * FROM appointments a " +
                "INNER JOIN prescriptions p ON a.id = p.appointment_id " +
                "WHERE a.patient_id = ? ORDER BY p.created_at DESC ";
        HashMap<Integer, Object> placeholders = new HashMap<>();
        placeholders.put(1, id);
        return new DatabaseReadCall(query, placeholders);
    }

    /**
     * getPrescriptionByAppointmentId
     *
     * @param id Integer
     *
     * @return DatabaseReadCall
     */
    public static DatabaseReadCall getRecentPrescription(Integer id) {
        String query = "SELECT * FROM prescriptions WHERE patient_id = ? ORDER BY created_at DESC LIMIT 1;";
        HashMap<Integer, Object> placeholders = new HashMap<>();
        placeholders.put(1, id);
        return new DatabaseReadCall(query, placeholders);
    }
}
