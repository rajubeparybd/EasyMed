package com.easymed.controllers.doctor;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class AppointmentCardController implements Initializable {
    @FXML
    private Circle patientImage;

    @FXML
    private Label patientName;

    @FXML
    private Label patientType;

    @FXML
    private JFXButton viewButton;

    @FXML
    private JFXButton viewAppointment;

    /**
     * View Appointment Details
     *
     * @param actionEvent button click event
     */
    @FXML
    public void viewAppointmentDetails(ActionEvent actionEvent) {
        //TODO: Goto appointment details view
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
