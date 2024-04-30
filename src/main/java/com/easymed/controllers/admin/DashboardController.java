package com.easymed.controllers.admin;

import com.easymed.database.models.User;
import com.easymed.database.services.UserService;
import com.easymed.enums.Role;
import com.easymed.utils.DatabaseReadCall;
import com.easymed.utils.GreetingMaker;
import com.easymed.utils.Helpers;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    private final User user = User.getInstance();

    @FXML
    private AreaChart<String, Integer> doctorsChart;

    @FXML
    private AreaChart<String, Integer> patientsChart;

    @FXML
    private ImageView profilePic;

    @FXML
    private Label numOfPatients;

    @FXML
    private Label numOfDoctors;

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


    /**
     * Set data to the controller from the previous controller
     *
     * @param data data to be set
     */
    public void setData(HashMap<String, String> data) {
        this.user.setId(Integer.valueOf(data.get("id")));
        this.user.setName(data.get("name"));
        this.user.setEmail(data.get("email"));
        this.user.setProfilePic(data.get("picture"));
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
            if (this.user.getProfilePic() != null) {
                File file = new File(this.user.getProfilePic());
                if (file.exists()) {
                    Image image = new Image(file.toURI().toString());
                    profilePic.setImage(image);
                } else
                    profilePic.setImage(null);
            }

            // Set the number of Doctors
            DatabaseReadCall getNumOfDoctors = UserService.getNumberOfUsers(Role.getText(Role.DOCTOR));
            getNumOfDoctors.setOnSucceeded(event -> {
                try {
                    if (getNumOfDoctors.getValue().next())
                        numOfDoctors.setText(getNumOfDoctors.getValue().getString("count"));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            getNumOfDoctors.setOnFailed(event -> {
                numOfDoctors.setText("0");
            });
            new Thread(getNumOfDoctors).start();

            // Set the number of Patients
            DatabaseReadCall getNumOfPatients = UserService.getNumberOfUsers(Role.getText(Role.PATIENT));
            getNumOfPatients.setOnSucceeded(event -> {
                try {
                    if (getNumOfPatients.getValue().next())
                        numOfPatients.setText(getNumOfPatients.getValue().getString("count"));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            getNumOfPatients.setOnFailed(event -> {
                numOfPatients.setText("0");
            });
            new Thread(getNumOfPatients).start();

            // Set the data for the doctor chart
            DatabaseReadCall getDoctorChartData = UserService.getChartData(Role.getText(Role.DOCTOR));
            getDoctorChartData.setOnSucceeded(event -> {
                try {
                    XYChart.Series<String, Integer> series = new XYChart.Series<>();
                    while (getDoctorChartData.getValue().next()) {
                        Integer count = getDoctorChartData.getValue().getInt("count");
                        Date date = getDoctorChartData.getValue().getDate("date");
                        String formattedDate = Helpers.getDate(date, "MM-dd");
                        series.getData().add(new XYChart.Data<>(formattedDate, count));
                    }
                    doctorsChart.getXAxis().setLabel("Registration Date");
                    doctorsChart.getYAxis().setLabel("Number of Doctors");
                    doctorsChart.getXAxis().setTickLabelRotation(-30);
                    doctorsChart.getXAxis().setAnimated(false);
                    doctorsChart.getData().add(series);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            new Thread(getDoctorChartData).start();

            // Set the data for the patient chart
            DatabaseReadCall getPatientChartData = UserService.getChartData(Role.getText(Role.PATIENT));
            getPatientChartData.setOnSucceeded(event -> {
                try {
                    XYChart.Series<String, Integer> series = new XYChart.Series<>();
                    while (getPatientChartData.getValue().next()) {
                        Integer count = getPatientChartData.getValue().getInt("count");
                        Date date = getPatientChartData.getValue().getDate("date");
                        String formattedDate = Helpers.getDate(date, "MM-dd");
                        series.getData().add(new XYChart.Data<>(formattedDate, count));
                    }
                    patientsChart.getXAxis().setLabel("Registration Date");
                    patientsChart.getYAxis().setLabel("Number of Patients");
                    patientsChart.getXAxis().setTickLabelRotation(-30);
                    patientsChart.getXAxis().setAnimated(false);
                    patientsChart.getData().add(series);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            new Thread(getPatientChartData).start();
        });
    }
}
