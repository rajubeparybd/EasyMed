package com.easymed;

import com.easymed.database.models.User;
import com.easymed.database.services.AuthService;
import com.easymed.utils.DatabaseWriteCall;
import com.easymed.utils.Helpers;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    /**
     * Main method to launch the application
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/easymed/images/EasyMedIcon.png")));
        stage.getIcons().add(icon);
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/auth/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle(Helpers.getTitle("Application"));
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {
            event.consume();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Are you sure you want to exit?");
            alert.setContentText("Any unsaved changes will be lost.");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    DatabaseWriteCall deleteCode = AuthService.deleteForgetPasswordAllCode(User.getInstance().getEmail());
                    deleteCode.getInsertedRows();
                    stage.close();
                }
            });
        });
        stage.show();
    }
}