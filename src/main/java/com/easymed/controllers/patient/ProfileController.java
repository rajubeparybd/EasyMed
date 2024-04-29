package com.easymed.controllers.patient;

import com.easymed.database.models.User;
import com.easymed.database.services.UserService;
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
        FXMLScene.switchScene("/com/easymed/views/patient/update-profile.fxml", (Node) event.getSource());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            VBox sidebar = (VBox) rootPane.getLeft();
            Helpers.toggleMenuClass(sidebar, "profile");
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setTitle(Helpers.getTitle("Profile"));

            DatabaseReadCall getUserInformation = UserService.getUserInfo(this.user.getId(), this.user.getEmail());
            setUserInformation(getUserInformation);
            new Thread(getUserInformation).start();
        });
    }

    /**
     * show information in profile page
     *
     * @param getUserInformation all information of a user
     */
    private void setUserInformation(DatabaseReadCall getUserInformation) {
        getUserInformation.setOnSucceeded(event -> {
            try {
                ResultSet resultSet = getUserInformation.getValue();
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

                    this.userId.setText(String.valueOf(id != 0 ? id : "N/A"));
                    this.name.setText(userName != null ? userName : "N/A");
                    this.email.setText(userEmail != null ? userEmail : "N/A");
                    this.phone.setText(userPhone != null ? userPhone : "N/A");
                    this.dateOfBirth.setText(userDob != null ? userDob.toString() : "N/A");
                    this.gender.setText(userGender != null ? userGender : "N/A");
                    this.bloodGroup.setText(userBloodGroup != null ? userBloodGroup : "N/A");

                    if (userPicture != null) {
                        File file = new File(userPicture);
                        if (file.exists()) {
                            Image image = new Image(file.toURI().toString());
                            profilePic.setImage(image);
                        } else
                            profilePic.setImage(null);
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
                System.out.println("PatientController: SQLException: " + e.getMessage());
            }
        });
    }
}
