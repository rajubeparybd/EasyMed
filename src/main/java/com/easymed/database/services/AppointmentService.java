package com.easymed.database.services;

import com.easymed.utils.DatabaseReadCall;
import com.easymed.utils.DatabaseWriteCall;

import java.util.HashMap;

/**
 * AppointmentService for handling appointment related database operations
 *
 * @author Asif Ahammed
 * @since 1.0.0
 */
public class AppointmentService {
    /**
     * Attempt to register a user as a patient
     *
     * @param patient_id patient id
     * @param doctor_id  doctor id
     * @param reason     reason
     *
     * @return DatabaseWriteCall
     */
    public static DatabaseWriteCall attemptAppointment(Integer patient_id, Integer doctor_id, String reason, String appointmentDate) {
        String query = "INSERT INTO appointments (patient_id, doctor_id, reason, appointment_date) VALUES (?,?,?,?)";

        HashMap<Integer, Object> placeholders = new HashMap<>();
        int index = 1;

        placeholders.put(index++, patient_id);
        placeholders.put(index++, doctor_id);
        placeholders.put(index++, reason);
        placeholders.put(index, appointmentDate);

        return new DatabaseWriteCall(query, placeholders);
    }

    /**
     * Get appointment by doctor id
     *
     * @param id doctor id
     *
     * @return DatabaseReadCall
     */
    public static DatabaseReadCall getAppointmentsByDoctorId(Integer id) {
        String query = "SELECT * FROM users as u JOIN appointments as ap ON u.id = ap.patient_id WHERE ap.doctor_id = ? AND status = 'Scheduled' ORDER BY appointment_date";

        HashMap<Integer, Object> placeholders = new HashMap<>();
        placeholders.put(1, id);

        return new DatabaseReadCall(query, placeholders);
    }

    /**
     * get Appointments By Search query
     *
     * @param searchText String searchText
     * @param doctor_id  String doctor_id
     *
     * @return DatabaseReadCall
     */
    public static DatabaseReadCall getAppointmentsBySearch(String searchText, Integer doctor_id) {
        String query = "SELECT * FROM users as u JOIN appointments as ap ON u.id = ap.patient_id " + "WHERE " + "(name LIKE ? OR " + "email LIKE ? OR " + "reason LIKE ? OR" + " appointment_date LIKE ? OR " + "status LIKE ?) AND ap.doctor_id = ? AND status = 'Scheduled' ";

        HashMap<Integer, Object> placeholders = new HashMap<>();
        int i;
        for (i = 1; i <= 5; i++) {
            placeholders.put(i, "%" + searchText + "%");
        }
        placeholders.put(i, doctor_id);
        return new DatabaseReadCall(query, placeholders);
    }

    /**
     * Get Appointment by Appointment id
     *
     * @param id Integer
     *
     * @return DatabaseReadCall
     */
    public static DatabaseReadCall getAppointmentByAppointmentId(Integer id) {
        String query = "SELECT * FROM appointments WHERE id = ?";
        HashMap<Integer, Object> placeholders = new HashMap<>();
        placeholders.put(1, id);
        return new DatabaseReadCall(query, placeholders);
    }

    /**
     * Update Appointment by Appointment id
     *
     * @param id Integer
     *
     * @return DatabaseReadCall
     */
    public static DatabaseWriteCall updateAppointment(Integer id) {
        String query = "UPDATE appointments SET status = 'Completed' WHERE id = ?";
        HashMap<Integer, Object> placeholders = new HashMap<>();
        placeholders.put(1, id);
        return new DatabaseWriteCall(query, placeholders);
    }

    /**
     * Count All To Days Appointment for Doctor
     *
     * @param id Integer
     *
     * @return DatabaseReadCall
     */
    public static DatabaseReadCall countAppointments(Integer id) {
        String query = "SELECT COUNT(*) FROM appointments WHERE DATE(appointment_date) = CURDATE() AND doctor_id = ?";
        HashMap<Integer, Object> placeholders = new HashMap<>();
        placeholders.put(1, id);
        return new DatabaseReadCall(query, placeholders);

    }

    /**
     * get All To Days Appointment information for Doctor
     *
     * @param doctorId Integer
     *
     * @return DatabaseReadCall
     */
    public static DatabaseReadCall getToDaysAppointments(Integer doctorId) {
        String query = "SELECT * " +
                "FROM appointments AS ap " +
                "INNER JOIN users AS us ON ap.patient_id = us.id " +
                "WHERE DATE(ap.appointment_date) = CURDATE() " +
                "AND ap.doctor_id = ? AND status = 'Scheduled'";
        HashMap<Integer, Object> placeholders = new HashMap<>();
        placeholders.put(1, doctorId);
        return new DatabaseReadCall(query, placeholders);
    }

    /**
     * get search information of To Days Appointment for Doctor
     *
     * @param doctorId Integer
     *
     * @return DatabaseReadCall
     */
    public static DatabaseReadCall getToDaysAppointments(Integer doctorId, String searchText) {
        String query = "SELECT * " +
                "FROM appointments AS ap " +
                "INNER JOIN users AS us ON ap.patient_id = us.id " +
                "WHERE DATE(ap.appointment_date) = CURDATE() " +
                "AND ap.doctor_id = ? " +
                "AND (us.name LIKE ? OR us.email LIKE ?)";
        HashMap<Integer, Object> placeholders = new HashMap<>();
        placeholders.put(1, doctorId);
        String likeSearchText = "%" + searchText + "%";
        placeholders.put(2, likeSearchText);
        placeholders.put(3, likeSearchText);
        return new DatabaseReadCall(query, placeholders);
    }

    /**
     * get Chart Data for Appointments for Doctor Dashboard
     *
     * @param doctorId Integer
     *
     * @return DatabaseReadCall
     */
    public static DatabaseReadCall getChartDataForAppointments(Integer doctorId) {
        String query = "SELECT COUNT(*) AS count, DATE(created_at) AS date FROM appointments WHERE status ='Scheduled' AND doctor_id = ? GROUP BY DATE(created_at)";
        HashMap<Integer, Object> placeholders = new HashMap<>();
        placeholders.put(1, doctorId);
        return new DatabaseReadCall(query, placeholders);
    }

    /**
     * get Chart Data for Patient for Doctor Dashboard
     *
     * @param doctorId Integer
     *
     * @return DatabaseReadCall
     */
    public static DatabaseReadCall getChartDataForPatients(Integer doctorId) {
        String query = "SELECT COUNT(*) AS count, DATE(created_at) AS date FROM appointments WHERE status ='Completed' AND doctor_id = ? GROUP BY DATE(created_at)";
        HashMap<Integer, Object> placeholders = new HashMap<>();
        placeholders.put(1, doctorId);
        return new DatabaseReadCall(query, placeholders);
    }
}
