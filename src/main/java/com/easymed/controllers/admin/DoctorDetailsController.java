package com.easymed.controllers.admin;

import com.easymed.database.services.DoctorService;
import com.easymed.utils.DatabaseReadCall;
import com.easymed.utils.Helpers;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.json.JSONObject;

import java.io.File;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DoctorDetailsController implements Initializable {

    @FXML
    private Label nameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label dobLabel;
    @FXML
    private Label genderLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label bloodGroupLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label zipLabel;
    @FXML
    private Label bioLabel;
    @FXML
    private Label specialtiesLabel;
    @FXML
    private Label feesLabel;
    @FXML
    private Label educationLabel;
    @FXML
    private Label scheduleLabel;
    @FXML
    private Label experienceLabel;
    @FXML
    private Label designationLabel;
    @FXML
    private ImageView doctorPictureLabel;
    @FXML
    private Label hospitalLabel;
    @FXML
    private Label hospitalAddressLabel;

    private Integer doctorId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            DatabaseReadCall getDoctorInfo = DoctorService.getDoctorInformation(doctorId);
            setDoctorInformation(getDoctorInfo);
            new Thread(getDoctorInfo).start();
        });
    }

    /**
     * set doctor id to local variable
     *
     * @param doctorId doctor id ;
     */
    public void setDoctorId(String doctorId) {
        this.doctorId = Integer.parseInt(doctorId);
    }

    /**
     * set the doctor information to all label
     *
     * @param doctorInformation doctorInformation's
     */
    private void setDoctorInformation(DatabaseReadCall doctorInformation) {
        doctorInformation.setOnSucceeded(event -> {
            try {
                ResultSet resultSet = doctorInformation.getValue();
                if (resultSet.next()) {

                    int id = resultSet.getInt("id");
                    String doctorName = resultSet.getString("name");
                    String doctorEmail = resultSet.getString("email");
                    String doctorDob = resultSet.getString("dob");
                    String doctorGender = resultSet.getString("gender");
                    String doctorPhone = resultSet.getString("phone");
                    String doctorPicture = resultSet.getString("picture");
                    String doctorBloodGroup = resultSet.getString("blood_group");
                    String doctorBio = resultSet.getString("bio");
                    String doctorSpecialties = resultSet.getString("spacialities");
                    int doctorFees = resultSet.getInt("fees");
                    String doctorEducation = resultSet.getString("education");
                    String doctorSchedule = resultSet.getString("schedule");
                    String doctorExperience = resultSet.getString("experience");
                    String doctorDesignation = resultSet.getString("designation");
                    String doctorHospitalName = resultSet.getString("hospital");
                    String doctorHospitalAddress = resultSet.getString("hospital_address");

                    nameLabel.setText(doctorName);
                    emailLabel.setText(doctorEmail);
                    dobLabel.setText(doctorDob == null ? "N/A" : doctorDob);
                    genderLabel.setText(doctorGender);
                    phoneLabel.setText(doctorPhone);
                    bloodGroupLabel.setText(doctorBloodGroup);
                    bioLabel.setText(doctorBio);
                    specialtiesLabel.setText(doctorSpecialties);
                    feesLabel.setText(doctorFees + " Tk");
                    educationLabel.setText(doctorEducation);
                    scheduleLabel.setText(doctorSchedule);
                    experienceLabel.setText(doctorExperience);
                    designationLabel.setText(doctorDesignation);
                    hospitalLabel.setText(doctorHospitalName);
                    hospitalAddressLabel.setText(doctorHospitalAddress);

                    String addressJsonString = resultSet.getString("address");
                    if (addressJsonString != null && Helpers.isValidJson(addressJsonString)) {
                        JSONObject jsonObject = new JSONObject(addressJsonString);
                        String address = jsonObject.getString("address");
                        String city = jsonObject.getString("city");
                        int zip = jsonObject.getInt("zip");
                        addressLabel.setText(address);
                        cityLabel.setText(city);
                        zipLabel.setText(String.valueOf(zip));
                    }
                    if (doctorPicture != null) {
                        File file = new File(doctorPicture);
                        if (file.exists()) {
                            Image image = new Image(file.toURI().toString());
                            doctorPictureLabel.setImage(image);
                        } else
                            doctorPictureLabel.setImage(null);
                    }

                }
            } catch (SQLException e) {
                System.out.println("DoctorDetailsController: SQLException: " + e.getMessage());
            }
        });
    }

}
