/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.gui.view;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Stream;

/**
 * A toolbar to edit a crossword grid.
 * <p>
 * The toolbar has two 'modes':
 * <ul>
 *     <li>Default mode: 'Resize grid (not toggled)', 'Clear grid', 'Solve' and 'Dictionaries' are
 *     visible.</li>
 *     <li>Resize mode: When 'Resize grid' is toggled, then it makes visible the 'Add column',
 *     'Add row', 'Delete row', 'Delete column' and 'Delete grid' buttons and hides the other
 *     ones.</li>
 * </ul>
 */
public final class CrosswordEditionToolbar extends ToolBar {

    /** The grid edition buttons disable property. */
    private final BooleanProperty gridEditionButtonsDisableProperty;

    /** The 'resize grid' button. When toggled, the toolbar switches to 'resize mode'. */
    @FXML
    private ToggleButton resizeGridButton;

    /** The 'add column' button. */
    @FXML
    private Button addColumnButton;

    /** The 'add row' button. */
    @FXML
    private Button addRowButton;

    /** The 'delete column' button. */
    @FXML
    private Button deleteColumnButton;

    /** The 'delete grid' button. */
    @FXML
    private Button deleteGridButton;

    /** The 'delete row' button. */
    @FXML
    private Button deleteRowButton;

    /** The 'clear grid' menu button. */
    @FXML
    private MenuButton clearGridMenuButton;

    /** The 'clear grid letters' menu item. */
    @FXML
    private MenuItem clearGridLettersMenuItem;

    /** The 'clear grid content' menu item. */
    @FXML
    private MenuItem clearGridContentMenuItem;

    /** The solve button. */
    @FXML
    private Button solveButton;

    /** The 'dictionaries' toggle button. */
    @FXML
    private ToggleButton dictionariesToggleButton;

    /**
     * Constructs an instance.
     */
    public CrosswordEditionToolbar() {
        gridEditionButtonsDisableProperty = new SimpleBooleanProperty(this, "gridEdition" +
                "ButtonsDisableProperty", false);

        final String fxmlName = CrosswordEditionToolbar.class.getSimpleName() + ".fxml";
        final URL location = Objects.requireNonNull(getClass().getResource(fxmlName),
                "Failed to locate " + fxmlName);
        final ResourceBundle resourceBundle =
                ResourceBundle.getBundle(CrosswordEditionToolbar.class.getName());
        final FXMLLoader fxmlLoader = new FXMLLoader(location, resourceBundle);
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    @FXML
    private void initialize() {
        Stream.of(resizeGridButton, addColumnButton, addRowButton, deleteColumnButton,
                      deleteRowButton, deleteGridButton, clearGridMenuButton)
              .map(Node::disableProperty)
              .forEach(disabledProperty -> disabledProperty.bind(gridEditionButtonsDisableProperty));
    }

    /**
     * Returns the on add column button action property.
     *
     * @return the on add column button action property
     */
    public ObjectProperty<EventHandler<ActionEvent>> onAddColumnActionButtonProperty() {
        return addColumnButton.onActionProperty();
    }

    /**
     * Returns the on add row button action property.
     *
     * @return the on add row button action property
     */
    public ObjectProperty<EventHandler<ActionEvent>> onAddRowActionButtonProperty() {
        return addRowButton.onActionProperty();
    }

    /**
     * Returns the on delete column button action property.
     *
     * @return the on delete column button action property
     */
    public ObjectProperty<EventHandler<ActionEvent>> onDeleteColumnActionButtonProperty() {
        return deleteColumnButton.onActionProperty();
    }

    /**
     * Returns the on delete row button action property.
     *
     * @return the on delete row button action property
     */
    public ObjectProperty<EventHandler<ActionEvent>> onDeleteRowActionButtonProperty() {
        return deleteRowButton.onActionProperty();
    }

    /**
     * Returns the on clear grid letters menu item action property.
     *
     * @return the on clear grid letters menu item action property
     */
    public ObjectProperty<EventHandler<ActionEvent>> onClearGridLettersMenuItemActionProperty() {
        return clearGridLettersMenuItem.onActionProperty();
    }

    /**
     * Returns the on clear grid content menu item action property.
     *
     * @return the on clear grid content menu item action property
     */
    public ObjectProperty<EventHandler<ActionEvent>> onClearGridContentMenuItemActionProperty() {
        return clearGridContentMenuItem.onActionProperty();
    }

    /**
     * Returns the on delete grid action property.
     *
     * @return the on delete grid action property.
     */
    public ObjectProperty<EventHandler<ActionEvent>> onDeleteGridActionProperty() {
        return deleteGridButton.onActionProperty();
    }

    /**
     * Returns the on solve button action property.
     *
     * @return the on solve button action property.
     */
    public ObjectProperty<EventHandler<ActionEvent>> onSolveButtonActionProperty() {
        return solveButton.onActionProperty();
    }

    /**
     * Returns the dictionaries toggle button selected property.
     *
     * @return the dictionaries toggle button selected property
     */
    public BooleanProperty dictionariesToggleButtonSelectedProperty() {
        return dictionariesToggleButton.selectedProperty();
    }

    /**
     * Returns the resize mode property, which is basically the selected property of the 'resize
     * grid' button.
     *
     * @return the resize mode property
     */
    public BooleanProperty resizeModeProperty() {
        return resizeGridButton.selectedProperty();
    }

    /**
     * Returns the grid edition controls disable property.
     * <p>
     * The controls are 'resize grid', 'add column', 'delete column', 'add row','delete row' and
     * 'clear grid'.
     *
     * @return the grid edition controls disable property
     */
    public BooleanProperty gridEditionButtonsDisableProperty() {
        return gridEditionButtonsDisableProperty;
    }

    /**
     * Returns the solve button disable property.
     *
     * @return the solve button disable property.
     */
    public BooleanProperty solveButtonDisableProperty() {
        return solveButton.disableProperty();
    }

    /**
     * Returns the solve button text property.
     *
     * @return the solve button text property
     */
    public StringProperty solveButtonTextProperty() {
        return solveButton.textProperty();
    }
}
