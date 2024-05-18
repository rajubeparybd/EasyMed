package com.easymed.controllers.auth;

import com.easymed.database.services.AuthService;
import com.easymed.enums.Role;
import com.easymed.utils.*;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * LoginController is the controller class for the login.fxml file.
 *
 * @author Raju Bepary
 * @since 1.0.0
 */
public class LoginController implements Initializable {

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    private Button loginButton;

    @FXML
    private Label emailIValidationFeedback;

    @FXML
    private Label passwordValidationFeedback;

    @FXML
    private ImageView img1Success;

    @FXML
    private ImageView img2Success;

    @FXML
    private ImageView img1Wrong;

    @FXML
    private ImageView img2Wrong;

    @FXML
    private Circle smCIr1;

    @FXML
    private Circle smCir2;

    @FXML
    private Circle smCir3;

    @FXML
    private Circle bigCir1;

    @FXML
    private Circle bigCir2;

    @FXML
    private Circle bigCir3;

    @FXML
    private Circle bigCir4;

    @FXML
    private Label welcomeText;

    @FXML
    private Label exclusivity;

    @FXML
    private Label infoText;

    @FXML
    private SVGPath svg;

    @FXML
    private ImageView doctorImg;

    @FXML
    private MFXProgressSpinner spinner;

    @FXML
    private BorderPane rootPane;

    @FXML
    private Label loginFailedWarning;

    /**
     * This method is called when the login.fxml file is loaded.
     *
     * @param url            location of the fxml file
     * @param resourceBundle resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            Transitions.changeSVG(rootPane, svg, doctorImg);
            email.setText("asif@gmail.com");
            password.setText("password");
        });

        emailIValidationFeedback.setVisible(false);
        passwordValidationFeedback.setVisible(false);
        img1Success.setVisible(false);
        img2Success.setVisible(false);
        img1Wrong.setVisible(false);
        img2Wrong.setVisible(false);

        Transitions.circleTransition(bigCir1, 20, 10);
        Transitions.circleTransition(smCIr1, 20, 15);
        Transitions.circleTransition(bigCir2, 20, 10);
        Transitions.circleTransition(smCir2, 20, 15);
        Transitions.circleTransition(bigCir3, 20, 10);
        Transitions.circleTransition(smCir3, 10, 15);
        Transitions.circleTransition(bigCir4, 20, 10);

        Platform.runLater(() -> {
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setTitle(Helpers.getTitle("Login"));
            stage.maximizedProperty().addListener((ov, t, t1) -> {
                if (t) Transitions.changeSVG(rootPane, svg, doctorImg);
            });
        });
    }

    /**
     * emailType method validate email address while type in the email input box
     *
     * @param keyEvent Type Event
     */
    public void emailType(KeyEvent keyEvent) {
        if (email.getText().isEmpty()) {
            Validations.inputIsInvalid(img1Success, img1Wrong, emailIValidationFeedback, "Email Address is required");
        } else if (!Validations.isEmailValid(email.getText())) {
            Validations.inputIsInvalid(img1Success, img1Wrong, emailIValidationFeedback, "Invalid Email Address");
        } else {
            Validations.inputIsValid(img1Wrong, img1Success, emailIValidationFeedback);
        }
    }

    /**
     * passwordType method validate password address while type in the password input box
     *
     * @param keyEvent Type Event
     */
    public void passwordType(KeyEvent keyEvent) {
        if (password.getText().isEmpty()) {
            Validations.inputIsInvalid(img2Success, img2Wrong, passwordValidationFeedback, "Password is required");
        } else {
            Validations.inputIsValid(img2Wrong, img2Success, passwordValidationFeedback);
        }
    }

    /**
     * signUp method switch to the registration scene
     *
     * @param actionEvent action event
     */
    public void signUp(ActionEvent actionEvent) {
        FXMLScene.switchScene("/com/easymed/views/auth/registration.fxml", (Node) actionEvent.getSource());
    }

    /**
     * login method validate the email and password and switch to the dashboard scene
     *
     * @param actionEvent action event
     */
    public void login(ActionEvent actionEvent) {
        String email = this.email.getText();
        String password = this.password.getText();
        if (email.isEmpty())
            Validations.inputIsInvalid(img1Success, img1Wrong, emailIValidationFeedback, "Email Address is required");

        if (password.isEmpty())
            Validations.inputIsInvalid(img2Success, img2Wrong, passwordValidationFeedback, "Password is required");

        if (!Validations.isEmailValid(email)) this.emailIValidationFeedback.setVisible(true);
        if (!Validations.isPasswordValid(password)) this.passwordValidationFeedback.setVisible(true);

        if (Validations.isEmailValid(email) && Validations.isPasswordValid(password)) {
            loginButton.setDisable(true);
            spinner.setVisible(true);
            loginFailedWarning.setVisible(false);

            DatabaseReadCall databaseReadCall = AuthService.attemptLogin(email, password);
            databaseReadCall.setOnSucceeded(event -> {
                try {
                    if (databaseReadCall.getValue().next()) {
                        Notification.success("Success", "Login successful");

                        // User data to be passed to the dashboard scene
                        HashMap<String, String> userData = new HashMap<>();
                        ResultSet user = databaseReadCall.getValue();
                        userData.put("id", user.getString("id"));
                        userData.put("name", user.getString("name"));
                        userData.put("email", user.getString("email"));
                        userData.put("role", user.getString("role"));
                        userData.put("picture", user.getString("picture"));

                        // Switch to the dashboard scene based on the user role
                        if (Role.isPatient(user.getString("role"))) {
                            FXMLScene.switchScene("/com/easymed/views/patient/dashboard.fxml", (Node) actionEvent.getSource(), userData);
                        } else if (Role.isDoctor(user.getString("role"))) {
                            FXMLScene.switchScene("/com/easymed/views/doctor/dashboard.fxml", (Node) actionEvent.getSource(), userData);
                        } else if (Role.isAdmin(user.getString("role"))) {
                            FXMLScene.switchScene("/com/easymed/views/admin/dashboard.fxml", (Node) actionEvent.getSource(), userData);
                        }

                    } else {
                        loginFailedWarning.setVisible(true);
                        Notification.error("Error", "Invalid email or password");
                    }
                } catch (SQLException e) {
                    System.out.println("LoginController.login: " + e.getMessage());
                    Notification.error("Error", "Something went wrong. Please try again.");
                }
                loginButton.setDisable(false);
                spinner.setVisible(false);
            });
            databaseReadCall.setOnFailed(event -> {
                Notification.error("Error", "Something went wrong. Please try again.");
            });
            new Thread(databaseReadCall).start();
        }
    }

    /**
     * forgetPassword method switch to the forget password scene
     *
     * @param actionEvent action event
     */
    public void forgetPassword(ActionEvent actionEvent) {
        FXMLScene.switchScene("/com/easymed/views/auth/forget-password.fxml", (Node) actionEvent.getSource());
    }
}
