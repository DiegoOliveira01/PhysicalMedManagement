module com.physicalmed.physicalmedmanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires java.desktop;


    opens com.physicalmed.physicalmedmanagement to javafx.fxml;
    //exports com.physicalmed.physicalmedmanagement;
    exports com.physicalmed.physicalmedmanagement.utils;
    opens com.physicalmed.physicalmedmanagement.utils to javafx.fxml;
    exports com.physicalmed.physicalmedmanagement.controller;
    opens com.physicalmed.physicalmedmanagement.controller to javafx.fxml;
    exports com.physicalmed.physicalmedmanagement.model;
    opens com.physicalmed.physicalmedmanagement.model to javafx.fxml;
    exports com.physicalmed.physicalmedmanagement.view;
    opens com.physicalmed.physicalmedmanagement.view to javafx.fxml;
    exports com.physicalmed.physicalmedmanagement.viewmodel;
    opens com.physicalmed.physicalmedmanagement.viewmodel to javafx.fxml;
}