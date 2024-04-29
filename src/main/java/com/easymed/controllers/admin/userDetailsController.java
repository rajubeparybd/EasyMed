package com.easymed.controllers.admin;

//public class userDetailsController {
//}

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class userDetailsController {

    @FXML
    private Label patientDataLabel;

    public void setDoctorData(String patientData) {
        // TODO : set doctor data in modal
        patientDataLabel.setText(patientData);
    }
}
