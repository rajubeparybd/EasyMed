package com.easymed.controllers.doctor;

import com.easymed.database.models.User;
import com.easymed.utils.GreetingMaker;
import com.easymed.utils.Helpers;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    private final User user = User.getInstance();

    @FXML
    private TextField searchBox;

    @FXML
    private VBox appointmentListContainer;

    @FXML
    private Label patients;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setTitle(user.getName() + " " + Helpers.getTitle("Dashboard"));
            greetings.setText((new GreetingMaker()).printTimeOfDay());
            name.setText(this.user.getName());
            String dateString = Helpers.getDate();
            String timeString = Helpers.getTime();
            date.setText(dateString + " | " + timeString);
        });
    }


    /**
     * Search for a patient
     *
     * @param keyEvent key event
     */
    @FXML
    public void search(KeyEvent keyEvent) {
    }
}
