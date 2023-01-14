/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * A utility class to load the application views.
 */
final class CrosswordSolverViewLoader {

    /** The path to the fxml file. */
    private static final String FXML_LOCATION = "CrosswordSolverRoot.fxml";

    /**
     * Private constructor to prevent instantiation.
     */
    private CrosswordSolverViewLoader() {
        // Nothing to do.
    }

     /**
     * Loads the application views.
     *
     * @return the loaded object hierarchy
     * @throws IOException if an error occurs during loading
     */
    static Parent load(final CrosswordSolverRootController crosswordSolverController) throws IOException {
        final FXMLLoader loader = new FXMLLoader();
        final URL fxmlLocation = Objects.requireNonNull(CrosswordGuiApplication.class.getResource(
                FXML_LOCATION));
        loader.setLocation(fxmlLocation);
        final ResourceBundle resourceBundle =
                ResourceBundle.getBundle(CrosswordSolverRootController.class.getName());
        loader.setResources(resourceBundle);
        loader.setControllerFactory(unusedClassParam -> crosswordSolverController);
        return loader.load();
    }
}
