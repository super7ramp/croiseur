/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui;

import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * A utility class to load the application views.
 */
final class ViewLoader {

    /**
     * Private constructor to prevent instantiation.
     */
    private ViewLoader() {
        // Nothing to do.
    }

    /**
     * Loads a view from a FXML file, using the given controller.
     * <p>
     * View is expected to be named like given controller class name, with "Controller" suffix
     * replaced with "View" and the extension ".fxml".
     * <p>
     * A resource bundle will also be loaded for the controller.
     *
     * @param controller the controller
     * @param <T>        the loaded object type
     * @return the loaded object hierarchy
     * @throws IOException if an error occurs during loading
     */
    static <T> T load(final Object controller) throws IOException {
        final Class<?> clazz = controller.getClass();
        final String viewName = clazz.getSimpleName().replace("Controller", "View") + ".fxml";
        final URL fxmlLocation =
                Objects.requireNonNull(clazz.getResource(viewName), "Failed to locate " + viewName);
        final FXMLLoader loader = new FXMLLoader(fxmlLocation);
        loader.setControllerFactory(unusedClassParam -> controller);
        return loader.load();
    }
}
