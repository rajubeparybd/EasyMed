package com.easymed.controllers.doctor;

import com.easymed.utils.FXMLScene;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class AppointmentCardController implements Initializable {
    @FXML
    private ImageView patientProfileImage;

    @FXML
    private Label patientName;

    @FXML
    private Label patientEmail;

    @FXML
    private JFXButton viewButton;

    @FXML
    private JFXButton viewAppointment;

    private String name;
    private String email;
    private String dob;
    private String patientProfile;
    private Integer appointmentId;
    private Integer patientId;


    /**
     * View Appointment Details
     *
     * @param actionEvent button click event
     */
    @FXML
    public void viewAppointmentDetails(ActionEvent actionEvent) {
        CheckAppointmentController checkAppointmentController = FXMLScene.switchFxmlScene("/com/easymed/views/doctor/check-appointment.fxml", (Node) actionEvent.getSource());
        if (checkAppointmentController != null) {
            checkAppointmentController.setPatientInfo(name, appointmentId, email, patientId, dob, patientProfile);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * set card info
     *
     * @param name           String
     * @param email          String
     * @param patientProfile String
     * @param dob            String
     * @param patientId      Integer
     * @param appointmentId  Integer
     */
    void setUserInfo(String name, String email, String patientProfile, String dob, Integer patientId, Integer appointmentId) {
        this.patientName.setText(name);
        this.patientEmail.setText(email);

        this.name = name;
        this.email = email;
        this.dob = dob;
        this.patientId = patientId;
        this.patientProfile = patientProfile;
        this.appointmentId = appointmentId;


        if (patientProfile != null) {
            File file = new File(patientProfile);
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                patientProfileImage.setImage(image);
                Circle clip = new Circle();
                clip.setCenterX(patientProfileImage.getFitWidth() / 2);
                clip.setCenterY(patientProfileImage.getFitHeight() / 2);
                clip.setRadius(Math.min(patientProfileImage.getFitWidth(), patientProfileImage.getFitHeight()) / 2);

                patientProfileImage.setClip(clip);

            } else
                patientProfileImage.setImage(null);
        }

    }
}
