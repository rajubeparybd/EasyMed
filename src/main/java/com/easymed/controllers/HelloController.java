package com.easymed.controllers;

import io.github.cdimascio.dotenv.Dotenv;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        String appName = Dotenv.load().get("APP_NAME");
        welcomeText.setText("Welcome to " + appName + "!");
    }
}