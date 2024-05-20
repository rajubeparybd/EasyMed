package com.easymed.controllers.doctor;

import com.easymed.utils.FXMLScene;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.File;

public class PatientCardController {
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
     * @param actionEvent Button Action
     */
    public void viewDetailsButton(ActionEvent actionEvent) {
        PatientDetailsController patientDetailsController = FXMLScene.switchFxmlScene("/com/easymed/views/doctor/details-patient.fxml", (Node) actionEvent.getSource());
        if (patientDetailsController != null) {
            patientDetailsController.setPatientInfo(patientName, appointmentId, patientEmail, patientId, dob, patientProfile);
        }
    }
}
