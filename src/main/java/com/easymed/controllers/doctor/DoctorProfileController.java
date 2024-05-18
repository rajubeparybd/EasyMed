package com.easymed.controllers.doctor;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.File;

public class DoctorProfileController {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label nameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label spacialitiesLabel;
    @FXML
    private Label hospitalNameLabel;
    @FXML
    private Label hospitalAddressLabel;
    @FXML
    private ImageView profilePicLabel;

    public void setProfileData(String name, String email, String spacialities, String hospital, String hospitalAddress, String doctorProfile) {
        nameLabel.setText(name);
        emailLabel.setText(email);
        spacialitiesLabel.setText(spacialities);
        hospitalNameLabel.setText(hospital);
        hospitalAddressLabel.setText(hospitalAddress);
        if (doctorProfile != null) {
            File file = new File(doctorProfile);
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                profilePicLabel.setImage(image);
            } else
                profilePicLabel.setImage(null);
        }
    }
}
