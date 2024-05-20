package com.easymed.controllers.doctor;

import com.easymed.database.services.PrescriptionService;
import com.easymed.database.services.UserService;
import com.easymed.utils.DatabaseReadCall;
import com.easymed.utils.Helpers;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class PatientDetailsController implements Initializable {
    public Label zipLabel;
    public Label cityLabel;
    public Label addressLabel;
    public Label bloodGroupLabel;
    public Label genderLabel;
    public Label userIdLabel;
    public Label phoneLabel;
    public GridPane prescriptionContainer;
    @FXML
    private BorderPane rootPane;
    @FXML
    private ImageView imageLabel;
    @FXML
    private Label dobLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label nameLabel;

    private Integer appointmentId;
    private Integer patientId;

//    private Integer prescriptionId;
//    private String appointmentDate;
//    private String nextCheckUpDate;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            VBox sidebar = (VBox) rootPane.getLeft();
            Helpers.toggleMenuClass(sidebar, "patients");
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setTitle(Helpers.getTitle("Patient Details"));
        });
    }

    public void setPatientInfo(String name, Integer appointmentId, String email, Integer patientId, String dob, String patientProfile) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.nameLabel.setText(name);
        this.emailLabel.setText(email);
        this.dobLabel.setText(dob);
        if (patientProfile != null) {
            File file = new File(patientProfile);
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                imageLabel.setImage(image);
            } else imageLabel.setImage(null);
        }
        DatabaseReadCall getPatientInfo = UserService.getUserInfo(patientId, email);
        loadPatientInfo(getPatientInfo);

        DatabaseReadCall getPatientPrescription = PrescriptionService.getPatientPrescription(patientId);
        loadPatientPrescription(getPatientPrescription);


    }

    public void loadPatientInfo(DatabaseReadCall getPatientInfo) {
        getPatientInfo.setOnSucceeded(event -> {
            ResultSet resultSet = getPatientInfo.getValue();
            try {
                if (resultSet.next()) {
                    phoneLabel.setText(resultSet.getString("phone"));
                    genderLabel.setText(resultSet.getString("gender"));
                    bloodGroupLabel.setText(resultSet.getString("blood_group"));

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
            controller.setPrescriptionInfo(patientId, prescriptionId, appointmentDate, nextCheckUpDate);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
