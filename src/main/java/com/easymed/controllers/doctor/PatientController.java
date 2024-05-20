package com.easymed.controllers.doctor;

import com.easymed.database.models.User;
import com.easymed.database.services.PatientService;
import com.easymed.utils.DatabaseReadCall;
import com.easymed.utils.Helpers;
import javafx.application.Platform;
import javafx.event.ActionEvent;
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

public class PatientController implements Initializable {
    private final User user = User.getInstance();

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
            Helpers.toggleMenuClass(sidebar, "patients");
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setTitle(Helpers.getTitle("Patients List"));
        });
        loadPatientData();
    }

    /**
     * Search for a patient
     *
     * @param keyEvent Search box key event
     */
    @FXML
    public void search(KeyEvent keyEvent) {
        String query = searchBox.getText();
        if (query.length() >= 3) {
            DatabaseReadCall getSearchInformation = PatientService.getPatients(query, this.user.getId());
            loadInformation(getSearchInformation);
        } else {
            loadPatientData();
        }
    }

    /**
     * load Patient Data
     */
    private void loadPatientData() {
        DatabaseReadCall getPatientsInfo = PatientService.getPatients(this.user.getId());
        loadInformation(getPatientsInfo);
    }

    /**
     * Load information of Patients
     *
     * @param getPatientsInfo Database Read call get Patient of a doctor
     */
    private void loadInformation(DatabaseReadCall getPatientsInfo) {
        getPatientsInfo.setOnSucceeded(event -> {
            ResultSet resultSet = getPatientsInfo.getValue();
            if (resultSet != null) {
                try {
                    patientContainer.getChildren().clear();
                    int maxColumn = 2;
                    int row = 1;
                    int col = 1;
                    patientContainer.setHgap(10);
                    patientContainer.setVgap(10);
                    while (resultSet.next()) {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/easymed/views/doctor/patient-card.fxml"));
                        AnchorPane patientProfilePane = fxmlLoader.load();
                        PatientCardController patientCardController = fxmlLoader.getController();

                        Integer appointmentId = resultSet.getInt("ap.id");
                        Integer patientId = resultSet.getInt("patient_id");
                        String name = resultSet.getString("name");
                        String email = resultSet.getString("email");
                        String reason = resultSet.getString("reason");
                        String appointmentDate = resultSet.getString("appointment_date");
                        String userProfile = resultSet.getString("picture");
                        String dob = resultSet.getString("dob");

                        patientCardController.setProfileData(appointmentId, patientId, name, email, reason, appointmentDate, userProfile, dob);
                        patientContainer.add(patientProfilePane, col, row);
                        col++;
                        if (col > maxColumn) {
                            col = 1;
                            row++;
                        }
                    }
                } catch (SQLException e) {
                    System.out.println("patient Card Controller : SQLException: " + e.getMessage());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        new Thread(getPatientsInfo).start();
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
