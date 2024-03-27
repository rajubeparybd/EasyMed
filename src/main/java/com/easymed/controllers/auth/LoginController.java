package com.easymed.controllers.auth;

import com.easymed.database.services.AuthService;
import com.easymed.utils.DatabaseReadCall;
import com.easymed.utils.Transitions;
import com.easymed.utils.Validations;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import org.controlsfx.control.Notifications;

import java.net.URL;
import java.sql.SQLException;
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
        Platform.runLater(this::changeSVG);

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
            stage.maximizedProperty().addListener((ov, t, t1) -> {
                if (t) changeSVG();
            });
        });
    }

    private void changeSVG() {
        double height = rootPane.getScene().getHeight();
        double width = rootPane.getScene().getWidth();
        double lx = 344;
        double cx = 344;
        double rx = 571.04;
        double ty = width / 2;
        String path = "M 0 " + Math.round(lx) + " C 0 " + Math.round(cx) + " 435 " + Math.round(rx) + " " + Math.round(ty) + " 0 L " + Math.round(ty) + " " + Math.round(height) + " L 0 " + Math.round(height) + " Z M 0 344.1211";
        svg.setContent(path);
        doctorImg.setLayoutX(ty - 247);
    }

    /**
     * emailType method validate email address while type in the email input box
     *
     * @param keyEvent Type Event
     */
    public void emailType(KeyEvent keyEvent) {
        if (email.getText().isEmpty()) {
            emailIsRequired();
        } else if (!Validations.isEmailValid(email.getText())) {
            img1Success.setVisible(false);
            img1Wrong.setVisible(true);
            emailIValidationFeedback.setText("Invalid Email Address");
            emailIValidationFeedback.setVisible(true);
        } else {
            emailIsValid();
        }
    }

    /**
     * passwordType method validate password address while type in the password input box
     *
     * @param keyEvent Type Event
     */
    public void passwordType(KeyEvent keyEvent) {
        if (password.getText().isEmpty()) {
            passwordIsRequired();
        } else if (!Validations.isPasswordValid(password.getText())) {
            img2Success.setVisible(false);
            img2Wrong.setVisible(true);
            passwordValidationFeedback.setText("Minimum 6 characters required.");
            passwordValidationFeedback.setVisible(true);
        } else {
            passwordIsValid();
        }
    }

    private void emailIsRequired() {
        img1Success.setVisible(false);
        img1Wrong.setVisible(true);
        emailIValidationFeedback.setText("Email Address is required");
        emailIValidationFeedback.setVisible(true);
    }

    private void passwordIsRequired() {
        img2Success.setVisible(false);
        img2Wrong.setVisible(true);
        passwordValidationFeedback.setText("Password is required");
        passwordValidationFeedback.setVisible(true);
    }

    private void emailIsValid() {
        img1Wrong.setVisible(false);
        img1Success.setVisible(true);
        emailIValidationFeedback.setVisible(false);
    }

    private void passwordIsValid() {
        img2Wrong.setVisible(false);
        img2Success.setVisible(true);
        passwordValidationFeedback.setVisible(false);
    }

    public void signUp(ActionEvent actionEvent) {
        //TODO: Implement sign up scene
    }

    public void login(ActionEvent actionEvent) {
        String email = this.email.getText();
        String password = this.password.getText();
        if (email.isEmpty()) emailIsRequired();
        if (password.isEmpty()) passwordIsRequired();
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
                        Notifications.create()
                                .title("Success")
                                .text("Login Successful")
                                .showInformation();
                        //TODO: Implement dashboard scene according to the user role
                    } else {
                        loginFailedWarning.setVisible(true);
                        Notifications.create()
                                .text("Login Failed. Please try again.")
                                .showWarning();
                    }
                } catch (SQLException e) {
                    System.out.println("LoginController.login: " + e.getMessage());
                }
                loginButton.setDisable(false);
                spinner.setVisible(false);
            });
            databaseReadCall.setOnFailed(event -> {
                Notifications.create()
                        .title("Error")
                        .text("Something went wrong. Please try again.")
                        .showError();
            });
            new Thread(databaseReadCall).start();
        }
    }

    public void forgetPassword(ActionEvent actionEvent) {
        //TODO: Implement forget password scene
    }


}
