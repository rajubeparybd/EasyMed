package com.easymed.controllers.admin;

import com.easymed.database.models.User;
import com.easymed.utils.GreetingMaker;
import com.easymed.utils.Helpers;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    private final User user = User.getInstance();

    @FXML
    private ImageView profilePic;

    @FXML
    private Label numOfPatients;

    @FXML
    private Label numOfDoctors;

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
        this.user.setProfilePic(data.get("picture"));
        this.user.setRole(data.get("role"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            VBox sidebar = (VBox) rootPane.getLeft();
            Helpers.toggleMenuClass(sidebar, "dashboard");

            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setTitle(user.getName() + " " + Helpers.getTitle("Dashboard"));
            greetings.setText((new GreetingMaker()).printTimeOfDay());
            name.setText(this.user.getName());
            String dateString = Helpers.getDate();
            String timeString = Helpers.getTime();
            date.setText(dateString + " | " + timeString);
            if (this.user.getProfilePic() != null) {
                File file = new File(this.user.getProfilePic());
                if (file.exists()) {
                    Image image = new Image(file.toURI().toString());
                    profilePic.setImage(image);
                } else
                    profilePic.setImage(null);
            }
        });
    }
}
