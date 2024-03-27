package com.easymed.utils;

/**
 * Validations class is used to validate different types of data.
 *
 * @author Raju Bepary
 * @since 1.0.0
 */
public class Validations {

    /**
     * isEmailValid method is used to validate an email address.
     *
     * @param email Email address to be validated.
     *
     * @return boolean
     */
    public static boolean isEmailValid(String email) {
        return email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    }

    /**
     * isPasswordValid method is used to validate a password.
     *
     * @param password Password to be validated.
     *
     * @return boolean
     */
    public static boolean isPasswordValid(String password) {
        return (password.length() >= 6);
    }
}
