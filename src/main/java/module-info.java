module com.easymed {
    requires javafx.controls;
    requires javafx.fxml;
    requires io.github.cdimascio.dotenv.java;
    requires java.mail;
    requires java.sql;
    requires mysql.connector.j;

    opens com.easymed.controllers to javafx.fxml;
    exports com.easymed.controllers;
    opens com.easymed to javafx.fxml;
    exports com.easymed;
}