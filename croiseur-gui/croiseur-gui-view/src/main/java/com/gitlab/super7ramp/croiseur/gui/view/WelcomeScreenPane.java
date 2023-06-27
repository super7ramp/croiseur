/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view;

import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleDetails;
import com.gitlab.super7ramp.croiseur.common.puzzle.SavedPuzzle;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * A welcome screen, allowing to search, open existing or new puzzles.
 */
public final class WelcomeScreenPane extends VBox {

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
    public WelcomeScreenPane() {
        recentPuzzles = new SimpleListProperty<>(this, "recentPuzzles",
                                                 FXCollections.observableArrayList());
        FxmlLoaderHelper.load(this, ResourceBundle.getBundle(getClass().getName()));
    }

    @FXML
    private void initialize() {
        initializeListView();
        initializeOpenButton();
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
     * <p>
     * Note that this list contains all the recent puzzles unfiltered; The puzzles that will be
     * actually displayed will be the puzzles whose metadata contain the
     * {@link #searchTextField searched substring}.
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
     * Initializes {@link #recentPuzzleListView}.
     */
    private void initializeListView() {
        recentPuzzleListView.setCellFactory(l -> new SavedPuzzleListCell());

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
    private Predicate<SavedPuzzle> createSearchPredicate() {
        return puzzle -> {
            final PuzzleDetails details = puzzle.details();
            final String searchText = searchTextField.getText();
            return Stream.of(details.title(), details.author(), details.editor(),
                             details.copyright(),
                             details.date().map(LocalDate::toString).orElse(""))
                         .anyMatch(text -> text.contains(searchText));
        };
    }

    /**
     * Initializes the "Open" button: Make sure open button is disabled if no puzzle is selected.
     */
    private void initializeOpenButton() {
        final SelectionModel<SavedPuzzle> selectionModel = recentPuzzleListView.getSelectionModel();
        final BooleanBinding noPuzzleSelected =
                Bindings.createBooleanBinding(() -> selectionModel.getSelectedItem() == null,
                                              selectionModel.selectedItemProperty());
        openPuzzleButton.disableProperty().bind(noPuzzleSelected);
    }

}
