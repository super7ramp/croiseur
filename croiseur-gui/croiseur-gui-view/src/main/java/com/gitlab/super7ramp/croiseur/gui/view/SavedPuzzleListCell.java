/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view;

import com.gitlab.super7ramp.croiseur.gui.view.model.SavedPuzzleViewModel;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

import java.util.ResourceBundle;

/**
 * A specialized {@link ListCell} for displaying saved puzzles.
 */
public final class SavedPuzzleListCell extends ListCell<SavedPuzzleViewModel> {

    /** The graphic container, when element is present. */
    @FXML
    private HBox containerHBox;

    /** The card displaying puzzle information, child of {@link #containerHBox}. */
    @FXML
    private SavedPuzzleCard card;

    /** The action buttons. */
    @FXML
    private Button editButton, deleteButton;

    /**
     * Constructs an instance.
     */
    public SavedPuzzleListCell() {
        FxmlLoaderHelper.load(this, ResourceBundle.getBundle(getClass().getName()));
    }

    /**
     * The edit button's action.
     *
     * @return the property to represent the edit button's action
     */
    public ObjectProperty<EventHandler<ActionEvent>> onEditButtonActionProperty() {
        return editButton.onActionProperty();
    }

    /**
     * The delete button's action.
     *
     * @return the property to represent the delete button's action
     */
    public ObjectProperty<EventHandler<ActionEvent>> onDeleteButtonActionProperty() {
        return deleteButton.onActionProperty();
    }

    @Override
    protected void updateItem(final SavedPuzzleViewModel item, final boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
            card.reset();
        } else {
            card.set(item);
            setGraphic(containerHBox);
            setText(null);
        }
    }

}