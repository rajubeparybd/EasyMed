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
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * RegistrationController is the controller class for the registration.fxml file.
 *
 * @author Raju Bepary
 * @since 1.0.0
 */
public class RegistrationController implements Initializable {

    @FXML
    private TextField name;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    private Button registrationButton;

    @FXML
    private Label emailValidationFeedback;

    @FXML
    private Label passwordValidationFeedback;

    @FXML
    private Label nameValidationFeedback;

    @FXML
    private ImageView img1Success;

    @FXML
    private ImageView img2Success;

    @FXML
    private ImageView img3Success;

    @FXML
    private ImageView img1Wrong;

    @FXML
    private ImageView img2Wrong;

    @FXML
    private ImageView img3Wrong;

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
    private Label registrationFailedWarning;

    private Boolean isUserExistsByEmail = false;

    /**
     * This method is called when the registration.fxml file is loaded.
     *
     * @param url            location of the fxml file
     * @param resourceBundle resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            Transitions.changeSVG(rootPane, svg, doctorImg);
        });

        nameValidationFeedback.setVisible(false);
        emailValidationFeedback.setVisible(false);
        passwordValidationFeedback.setVisible(false);
        img1Success.setVisible(false);
        img2Success.setVisible(false);
        img3Success.setVisible(false);
        img1Wrong.setVisible(false);
        img2Wrong.setVisible(false);
        img3Wrong.setVisible(false);

        Transitions.circleTransition(bigCir1, 20, 10);
        Transitions.circleTransition(smCIr1, 20, 15);
        Transitions.circleTransition(bigCir2, 20, 10);
        Transitions.circleTransition(smCir2, 20, 15);
        Transitions.circleTransition(bigCir3, 20, 10);
        Transitions.circleTransition(smCir3, 10, 15);
        Transitions.circleTransition(bigCir4, 20, 10);

        Platform.runLater(() -> {
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setTitle(Helpers.getTitle("Registration"));
            stage.maximizedProperty().addListener((ov, t, t1) -> {
                if (t) Transitions.changeSVG(rootPane, svg, doctorImg);
            });
        });

        email.focusedProperty().addListener((observableValue, focusOut, onFocused) -> {
            if (!onFocused) {
                if (email.getText().isEmpty()) {
                    Validations.inputIsInvalid(img2Success, img2Wrong, emailValidationFeedback, "Email Address is required");
                } else if (!Validations.isEmailValid(email.getText())) {
                    Validations.inputIsInvalid(img2Success, img2Wrong, emailValidationFeedback, "Invalid Email Address");
                } else {
                    Validations.inputIsValid(img2Wrong, img2Success, emailValidationFeedback);
                }
            }
            if (focusOut && !email.getText().isEmpty()) {
                DatabaseReadCall databaseReadCall = AuthService.checkUserExistsByEmail(email.getText());
                databaseReadCall.setOnSucceeded(event -> {
                    try {
                        if (databaseReadCall.getValue().next()) {
                            Validations.inputIsInvalid(img2Success, img2Wrong, emailValidationFeedback, "Email Address already exists");
                            isUserExistsByEmail = true;
                        } else {
                            Validations.inputIsValid(img2Wrong, img2Success, emailValidationFeedback);
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
            Validations.inputIsInvalid(img1Success, img1Wrong, nameValidationFeedback, "Name is required");
        } else {
            Validations.inputIsValid(img1Wrong, img1Success, nameValidationFeedback);
        }
    }

    /**
     * emailType method validate email address while type in the email input box
     *
     * @param keyEvent Type Event
     */
    public void emailType(KeyEvent keyEvent) {
        if (email.getText().isEmpty()) {
            Validations.inputIsInvalid(img2Success, img2Wrong, emailValidationFeedback, "Email Address is required");
        } else if (!Validations.isEmailValid(email.getText())) {
            Validations.inputIsInvalid(img2Success, img2Wrong, emailValidationFeedback, "Invalid Email Address");
        } else {
            Validations.inputIsValid(img2Wrong, img2Success, emailValidationFeedback);
        }
    }

    /**
     * passwordType method validate password address while type in the password input box
     *
     * @param keyEvent Type Event
     */
    public void passwordType(KeyEvent keyEvent) {
        if (password.getText().isEmpty()) {
            Validations.inputIsInvalid(img3Success, img3Wrong, passwordValidationFeedback, "Password is required");
        } else if (!Validations.isPasswordValid(password.getText())) {
            Validations.inputIsInvalid(img3Success, img3Wrong, passwordValidationFeedback, "Password must be at least 6 characters long");
        } else {
            Validations.inputIsValid(img3Wrong, img3Success, passwordValidationFeedback);
        }
    }

    public void login(ActionEvent actionEvent) {
        FXMLScene.switchScene("/com/easymed/views/auth/login.fxml", (Node) actionEvent.getSource());
    }

    public void registration(ActionEvent actionEvent) {
        String name = this.name.getText();
        String email = this.email.getText();
        String password = this.password.getText();
        if (name.isEmpty())
            Validations.inputIsInvalid(img1Success, img1Wrong, nameValidationFeedback, "Name is required");
        else
            Validations.inputIsValid(img1Wrong, img1Success, nameValidationFeedback);

        if (email.isEmpty())
            Validations.inputIsInvalid(img2Success, img2Wrong, emailValidationFeedback, "Email Address is required");

        if (password.isEmpty())
            Validations.inputIsInvalid(img3Success, img3Wrong, passwordValidationFeedback, "Password is required");

        if (!Validations.isEmailValid(email)) this.emailValidationFeedback.setVisible(true);
        if (!Validations.isPasswordValid(password)) this.passwordValidationFeedback.setVisible(true);

        if (Validations.isEmailValid(email) && Validations.isPasswordValid(password) && !isUserExistsByEmail) {
            registrationButton.setDisable(true);
            spinner.setVisible(true);
            registrationFailedWarning.setVisible(false);

            DatabaseWriteCall databaseWriteCall = AuthService.attemptRegistration(name, email, password, String.valueOf(Role.PATIENT));
            databaseWriteCall.setOnSucceeded(event -> {
                if (databaseWriteCall.isDone()) {
                    Notification.success("Success", "Account Registration successful");

                    //TODO: Implement dashboard scene according to the user role

                } else {
                    registrationFailedWarning.setVisible(true);
                    Notification.error("Registration Failed Please try again.");
                }

                registrationButton.setDisable(false);
                spinner.setVisible(false);
            });
            databaseWriteCall.setOnFailed(event -> {
                Notification.error("Error", "Something went wrong. Please try again.");
            });
            new Thread(databaseWriteCall).start();

        } else {
            registrationFailedWarning.setVisible(true);
            Notification.error("Error", "Something went wrong. Please try again.");
        }
    }
}
