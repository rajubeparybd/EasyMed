package com.easymed.controllers.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class DoctorController implements Initializable {

    @FXML
    private BorderPane rootPane;

    @FXML
    private TextField searchBox;

    @FXML
    private GridPane doctorContainer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * Search for a doctor
     *
     * @param keyEvent Search box key event
     */
    @FXML
    public void search(KeyEvent keyEvent) {
        //TODO: Search doctor
    }

    /**
     * Show the form to add a new doctor
     *
     * @param actionEvent Add Doctor key event
     */
    @FXML
    public void addDoctor(ActionEvent actionEvent) {
        //TODO: Add doctor form
    }
}