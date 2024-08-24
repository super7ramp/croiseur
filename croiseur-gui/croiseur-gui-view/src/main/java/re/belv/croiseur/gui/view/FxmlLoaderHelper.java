/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.view;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;

/**
 * Common boilerplate code to load object hierarchies from FXML files.
 */
final class FxmlLoaderHelper {

    /** Private constructor to prevent instantiation, static methods only. */
    private FxmlLoaderHelper() {
        // Nothing to do.
    }

    /**
     * Loads given object hierarchy from FXML.
     *
     * @param object the object for which to load the object hierarchy; FXML file is assumed to be
     *               named like this object's class simple name, with the extension ".fxml", and
     *               located in the same package
     * @throws NullPointerException if FXML file cannot be located
     * @throws UncheckedIOException if an error occurs during loading
     */
    static void load(final Object object) {
        load(object, null);
    }

    /**
     * Loads given object hierarchy from FXML.
     *
     * @param object         the object for which to load the object hierarchy; FXML file is assumed
     *                       to be named like this object's class simple name, with the extension
     *                       ".fxml", and located in the same package
     * @param resourceBundle the resource bundle to use to resolve resource key attribute values;
     *                       may be {@code null} if not necessary
     * @throws NullPointerException if FXML file cannot be located
     * @throws UncheckedIOException if an error occurs during loading
     */
    static void load(final Object object, final ResourceBundle resourceBundle) {
        final Class<?> clazz = object.getClass();
        final String fxmlName = clazz.getSimpleName() + ".fxml";
        final URL location = Objects.requireNonNull(clazz.getResource(fxmlName), "Failed to locate " + fxmlName);
        final FXMLLoader fxmlLoader = new FXMLLoader(location, resourceBundle);
        fxmlLoader.setRoot(object);
        fxmlLoader.setController(object);
        // Only to help SceneBuilder find other custom controls shipped in the same jar
        fxmlLoader.setClassLoader(clazz.getClassLoader());
        try {
            fxmlLoader.load();
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }
}
