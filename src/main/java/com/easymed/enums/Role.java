package com.easymed.enums;

/**
 * User roles enumeration
 *
 * @author Raju Bepary
 * @since 1.0.0
 */
public enum Role {
    /**
     * User roles
     */
    ADMIN, DOCTOR, PATIENT;

    public static String getText(Role role) {
        return String.valueOf(role);
    }

    /**
     * Get the text representation of the role
     *
     * @param role Role to get text representation
     * @return String representation of the role
     */
    public static String getRoleText(Role role) {
        return switch (role) {
            case ADMIN -> "Admin";
            case DOCTOR -> "Doctor";
            case PATIENT -> "Patient";
            default -> "Unknown";
        };
    }

    /**
     * Check if the role is admin
     *
     * @return Boolean
     */
    public static boolean isAdmin(String role) {
        return role.toUpperCase().equals(ADMIN.toString());
    }

    /**
     * Check if the role is doctor
     *
     * @return Boolean
     */
    public static boolean isDoctor(String role) {
        return role.toUpperCase().equals(DOCTOR.toString());
    }

    /**
     * Check if the role is patient
     *
     * @return Boolean
     */
    public static boolean isPatient(String role) {
        return role.toUpperCase().equals(PATIENT.toString());
    }
}
