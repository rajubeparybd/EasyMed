package com.easymed.controllers.auth;

import com.easymed.database.services.AuthService;
import com.easymed.utils.*;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * RegistrationController is the controller class for the forgetPassword.fxml file.
 *
 * @author Raju Bepary
 * @since 1.0.0
 */
public class ForgetPasswordController implements Initializable {
    @FXML
    private TextField email;

    @FXML
    private Button forgetPasswordButton;

    @FXML
    private Label emailValidationFeedback;

    @FXML
    private ImageView img1;

    @FXML
    private ImageView img2;

    @FXML
    private ImageView img3;

    @FXML
    private Pane contentArea;

    @FXML
    private ImageView imgSuccess;

    @FXML
    private ImageView imgWrong;

    @FXML
    private MFXProgressSpinner spinner;

    @FXML
    private Label forgetPasswordFailedWarning;

    private Boolean isUserExistsByEmail = false;
    private Stage mainStage;

    /**
     * Setter method for the main stage
     *
     * @param mainStage Main stage
     */
    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    /**
     * This method is called when the forget-password.fxml file is loaded.
     *
     * @param url            location of the fxml file
     * @param resourceBundle resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        emailValidationFeedback.setVisible(false);
        forgetPasswordFailedWarning.setVisible(false);
        imgSuccess.setVisible(false);
        imgWrong.setVisible(false);
        spinner.setVisible(false);

        email.focusedProperty().addListener((observableValue, focusOut, onFocused) -> {
            if (!onFocused) {
                if (email.getText().isEmpty()) {
                    Validations.inputIsInvalid(imgSuccess, imgWrong, emailValidationFeedback, "Email Address is required");
                } else if (!Validations.isEmailValid(email.getText())) {
                    Validations.inputIsInvalid(imgSuccess, imgWrong, emailValidationFeedback, "Invalid Email Address");
                } else {
                    Validations.inputIsValid(imgWrong, imgSuccess, emailValidationFeedback);
                }
            }
            if (focusOut && !email.getText().isEmpty()) {
                DatabaseReadCall databaseReadCall = AuthService.checkUserExistsByEmail(email.getText());
                databaseReadCall.setOnSucceeded(event -> {
                    try {
                        if (!databaseReadCall.getValue().next()) {
                            Validations.inputIsInvalid(imgSuccess, imgWrong, emailValidationFeedback, "Email Address is not registered");
                            isUserExistsByEmail = true;
                        } else {
                            Validations.inputIsValid(imgWrong, imgSuccess, emailValidationFeedback);
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
     * emailType method validate email address while type in the email input box
     *
     * @param keyEvent Type Event
     */
    public void emailType(KeyEvent keyEvent) {
        if (email.getText().isEmpty()) {
            Validations.inputIsInvalid(imgSuccess, imgWrong, emailValidationFeedback, "Email Address is required");
        } else if (!Validations.isEmailValid(email.getText())) {
            Validations.inputIsInvalid(imgSuccess, imgWrong, emailValidationFeedback, "Invalid Email Address");
        } else {
            Validations.inputIsValid(imgWrong, imgSuccess, emailValidationFeedback);
        }
    }


    public void forgetPassword(ActionEvent actionEvent) {
        String email = this.email.getText();

        if (email.isEmpty())
            Validations.inputIsInvalid(imgSuccess, imgWrong, emailValidationFeedback, "Email Address is required");

        if (!Validations.isEmailValid(email)) this.emailValidationFeedback.setVisible(true);

        if (Validations.isEmailValid(email) && !isUserExistsByEmail) {
            forgetPasswordButton.setDisable(true);
            spinner.setVisible(true);
            forgetPasswordFailedWarning.setVisible(false);

            DatabaseWriteCall databaseWriteCall = AuthService.sendForgetPasswordCode(email);
            databaseWriteCall.setOnSucceeded(event -> {
                if (databaseWriteCall.isDone()) {
                    String code = AuthService.forgotPasswordCode;
                    EmailTemplate emailTemplate = new EmailTemplate(code);
                    Email mail = new Email(email, "Forget Password Code", emailTemplate.getForgetPassTemplate());
                    mail.setOnSucceeded(workerStateEvent -> {
                        Notification.success("Success", "Code sent to email successfully.");
                        InputStream okImg = null;
                        InputStream secondImg = null;
                        try {
                            okImg = new FileInputStream("src/main/resources/com/easymed/images/okIcon.png");
                            secondImg = new FileInputStream("src/main/resources/com/easymed/images/icons8-circled-2-240.png");

                        } catch (FileNotFoundException e) {
                            System.out.println("File not found." + e.getMessage());
                        }
                        img1.setImage(new Image(okImg));
                        img2.setImage(new Image(secondImg));
                        FXMLScene fxmlScene = FXMLScene.load("/com/easymed/views/auth/forgetPassword-otp.fxml");
                        ForgetPasswordOtpController controller = (ForgetPasswordOtpController) fxmlScene.getController();
                        controller.setData(email, code, contentArea, img1, img2, img3);
                        controller.setMainStage(mainStage);
                        contentArea.getChildren().removeAll();
                        contentArea.getChildren().setAll(fxmlScene.getRoot());
                    });

                    mail.setOnFailed(workerStateEvent -> {
                        spinner.setVisible(false);
                        forgetPasswordButton.setDisable(false);
                        AuthService.forgotPasswordCode = null;
                        DatabaseWriteCall deleteCode = AuthService.deleteForgetPasswordCode(email);
                        if (deleteCode.getInsertedRows() > 0)
                            Notification.error("Failed to send code to email. Please try again.");
                    });

                    Thread thread = new Thread(mail);
                    thread.setDaemon(true);
                    thread.start();
                } else {
                    forgetPasswordFailedWarning.setVisible(true);
                    Notification.error("Failed to send code to email. Please try again.");
                }
            });

            databaseWriteCall.setOnFailed(event -> {
                Notification.error("Error", "Something went wrong. Please try again.");
            });

            new Thread(databaseWriteCall).start();
        } else {
            forgetPasswordFailedWarning.setVisible(true);
            Notification.error("Error", "Something went wrong. Please try again.");
        }
    }
}
