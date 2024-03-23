package org.example.project3.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.project3.MainApplication;
import org.example.project3.mongo.MongoDBConnector;
import org.example.project3.util.AlertMessage;
import org.bson.Document;
import org.example.project3.util.PasswordHashing;

import java.io.IOException;
import java.util.Objects;


public class Start_pageController {

    public AnchorPane signup_page;
    public AnchorPane signin_page;
    public PasswordField registerСonfirm_Password_field;
    public PasswordField registerPassword_field;
    public TextField registerName_field;
    public TextField loginName_field;
    public PasswordField loginPassword_field;
    public TextField login_ShowPassword;
    public CheckBox login_selectShowPassword;

    Stage stage = new Stage();

    AlertMessage alertMessage = new AlertMessage();

    MongoDBConnector mongoDBConnector = new MongoDBConnector();

    public void initialize() {
        signup_page.setVisible(false);
        signin_page.setVisible(true);
    }

    public void sign_up_clicked(MouseEvent actionEvent) {
        signup_page.setVisible(true);
        signin_page.setVisible(false);
    }

    public void sign_in_clicked(MouseEvent actionEvent) {
        signup_page.setVisible(false);
        signin_page.setVisible(true);
    }

    public void register(ActionEvent actionEvent) {
        try {
            String username = registerName_field.getText().trim();
            String password = registerPassword_field.getText().trim();
            String confirmPassword = registerСonfirm_Password_field.getText().trim();

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                alertMessage.ErrorAlert("Заповніть всі поля");
            }
            else if (!username.matches("^[a-zA-Z]+$")) {
                alertMessage.ErrorAlert("Логін повинен містити тільки букви");
            }
            else if (password.length() < 6  || password.length() > 20){
                alertMessage.ErrorAlert("Пароль повинен містити не менше 6 символів і не більше 20 символів");
            }
            else if (username.length() < 3  || username.length() > 20){
                alertMessage.ErrorAlert("Логін повинен містити не менше 3 символів і не більше 20 символів");
            }
            else if (!password.equals(confirmPassword)) {
                alertMessage.ErrorAlert("Паролі не співпадають");
            } else if (mongoDBConnector.existsByName(username)) {
                alertMessage.ErrorAlert("Користувач з таким ім'ям вже існує");
            } else {
                password = PasswordHashing.hashPassword(password);
                mongoDBConnector.getDatabase().getCollection("user").insertOne(new Document("login", username).append("password", password));
                HomePage(stage);
                Stage currentStage = (Stage) loginName_field.getScene().getWindow();
                currentStage.close();
                alertMessage.InformationAlert("Ви успішно зареєструвались");
            }

            HomePage(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void login(ActionEvent actionEvent) {
        try {
            String username = loginName_field.getText().trim();
            String password = loginPassword_field.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                alertMessage.ErrorAlert("Заповніть всі поля");
            } else if (!mongoDBConnector.existsByName(username)) {
                alertMessage.ErrorAlert("Користувача з таким ім'ям не існує");
            } else {
                Document user = mongoDBConnector.getDatabase().getCollection("user").find(new Document("login", username)).first();
                if (user != null && PasswordHashing.checkPassword(password, user.getString("password"))) {
                    HomePage(stage);
                    Stage currentStage = (Stage) loginName_field.getScene().getWindow();
                    currentStage.close();
                    alertMessage.InformationAlert("Ви успішно увійшли");
                } else {
                    alertMessage.ErrorAlert("Неправильний пароль");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showPassword(ActionEvent actionEvent) {
        if (login_selectShowPassword.isSelected()) {
            login_ShowPassword.setText(loginPassword_field.getText());
            login_ShowPassword.setVisible(true);
            loginPassword_field.setVisible(false);
        } else {
            loginPassword_field.setText(login_ShowPassword.getText());
            loginPassword_field.setVisible(true);
            login_ShowPassword.setVisible(false);
        }
    }

    public void HomePage(Stage stage) throws IOException {
        //візьми теперішню сцену і заміни її на нову
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/project3/HomePage.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
}