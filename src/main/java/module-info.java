module com.blacksky.blacksky_live_soccer {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires org.json;
    requires java.sql;


    opens com.blacksky.blacksky_live_soccer to javafx.fxml;
    exports com.blacksky.blacksky_live_soccer;
    exports com.blacksky.blacksky_live_soccer.controllers;
    opens com.blacksky.blacksky_live_soccer.controllers to javafx.fxml;
}