package com.easymed.controllers.patient;

import com.easymed.database.models.User;
import com.easymed.utils.GreetingMaker;
import com.easymed.utils.Helpers;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    private final User user = User.getInstance();

    @FXML
    private VBox contentArea;

    @FXML
    private Label welcomeText;

    @FXML
    private BorderPane rootPane;

    @FXML
    private Label date;

    @FXML
    private Label greetings;

    @FXML
    private Rectangle img;

    @FXML
    private Label name;

    @FXML
    private Label age;

    @FXML
    private Label bloodGroup;

    @FXML
    private Label height;

    @FXML
    private Label weight;

    @FXML
    private Label appointDoctor;

    @FXML
    private Label appointHospital;

    @FXML
    private Label appointTime;

    @FXML
    private Label medName;

    @FXML
    private Label Medtime;

    /**
     * Set data to the controller from the previous controller
     *
     * @param data data to be set
     */
    public void setData(HashMap<String, String> data) {
        this.user.setId(Integer.valueOf(data.get("id")));
        this.user.setName(data.get("name"));
        this.user.setEmail(data.get("email"));
        this.user.setRole(data.get("role"));
    }

    /**
     * Switches the scene to the blood banks view when the find blood button is clicked
     *
     * @param event find blood button click event
     */
    @FXML
    void findBlood(ActionEvent event) {
        //TODO: Implement the find blood functionality
        System.out.println("Find Blood");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            greetings.setText((new GreetingMaker()).printTimeOfDay());
            name.setText(this.user.getName());
            String dateString = Helpers.getDate();
            String timeString = Helpers.getTime();
            date.setText(dateString + " | " + timeString);

        });
    }
}
