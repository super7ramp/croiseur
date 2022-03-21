package com.gitlab.super7ramp.crosswords.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The crossword GUI application.
 */
public final class CrosswordGuiApplication extends Application {

    /**
     * Constructor.
     */
    public CrosswordGuiApplication() {
        // Nothing to do
    }

    @Override
    public void start(Stage stage) throws IOException {
        final Parent customGridCreation = FXMLLoader.load(CrosswordGuiApplication.class.getResource(
                "custom-grid-creation.fxml"));
        final Scene scene = new Scene(customGridCreation);
        stage.setTitle("Custom Grid Creation");
        stage.setScene(scene);
        stage.show();
    }
}