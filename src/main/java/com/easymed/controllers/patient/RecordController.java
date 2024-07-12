package com.easymed.controllers.patient;

import com.easymed.controllers.doctor.PrescriptionDetailsController;
import com.easymed.database.models.User;
import com.easymed.database.services.PrescriptionService;
import com.easymed.database.services.UserService;
import com.easymed.utils.DatabaseReadCall;
import com.easymed.utils.Helpers;
import com.easymed.utils.ImageHandler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class RecordController implements Initializable {
    private final User user = User.getInstance();
    public Label bloodGroupLabel;
    public Label genderLabel;
    public Label phoneLabel;
    public GridPane prescriptionContainer;
    @FXML
    private BorderPane rootPane;
    @FXML
    private TextField searchBox;
    @FXML
    private GridPane patientContainer;
    @FXML
    private ImageView imageLabel;
    @FXML
    private Label dobLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label nameLabel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            VBox sidebar = (VBox) rootPane.getLeft();
            Helpers.toggleMenuClass(sidebar, "records");
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setTitle(Helpers.getTitle("Patient Records"));
        });
        DatabaseReadCall getPatientInfo = UserService.getUserInfo(this.user.getId(), this.user.getEmail());
        loadPatientInfo(getPatientInfo);

        DatabaseReadCall getPatientPrescription = PrescriptionService.getPatientPrescription(this.user.getId());
        loadPatientPrescription(getPatientPrescription);
    }

    public void loadPatientInfo(DatabaseReadCall getPatientInfo) {
        getPatientInfo.setOnSucceeded(event -> {
            ResultSet resultSet = getPatientInfo.getValue();
            try {
                if (resultSet.next()) {
                    nameLabel.setText(resultSet.getString("name"));
                    emailLabel.setText(resultSet.getString("email"));
                    dobLabel.setText(resultSet.getString("dob"));
                    phoneLabel.setText(resultSet.getString("phone"));
                    genderLabel.setText(resultSet.getString("gender"));
                    bloodGroupLabel.setText(resultSet.getString("blood_group"));
                    String patientProfile = resultSet.getString("picture");

                    ImageHandler.setProfileImage(patientProfile, imageLabel);

                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        new Thread(getPatientInfo).start();
    }

    public void loadPatientPrescription(DatabaseReadCall getPatientPrescription) {
        getPatientPrescription.setOnSucceeded(event -> {
            ResultSet resultSet = getPatientPrescription.getValue();
            try {
                int rowIndex = 0;
                int count = 1;
                while (resultSet.next()) {
                    int prescriptionId = resultSet.getInt("p.id");
                    String appointmentDate = resultSet.getString("appointment_date");
                    String nextCheckUpDate = resultSet.getString("next_checkup");

                    Label prescriptionIdLabel = new Label(count + " .");
                    Label appointmentDateLabel = new Label(appointmentDate);
                    Label nextCheckUpDateLabel = new Label(nextCheckUpDate);
                    Button viewDetails = new Button("View");
                    viewDetails.getStyleClass().add("btn3");

                    HBox prescriptionBox = new HBox(prescriptionIdLabel);
                    HBox appointmentBox = new HBox(appointmentDateLabel);
                    HBox nextCheckUpBox = new HBox(nextCheckUpDateLabel);
                    HBox viewDetailsBox = new HBox(viewDetails);

                    prescriptionBox.setAlignment(Pos.CENTER);
                    appointmentBox.setAlignment(Pos.CENTER);
                    nextCheckUpBox.setAlignment(Pos.CENTER);
                    viewDetailsBox.setAlignment(Pos.CENTER);

                    prescriptionIdLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");
                    appointmentDateLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");
                    nextCheckUpDateLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");

                    viewDetails.setOnAction(e -> {
                        displayPrescriptionInfo(prescriptionId, appointmentDate, nextCheckUpDate);
                    });
                    prescriptionContainer.addRow(rowIndex, prescriptionBox, appointmentBox, nextCheckUpBox, viewDetailsBox);

                    // Apply alternating row colors
                    if (rowIndex % 2 == 0) {
                        applyRowStyle(prescriptionContainer, rowIndex, "#e0e0e0");
                    } else {
                        applyRowStyle(prescriptionContainer, rowIndex, "#f0f0f0");
                    }
                    count++;
                    rowIndex++;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        new Thread(getPatientPrescription).start();
    }

    private void applyRowStyle(GridPane gridPane, int rowIndex, String style) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == rowIndex) {
                node.setStyle("-fx-background-color: " + style + ";");
            }
        }
    }

    private void displayPrescriptionInfo(Integer prescriptionId, String appointmentDate, String nextCheckUpDate) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/easymed/views/doctor/prescription-details.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(Helpers.getTitle("Prescription Details"));
            stage.setScene(new Scene(root));

            Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/easymed/images/EasyMedIcon.png")));
            stage.getIcons().add(icon);

            PrescriptionDetailsController controller = fxmlLoader.getController();
            controller.setPrescriptionInfo(this.user.getId(), prescriptionId, appointmentDate, nextCheckUpDate);

            stage.setMinWidth(690);
            stage.setMinHeight(620);

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
