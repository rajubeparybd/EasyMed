package com.easymed.utils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

/**
 * Notification class is used to show different types of notifications.
 *
 * @author Asif Ahmed
 * @since 1.0.0
 */
public class ImageHandler {

    private static final String DEFAULT_IMAGE_PATH = "src/main/resources/com/easymed/images/patient.png";

    public static void setProfileImage(String picture, ImageView imageLabel) {
        setProfileImage(picture, imageLabel, 400, 400);
    }

    public static void setProfileImage(String picture, ImageView imageLabel, double width, double height) {
        File file = null;
        if (picture != null) {
            file = new File(picture);
        }
        if (file == null || !file.exists()) {
            file = new File(DEFAULT_IMAGE_PATH);
        }

        if (file.exists()) {
            Image image = new Image(file.toURI().toString(), width, height, false, false);
            imageLabel.setImage(image);
        } else {
            imageLabel.setImage(null);
        }
    }
}
