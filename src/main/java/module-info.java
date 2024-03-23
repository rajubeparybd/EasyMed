module com.easymed.easymed {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.easymed.controllers to javafx.fxml;
    exports com.easymed.controllers;
    opens com.easymed to javafx.fxml;
    exports com.easymed;
}