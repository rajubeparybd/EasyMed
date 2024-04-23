module com.easymed {
    requires javafx.controls;
    requires javafx.fxml;
    requires io.github.cdimascio.dotenv.java;
    requires java.mail;
    requires java.sql;
    requires mysql.connector.j;
    requires org.controlsfx.controls;
    requires MaterialFX;
    requires com.jfoenix;

    opens com.easymed.controllers.auth to javafx.fxml;
    exports com.easymed.controllers.auth;
    opens com.easymed.controllers.patient to javafx.fxml;
    exports com.easymed.controllers.patient;
    opens com.easymed.controllers.admin to javafx.fxml;
    exports com.easymed.controllers.admin;
    opens com.easymed.controllers.doctor to javafx.fxml;
    exports com.easymed.controllers.doctor;
    opens com.easymed to javafx.fxml;
    exports com.easymed;
}