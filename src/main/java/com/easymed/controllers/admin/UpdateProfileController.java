package com.easymed.controllers.admin;

import com.easymed.database.models.User;
import com.easymed.database.services.AuthService;
import com.easymed.database.services.UserService;
import com.easymed.utils.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.File;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Objects;
import java.util.ResourceBundle;

public class UpdateProfileController implements Initializable {

    private final User user = User.getInstance();

    @FXML
    private TextField name;
    @FXML
    private Label nameValidationFeedback;
    @FXML
    private ImageView nameWrong;
    @FXML
    private ImageView nameSuccess;

    @FXML
    private TextField email;
    @FXML
    private Label emailValidationFeedback;
    @FXML
    private ImageView emailSuccess;
    @FXML
    private ImageView emailWrong;

    @FXML
    private TextField password;
    @FXML
    private Label passwordValidationFeedback;
    @FXML
    private ImageView passwordSuccess;
    @FXML
    private ImageView passwordWrong;

    @FXML
    private TextField phone;
    @FXML
    private Label phoneValidationFeedback;
    @FXML
    private ImageView phoneWrong;
    @FXML
    private ImageView phoneSuccess;


    @FXML
    private ComboBox<String> bloodGroup;
    @FXML
    private Label bloodGroupValidationFeedback;
    @FXML
    private ImageView bloodGroupWrong;
    @FXML
    private ImageView bloodGroupSuccess;

    @FXML
    private TextField address;
    @FXML
    private Label addressValidationFeedback;
    @FXML
    private ImageView addressWrong;
    @FXML
    private ImageView addressSuccess;


    @FXML
    private TextField city;
    @FXML
    private Label cityValidationFeedback;
    @FXML
    private ImageView citySuccess;
    @FXML
    private ImageView cityWrong;

    @FXML
    private TextField zip;
    @FXML
    private Label zipValidationFeedback;
    @FXML
    private ImageView zipSuccess;
    @FXML
    private ImageView zipWrong;

    @FXML
    private DatePicker dob;
    @FXML
    private Label dobValidationFeedback;
    @FXML
    private ImageView dobSuccess;
    @FXML
    private ImageView dobWrong;

    @FXML
    private ComboBox<String> gender;
    @FXML
    private Label genderValidationFeedback;
    @FXML
    private ImageView genderSuccess;
    @FXML
    private ImageView genderWrong;

    @FXML
    private TextField profilePicture;
    @FXML
    private Label profilePictureValidationFeedback;
    @FXML
    private ImageView profilePictureSuccess;
    @FXML
    private ImageView profilePictureWrong;
    @FXML
    private Button uploadImageButton;
    @FXML
    private ImageView imgProfile;

    @FXML
    private BorderPane rootPane;

    @FXML
    private Button updateProfileButton;

    private Boolean isUserExistsByEmail = false;
    private String dbPassword;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            VBox sidebar = (VBox) rootPane.getLeft();
            Helpers.toggleMenuClass(sidebar, "profile");
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setTitle(Helpers.getTitle("Update Profile"));

            Helpers.setGenderComboBox(gender);
            Helpers.setBloodGroupComboBox(bloodGroup);


            nameValidationFeedback.setVisible(false);
            nameSuccess.setVisible(false);
            nameWrong.setVisible(false);

            emailValidationFeedback.setVisible(false);
            emailSuccess.setVisible(false);
            emailWrong.setVisible(false);

            passwordValidationFeedback.setVisible(false);
            passwordSuccess.setVisible(false);
            passwordWrong.setVisible(false);

            phoneValidationFeedback.setVisible(false);
            phoneSuccess.setVisible(false);
            phoneWrong.setVisible(false);

            genderValidationFeedback.setVisible(false);
            genderSuccess.setVisible(false);
            genderWrong.setVisible(false);

            bloodGroupValidationFeedback.setVisible(false);
            bloodGroupSuccess.setVisible(false);
            bloodGroupWrong.setVisible(false);

            addressValidationFeedback.setVisible(false);
            addressSuccess.setVisible(false);
            addressWrong.setVisible(false);

            cityValidationFeedback.setVisible(false);
            citySuccess.setVisible(false);
            cityWrong.setVisible(false);

            zipValidationFeedback.setVisible(false);
            zipSuccess.setVisible(false);
            zipWrong.setVisible(false);

            dobValidationFeedback.setVisible(false);
            dobSuccess.setVisible(false);
            dobWrong.setVisible(false);

            profilePictureValidationFeedback.setVisible(false);
            profilePictureSuccess.setVisible(false);
            profilePictureWrong.setVisible(false);
        });

        DatabaseReadCall getUserInformation = UserService.getUserInfo(this.user.getId(), this.user.getEmail());
        setUserInformation(getUserInformation);
        new Thread(getUserInformation).start();

        email.focusedProperty().addListener((observableValue, focusOut, onFocused) -> {
            if (!onFocused && email.getText().isEmpty()) {
                Validations.inputIsInvalid(emailSuccess, emailWrong, emailValidationFeedback, "Email Address is required");
            }
            if (focusOut && !email.getText().isEmpty() && Validations.isEmailValid(email.getText())) {
                DatabaseReadCall databaseReadCall = AuthService.checkUserExistsByEmail(email.getText());
                databaseReadCall.setOnSucceeded(event -> {
                    try {
                        if (databaseReadCall.getValue().next()) {
                            String userEmail = databaseReadCall.getValue().getString("email");
                            if (Objects.equals(userEmail, this.user.getEmail())) {
                                Validations.inputIsValid(emailWrong, emailSuccess, emailValidationFeedback);
                                isUserExistsByEmail = false;
                            } else {
                                Validations.inputIsInvalid(emailSuccess, emailWrong, emailValidationFeedback, "Email Address already exists");
                                isUserExistsByEmail = true;
                            }
                        } else {
                            Validations.inputIsValid(emailWrong, emailSuccess, emailValidationFeedback);
                            isUserExistsByEmail = false;
                        }
                    } catch (SQLException e) {
                        Notification.error("Error", "Something went wrong. Please try again.");
                    }
                });
                databaseReadCall.setOnFailed(event -> {
                    Notification.error("Error", "Something went wrong. Please try again.");
                });

                new Thread(databaseReadCall).start();
            } else {
                Validations.inputIsInvalid(emailSuccess, emailWrong, emailValidationFeedback, "Invalid Email Address");
            }
        });

        addEventListener(phone, phoneValidationFeedback, phoneSuccess, phoneWrong);
        addEventListener(name, nameValidationFeedback, nameSuccess, nameWrong);
        addEventListener(address, addressValidationFeedback, addressSuccess, addressWrong);
        addEventListener(city, cityValidationFeedback, citySuccess, cityWrong);
        addEventListener(zip, zipValidationFeedback, zipSuccess, zipWrong);
        addEventListener(profilePicture, profilePictureValidationFeedback, profilePictureSuccess, profilePictureWrong);
        addEventListener(dob, dobValidationFeedback, dobSuccess, dobWrong);
        addEventListener(gender, genderValidationFeedback, genderSuccess, genderWrong);
        addEventListener(bloodGroup, bloodGroupValidationFeedback, bloodGroupSuccess, bloodGroupWrong);
    }

    /**
     * show feedback , warning and success for input
     *
     * @param input    Combobox selected  Combobox input value
     * @param feedback Label it is a feedback message label
     * @param success  ImageView it is a success logo
     * @param failed   ImageView it is a failed logo
     */
    private void addEventListener(ComboBox<String> input, Label feedback, ImageView success, ImageView failed) {
        input.focusedProperty().addListener((observableValue, focusOut, onFocused) -> {
            if (onFocused && input.getValue() != null) {
                feedback.setVisible(false);
                success.setVisible(true);
                failed.setVisible(false);
            }
            if (focusOut && input.getValue() == null) {
                feedback.setVisible(true);
                success.setVisible(true);
                failed.setVisible(true);
            }
            input.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    feedback.setVisible(false);
                    success.setVisible(true);
                    failed.setVisible(false);
                }
            });
        });

    }

    /**
     * show feedback , warning and success for input
     *
     * @param input    TextField input value
     * @param feedback Label it is a feedback message label
     * @param success  ImageView it is a success logo
     * @param failed   ImageView it is a failed logo
     */
    private void addEventListener(TextField input, Label feedback, ImageView success, ImageView failed) {
        input.focusedProperty().addListener((observableValue, focusOut, onFocused) -> {
            if (onFocused && input.getText().isEmpty()) {
                feedback.setVisible(false);
                success.setVisible(true);
                failed.setVisible(false);
            }
            if (focusOut && input.getText().isEmpty()) {
                feedback.setVisible(true);
                success.setVisible(true);
                failed.setVisible(true);
            }
        });
    }

    /**
     * show feedback , warning and success for input
     *
     * @param input    DatePicker value
     * @param feedback Label it is a feedback message label
     * @param success  ImageView it is a success logo
     * @param failed   ImageView it is a failed logo
     */
    private void addEventListener(DatePicker input, Label feedback, ImageView success, ImageView failed) {
        input.focusedProperty().addListener((observableValue, focusOut, onFocused) -> {
            if (onFocused && input.getValue() == null) {
                feedback.setVisible(false);
                success.setVisible(true);
                failed.setVisible(false);
            }
            if (focusOut && input.getValue() == null) {
                feedback.setVisible(true);
                success.setVisible(true);
                failed.setVisible(true);
            }
        });
    }


    /**
     * nameType method validate name while type in the name input box
     *
     * @param keyEvent Type Event
     */
    public void nameType(KeyEvent keyEvent) {
        if (name.getText().isEmpty()) {
            Validations.inputIsInvalid(nameSuccess, nameWrong, nameValidationFeedback, "Name is required");
        } else if (!Validations.isNameValid(name.getText())) {
            Validations.inputIsInvalid(nameSuccess, nameWrong, nameValidationFeedback, "Invalid Name");
        } else {
            Validations.inputIsValid(nameWrong, nameSuccess, nameValidationFeedback);
        }
    }

    /**
     * emailType method validate email address while type in the email input box
     *
     * @param keyEvent Type Event
     */
    public void emailType(KeyEvent keyEvent) {
        if (email.getText().isEmpty()) {
            Validations.inputIsInvalid(emailSuccess, emailWrong, emailValidationFeedback, "Email Address is required");
        } else if (!Validations.isEmailValid(email.getText())) {
            Validations.inputIsInvalid(emailSuccess, emailWrong, emailValidationFeedback, "Invalid Email Address");
        } else {
            Validations.inputIsValid(emailWrong, emailSuccess, emailValidationFeedback);
        }
    }

    /**
     * passwordType method validate password address while type in the password input box
     *
     * @param keyEvent Type Event
     */
    public void passwordType(KeyEvent keyEvent) {
        if (password.getText().isEmpty()) {
            this.passwordSuccess.setVisible(false);
            this.passwordWrong.setVisible(false);
            this.passwordValidationFeedback.setVisible(false);
        } else if (!Validations.isPasswordValid(password.getText())) {
            Validations.inputIsInvalid(passwordSuccess, passwordWrong, passwordValidationFeedback, "Password must be at least 6 characters long");
        } else {
            Validations.inputIsValid(passwordWrong, passwordSuccess, passwordValidationFeedback);
        }
    }

    /**
     * phoneType method validate phone while type in the phone input box
     *
     * @param keyEvent Type Event
     */
    public void phoneType(KeyEvent keyEvent) {
        if (phone.getText().isEmpty()) {
            Validations.inputIsInvalid(phoneSuccess, phoneWrong, phoneValidationFeedback, "Phone number is required");
        } else if (!Validations.isPhoneValid(phone.getText())) {
            Validations.inputIsInvalid(phoneSuccess, phoneWrong, phoneValidationFeedback, "Invalid Phone number");
        } else {
            Validations.inputIsValid(phoneWrong, phoneSuccess, phoneValidationFeedback);
        }
    }


    /**
     * zipType method validate zip address while type in the zip input box
     *
     * @param keyEvent Type Event
     */
    public void zipType(KeyEvent keyEvent) {
        if (zip.getText().isEmpty()) {
            Validations.inputIsInvalid(zipSuccess, zipWrong, zipValidationFeedback, "zip is required");
        } else if (!Validations.isValidZip(zip.getText())) {
            Validations.inputIsInvalid(zipSuccess, zipWrong, zipValidationFeedback, "Invalid zip");
        } else {
            Validations.inputIsValid(zipWrong, zipSuccess, zipValidationFeedback);
        }
    }

    /**
     * UpdateButton for update user information in database
     *
     * @param actionEvent ActionEvent button
     *
     * @throws SQLException SQLException for any error in database
     */
    public void updateProfile(ActionEvent actionEvent) throws SQLException {
        String name = this.name.getText();
        String email = this.email.getText();
        String password = this.password.getText();
        String phone = this.phone.getText();
        String gender = this.gender.getValue();
        String bloodGroup = this.bloodGroup.getValue();
        String address = this.address.getText();
        String city = this.city.getText();
        String zip = this.zip.getText();
        LocalDate dobValue = dob.getValue();
        String dob = dobValue != null ? dobValue.toString() : null;
        String profilePicture = this.profilePicture.getText();

        if (name.isEmpty())
            Validations.inputIsInvalid(nameSuccess, nameWrong, nameValidationFeedback, "Name is required");
        else
            Validations.inputIsValid(nameWrong, nameSuccess, nameValidationFeedback);

        if (email.isEmpty())
            Validations.inputIsInvalid(emailSuccess, emailWrong, emailValidationFeedback, "Email Address is required");

        if (password.isEmpty()) password = dbPassword;

        if (!Validations.isEmailValid(email)) this.emailValidationFeedback.setVisible(true);

        if (phone.isEmpty())
            Validations.inputIsInvalid(phoneSuccess, phoneWrong, phoneValidationFeedback, "Phone number is required");
        else
            Validations.inputIsValid(phoneWrong, phoneSuccess, phoneValidationFeedback);

        if (address.isEmpty())
            Validations.inputIsInvalid(addressSuccess, addressWrong, addressValidationFeedback, "Address is required");
        else
            Validations.inputIsValid(addressWrong, addressSuccess, addressValidationFeedback);

        if (city.isEmpty())
            Validations.inputIsInvalid(citySuccess, cityWrong, cityValidationFeedback, "City is required");
        else
            Validations.inputIsValid(cityWrong, citySuccess, cityValidationFeedback);

        if (zip.isEmpty())
            Validations.inputIsInvalid(zipSuccess, zipWrong, zipValidationFeedback, "Zip Code is required");
        else
            Validations.inputIsValid(zipWrong, zipSuccess, zipValidationFeedback);

        if (gender == null)
            Validations.inputIsInvalid(genderSuccess, genderWrong, genderValidationFeedback, "Gender is required");
        else
            Validations.inputIsValid(genderWrong, genderSuccess, genderValidationFeedback);

        if (bloodGroup == null)
            Validations.inputIsInvalid(bloodGroupSuccess, bloodGroupWrong, bloodGroupValidationFeedback, "Blood Group is required");
        else
            Validations.inputIsValid(bloodGroupWrong, bloodGroupSuccess, bloodGroupValidationFeedback);

        if (dob == null)
            Validations.inputIsInvalid(dobSuccess, dobWrong, dobValidationFeedback, "Date of Birth is required");
        else
            Validations.inputIsValid(dobWrong, dobSuccess, dobValidationFeedback);

        if (profilePicture.isEmpty())
            Validations.inputIsInvalid(profilePictureSuccess, profilePictureWrong, profilePictureValidationFeedback, "Profile Picture is required");
        else
            Validations.inputIsValid(profilePictureWrong, profilePictureSuccess, profilePictureValidationFeedback);


        if (!isUserExistsByEmail && !name.isEmpty() && !email.isEmpty() && !phone.isEmpty() && !city.isEmpty() && !zip.isEmpty() && gender != null && bloodGroup != null && dob != null && !profilePicture.isEmpty()) {

            DatabaseWriteCall attemptUpdateUserData = UserService.attemptUpdateUserInformation(this.user.getId(), this.user.getEmail(), name, email, password, phone, address, city, zip, gender, bloodGroup, dob, profilePicture);
            updateUserInformation(attemptUpdateUserData);

            HashMap<String, String> newData = new HashMap<>();
            newData.put("name", name);
            newData.put("email", email);
            setData(newData);
        }
    }

    /**
     * uploadImage select profile picture for upload
     *
     * @param actionEvent button Action
     */
    public void uploadImage(ActionEvent actionEvent) {
        ImageFileHandler.uploadImage(actionEvent, this.user.getId(), profilePicture, imgProfile, "Select Profile Picture");
        profilePictureValidationFeedback.setVisible(false);
        profilePictureWrong.setVisible(false);
        profilePictureSuccess.setVisible(true);
    }

    /**
     * set user information all the text field
     *
     * @param getUserInformation DatabaseReadCall
     */
    private void setUserInformation(DatabaseReadCall getUserInformation) {
        getUserInformation.setOnSucceeded(event -> {
            ResultSet resultSet = getUserInformation.getValue();
            if (resultSet != null) {
                try {
                    if (resultSet.next()) {
                        String userName = resultSet.getString("name");
                        String userEmail = resultSet.getString("email");
                        String dbPassword = resultSet.getString("password");
                        this.dbPassword = (dbPassword != null) ? Hash.decrypt(dbPassword) : null;
                        Date userDob = resultSet.getDate("dob");
                        LocalDate dobDate = (userDob != null) ? userDob.toLocalDate() : null;
                        String userGender = resultSet.getString("gender");
                        String userPhone = resultSet.getString("phone");
                        String userPicture = resultSet.getString("picture");
                        String userBloodGroup = resultSet.getString("blood_group");
                        String userAddressJson = resultSet.getString("address");

                        name.setText((userName != null) ? userName : "");
                        email.setText((userEmail != null) ? userEmail : "");
                        phone.setText((userPhone != null) ? Helpers.getRawPhoneNumber(userPhone) : "");
                        gender.setValue(userGender);
                        bloodGroup.setValue(userBloodGroup);
                        dob.setValue(dobDate);
                        profilePicture.setText((userPicture != null) ? userPicture : "");
                        if (userPicture != null) {
                            File file = new File(userPicture);
                            Image image = new Image(file.toURI().toString());
                            imgProfile.setImage(image);
                        }

                        if (userAddressJson != null && Helpers.isValidJson(userAddressJson)) {
                            JSONObject jsonObject = new JSONObject(userAddressJson);
                            String userAddress = jsonObject.optString("address", "");
                            String userCity = jsonObject.optString("city", "");
                            int userZip = jsonObject.optInt("zip", 0);
                            address.setText(userAddress);
                            city.setText(userCity);
                            zip.setText(String.valueOf(userZip));
                        }
                    }
                } catch (SQLException e) {
                    System.out.println("PatientController: SQLException: " + e.getMessage());
                }
            }
        });

    }

    /**
     * update user information in database
     *
     * @param attemptUpdateUserData DatabaseWriteCall
     */
    private void updateUserInformation(DatabaseWriteCall attemptUpdateUserData) {
        attemptUpdateUserData.setOnSucceeded(event -> {
            Integer rowsAffected = attemptUpdateUserData.getValue();
            if (rowsAffected != null && rowsAffected > 0) {
                Notification.success("Success", "Information updated successfully.");
                FXMLScene.switchScene("/com/easymed/views/admin/profile.fxml", rootPane);
            } else
                Notification.error("Error", "Data update failed. Please try again.");
        });
        attemptUpdateUserData.setOnFailed(event -> {
            Throwable exception = attemptUpdateUserData.getException();
            if (exception != null) {
                exception.printStackTrace();
            }
            Notification.error("Error", "Something went wrong. Please try again.");
        });

        new Thread(attemptUpdateUserData).start();
    }

    /**
     * update user data in singleton use class
     *
     * @param data updated use data
     */
    public void setData(HashMap<String, String> data) {
        this.user.setName(data.get("name"));
        this.user.setEmail(data.get("email"));
    }
}
