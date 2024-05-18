package com.easymed.controllers.doctor;

import com.easymed.database.models.User;
import com.easymed.database.services.DoctorService;
import com.easymed.utils.DatabaseReadCall;
import com.easymed.utils.FXMLScene;
import com.easymed.utils.Helpers;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.File;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {

    private final User user = User.getInstance();

    @FXML
    private Label spacialities;

    @FXML
    private Label designation;

    @FXML
    private Label education;

    @FXML
    private Label hospital;

    @FXML
    private Label hospital_address;

    @FXML
    private Label schedule;

    @FXML
    private Label experience;

    @FXML
    private Label fees;

    @FXML
    private Label bio;

    @FXML
    private Label name;

    @FXML
    private Label email;

    @FXML
    private Label userId;

    @FXML
    private ImageView profilePic;

    @FXML
    private Label phone;

    @FXML
    private Label dateOfBirth;

    @FXML
    private Label gender;

    @FXML
    private Label bloodGroup;

    @FXML
    private Label address;

    @FXML
    private Label city;

    @FXML
    private Label zip;

    @FXML
    private BorderPane rootPane;

    @FXML
    private GridPane profileContainer;

    @FXML
    void updateProfile(ActionEvent event) {
        FXMLScene.switchScene("/com/easymed/views/doctor/update-profile.fxml", (Node) event.getSource());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            VBox sidebar = (VBox) rootPane.getLeft();
            Helpers.toggleMenuClass(sidebar, "profile");
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setTitle(Helpers.getTitle("Profile"));
            DatabaseReadCall getInformation = DoctorService.getDoctorInformation(this.user.getId());
            setInformation(getInformation);
            new Thread(getInformation).start();
        });
    }

    /**
     * show information in profile page
     *
     * @param getInformation all information of a user
     */
    private void setInformation(DatabaseReadCall getInformation) {
        getInformation.setOnSucceeded(event -> {
            try {
                ResultSet resultSet = getInformation.getValue();
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String userName = resultSet.getString("name");
                    String userEmail = resultSet.getString("email");
                    Date userDob = resultSet.getDate("dob");
                    String userGender = resultSet.getString("gender");
                    String userPhone = resultSet.getString("phone");
                    String userPicture = resultSet.getString("picture");
                    String userBloodGroup = resultSet.getString("blood_group");
                    String userAddressJson = resultSet.getString("address");

                    // Additional fields
                    String bio = resultSet.getString("bio");
                    String specialties = resultSet.getString("spacialities");
                    String designation = resultSet.getString("designation");
                    String education = resultSet.getString("education");
                    String hospital = resultSet.getString("hospital");
                    String hospitalAddress = resultSet.getString("hospital_address");
                    String schedule = resultSet.getString("schedule");
                    String experience = resultSet.getString("experience");
                    String fees = resultSet.getString("fees");


                    this.userId.setText(String.valueOf(id != 0 ? id : "N/A"));
                    this.name.setText(userName != null ? userName : "N/A");
                    this.email.setText(userEmail != null ? userEmail : "N/A");
                    this.phone.setText(userPhone != null ? userPhone : "N/A");
                    this.dateOfBirth.setText(userDob != null ? userDob.toString() : "N/A");
                    this.gender.setText(userGender != null ? userGender : "N/A");
                    this.bloodGroup.setText(userBloodGroup != null ? userBloodGroup : "N/A");

                    // Additional fields
                    this.bio.setText(bio != null ? bio : "N/A");
                    this.spacialities.setText(specialties != null ? specialties : "N/A");
                    this.designation.setText(designation != null ? designation : "N/A");
                    this.education.setText(education != null ? education : "N/A");
                    this.hospital.setText(hospital != null ? hospital : "N/A");
                    this.hospital_address.setText(hospitalAddress != null ? hospitalAddress : "N/A");
                    this.schedule.setText(schedule != null ? schedule : "N/A");
                    this.experience.setText(experience != null ? experience : "N/A");
                    this.fees.setText(fees != null ? fees + "৳" : "N/A");

                    if (userPicture != null) {
                        File file = new File(userPicture);
                        if (file.exists()) {
                            Image image = new Image(file.toURI().toString());
                            profilePic.setImage(image);
                        } else profilePic.setImage(null);
                    }

                    if (userAddressJson != null && Helpers.isValidJson(userAddressJson)) {
                        JSONObject jsonObject = new JSONObject(userAddressJson);
                        String address = jsonObject.getString("address");
                        String city = jsonObject.getString("city");
                        String zip = String.valueOf(jsonObject.getInt("zip"));

                        this.address.setText(address);
                        this.city.setText(city);
                        this.zip.setText(zip);
                    }
                }
            } catch (SQLException e) {
                System.out.println("DoctorProfile: SQLException: " + e.getMessage());
            }
        });
    }
}
