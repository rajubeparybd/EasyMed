package com.easymed.controllers.patient;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

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

    private String doctorId;

    /**
     * set Appointment Profile Info
     *
     * @param id              Integer
     * @param name            String
     * @param email           String
     * @param spacialities    String
     * @param hospital        String
     * @param hospitalAddress String
     * @param doctorProfile   String
     */
    public void setProfileData(String id, String name, String email, String spacialities, String hospital, String hospitalAddress, String doctorProfile) {
        this.doctorId = id;
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

    /**
     * Create Appointment Button
     *
     * @param event ActionEvent
     */
    public void createAppointment(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/easymed/views/patient/create-appointment.fxml"));
        try {
            Parent createAppointmentParent = loader.load();
            Scene createAppointmentScene = new Scene(createAppointmentParent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(createAppointmentScene);
            window.show();
            CreateAppointmentController createAppointmentController = loader.getController();
            createAppointmentController.setDoctorId(doctorId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
