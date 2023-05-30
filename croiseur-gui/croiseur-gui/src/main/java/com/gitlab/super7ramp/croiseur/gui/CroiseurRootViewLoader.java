/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * A utility class to load the application views.
 */
final class CroiseurRootViewLoader {

    /** The path to the fxml file. */
    private static final String FXML_LOCATION = "CroiseurRootView.fxml";

    /**
     * Private constructor to prevent instantiation.
     */
    private CroiseurRootViewLoader() {
        // Nothing to do.
    }

    /**
     * Loads the application views.
     *
     * @param rootController the root controller
     * @return the loaded object hierarchy
     * @throws IOException if an error occurs during loading
     */
    static Parent load(final CroiseurRootController rootController) throws IOException {
        final FXMLLoader loader = new FXMLLoader();
        final URL fxmlLocation =
                Objects.requireNonNull(CroiseurRootViewLoader.class.getResource(FXML_LOCATION));
        loader.setLocation(fxmlLocation);
        final ResourceBundle resourceBundle =
                ResourceBundle.getBundle(CroiseurRootController.class.getName());
        loader.setResources(resourceBundle);
        loader.setControllerFactory(unusedClassParam -> rootController);
        return loader.load();
    }
}
