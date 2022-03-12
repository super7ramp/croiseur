package com.gitlab.super7ramp.crosswords.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CustomGridCreationController {

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}