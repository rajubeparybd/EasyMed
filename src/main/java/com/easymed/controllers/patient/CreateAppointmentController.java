package com.easymed.controllers.patient;

import com.easymed.database.models.User;
import com.easymed.database.services.AppointmentService;
import com.easymed.utils.DatabaseWriteCall;
import com.easymed.utils.FXMLScene;
import com.easymed.utils.Helpers;
import com.easymed.utils.Notification;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class CreateAppointmentController implements Initializable {

    private final User user = User.getInstance();
    @FXML
    private Label reasonWarning;
    @FXML
    private Label appointmentDateWarning;

    private Integer doctorId;
    @FXML
    private BorderPane rootPane;
    @FXML
    private TextArea reasonTextArea;
    @FXML
    private Button appointmentCreateButton;

    @FXML
    private DatePicker appointmentDateField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            VBox sidebar = (VBox) rootPane.getLeft();
            Helpers.toggleMenuClass(sidebar, "doctors");
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setTitle(Helpers.getTitle("Create Appointment"));
            reasonWarning.setVisible(false);
            appointmentDateWarning.setVisible(false);
        });
    }

    /**
     * CreateAppointment submit
     *
     * @param actionEvent Button Action
     */
    public void createAppointmentSubmit(ActionEvent actionEvent) {
        String text = this.reasonTextArea.getText();
        LocalDate appointmentDate = appointmentDateField.getValue();

        reasonWarning.setVisible(text.length() <= 20);
        appointmentDateWarning.setVisible(appointmentDate == null);

        if (text.length() >= 20 && appointmentDate != null) {
            DatabaseWriteCall createAppointment = AppointmentService.attemptAppointment(this.user.getId(), doctorId, text, appointmentDate.toString());
            createAppointment.setOnSucceeded(de -> {
                Notification.success("Success", "Appointment created successfully");
                FXMLScene.switchScene("/com/easymed/views/patient/doctors-list.fxml", (Node) actionEvent.getSource());
            });
            createAppointment.setOnFailed(de -> {
                Notification.error("Error", "Something went wrong. Please try again.");
            });
            new Thread(createAppointment).start();
        }
    }

    /**
     * For Symptoms Key Event
     *
     * @param keyEvent KeyEvent
     */
    public void reasonType(KeyEvent keyEvent) {
        reasonWarning.setVisible(this.reasonTextArea.getText().length() <= 20);
    }

    /**
     * Set Doctor id
     *
     * @param doctorId String
     */
    public void setDoctorId(String doctorId) {
        this.doctorId = Integer.valueOf(doctorId);
    }
}
