package com.easymed.controllers.doctor;

import com.easymed.database.models.User;
import com.easymed.database.services.AppointmentService;
import com.easymed.database.services.IllnessService;
import com.easymed.database.services.MedicineService;
import com.easymed.database.services.PrescriptionService;
import com.easymed.utils.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class CheckAppointmentController implements Initializable {
    private final User user = User.getInstance();
    private final Integer doctor_id = this.user.getId();

    private Integer patient_id;
    private Integer appointment_id;
    private String patientEmail;
    private String next_checkupDate;
    private String date_of_birth;
    private String patientProfile;
    private Integer prescription_id;

    @FXML
    private Label nameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label dobLabel;
    @FXML
    private ImageView profilePicLabel;
    @FXML
    private Button appointmentCreateButton;
    @FXML
    private BorderPane rootPane;
    @FXML
    private TextField illnessTextField;
    @FXML
    private GridPane illnessContainer;
    @FXML
    private DatePicker symptomsDatePicker;
    @FXML
    private GridPane medicineContainer;
    @FXML
    private TextField medicineNameField;
    @FXML
    private CheckBox morning;
    @FXML
    private CheckBox afternoon;
    @FXML
    private CheckBox night;
    @FXML
    private DatePicker nextCheckUpDate;
    @FXML
    private Label wrongIllness;
    @FXML
    private Label wrongMedicine;
    @FXML
    private Label warningCheckUpDate;

    private int fieldCount = 0;
    private int fieldCount2 = 0;
    private MedicineData[][] MedicineDataArray;
    private String[][] IllnessDataArray;

    /**
     * set Patient info
     *
     * @param appointmentId Integer appointment Id
     * @param patientEmail  String patient email
     * @param patientId     Integer patient Id
     */
    public void setPatientInfo(String patientName, Integer appointmentId, String patientEmail, Integer patientId, String dob, String patientProfile) {
        this.patientEmail = patientEmail;
        this.appointment_id = appointmentId;
        this.patient_id = patientId;
        this.date_of_birth = dob;
        this.patientProfile = patientProfile;

        nameLabel.setText(patientName);
        emailLabel.setText(patientEmail);
        dobLabel.setText(dob);

        if (patientProfile != null) {
            File file = new File(patientProfile);
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                profilePicLabel.setImage(image);
            } else profilePicLabel.setImage(null);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            VBox sidebar = (VBox) rootPane.getLeft();
            Helpers.toggleMenuClass(sidebar, "appointments");
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setTitle(Helpers.getTitle("See Patient"));

            wrongIllness.setVisible(false);
            wrongMedicine.setVisible(false);
            warningCheckUpDate.setVisible(false);
        });
    }

    /**
     * CreateAppointment submit
     *
     * @param actionEvent Button Action
     */
    public void createAppointmentSubmit(ActionEvent actionEvent) {
        LocalDate selectedDate = nextCheckUpDate.getValue();
        boolean nextCheckUpDataIsValid = true;
        if (selectedDate != null) {
            warningCheckUpDate.setVisible(false);
            this.next_checkupDate = selectedDate.toString();
        } else {
            warningCheckUpDate.setVisible(true);
            nextCheckUpDataIsValid = false;
            System.err.println("No date selected!");
        }
        forIllness();
        forMedicine();

        boolean illnessDataIsValid = true;
        for (String[] rowArray : IllnessDataArray) {
            String name = rowArray[0];
            String symptoms = rowArray[1];
            if (name == null || name.isEmpty() || symptoms == null || symptoms.isEmpty()) {
                illnessDataIsValid = false;
                break;
            }
        }

        boolean medicineDataIsValid = true;
        for (MedicineData[] rowArray : MedicineDataArray) {
            String medicineName = rowArray[0] != null ? String.valueOf(rowArray[0]) : "";
            if (medicineName.isEmpty()) {
                medicineDataIsValid = false;
                break;
            }
        }

        wrongIllness.setVisible(!illnessDataIsValid);
        wrongMedicine.setVisible(!medicineDataIsValid);
        if (illnessDataIsValid && medicineDataIsValid && nextCheckUpDataIsValid) {
            DatabaseWriteCall createPrescription = PrescriptionService.AttemptPrescription(patient_id, doctor_id, appointment_id, next_checkupDate);
            insertPrescription(createPrescription);

            FXMLScene.switchScene("/com/easymed/views/doctor/appointments-list.fxml", (Node) actionEvent.getSource());

        }


    }

    /**
     * Insert illness data
     *
     * @param insertIllness DatabaseWriteCall
     */
    public void insertIllnessData(DatabaseWriteCall insertIllness) {
        insertIllness.setOnSucceeded(de -> {
            Integer insertedRow = insertIllness.getValue();
            if (insertedRow > 0) {
                System.out.println("Illness inserted successfully");
            }
        });
        new Thread(insertIllness).start();
    }

    /**
     * Insert Medicine data
     *
     * @param insertMedicine DatabaseWriteCall
     */
    public void insertMedicineData(DatabaseWriteCall insertMedicine) {
        insertMedicine.setOnSucceeded(de -> {
            Integer insertedRow = insertMedicine.getValue();
            if (insertedRow > 0) {
                System.out.println("Medicine inserted successfully");
            }
        });
        new Thread(insertMedicine).start();
    }

    /**
     * get Prescription ID
     *
     * @param getPrescription DatabaseReadCall
     */
    private void getPrescriptionId(DatabaseReadCall getPrescription) {
        getPrescription.setOnSucceeded(event -> {
            ResultSet resultSet = getPrescription.getValue();
            try {
                if (resultSet.next()) {
                    Integer app_id = resultSet.getInt("id");
                    this.prescription_id = resultSet.getInt("id");

                    for (String[] rowArray : IllnessDataArray) {
                        String name = rowArray[0];
                        String symptoms = rowArray[1];
                        DatabaseWriteCall insertIllness = IllnessService.AttemptIllness(patient_id, prescription_id, name, symptoms);
                        insertIllnessData(insertIllness);
                    }
                    for (MedicineData[] rowArray : MedicineDataArray) {
                        String medicineName = rowArray[0] != null ? rowArray[0].medicineName() : "";
                        boolean morningDosage = rowArray[1] != null && rowArray[1].morningDosage();
                        boolean afternoonDosage = rowArray[2] != null && rowArray[2].afternoonDosage();
                        boolean nightDosage = rowArray[3] != null && rowArray[3].nightDosage();
                        DatabaseWriteCall insertMedicine = MedicineService.AttemptMedicine(patient_id, prescription_id, medicineName, morningDosage, afternoonDosage, nightDosage);
                        insertMedicineData(insertMedicine);
                    }
                } else {
                    System.out.println("Null");
                }
            } catch (SQLException e) {
                System.out.println("Error in PatientService: " + e.getMessage());
                throw new RuntimeException(e);
            }
        });
        new Thread(getPrescription).start();
    }

    /**
     * Update Appointment
     *
     * @param updateAppointment DatabaseWriteCall
     */
    public void updateAppointmentById(DatabaseWriteCall updateAppointment) {
        updateAppointment.setOnSucceeded(de -> {
            Integer insertedRow = updateAppointment.getValue();
            if (insertedRow > 0) {
                System.out.println("Appointment Update successfully");
            }
        });
        updateAppointment.setOnFailed(de -> {
            Notification.error("Error", "Something went wrong. Please try again.");
        });
        new Thread(updateAppointment).start();
    }

    /**
     * insert data into prescription table
     */
    public void insertPrescription(DatabaseWriteCall createPrescription) {
        createPrescription.setOnSucceeded(de -> {
            Integer insertedRow = createPrescription.getValue();
            if (insertedRow > 0) {
                System.out.println("Prescription Created successfully");
                DatabaseReadCall getPrescription = PrescriptionService.getPrescriptionByAppointmentId(appointment_id);
                getPrescriptionId(getPrescription);
                DatabaseWriteCall updateAppointment = AppointmentService.updateAppointment(appointment_id);
                updateAppointmentById(updateAppointment);
            }
        });
        createPrescription.setOnFailed(de -> {
            Notification.error("Error", "Something went wrong. Please try again.");
        });
        new Thread(createPrescription).start();
    }

    /**
     * for illness
     */
    private void forIllness() {
        int numRows = -1;
        int numCols = -1;

        for (Node node : illnessContainer.getChildren()) {
            if (node instanceof TextField textField) {
                int rowIndex = GridPane.getRowIndex(node);
                int colIndex = GridPane.getColumnIndex(node);
                if (rowIndex > numRows) {
                    numRows = rowIndex;
                }
                if (colIndex > numCols) {
                    numCols = colIndex;
                }
            }
            if (node instanceof DatePicker datePicker) {
                int rowIndex = GridPane.getRowIndex(node);
                int colIndex = GridPane.getColumnIndex(node);
                if (rowIndex > numRows) {
                    numRows = rowIndex;
                }
                if (colIndex > numCols) {
                    numCols = colIndex;
                }
            }
        }
        numRows++;
        numCols++;

        IllnessDataArray = new String[numRows][numCols];

        for (Node node : illnessContainer.getChildren()) {
            if (node instanceof TextField textField) {
                int rowIndex = GridPane.getRowIndex(node);
                int colIndex = GridPane.getColumnIndex(node);
                String textValue = textField.getText();
                if (!textValue.isEmpty()) {
                    IllnessDataArray[rowIndex][colIndex] = textValue;
                }
            }
            if (node instanceof DatePicker datePicker) {
                int rowIndex = GridPane.getRowIndex(node);
                int colIndex = GridPane.getColumnIndex(node);
                LocalDate dateValue = datePicker.getValue();
                if (dateValue != null) {
                    IllnessDataArray[rowIndex][colIndex] = dateValue.toString();
                }
            }
        }
    }

    /**
     * for medicine
     */
    private void forMedicine() {
        int numRows = -1;
        int numCols = 4;

        for (Node node : medicineContainer.getChildren()) {
            if (node instanceof TextField textField) {
                int rowIndex = GridPane.getRowIndex(node);
                if (rowIndex > numRows) {
                    numRows = rowIndex;
                }
            }
        }
        numRows++;

        MedicineDataArray = new MedicineData[numRows][numCols];

        for (Node node : medicineContainer.getChildren()) {
            if (node instanceof TextField textField) {
                int rowIndex = GridPane.getRowIndex(node);
                int colIndex = GridPane.getColumnIndex(node);
                String textValue = textField.getText();
                if (!textValue.isEmpty()) {
                    MedicineDataArray[rowIndex][colIndex] = new MedicineData(textValue, false, false, false);
                }
            }
            if (node instanceof CheckBox checkBox) {
                int rowIndex = GridPane.getRowIndex(node);
                int colIndex = GridPane.getColumnIndex(node);
                boolean isSelected = checkBox.isSelected();
                switch (colIndex) {
                    case 1:
                        MedicineDataArray[rowIndex][colIndex] = new MedicineData("", isSelected, false, false);
                        break;
                    case 2:
                        MedicineDataArray[rowIndex][colIndex] = new MedicineData("", false, isSelected, false);
                        break;
                    case 3:
                        MedicineDataArray[rowIndex][colIndex] = new MedicineData("", false, false, isSelected);
                        break;
                }
            }
        }
    }

    /**
     * Add more illness
     *
     * @param actionEvent button action
     */
    public void addMoreIllness(ActionEvent actionEvent) {
        TextField nameTextField = new TextField();
        nameTextField.setPromptText("Common Cold");
        nameTextField.setPrefHeight(34);
        nameTextField.setPrefWidth(240);

        DatePicker symptomsTextField = new DatePicker();
        symptomsTextField.setPromptText("5/7/2024");
        symptomsTextField.setPrefHeight(34);
        symptomsTextField.setPrefWidth(222);

        Button removeButton = new Button("-");
        removeButton.setPrefHeight(34);
        removeButton.setPrefWidth(70);
        removeButton.getStyleClass().add("btn3");

        removeButton.setOnAction(event -> {
            int buttonIndex = illnessContainer.getChildren().indexOf(removeButton);
            int rowToRemove = buttonIndex / 3;

            Integer rowIndexObj = GridPane.getRowIndex(removeButton);
            if (rowIndexObj != null) {
                int rowIndex = rowIndexObj;

                if (rowIndex == rowToRemove) {
                    illnessContainer.getChildren().removeAll(nameTextField, symptomsTextField, removeButton);
                    for (Node node : illnessContainer.getChildren()) {
                        Integer nodeRowIndex = GridPane.getRowIndex(node);
                        if (nodeRowIndex != null && nodeRowIndex > rowToRemove) {
                            GridPane.setRowIndex(node, nodeRowIndex - 1);
                        }
                    }
                    fieldCount--;
                }
            }
        });

        int newRow = fieldCount + 1;
        illnessContainer.addRow(newRow, nameTextField, symptomsTextField, removeButton);
        fieldCount++;
    }

    /**
     * Add more Medicine
     *
     * @param actionEvent button action
     */
    public void addMoreMedicine(ActionEvent actionEvent) {
        TextField medicineNameField = new TextField();
        medicineNameField.setPromptText("Coldrex");
        medicineNameField.setPrefHeight(34);

        CheckBox morning = new CheckBox("D");
        CheckBox afternoon = new CheckBox("L");
        CheckBox night = new CheckBox("N");

        Button removeButton2 = new Button("-");
        removeButton2.setPrefHeight(34);
        removeButton2.setPrefWidth(70);
        removeButton2.getStyleClass().add("btn3");

        removeButton2.setOnAction(event -> {
            int buttonIndex = medicineContainer.getChildren().indexOf(removeButton2);
            int rowToRemove = buttonIndex / 5;

            Integer rowIndexObj = GridPane.getRowIndex(removeButton2);
            if (rowIndexObj != null) {
                int rowIndex = rowIndexObj;

                if (rowIndex == rowToRemove) {
                    medicineContainer.getChildren().removeAll(medicineNameField, morning, afternoon, night, removeButton2);
                    for (Node node : medicineContainer.getChildren()) {
                        Integer nodeRowIndex = GridPane.getRowIndex(node);
                        if (nodeRowIndex != null && nodeRowIndex > rowToRemove) {
                            GridPane.setRowIndex(node, nodeRowIndex - 1);
                        }
                    }
                    fieldCount2--;
                }
            }
        });

        int newRow = fieldCount2 + 1;
        medicineContainer.addRow(newRow, medicineNameField, morning, afternoon, night, removeButton2);
        fieldCount2++;
    }

    /**
     * Medicine Data Record
     *
     * @param medicineName
     * @param morningDosage
     * @param afternoonDosage
     * @param nightDosage
     */
    public record MedicineData(String medicineName, boolean morningDosage, boolean afternoonDosage,
                               boolean nightDosage) {
    }
}