package org.example.project3.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.example.project3.mongo.MongoDBConnector;
import org.example.project3.util.AlertMessage;
import org.bson.Document;
import org.example.project3.util.PasswordHashing;


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
                alertMessage.alert.show();
            }
            else if (!username.matches("^[a-zA-Z]+$")) {
                alertMessage.ErrorAlert("Логін повинен містити тільки букви");
                alertMessage.alert.show();
            }
            else if (password.length() < 6  || password.length() > 20){
                alertMessage.ErrorAlert("Пароль повинен містити не менше 6 символів і не більше 20 символів");
                alertMessage.alert.show();
            }
            else if (username.length() < 3  || username.length() > 20){
                alertMessage.ErrorAlert("Логін повинен містити не менше 3 символів і не більше 20 символів");
                alertMessage.alert.show();
            }
            else if (!password.equals(confirmPassword)) {
                alertMessage.ErrorAlert("Паролі не співпадають");
                alertMessage.alert.show();
            } else if (mongoDBConnector.existsByName(username)) {
                alertMessage.ErrorAlert("Користувач з таким ім'ям вже існує");
                alertMessage.alert.show();
            } else {
                password = PasswordHashing.hashPassword(password);
                mongoDBConnector.getDatabase().getCollection("user").insertOne(new Document("login", username).append("password", password));
                alertMessage.InformationAlert("Ви успішно зареєструвались");
                alertMessage.alert.show();
            }

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
                alertMessage.alert.show();
            } else if (!mongoDBConnector.existsByName(username)) {
                alertMessage.ErrorAlert("Користувача з таким ім'ям не існує");
                alertMessage.alert.show();
            } else {
                Document user = mongoDBConnector.getDatabase().getCollection("user").find(new Document("login", username)).first();
                if (user != null && PasswordHashing.checkPassword(password, user.getString("password"))) {
                    alertMessage.InformationAlert("Ви успішно увійшли");
                    alertMessage.alert.show();
                } else {
                    alertMessage.ErrorAlert("Неправильний пароль");
                    alertMessage.alert.show();
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
    
}