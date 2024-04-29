package com.easymed.controllers.admin;

import com.easymed.database.services.AuthService;
import com.easymed.database.services.PatientService;
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
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddPatientController implements Initializable {
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
    private Button addPatientButton;

    @FXML
    private BorderPane rootPane;

    private Boolean isUserExistsByEmail = false;

    private String doctorId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            VBox sidebar = (VBox) rootPane.getLeft();
            Helpers.toggleMenuClass(sidebar, "patients");
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setTitle(Helpers.getTitle("Add Patient"));

            Helpers.setGenderComboBox(gender);
            Helpers.setBloodGroupComboBox(bloodGroup);

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
     * addPatient method add patient to the database and redirect to the patients list page
     *
     * @param actionEvent Action Event
     *
     * @throws SQLException SQL Exception
     */
    public void addPatient(ActionEvent actionEvent) throws SQLException {
        String name = this.name.getText();
        String email = this.email.getText();
        String password = this.password.getText();
        String phone = this.phone.getText();
        String gender = this.gender.getValue();
        String bloodGroup = this.bloodGroup.getValue();

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

        if (!isUserExistsByEmail && !name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !phone.isEmpty() && Validations.isEmailValid(email) && Validations.isPasswordValid(password) && Validations.isPhoneValid(phone)) {

            DatabaseWriteCall attemptPatientRegistration = PatientService.attemptPatientRegistration(name, email, password, Role.getText(Role.PATIENT), gender, bloodGroup, phone);

            attemptPatientRegistration.setOnSucceeded(event -> {
                Notification.success("Success", "Patient added successfully");
                FXMLScene.switchScene("/com/easymed/views/admin/patients-list.fxml", (Node) actionEvent.getSource());
            });
            attemptPatientRegistration.setOnFailed(event -> {
                Notification.error("Error", "Something went wrong. Please try again.");
            });
            new Thread(attemptPatientRegistration).start();
        }
    }
}
