package com.easymed.controllers.admin;

import com.easymed.database.models.UserInfo;
import com.easymed.database.services.UserService;
import com.easymed.enums.Role;
import com.easymed.utils.DatabaseReadCall;
import com.easymed.utils.FXMLScene;
import com.easymed.utils.Helpers;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class PatientController implements Initializable {


    @FXML
    private BorderPane rootPane;

    @FXML
    private TextField searchBox;

    @FXML
    private GridPane patientContainer;

    @FXML
    private TableView<UserInfo> tableView;

    @FXML
    private TableColumn<UserInfo, String> userIdColumn;

    @FXML
    private TableColumn<UserInfo, Integer> serialCount;

    @FXML
    private TableColumn<UserInfo, String> nameColumn;

    @FXML
    private TableColumn<UserInfo, String> emailColumn;

    @FXML
    private TableColumn<UserInfo, String> phoneColumn;

    @FXML
    private TableColumn<UserInfo, String> ageColumn;

    @FXML
    private TableColumn<UserInfo, String> genderColumn;

    @FXML
    private TableColumn<UserInfo, String> bloodGroupColumn;

    @FXML
    private TableColumn<UserInfo, String> addressColumn;


    @FXML
    private TableColumn<UserInfo, String> createDateColumn;

    @FXML
    private TableColumn<UserInfo, String> updateDateColumn;

    /**
     * Get the property of the data
     *
     * @param data Integer data
     *
     * @return SimpleObjectProperty
     */
    private static SimpleObjectProperty<Integer> getProperty(Integer data) {
        return new SimpleObjectProperty<>(data);
    }

    /**
     * Get the property of the data
     *
     * @param data String data
     *
     * @return SimpleObjectProperty
     */
    private static SimpleObjectProperty<String> getProperty(String data) {
        return new SimpleObjectProperty<>(data);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            VBox sidebar = (VBox) rootPane.getLeft();
            Helpers.toggleMenuClass(sidebar, "patients");
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setTitle(Helpers.getTitle("Patient Management"));
            setupTableView();
        });
    }

    /**
     * Search for a patient
     *
     * @param keyEvent Search box key event
     */
    @FXML
    public void search(KeyEvent keyEvent) {
        String query = searchBox.getText();
        if (query.length() > 3) {
            dataModelProperties();
            DatabaseReadCall getSearchInformation = UserService.searchUsersInfo(Role.getText(Role.PATIENT), query);
            tableInformation(getSearchInformation);
            new Thread(getSearchInformation).start();
        } else {
            setupTableView();
        }
    }

    /**
     * Show the form to add a new patient
     *
     * @param actionEvent Add Patient key event
     */
    @FXML
    public void addPatient(ActionEvent actionEvent) {
        FXMLScene.switchScene("/com/easymed/views/admin/add-patient.fxml", (Node) actionEvent.getSource());
    }

    /*
     *  for show patient information in a table
     * */
    private void setupTableView() {
        dataModelProperties();
        DatabaseReadCall getPatientsInfo = UserService.getUsersInfo(Role.getText(Role.PATIENT));
        tableInformation(getPatientsInfo);
        new Thread(getPatientsInfo).start();
    }

    /*
     * Bind columns to data model properties
     * */
    private void dataModelProperties() {
        serialCount.setCellValueFactory(data -> getProperty(data.getValue().getCount()));
        userIdColumn.setCellValueFactory(data -> getProperty(data.getValue().getUserId()));
        nameColumn.setCellValueFactory(data -> getProperty(data.getValue().getName()));
        emailColumn.setCellValueFactory(data -> getProperty(data.getValue().getEmail()));
        phoneColumn.setCellValueFactory(data -> getProperty(data.getValue().getPhone()));
        ageColumn.setCellValueFactory(data -> getProperty(data.getValue().getDob()));
        genderColumn.setCellValueFactory(data -> getProperty(data.getValue().getGender()));
        bloodGroupColumn.setCellValueFactory(data -> getProperty(data.getValue().getBloodGroup()));
        addressColumn.setCellValueFactory(data -> getProperty(data.getValue().getAddress()));
    }

    /*
     * that holds table information
     * */
    private void tableInformation(DatabaseReadCall information) {
        information.setOnSucceeded(event -> {
            ResultSet resultSet = information.getValue();
            if (resultSet != null) {
                ObservableList<UserInfo> patients = FXCollections.observableArrayList();
                try {
                    Integer count = 0;
                    while (resultSet.next()) {
                        count++;
                        String phone = "N/A";
                        String blood = "N/A";
                        String dob = "N/A";
                        String gender = "N/A";

                        String full_address = "N/A";
                        String addressJsonString = resultSet.getString("address");
                        if (addressJsonString != null && Helpers.isValidJson(addressJsonString)) {
                            JSONObject jsonObject = new JSONObject(addressJsonString);
                            String address = jsonObject.getString("address");
                            String city = jsonObject.getString("city");
                            int zip = jsonObject.getInt("zip");
                            full_address = address + ", " + city + ", " + zip + ".";
                        }
                        String Phone_No = resultSet.getString("phone");
                        if (Phone_No != null) {
                            phone = Phone_No;
                        }
                        String blood_group = resultSet.getString("blood_group");
                        if (blood_group != null) {
                            blood = blood_group;
                        }
                        String date_of_birth = resultSet.getString("dob");
                        if (date_of_birth != null) {
                            dob = date_of_birth;
                        }
                        String user_gender = resultSet.getString("gender");
                        if (user_gender != null) {
                            gender = user_gender;
                        }

                        UserInfo patient = new UserInfo(
                                count,
                                resultSet.getString("id"),
                                resultSet.getString("name"),
                                resultSet.getString("email"),
                                phone,
                                full_address,
                                blood,
                                dob,
                                gender
                        );
                        patients.add(patient);
                    }
                } catch (SQLException e) {
                    System.out.println("PatientController: SQLException: " + e.getMessage());
                }
                tableView.setItems(patients);
            }
        });
    }
}
