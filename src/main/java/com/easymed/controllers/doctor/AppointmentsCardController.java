package com.easymed.controllers.doctor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class AppointmentsCardController {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label nameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label setReasonLabel;
    @FXML
    private ImageView profilePicLabel;
    @FXML
    private Label appointmentDateLabel;
    @FXML
    private Button checkAppointment;

    private String patientEmail;
    private Integer appointmentId;
    private Integer patientId;
    private String patientProfile;
    private String dob;
    private String patientName;

    /**
     * set Appointment Profile data
     *
     * @param appointmentId   Integer
     * @param patientId       Integer
     * @param name            String
     * @param email           String
     * @param reason          String
     * @param appointmentDate String
     * @param patientProfile  String
     * @param dob             String
     */
    public void setProfileData(Integer appointmentId, Integer patientId, String name, String email, String reason, String appointmentDate, String patientProfile, String dob) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.patientEmail = email;
        this.dob = dob;
        this.patientProfile = patientProfile;
        this.patientName = name;

        nameLabel.setText(name);
        emailLabel.setText(email);
        setReasonLabel.setText(reason);

        if (patientProfile != null) {
            File file = new File(patientProfile);
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                profilePicLabel.setImage(image);
            } else
                profilePicLabel.setImage(null);
        }
        appointmentDateLabel.setText(appointmentDate);
    }

    /**
     * see patient
     *
     * @param event Button Action
     */
    public void checkAppointmentButton(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/easymed/views/doctor/check-appointment.fxml"));
        try {
            Parent checkAppointmentParent = loader.load();
            Scene checkAppointmentScene = new Scene(checkAppointmentParent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(checkAppointmentScene);
            window.show();
            CheckAppointmentController createAppointmentController = loader.getController();
            createAppointmentController.setPatientInfo(patientName, appointmentId, patientEmail, patientId, dob, patientProfile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
