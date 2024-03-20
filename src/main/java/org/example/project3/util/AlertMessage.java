package org.example.project3.util;

import javafx.scene.control.Alert;

public class AlertMessage {
    public Alert alert;

    public void ErrorAlert(String message) {
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Помилка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();

    }

    public void InformationAlert(String message) {
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Інформація");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

}
