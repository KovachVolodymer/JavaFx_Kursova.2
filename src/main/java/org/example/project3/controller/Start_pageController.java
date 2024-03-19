package org.example.project3.controller;

import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class Start_pageController {

    public AnchorPane signup_page;
    public AnchorPane signin_page;

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



}