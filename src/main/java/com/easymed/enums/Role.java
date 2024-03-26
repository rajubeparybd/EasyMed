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

    /**
     * Get the text representation of the role
     *
     * @param role Role to get text representation
     *
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
    public boolean isAdmin() {
        return this == ADMIN;
    }

    /**
     * Check if the role is doctor
     *
     * @return Boolean
     */
    public boolean isDoctor() {
        return this == DOCTOR;
    }

    /**
     * Check if the role is patient
     *
     * @return Boolean
     */
    public boolean isPatient() {
        return this == PATIENT;
    }
}
