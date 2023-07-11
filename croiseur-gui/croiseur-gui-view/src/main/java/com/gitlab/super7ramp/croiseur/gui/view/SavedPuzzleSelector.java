/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view;

import com.gitlab.super7ramp.croiseur.gui.view.model.SavedPuzzleViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * A puzzle selector, allowing to search, open existing or new puzzles.
 */
public final class SavedPuzzleSelector extends VBox {

    /** The recent puzzles. */
    private final ListProperty<SavedPuzzleViewModel> recentPuzzles;

    /** The action to run when the open button of the selected puzzle cell is pressed. */
    private final ObjectProperty<EventHandler<ActionEvent>> onOpenSelectedPuzzleButtonAction;

    /** The action to run when the delete button of the selected puzzle cell is pressed. */
    private final ObjectProperty<EventHandler<ActionEvent>> onDeleteSelectedPuzzleButtonAction;

    /** A text field allowing to search the {@link #recentPuzzleListView} */
    @FXML
    private TextField searchTextField;

    /** A button to create a puzzle from scratch. */
    @FXML
    private Button newPuzzleButton;

    /** A collection of saved puzzles. */
    @FXML
    private ListView<SavedPuzzleViewModel> recentPuzzleListView;

    /**
     * Constructs an instance.
     */
    public SavedPuzzleSelector() {
        recentPuzzles = new SimpleListProperty<>(this, "recentPuzzles",
                                                 FXCollections.observableArrayList());
        onOpenSelectedPuzzleButtonAction =
                new SimpleObjectProperty<>(this, "onEditSelectedPuzzleButtonAction");
        onDeleteSelectedPuzzleButtonAction =
                new SimpleObjectProperty<>(this, "onDeleteSelectedPuzzleButtonAction");
        FxmlLoaderHelper.load(this, ResourceBundle.getBundle(getClass().getName()));
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
     * Returns the "open selected puzzle button action" property.
     * <p>
     * Use {@link #selectedPuzzleProperty()} to get the currently selected puzzle.
     *
     * @return the "open selected puzzle button action" property
     */
    public ObjectProperty<EventHandler<ActionEvent>> onOpenSelectedPuzzleButtonActionProperty() {
        return onOpenSelectedPuzzleButtonAction;
    }

    /**
     * Returns the "delete selected puzzle button action" property.
     * <p>
     * Use {@link #selectedPuzzleProperty()} to get the currently selected puzzle.
     *
     * @return the "delete selected puzzle button action" property
     */
    public ObjectProperty<EventHandler<ActionEvent>> onDeleteSelectedPuzzleButtonActionProperty() {
        return onDeleteSelectedPuzzleButtonAction;
    }

    /**
     * Returns the "recent puzzles" property.
     * <p>
     * Note that this list contains all the recent puzzles unfiltered; The puzzles that will be
     * actually displayed will be the puzzles whose metadata contain the
     * {@link #searchTextField searched substring}.
     *
     * @return the recent puzzles property
     */
    public ListProperty<SavedPuzzleViewModel> recentPuzzles() {
        return recentPuzzles;
    }

    /**
     * Returns the "selected puzzle" property.
     * <p>
     * Property value is {@code null} when no puzzle is selected.
     *
     * @return the selected puzzle property
     */
    public ReadOnlyProperty<SavedPuzzleViewModel> selectedPuzzleProperty() {
        return recentPuzzleListView.getSelectionModel().selectedItemProperty();
    }

    /**
     * Initializes the control after object hierarchy has been loaded from FXML.
     */
    @FXML
    private void initialize() {
        initializeListViewCells();
        initializeListViewSearch();
    }

    /**
     * Initializes {@link #recentPuzzleListView} cell factory.
     */
    private void initializeListViewCells() {
        recentPuzzleListView.setCellFactory(l -> {
            final var listCell = new SavedPuzzleListCell();
            listCell.onOpenButtonActionProperty().bind(onOpenSelectedPuzzleButtonAction);
            listCell.onDeleteButtonActionProperty().bind(onDeleteSelectedPuzzleButtonAction);
            return listCell;
        });
    }

    /**
     * Initializes {@link #recentPuzzleListView} filter using {@link #searchTextField}.
     */
    private void initializeListViewSearch() {
        final var filteredPuzzles = new FilteredList<>(recentPuzzles);
        final var searchPredicate = Bindings.createObjectBinding(this::createSearchPredicate,
                                                                 searchTextField.textProperty());
        filteredPuzzles.predicateProperty().bind(searchPredicate);
        recentPuzzleListView.setItems(filteredPuzzles);
    }

    /**
     * Returns a new predicate matching puzzles whose metadata contains the current
     * {@link #searchTextField}'s text.
     *
     * @return a new predicate matching puzzles whose metadata contains the current
     * {@link #searchTextField}'s text
     */
    private Predicate<SavedPuzzleViewModel> createSearchPredicate() {
        return puzzle -> {
            // Convert everything to lowercase so that search is case-insensitive
            final String searchText = searchTextField.getText().toLowerCase();
            return Stream.of(puzzle.title(), puzzle.author(), puzzle.editor(),
                             puzzle.copyright(), puzzle.date())
                         .anyMatch(text -> text.toLowerCase().contains(searchText));
        };
    }
}
