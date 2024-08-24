/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;

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
     * Resource file is expected be named like given controller class name, without its "Controller"
     * suffix.
     *
     * @param controller the controller
     * @param <T>        the loaded object type
     * @return the loaded object hierarchy
     * @throws IOException if an error occurs during loading
     */
    static <T> T load(final Object controller) throws IOException {
        final Class<?> clazz = controller.getClass();

        final String viewName = clazz.getSimpleName().replace("Controller", "View") + ".fxml";
        final URL fxmlLocation = Objects.requireNonNull(clazz.getResource(viewName), "Failed to locate " + viewName);

        final String resourceBundleName = clazz.getName().replace("Controller", "");
        final ResourceBundle resourceBundle = ResourceBundle.getBundle(resourceBundleName);

        final FXMLLoader loader = new FXMLLoader(fxmlLocation, resourceBundle);
        loader.setControllerFactory(unusedClassParam -> controller);
        return loader.load();
    }
}
