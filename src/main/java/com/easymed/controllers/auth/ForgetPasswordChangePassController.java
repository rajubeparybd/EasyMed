package com.easymed.controllers.auth;

import com.easymed.database.services.AuthService;
import com.easymed.utils.DatabaseWriteCall;
import com.easymed.utils.Helpers;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ForgetPasswordChangePassController implements Initializable {

    @FXML
    private TextField password;
    @FXML
    private TextField confirmPassword;
    @FXML
    private Label warning;

    @FXML
    private Pane contentArea;
    private Stage mainStage;
    private String email;

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            Stage stage = (Stage) contentArea.getScene().getWindow();
            stage.setTitle(Helpers.getTitle("Change Password"));
        });
        warning.setVisible(false);
    }

    public void setData(String email) {
        this.email = email;
    }

    public void changePassword(ActionEvent actionEvent) {
        warning.setVisible(false);
        if (password.getText().equals(confirmPassword.getText())) {
            DatabaseWriteCall changePassword = AuthService.changePassword(email, password.getText());
            changePassword.setOnSucceeded(e -> {
                DatabaseWriteCall deleteOtp = AuthService.deleteForgetPasswordCode(email);
                deleteOtp.getInsertedRows();
                Stage stage = (Stage) contentArea.getScene().getWindow();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Password Changed", ButtonType.OK);
                alert.setTitle("Changed Password");
                alert.setHeaderText("Your Password has been changed successfully");
                alert.show();
                stage.close();
            });
            new Thread(changePassword).start();
        } else {
            warning.setVisible(true);
        }
    }
}
