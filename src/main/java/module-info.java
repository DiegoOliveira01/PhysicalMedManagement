module com.physicalmed.physicalmedmanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;


    opens com.physicalmed.physicalmedmanagement to javafx.fxml;
    exports com.physicalmed.physicalmedmanagement;
    exports com.physicalmed.physicalmedmanagement.utils;
    opens com.physicalmed.physicalmedmanagement.utils to javafx.fxml;
}