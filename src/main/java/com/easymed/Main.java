package com.easymed;

import com.easymed.utils.Helpers;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
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
        stage.show();
    }
}