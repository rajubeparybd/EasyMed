package com.easymed.controllers.doctor;

import com.easymed.database.services.IllnessService;
import com.easymed.database.services.MedicineService;
import com.easymed.utils.DatabaseReadCall;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PrescriptionDetailsController {


    public Label PID;
    public Label ApDate;
    public Label NeDate;
    public VBox medicineContainer;
    public VBox illnessContainer;
    private Integer patientId;

    public void setPrescriptionInfo(Integer patientId, Integer prescriptionId, String appointmentDate, String nextCheckUpDate) {
        this.patientId = patientId;
        this.PID.setText(prescriptionId.toString());
        this.ApDate.setText(appointmentDate);
        this.NeDate.setText(nextCheckUpDate);

        DatabaseReadCall getIllness = IllnessService.getIllness(patientId, prescriptionId);
        loadIllness(getIllness);
        DatabaseReadCall getMedicine = MedicineService.getMedicine(patientId, prescriptionId);
        loadMedicine(getMedicine);

    }

    private void loadMedicine(DatabaseReadCall getMedicine) {
        getMedicine.setOnSucceeded(event -> {
            ResultSet resultSet = getMedicine.getValue();
            try {
                int count = 1;
                while (resultSet.next()) {
                    String medicineName = resultSet.getString("name");
                    String morning = resultSet.getString("morning");
                    String afternoon = resultSet.getString("afternoon");
                    String night = resultSet.getString("night");

                    Label countLabel = new Label(count + " .");
                    Label medicineLabel = new Label(medicineName);
                    Label morningLabel = new Label(morning);
                    Label afternoonLabel = new Label(afternoon);
                    Label nightLabel = new Label(night);

                    countLabel.setStyle("-fx-font-size: 16px; -fx-padding: 10px 10px 5px 5px;");
                    medicineLabel.setStyle("-fx-font-size: 16px; -fx-padding: 10px 10px 5px 5px;");
                    morningLabel.setStyle("-fx-font-size: 16px; -fx-padding: 10px 10px 5px 5px;");
                    afternoonLabel.setStyle("-fx-font-size: 16px; -fx-padding: 10px 10px 5px 5px;");
                    nightLabel.setStyle("-fx-font-size: 16px; -fx-padding: 10px 10px 5px 5px;");

                    countLabel.setPrefWidth(50);
                    medicineLabel.setPrefWidth(200);
                    morningLabel.setPrefWidth(100);
                    afternoonLabel.setPrefWidth(100);
                    nightLabel.setPrefWidth(100);

                    HBox medicineHbox = new HBox(countLabel, medicineLabel, morningLabel, afternoonLabel, nightLabel);
                    if (count % 2 == 0) {
                        medicineHbox.setStyle("-fx-background-color: #e0e0e0;");
                    }
                    medicineContainer.getChildren().add(medicineHbox);
                    count++;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        new Thread(getMedicine).start();
    }

    private void loadIllness(DatabaseReadCall getIllness) {
        getIllness.setOnSucceeded(event -> {
            ResultSet resultSet = getIllness.getValue();
            try {
                int count = 1;
                while (resultSet.next()) {
                    String illnessName = resultSet.getString("name");
                    String symptomsDate = resultSet.getString("symptoms_date");

                    Label countLabel = new Label(count + " .");
                    Label illnessNameLabel = new Label(illnessName);
                    Label symptomsDateLabel = new Label(symptomsDate);

                    countLabel.setStyle("-fx-font-size: 16px; -fx-padding: 10px 10px 5px 5px;");
                    illnessNameLabel.setStyle("-fx-font-size: 16px; -fx-padding: 10px 10px 5px 5px;");
                    symptomsDateLabel.setStyle("-fx-font-size: 16px; -fx-padding: 10px 10px 5px 5px;");

                    countLabel.setPrefWidth(50);
                    illnessNameLabel.setPrefWidth(300);
                    symptomsDateLabel.setPrefWidth(200);

                    HBox medicineHbox = new HBox(countLabel, illnessNameLabel, symptomsDateLabel);
                    if (count % 2 == 0) {
                        medicineHbox.setStyle("-fx-background-color: #e0e0e0;");
                    }
                    illnessContainer.getChildren().add(medicineHbox);
                    count++;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        new Thread(getIllness).start();
    }
}
