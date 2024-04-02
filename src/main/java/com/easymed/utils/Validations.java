package com.easymed.utils;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

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

    /**
     * invalidInput method is used to show invalid input feedback.
     *
     * @param success  ImageView
     * @param wrong    ImageView
     * @param feedback Label
     * @param message  String
     */
    public static void inputIsInvalid(ImageView success, ImageView wrong, Label feedback, String message) {
        success.setVisible(false);
        wrong.setVisible(true);
        feedback.setText(message);
        feedback.setVisible(true);
    }

    public static void inputIsValid(ImageView wrong, ImageView success, Label feedback) {
        wrong.setVisible(false);
        success.setVisible(true);
        feedback.setVisible(false);
    }
}
