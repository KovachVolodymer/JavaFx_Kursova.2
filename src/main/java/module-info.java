module org.example.project3 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.mongodb.driver.core;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires jbcrypt;


    opens org.example.project3 to javafx.fxml;
    exports org.example.project3;
    exports org.example.project3.controller;
    opens org.example.project3.controller to javafx.fxml;
}