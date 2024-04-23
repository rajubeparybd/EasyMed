package com.easymed.controllers.patient;

import com.easymed.utils.Helpers;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class RecordController implements Initializable {

    @FXML
    private BorderPane rootPane;

    @FXML
    private TextField searchBox;

    @FXML
    private GridPane patientContainer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            VBox sidebar = (VBox) rootPane.getLeft();
            Helpers.toggleMenuClass(sidebar, "records");
        });
    }

    /**
     * Search for a patient
     *
     * @param keyEvent Search box key event
     */
    @FXML
    public void search(KeyEvent keyEvent) {
        //TODO: Search patient
    }

    /**
     * Show the form to add a new patient
     *
     * @param actionEvent Add Patient key event
     */
    @FXML
    public void addPatient(ActionEvent actionEvent) {
        //TODO: Add patient form
    }
}
