package com.gitlab.super7ramp.crosswords.gui.control;

import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Objects;

/**
 * A toolbar to edit a grid.
 */
public final class CrosswordGridEditionToolbar extends ToolBar {

    @FXML
    private Button addColumnButton;

    @FXML
    private Button addRowButton;

    @FXML
    private Button deleteColumnButton;

    @FXML
    private Button deleteRowButton;

    /**
     * Constructs an instance.
     */
    public CrosswordGridEditionToolbar() {
        final String fxmlName = CrosswordGridEditionToolbar.class.getSimpleName() + ".fxml";
        final URL location = Objects.requireNonNull(getClass().getResource(fxmlName), "Failed to "
                + "locate " + fxmlName);
        final FXMLLoader fxmlLoader = new FXMLLoader(location);
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    public ObjectProperty<EventHandler<ActionEvent>> onAddColumnActionProperty() {
        return addColumnButton.onActionProperty();
    }

    public ObjectProperty<EventHandler<ActionEvent>> onAddRowActionProperty() {
        return addRowButton.onActionProperty();
    }

    public ObjectProperty<EventHandler<ActionEvent>> onDeleteColumnActionProperty() {
        return deleteColumnButton.onActionProperty();
    }

    public ObjectProperty<EventHandler<ActionEvent>> onDeleteRowActionProperty() {
        return deleteRowButton.onActionProperty();
    }
}
