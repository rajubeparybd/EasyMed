package com.easymed.controllers.patient;

import com.easymed.database.models.User;
import com.easymed.database.services.DoctorService;
import com.easymed.database.services.MedicineService;
import com.easymed.database.services.PrescriptionService;
import com.easymed.database.services.UserService;
import com.easymed.utils.DatabaseReadCall;
import com.easymed.utils.GreetingMaker;
import com.easymed.utils.Helpers;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    private final User user = User.getInstance();

    @FXML
    private VBox contentArea;
    @FXML
    private Label welcomeText;
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
    private Label ageLabel;
    @FXML
    private Label bloodGroupLabel;
    @FXML
    private Button findBloodButton;
    @FXML
    private Label appointDoctor;
    @FXML
    private Label appointHospital;
    @FXML
    private Label appointTime;
    @FXML
    private Label medName;
    @FXML
    private Label Medtime;
    @FXML
    private ImageView userImage;
    @FXML
    private VBox medicineContainer;


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

    /**
     * Switches the scene to the blood banks view when the find blood button is clicked
     *
     * @param event find blood button click event
     */
    @FXML
    void findBlood(ActionEvent event) {
        //TODO: Implement the find blood functionality
        System.out.println("Find Blood");
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

            DatabaseReadCall getUserInfo = UserService.getUserInfo(this.user.getId(), this.user.getEmail());
            setUserUiInfo(getUserInfo);

            DatabaseReadCall getNextDate = PrescriptionService.getRecentPrescription(this.user.getId());
            getNextDate.setOnSucceeded(event -> {
                ResultSet resultSet = getNextDate.getValue();
                try {
                    if (resultSet.next()) {
                        appointTime.setText(resultSet.getString("next_checkup"));

                        DatabaseReadCall getDoctorInfo = DoctorService.getDoctorInformation(resultSet.getInt("doctor_id"));
                        setDoctorUiInfo(getDoctorInfo);

                        DatabaseReadCall getMedicine = MedicineService.getMedicine(this.user.getId(), resultSet.getInt("id"));
                        loadMedicine(getMedicine);

                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            new Thread(getNextDate).start();

        });


    }

    /**
     * set user info
     *
     * @param getUserInfo DatabaseReadCall
     */
    private void setUserUiInfo(DatabaseReadCall getUserInfo) {
        getUserInfo.setOnSucceeded(event -> {
            ResultSet resultSet = getUserInfo.getValue();
            try {
                if (resultSet.next()) {
                    String profilePicture = resultSet.getString("picture");
                    if (profilePicture != null) {
                        File file = new File(profilePicture);
                        if (file.exists()) {
                            Image image = new Image(file.toURI().toString());
                            userImage.setImage(image);
                        } else
                            userImage.setImage(null);
                    }
                    //ageLabel.setText(resultSet.getString("dob"));
                    Date dateOfBirth = resultSet.getDate("dob");
                    LocalDate dob = dateOfBirth.toLocalDate();
                    LocalDate currentDate = LocalDate.now();
                    int age = Period.between(dob, currentDate).getYears();
                    ageLabel.setText(String.valueOf(age));

                    bloodGroupLabel.setText(resultSet.getString("blood_group"));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        new Thread(getUserInfo).start();
    }

    /**
     * set doctor ui info
     *
     * @param getDoctorInfo DatabaseReadCall
     */
    private void setDoctorUiInfo(DatabaseReadCall getDoctorInfo) {
        getDoctorInfo.setOnSucceeded(event -> {
            ResultSet resultSet = getDoctorInfo.getValue();
            try {
                if (resultSet.next()) {
                    appointDoctor.setText(resultSet.getString("u.name"));
                    appointHospital.setText(resultSet.getString("hospital"));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        new Thread(getDoctorInfo).start();
    }

    /**
     * load recent Medicine
     *
     * @param getMedicine DatabaseReadCall
     */
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
                    medicineHbox.setPrefWidth(900);
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

}

