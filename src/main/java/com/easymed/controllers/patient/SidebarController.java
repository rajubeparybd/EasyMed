package com.easymed.controllers.patient;

import com.easymed.database.models.User;
import com.easymed.utils.FXMLScene;
import com.easymed.utils.Helpers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class SidebarController implements Initializable {

    private final User user = User.getInstance();

    @FXML
    private VBox sidebar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * Switches the scene to the dashboard view when the dashboard button is clicked
     *
     * @param actionEvent dashboard button click event
     */
    @FXML
    public void dashboard(ActionEvent actionEvent) {
        Helpers.toggleMenuClass(actionEvent);
        FXMLScene.switchScene("/com/easymed/views/patient/dashboard.fxml", (Node) actionEvent.getSource(), user.getUserData());
    }

    /**
     * Switches the scene to the records view when the records button is clicked
     *
     * @param actionEvent records button click event
     */
    @FXML
    public void records(ActionEvent actionEvent) {
        Helpers.toggleMenuClass(actionEvent);
        //TODO: Implement records view
        System.out.println("Records");
    }

    /**
     * Switches the scene to the chat box view when the chat box button is clicked
     *
     * @param actionEvent chat box button click event
     */
    @FXML
    public void chatBox(ActionEvent actionEvent) {
        Helpers.toggleMenuClass(actionEvent);
        //TODO: Implement chat box view
        System.out.println("Chat Box");
    }


    /**
     * Switches the scene to the doctors list view when the doctors button is clicked
     *
     * @param actionEvent doctors button click event
     */
    @FXML
    public void doctors(ActionEvent actionEvent) {
        Helpers.toggleMenuClass(actionEvent);
        FXMLScene.switchScene("/com/easymed/views/patient/doctors-list.fxml", (Node) actionEvent.getSource());
    }

    /**
     * Switches the scene to the profile view when the profile button is clicked
     *
     * @param actionEvent profile button click event
     */
    @FXML
    public void profile(ActionEvent actionEvent) {
        Helpers.toggleMenuClass(actionEvent);
        FXMLScene.switchScene("/com/easymed/views/patient/profile.fxml", (Node) actionEvent.getSource());
    }

    /**
     * Logs out the user when the logout button is clicked
     *
     * @param actionEvent logout button click event
     */
    @FXML
    public void logout(ActionEvent actionEvent) {
        FXMLScene.switchScene("/com/easymed/views/auth/login.fxml", (Node) actionEvent.getSource());
    }
}
