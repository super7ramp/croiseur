/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view;

import com.gitlab.super7ramp.croiseur.common.puzzle.SavedPuzzle;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.ResourceBundle;

/**
 * A welcome screen, allowing to search, open existing or new puzzles.
 */
public final class WelcomeScreen extends VBox {

    /** The recent puzzles. */
    private final ListProperty<SavedPuzzle> recentPuzzles;

    /** A text field allowing to search the {@link #recentPuzzleListView} */
    @FXML
    private TextField searchTextField;

    /** A button to create a puzzle from scratch. */
    @FXML
    private Button newPuzzleButton;

    /** A button to open the selected recent puzzle. Disabled if no puzzle selected. */
    @FXML
    private Button openPuzzleButton;

    /** A collection of saved puzzles. */
    @FXML
    private ListView<SavedPuzzle> recentPuzzleListView;

    /**
     * Constructs an instance.
     */
    public WelcomeScreen() {
        recentPuzzles = new SimpleListProperty<>(this, "recentPuzzles",
                                                 FXCollections.observableArrayList());
        FxmlLoaderHelper.load(this, ResourceBundle.getBundle(getClass().getName()));
    }

    @FXML
    private void initialize() {
        recentPuzzleListView.setCellFactory(l -> new SavedPuzzleListCell());
        recentPuzzleListView.setItems(recentPuzzles);
        initializeOpenButton();
        // TODO initialize search text
    }

    /**
     * Returns the "open puzzle button action" property.
     * <p>
     * Use {@link #selectedPuzzleProperty()} to get the currently selected puzzle.
     *
     * @return the "open puzzle button action" property
     */
    public ObjectProperty<EventHandler<ActionEvent>> onOpenPuzzleButtonActionProperty() {
        return openPuzzleButton.onActionProperty();
    }

    /**
     * Returns the "new puzzle button action" property.
     *
     * @return the "new puzzle button action" property
     */
    public ObjectProperty<EventHandler<ActionEvent>> onNewPuzzleButtonActionProperty() {
        return newPuzzleButton.onActionProperty();
    }

    /**
     * Returns the "recent puzzles" property.
     *
     * @return the recent puzzles property
     */
    public ListProperty<SavedPuzzle> recentPuzzles() {
        return recentPuzzles;
    }

    /**
     * Returns the "selected puzzle" property.
     * <p>
     * Property value is {@code null} when no puzzle is selected.
     *
     * @return the selected puzzle property
     */
    public ReadOnlyProperty<SavedPuzzle> selectedPuzzleProperty() {
        return recentPuzzleListView.getSelectionModel().selectedItemProperty();
    }

    /**
     * Makes sure open button is disabled if no puzzle is selected.
     */
    private void initializeOpenButton() {
        final SelectionModel<SavedPuzzle> selectionModel = recentPuzzleListView.getSelectionModel();
        final BooleanBinding noPuzzleSelected =
                Bindings.createBooleanBinding(() -> selectionModel.getSelectedItem() == null,
                                              selectionModel.selectedItemProperty());
        openPuzzleButton.disableProperty().bind(noPuzzleSelected);
    }

}
