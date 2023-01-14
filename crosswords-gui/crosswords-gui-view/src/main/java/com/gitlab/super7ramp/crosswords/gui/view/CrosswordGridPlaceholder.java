/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.gui.view;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * A placeholder for the crossword grid.
 */
public final class CrosswordGridPlaceholder extends VBox {

    /** The wrapping width. */
    private final DoubleProperty wrappingWidth;

    /** The error text. */
    @FXML
    private Text errorText;

    /** The description text. */
    @FXML
    private Text descriptionText;

    /** The advice text. */
    @FXML
    private Text adviceText;

    /**
     * Constructs an instance.
     */
    public CrosswordGridPlaceholder() {
        wrappingWidth = new SimpleDoubleProperty(this, "wrappingWidth", 0.0);
        final String fxmlName = CrosswordGridPlaceholder.class.getSimpleName() + ".fxml";
        final URL location = Objects.requireNonNull(getClass().getResource(fxmlName), "Failed to "
                + "locate " + fxmlName);
        final ResourceBundle resourceBundle =
                ResourceBundle.getBundle(CrosswordGridPlaceholder.class.getName());
        final FXMLLoader fxmlLoader = new FXMLLoader(location, resourceBundle);
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    /**
     * Returns the wrapping width property.
     *
     * @return the wrapping width property
     */
    public DoubleProperty wrappingWidthProperty() {
        return wrappingWidth;
    }

    /**
     * Initializes the widget.
     */
    @FXML
    private void initialize() {
        errorText.wrappingWidthProperty().bind(wrappingWidth);
        descriptionText.wrappingWidthProperty().bind(wrappingWidth);
        adviceText.wrappingWidthProperty().bind(wrappingWidth);
    }
}
