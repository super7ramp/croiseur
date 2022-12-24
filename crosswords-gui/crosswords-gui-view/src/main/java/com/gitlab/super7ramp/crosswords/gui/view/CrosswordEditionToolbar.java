package com.gitlab.super7ramp.crosswords.gui.view;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

/**
 * A toolbar to edit a crossword grid.
 */
public final class CrosswordEditionToolbar extends ToolBar {

    /** The 'add column' button. */
    @FXML
    private Button addColumnButton;

    /** The 'add row' button. */
    @FXML
    private Button addRowButton;

    /** The 'delete column' button. */
    @FXML
    private Button deleteColumnButton;

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

    /** The 'clear grid structure' menu item. */
    @FXML
    private MenuItem clearGridStructureMenuItem;

    /** The solve button. */
    @FXML
    private Button solveButton;

    /** The 'dictionaries' toggle button. */
    @FXML
    private ToggleButton dictionariesToggleButton;

    /** The grid edition buttons disable property. */
    private final BooleanProperty gridEditionButtonsDisableProperty;

    /**
     * Constructs an instance.
     */
    public CrosswordEditionToolbar() {
        gridEditionButtonsDisableProperty = new SimpleBooleanProperty(this, "gridEdition" +
                "ButtonsDisableProperty", false);

        final String fxmlName = CrosswordEditionToolbar.class.getSimpleName() + ".fxml";
        final URL location = Objects.requireNonNull(getClass().getResource(fxmlName), "Failed to "
                + "locate " + fxmlName);
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
        addColumnButton.disableProperty().bind(gridEditionButtonsDisableProperty);
        addRowButton.disableProperty().bind(gridEditionButtonsDisableProperty);
        deleteColumnButton.disableProperty().bind(gridEditionButtonsDisableProperty);
        deleteRowButton.disableProperty().bind(gridEditionButtonsDisableProperty);
        clearGridMenuButton.disableProperty().bind(gridEditionButtonsDisableProperty);
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
     * Returns the on clear grid structure menu item action property.
     *
     * @return the on clear grid structure menu item action property
     */
    public ObjectProperty<EventHandler<ActionEvent>> onClearGridStructureMenuItemActionProperty() {
        return clearGridStructureMenuItem.onActionProperty();
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
     * Returns the grid edition controls disable property.
     * <p>
     * The controls are the 'add column', 'delete column', 'add row','delete row' and the crossword
     * grid pane itself.
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
