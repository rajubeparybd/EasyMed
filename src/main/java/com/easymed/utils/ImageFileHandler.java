package com.easymed.utils;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

public class ImageFileHandler {

    /**
     * select profile picture for upload
     *
     * @param actionEvent    button Action
     * @param profilePicture TextField to display file path
     * @param imgProfile     ImageView to display selected image
     * @param title          title for file chooser dialog
     */
    public static void uploadImage(ActionEvent actionEvent, Integer user_id, TextField profilePicture, ImageView imgProfile, String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(Helpers.getTitle(title));
        configureFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            String fileName = file.getName();
            String extension = getFileExtension(fileName);
            List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png");
            if (allowedExtensions.contains(extension.toLowerCase())) {
                String destFileName = user_id + "." + extension;
                File destFile = new File(Helpers.getImageFilePath() + destFileName);
                try {
                    Files.copy(file.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    profilePicture.setText(destFile.getAbsolutePath());
                    Image image = new Image(destFile.toURI().toString());
                    imgProfile.setImage(image);
                } catch (IOException e) {
                    showAlert("Failed to upload file: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                showAlert("Invalid file type. Please select a JPEG or PNG file.");
            }
        }
    }

    /**
     * configure file jpg, png , jpeg
     *
     * @param fileChooser image file
     */
    private static void configureFileChooser(FileChooser fileChooser) {
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPEG Files", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("PNG Files", "*.png")
        );
    }

    /**
     * get file Extension
     *
     * @param fileName file path in string
     *
     * @return extension jpg png jpeg
     */
    private static String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex != -1 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1);
        }
        return "";
    }

    /**
     * for Show Alert
     *
     * @param message alert message in string
     */
    private static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
