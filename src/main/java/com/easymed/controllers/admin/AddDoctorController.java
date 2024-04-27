package com.easymed.controllers.admin;

import com.easymed.database.services.AuthService;
import com.easymed.database.services.DoctorService;
import com.easymed.enums.Gender;
import com.easymed.enums.Role;
import com.easymed.utils.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddDoctorController implements Initializable {
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
    private ComboBox<String> gender;

    @FXML
    private ComboBox<String> bloodGroup;

    @FXML
    private TextField bio;

    @FXML
    private TextField spacialities;
    @FXML
    private Label spacialitiesValidationFeedback;
    @FXML
    private ImageView spacialitiesSuccess;
    @FXML
    private ImageView spacialitiesWrong;

    @FXML
    private TextField fees;
    @FXML
    private Label feesValidationFeedback;
    @FXML
    private ImageView feesSuccess;
    @FXML
    private ImageView feesWrong;

    @FXML
    private TextField designation;
    @FXML
    private Label designationValidationFeedback;
    @FXML
    private ImageView designationSuccess;
    @FXML
    private ImageView designationWrong;

    @FXML
    private TextField hospital;
    @FXML
    private Label hospitalValidationFeedback;
    @FXML
    private ImageView hospitalSuccess;
    @FXML
    private ImageView hospitalWrong;

    @FXML
    private TextField hospital_address;
    @FXML
    private Label hospitalAddressValidationFeedback;
    @FXML
    private ImageView hospitalAddressSuccess;
    @FXML
    private ImageView hospitalAddressWrong;

    @FXML
    private TextField experience;

    @FXML
    private TextField education;

    @FXML
    private TextField schedule;

    @FXML
    private Button addDoctorButton;

    @FXML
    private BorderPane rootPane;

    private Boolean isUserExistsByEmail = false;

    private String doctorId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            VBox sidebar = (VBox) rootPane.getLeft();
            Helpers.toggleMenuClass(sidebar, "doctors");
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setTitle("Add Doctor");

            gender.getItems().addAll(Gender.getText(Gender.MALE), Gender.getText(Gender.FEMALE), Gender.getText(Gender.OTHER));
            bloodGroup.getItems().addAll("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");

            nameValidationFeedback.setVisible(false);
            nameWrong.setVisible(false);
            nameSuccess.setVisible(false);

            emailValidationFeedback.setVisible(false);
            emailWrong.setVisible(false);
            emailSuccess.setVisible(false);

            passwordValidationFeedback.setVisible(false);
            passwordWrong.setVisible(false);
            passwordSuccess.setVisible(false);

            phoneValidationFeedback.setVisible(false);
            phoneWrong.setVisible(false);
            phoneSuccess.setVisible(false);

            spacialitiesValidationFeedback.setVisible(false);
            spacialitiesWrong.setVisible(false);
            spacialitiesSuccess.setVisible(false);

            feesValidationFeedback.setVisible(false);
            feesWrong.setVisible(false);
            feesSuccess.setVisible(false);

            designationValidationFeedback.setVisible(false);
            designationWrong.setVisible(false);
            designationSuccess.setVisible(false);

            hospitalValidationFeedback.setVisible(false);
            hospitalWrong.setVisible(false);
            hospitalSuccess.setVisible(false);

            hospitalAddressValidationFeedback.setVisible(false);
            hospitalAddressWrong.setVisible(false);
            hospitalAddressSuccess.setVisible(false);

        });


        email.focusedProperty().addListener((observableValue, focusOut, onFocused) -> {
            if (!onFocused) {
                if (email.getText().isEmpty()) {
                    Validations.inputIsInvalid(emailSuccess, emailWrong, emailValidationFeedback, "Email Address is required");
                } else if (!Validations.isEmailValid(email.getText())) {
                    Validations.inputIsInvalid(emailSuccess, emailWrong, emailValidationFeedback, "Invalid Email Address");
                } else {
                    Validations.inputIsValid(emailWrong, emailSuccess, emailValidationFeedback);
                }
            }
            if (focusOut && !email.getText().isEmpty() && Validations.isEmailValid(email.getText())) {
                DatabaseReadCall databaseReadCall = AuthService.checkUserExistsByEmail(email.getText());
                databaseReadCall.setOnSucceeded(event -> {
                    try {
                        if (databaseReadCall.getValue().next()) {
                            Validations.inputIsInvalid(emailSuccess, emailWrong, emailValidationFeedback, "Email Address already exists");
                            isUserExistsByEmail = true;
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
                isUserExistsByEmail = true;
            }
        });

        phone.focusedProperty().addListener((observableValue, focusOut, onFocused) -> {
            if (!onFocused) {
                if (phone.getText().isEmpty()) {
                    Validations.inputIsInvalid(phoneSuccess, phoneWrong, phoneValidationFeedback, "Phone Number is required");
                } else if (!Validations.isPhoneValid(phone.getText())) {
                    Validations.inputIsInvalid(phoneSuccess, phoneWrong, phoneValidationFeedback, "Invalid Phone Number");
                } else {
                    Validations.inputIsValid(phoneWrong, phoneSuccess, phoneValidationFeedback);
                }
            }
            if (focusOut && !phone.getText().isEmpty() && Validations.isPhoneValid(phone.getText())) {
                Validations.inputIsValid(phoneWrong, phoneSuccess, phoneValidationFeedback);
            } else {
                Validations.inputIsInvalid(phoneSuccess, phoneWrong, phoneValidationFeedback, "Invalid Phone Number");
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
            Validations.inputIsInvalid(passwordSuccess, passwordWrong, passwordValidationFeedback, "Password is required");
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
     * feesType method validate fees address while type in the fees input box
     *
     * @param keyEvent Type Event
     */
    public void feesType(KeyEvent keyEvent) {
        if (fees.getText().isEmpty()) {
            Validations.inputIsInvalid(feesSuccess, feesWrong, feesValidationFeedback, "Fees is required");
        } else if (!Validations.isValidFees(fees.getText())) {
            Validations.inputIsInvalid(feesSuccess, feesWrong, feesValidationFeedback, "Invalid Fees");
        } else {
            Validations.inputIsValid(feesWrong, feesSuccess, feesValidationFeedback);
        }
    }

    public void addDoctor(ActionEvent actionEvent) throws SQLException {
        String name = this.name.getText();
        String email = this.email.getText();
        String password = this.password.getText();
        String phone = this.phone.getText();
        String gender = this.gender.getValue();
        String bloodGroup = this.bloodGroup.getValue();
        String bio = this.bio.getText();
        String spacialities = this.spacialities.getText();
        String fees = this.fees.getText();
        String designation = this.designation.getText();
        String hospital = this.hospital.getText();
        String hospitalAddress = this.hospital_address.getText();
        String experience = this.experience.getText();
        String education = this.education.getText();
        String schedule = this.schedule.getText();


        if (name.isEmpty())
            Validations.inputIsInvalid(nameSuccess, nameWrong, nameValidationFeedback, "Name is required");
        else
            Validations.inputIsValid(nameWrong, nameSuccess, nameValidationFeedback);

        if (email.isEmpty())
            Validations.inputIsInvalid(emailSuccess, emailWrong, emailValidationFeedback, "Email Address is required");

        if (password.isEmpty())
            Validations.inputIsInvalid(passwordSuccess, passwordWrong, passwordValidationFeedback, "Password is required");

        if (!Validations.isEmailValid(email)) this.emailValidationFeedback.setVisible(true);
        if (!Validations.isPasswordValid(password)) this.passwordValidationFeedback.setVisible(true);

        if (phone.isEmpty())
            Validations.inputIsInvalid(phoneSuccess, phoneWrong, phoneValidationFeedback, "Phone number is required");
        else
            Validations.inputIsValid(phoneWrong, phoneSuccess, phoneValidationFeedback);

        if (spacialities.isEmpty())
            Validations.inputIsInvalid(spacialitiesSuccess, spacialitiesWrong, spacialitiesValidationFeedback, "Spacialities is required");
        else
            Validations.inputIsValid(spacialitiesWrong, spacialitiesSuccess, spacialitiesValidationFeedback);

        if (fees.isEmpty())
            Validations.inputIsInvalid(feesSuccess, feesWrong, feesValidationFeedback, "Fees is required");
        else
            Validations.inputIsValid(feesWrong, feesSuccess, feesValidationFeedback);

        if (designation.isEmpty())
            Validations.inputIsInvalid(designationSuccess, designationWrong, designationValidationFeedback, "Designation is required");
        else
            Validations.inputIsValid(designationWrong, designationSuccess, designationValidationFeedback);

        if (hospital.isEmpty())
            Validations.inputIsInvalid(hospitalSuccess, hospitalWrong, hospitalValidationFeedback, "Hospital is required");
        else
            Validations.inputIsValid(hospitalWrong, hospitalSuccess, hospitalValidationFeedback);

        if (hospitalAddress.isEmpty())
            Validations.inputIsInvalid(hospitalAddressSuccess, hospitalAddressWrong, hospitalAddressValidationFeedback, "Hospital Address is required");
        else
            Validations.inputIsValid(hospitalAddressWrong, hospitalAddressSuccess, hospitalAddressValidationFeedback);

        if (!isUserExistsByEmail && !name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !phone.isEmpty() && !spacialities.isEmpty() && !fees.isEmpty() && !designation.isEmpty() && !hospital.isEmpty() && !hospitalAddress.isEmpty() && Validations.isEmailValid(email) && Validations.isPasswordValid(password) && Validations.isPhoneValid(phone)) {

            DatabaseWriteCall attemptDoctorRegistration = DoctorService.attemptDoctorRegistration(name, email, password, Role.getText(Role.DOCTOR), gender, bloodGroup, phone);

            attemptDoctorRegistration.setOnSucceeded(event -> {
                DatabaseReadCall getDoctorId = DoctorService.getDoctorIdByEmail(email);
                getDoctorId.setOnSucceeded(we -> {
                    try {
                        ResultSet resultSet = getDoctorId.getValue();
                        if (resultSet.next()) this.doctorId = resultSet.getString("id");
                        DatabaseWriteCall attemptDoctorProfileRegistration = DoctorService.attemptDoctorProfileRegistration(this.doctorId, bio, spacialities, fees, designation, hospital, hospitalAddress, experience, education, schedule);
                        attemptDoctorProfileRegistration.setOnSucceeded(de -> {
                            Notification.success("Success", "Doctor added successfully");
                            FXMLScene.switchScene("/com/easymed/views/admin/doctors-list.fxml", (Node) actionEvent.getSource());
                        });
                        attemptDoctorProfileRegistration.setOnFailed(de -> {
                            Notification.error("Error", "Something went wrong. Please try again.");
                        });
                        new Thread(attemptDoctorProfileRegistration).start();
                    } catch (SQLException e) {
                        System.out.println("Doctor registration failed with error: " + e.getMessage());
                    }
                });
                new Thread(getDoctorId).start();
            });
            attemptDoctorRegistration.setOnFailed(event -> {
                Notification.error("Error", "Something went wrong. Please try again.");
            });
            new Thread(attemptDoctorRegistration).start();
        }
    }
}
