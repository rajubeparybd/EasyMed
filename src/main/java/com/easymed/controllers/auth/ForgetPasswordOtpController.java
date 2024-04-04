package com.easymed.controllers.auth;

import com.easymed.database.services.AuthService;
import com.easymed.utils.DatabaseReadCall;
import com.easymed.utils.FXMLScene;
import com.easymed.utils.Helpers;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ForgetPasswordOtpController implements Initializable {
    @FXML
    private HBox verifyBox;
    @FXML
    private Label warning;
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private String code;
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
            stage.setTitle(Helpers.getTitle("Verify Code"));
        });
        warning.setVisible(false);
    }

    public void setData(String email, String code, Pane contentArea, ImageView img1, ImageView img2, ImageView img3) {
        this.email = email;
        this.code = code;
        this.contentArea = contentArea;
        this.img1 = img1;
        this.img2 = img2;
        this.img3 = img3;
    }


    public void verifyCodePress(KeyEvent ke) {
        for (int i = 0; i < verifyBox.getChildren().size(); i++) {
            if (verifyBox.getChildren().get(i) instanceof TextField && verifyBox.getChildren().get(i).isFocused()) {
                TextField textField = (TextField) ke.getSource();
                if (!textField.getText().matches("\\d*")) {
                    textField.setText(textField.getText().replaceAll("\\D", ""));
                } else if (textField.getText().length() > 1) {
                    textField.setText(textField.getText().substring(0, 1));
                } else if (textField.getText().length() == 1 && i != verifyBox.getChildren().size() - 1) {
                    verifyBox.getChildren().get(i + 1).requestFocus();
                    break;
                }
            }
        }
    }

    public void verifyBtn(ActionEvent ae) throws IOException {
        warning.setVisible(false);
        StringBuilder verificationCode = new StringBuilder();
        for (Node tf : verifyBox.getChildren()) {
            verificationCode.append(((TextField) tf).getText());
        }
        DatabaseReadCall readCode = AuthService.checkForgetPasswordCode(email, verificationCode.toString());
        readCode.setOnSucceeded(e -> {
            try {
                if (verificationCode.toString().equals(code) && readCode.getValue().next()) {
                    InputStream okImg = new FileInputStream("src/main/resources/com/easymed/images/okIcon.png");
                    InputStream thirdImg = new FileInputStream("src/main/resources/com/easymed/images/icons8-circled-3-240.png");
                    img2.setImage(new Image(okImg));
                    img3.setImage(new Image(thirdImg));
                    FXMLScene fxmlScene = FXMLScene.load("/com/easymed/views/auth/forgetPassword-change-pass.fxml");
                    ForgetPasswordChangePassController controller = (ForgetPasswordChangePassController) fxmlScene.getController();
                    controller.setMainStage(mainStage);
                    controller.setData(email);
                    controller.setMainStage(mainStage);
                    contentArea.getChildren().removeAll();
                    contentArea.getChildren().setAll(fxmlScene.getRoot());
                } else {
                    warning.setVisible(true);
                }
            } catch (IOException ioException) {
                System.out.println("File not found." + ioException.getMessage());
            } catch (SQLException ex) {
                System.out.println("SQL Exception: " + ex.getMessage());
            }
        });

        new Thread(readCode).start();
    }
}
