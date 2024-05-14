package com.easymed.controllers.doctor;

import com.easymed.database.models.User;
import com.easymed.database.services.AppointmentService;
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

public class AppointmentController implements Initializable {
    private final User user = User.getInstance();
    @FXML
    public TextField searchBox;
    @FXML
    private BorderPane rootPane;
    @FXML
    private GridPane patientContainer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            VBox sidebar = (VBox) rootPane.getLeft();
            Helpers.toggleMenuClass(sidebar, "appointments");
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setTitle(Helpers.getTitle("Appointments List"));
        });
        loadPatientData();
    }

    /**
     * load Patient Data
     */
    private void loadPatientData() {
        DatabaseReadCall getPatientsInfo = AppointmentService.getAppointmentsByDoctorId(this.user.getId());
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
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/easymed/views/doctor/appointments-card.fxml"));
                        AnchorPane patientProfilePane = fxmlLoader.load();
                        AppointmentsCardController appointmentsCardController = fxmlLoader.getController();

                        Integer appointmentId = resultSet.getInt("ap.id");
                        Integer patientId = resultSet.getInt("patient_id");
                        String name = resultSet.getString("name");
                        String email = resultSet.getString("email");
                        String reason = resultSet.getString("reason");
                        String appointmentDate = resultSet.getString("appointment_date");
                        String userProfile = resultSet.getString("picture");
                        String dob = resultSet.getString("dob");

                        appointmentsCardController.setProfileData(appointmentId, patientId, name, email, reason, appointmentDate, userProfile, dob);
                        patientContainer.add(patientProfilePane, col, row);
                        col++;
                        if (col > maxColumn) {
                            col = 1;
                            row++;
                        }
                    }
                } catch (SQLException e) {
                    System.out.println("Appointment Controller: SQLException: " + e.getMessage());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        new Thread(getPatientsInfo).start();
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
            DatabaseReadCall getSearchInformation = AppointmentService.getAppointmentsBySearch(query, this.user.getId());
            loadInformation(getSearchInformation);
        } else {
            loadPatientData();
        }
    }
}
