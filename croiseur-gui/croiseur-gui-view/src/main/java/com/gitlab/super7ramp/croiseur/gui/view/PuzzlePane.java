/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view;

import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;

import java.util.ResourceBundle;

/**
 * A pane to edit puzzle details.
 */
public final class PuzzlePane extends Accordion {

    @FXML
    private TextField title;

    @FXML
    private TextField author;

    @FXML
    private TextField editor;

    @FXML
    private TextField copyright;

    @FXML
    private DatePicker date;

    /**
     * Constructs an instance.
     */
    public PuzzlePane() {
        FxmlLoaderHelper.load(this, ResourceBundle.getBundle(getClass().getName()));
    }

    @FXML
    private void initialize() {
        initializeTitledPanes();
    }

    /**
     * Initializes titled panes: Make sure always one titled pane is expanded.
     */
    private void initializeTitledPanes() {
        expandedPaneProperty().addListener((observable, oldValue, newValue) -> {
            final boolean hasExpanded = getPanes().stream().anyMatch(TitledPane::isExpanded);
            if (!hasExpanded && oldValue != null) {
                Platform.runLater(() -> setExpandedPane(oldValue));
            }
        });
        setExpandedPane(getPanes().get(0));
    }

    /**
     * Returns the title property.
     *
     * @return the title property
     */
    public StringProperty titleProperty() {
        return title.textProperty();
    }

    /**
     * Returns the author property.
     *
     * @return the author property
     */
    public StringProperty authorProperty() {
        return author.textProperty();
    }

    /**
     * Returns the editor property.
     *
     * @return the editor property
     */
    public StringProperty editorProperty() {
        return editor.textProperty();
    }

    /**
     * Returns the copyright property.
     *
     * @return the copyright property
     */
    public StringProperty copyrightProperty() {
        return copyright.textProperty();
    }

    /**
     * Returns the date property.
     *
     * @return the date property
     */
    public StringProperty dateProperty() {
        return date.getEditor().textProperty();
    }
}
