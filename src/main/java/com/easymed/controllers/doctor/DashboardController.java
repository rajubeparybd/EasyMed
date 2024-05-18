package com.easymed.controllers.doctor;

import com.easymed.database.models.User;
import com.easymed.database.services.AppointmentService;
import com.easymed.utils.DatabaseReadCall;
import com.easymed.utils.GreetingMaker;
import com.easymed.utils.Helpers;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    private final User user = User.getInstance();

    @FXML
    private TextField searchBox;

    @FXML
    private VBox appointmentListContainer;

    @FXML
    private Label patients;

    @FXML
    private BorderPane rootPane;

    @FXML
    private Label date;

    @FXML
    private Label greetings;

    @FXML
    private Rectangle img;

    @FXML
    private Label name;

    @FXML
    private AreaChart<String, Integer> appointmentChart;
    @FXML
    private AreaChart<String, Integer> patientChart;


    /**
     * Set data to the controller from the previous controller
     *
     * @param data data to be set
     */
    public void setData(HashMap<String, String> data) {
        this.user.setId(Integer.valueOf(data.get("id")));
        this.user.setName(data.get("name"));
        this.user.setEmail(data.get("email"));
        this.user.setRole(data.get("role"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            VBox sidebar = (VBox) rootPane.getLeft();
            Helpers.toggleMenuClass(sidebar, "dashboard");

            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setTitle(user.getName() + " " + Helpers.getTitle("Dashboard"));
            greetings.setText((new GreetingMaker()).printTimeOfDay());
            name.setText(this.user.getName());
            String dateString = Helpers.getDate();
            String timeString = Helpers.getTime();
            date.setText(dateString + " | " + timeString);
            
            DatabaseReadCall countToDaysAppointment = AppointmentService.countAppointments(this.user.getId());
            countAppointments(countToDaysAppointment);

            loadAppointmentListContainerCards();

            loadAppointmentChart();
            loadPatientChart();

        });
    }

    /**
     * Load Appointment card
     */
    public void loadAppointmentListContainerCards() {
        DatabaseReadCall ToDaysAppointment = AppointmentService.getToDaysAppointments(this.user.getId());
        SetAppointmentListContainerCards(ToDaysAppointment);
    }

    /**
     * set Appointment card information
     *
     * @param ToDaysAppointment DatabaseReadCall
     */
    public void SetAppointmentListContainerCards(DatabaseReadCall ToDaysAppointment) {
        ToDaysAppointment.setOnSucceeded(event -> {
            ResultSet resultSet = ToDaysAppointment.getResultSet();
            try {
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String email = resultSet.getString("email");
                    String dob = resultSet.getString("dob");
                    String patientProfile = resultSet.getString("picture");
                    Integer appointmentId = resultSet.getInt("id");
                    Integer patientId = resultSet.getInt("patient_id");


                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/easymed/views/doctor/appointment-card.fxml"));
                    AnchorPane appointmentCardRoot = loader.load();
                    AppointmentCardController appointmentCardController = loader.getController();
                    VBox appointmentCard = new VBox(appointmentCardRoot);
                    appointmentCardController.setUserInfo(name, email, patientProfile, dob, patientId, appointmentId);
                    appointmentListContainer.getChildren().add(appointmentCard);
                }
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        });
        new Thread(ToDaysAppointment).start();
    }

    /**
     * count to days Appointment
     *
     * @param countToDaysAppointment DatabaseReadCall
     */
    public void countAppointments(DatabaseReadCall countToDaysAppointment) {
        countToDaysAppointment.setOnSucceeded(event -> {
            ResultSet resultset = countToDaysAppointment.getValue();
            try {
                if (resultset.next()) {
                    patients.setText(String.valueOf(resultset.getInt(1)));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        new Thread(countToDaysAppointment).start();
    }

    /**
     * Search for a patient
     *
     * @param keyEvent key event
     */
    @FXML
    public void search(KeyEvent keyEvent) {
        if (!this.searchBox.getText().isEmpty()) {
            appointmentListContainer.getChildren().clear();
            DatabaseReadCall ToDaysAppointment = AppointmentService.getToDaysAppointments(this.user.getId(), this.searchBox.getText());
            SetAppointmentListContainerCards(ToDaysAppointment);
        } else {
            appointmentListContainer.getChildren().clear();
            loadAppointmentListContainerCards();
        }
    }

    /**
     * Load patient Chart
     */
    public void loadPatientChart() {
        DatabaseReadCall getDoctorChartData = AppointmentService.getChartDataForPatients(this.user.getId());
        getDoctorChartData.setOnSucceeded(event -> {
            try {
                XYChart.Series<String, Integer> series = new XYChart.Series<>();
                while (getDoctorChartData.getValue().next()) {
                    Integer count = getDoctorChartData.getValue().getInt("count");
                    Date date = getDoctorChartData.getValue().getDate("date");
                    String formattedDate = Helpers.getDate(date, "MM-dd");
                    series.getData().add(new XYChart.Data<>(formattedDate, count));
                }
                patientChart.getXAxis().setLabel("Created Date");
                patientChart.getYAxis().setLabel("No. of Patient");
                patientChart.getXAxis().setTickLabelRotation(-30);
                patientChart.getXAxis().setAnimated(false);
                patientChart.getData().add(series);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        new Thread(getDoctorChartData).start();
    }

    /**
     * Load Appointment Chart
     */
    public void loadAppointmentChart() {
        DatabaseReadCall getDoctorChartData = AppointmentService.getChartDataForAppointments(this.user.getId());
        getDoctorChartData.setOnSucceeded(event -> {
            try {
                XYChart.Series<String, Integer> series = new XYChart.Series<>();
                while (getDoctorChartData.getValue().next()) {
                    Integer count = getDoctorChartData.getValue().getInt("count");
                    Date date = getDoctorChartData.getValue().getDate("date");
                    String formattedDate = Helpers.getDate(date, "MM-dd");
                    series.getData().add(new XYChart.Data<>(formattedDate, count));
                }
                appointmentChart.getXAxis().setLabel("Created Date");
                appointmentChart.getYAxis().setLabel("No. of Appointments");
                appointmentChart.getXAxis().setTickLabelRotation(-30);
                appointmentChart.getXAxis().setAnimated(false);
                appointmentChart.getData().add(series);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        new Thread(getDoctorChartData).start();
    }
}
