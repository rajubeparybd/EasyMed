package com.easymed.controllers.patient;


import com.easymed.database.services.DoctorService;
import com.easymed.utils.DatabaseReadCall;
import com.easymed.utils.Helpers;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        Platform.runLater(() -> {
            VBox sidebar = (VBox) rootPane.getLeft();
            Helpers.toggleMenuClass(sidebar, "doctors");
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setTitle(Helpers.getTitle("Doctors List"));
        });
        loadDoctorData();
    }

    /**
     * Load Doctor data
     */
    private void loadDoctorData() {
        DatabaseReadCall getDoctorsInfo = DoctorService.getDoctorsInformation();
        loadInformation(getDoctorsInfo);
    }

    /**
     * load doctor
     *
     * @param getDoctorsInfo DatabaseReaCall to get Doctors information
     */
    private void loadInformation(DatabaseReadCall getDoctorsInfo) {
        getDoctorsInfo.setOnSucceeded(event -> {
            ResultSet resultSet = getDoctorsInfo.getValue();
            if (resultSet != null) {
                try {
                    doctorContainer.getChildren().clear();
                    int maxcolumn = 2;
                    int row = 1;
                    int col = 1;
                    doctorContainer.setHgap(10);
                    doctorContainer.setVgap(10);
                    while (resultSet.next()) {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/easymed/views/patient/doctors-card.fxml"));
                        AnchorPane doctorProfilePane = fxmlLoader.load();
                        DoctorProfileController doctorProfileController = fxmlLoader.getController();
                        String id = resultSet.getString("id");
                        String name = resultSet.getString("name");
                        String email = resultSet.getString("email");
                        String specialization = resultSet.getString("spacialities");
                        String hospitalName = resultSet.getString("hospital");
                        String hospitalAddress = resultSet.getString("hospital_address");
                        String doctorProfilePic = resultSet.getString("picture");

                        doctorProfileController.setProfileData(id, name, email, specialization, hospitalName, hospitalAddress, doctorProfilePic);
                        doctorContainer.add(doctorProfilePane, col, row);
                        col++;
                        if (col > maxcolumn) {
                            col = 1;
                            row++;
                        }
                    }
                } catch (SQLException e) {
                    System.out.println("DoctorController: SQLException: " + e.getMessage());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        new Thread(getDoctorsInfo).start();
    }

    /**
     * Search for a doctor
     *
     * @param keyEvent Search box key event
     */
    @FXML
    public void search(KeyEvent keyEvent) {
        String query = searchBox.getText();
        if (query.length() >= 3) {
            DatabaseReadCall getSearchInformation = DoctorService.searchDocsInformations(query);
            loadInformation(getSearchInformation);
        } else {
            loadDoctorData();
        }
    }
}

