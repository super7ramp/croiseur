/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view;

import com.gitlab.super7ramp.croiseur.gui.view.control.cell.TextFieldListCell;
import com.gitlab.super7ramp.croiseur.gui.view.model.ClueViewModel;
import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionModel;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

import java.util.ResourceBundle;
import java.util.function.Function;

/**
 * Clues pane.
 */
public final class CluesPane extends HBox {

    /** Converter for user clue (main text). */
    private static final StringConverter<ClueViewModel> USER_CLUE_STRING_CONVERTER =
            new StringConverter<>() {
                @Override
                public String toString(final ClueViewModel model) {
                    return model.userContent();
                }

                @Override
                public ClueViewModel fromString(final String value) {
                    final ClueViewModel clueViewModel = new ClueViewModel();
                    clueViewModel.userContent(value);
                    // System content is lost but that's no big deal
                    return clueViewModel;
                }
            };

    /** Converter for system clue (prompt text). */
    private static final Function<ClueViewModel, String> SYSTEM_CLUE_STRING_CONVERTER =
            viewModel -> viewModel.systemContent().isEmpty() ? defaultPromptText() :
                    viewModel.systemContent();

    /** Fixed cell height. Not great, there is no nice solution to control list view height. */
    private static final double CELL_HEIGHT = 39.2;

    /** The across clues. */
    private final ListProperty<ClueViewModel> acrossClues;

    /** The down clues. */
    private final ListProperty<ClueViewModel> downClues;

    /** The selected across clue index. Value is -1 if no across clue is selected. */
    private final IntegerProperty selectedAcrossClueIndex;

    /** The selected down clue index. Value is -1 if no down clue is selected. */
    private final IntegerProperty selectedDownClueIndex;

    /** The across clue list view. */
    @FXML
    private ListView<ClueViewModel> acrossClueListView;

    /** The down clue list view. */
    @FXML
    private ListView<ClueViewModel> downClueListView;

    /**
     * Constructs an instance.
     */
    public CluesPane() {
        acrossClues =
                new SimpleListProperty<>(this, "acrossClues", FXCollections.observableArrayList());
        downClues =
                new SimpleListProperty<>(this, "downClues", FXCollections.observableArrayList());
        selectedAcrossClueIndex = new SimpleIntegerProperty(this, "selectedAcrossClue", -1);
        selectedDownClueIndex = new SimpleIntegerProperty(this, "selectedDownClue", -1);
        FxmlLoaderHelper.load(this, ResourceBundle.getBundle(getClass().getName()));
    }

    /**
     * The across (= horizontal) clues.
     *
     * @return the across clues
     */
    public ListProperty<ClueViewModel> acrossCluesProperty() {
        return acrossClues;
    }

    /**
     * The down (= vertical) clues.
     *
     * @return the down clues
     */
    public ListProperty<ClueViewModel> downCluesProperty() {
        return downClues;
    }

    /**
     * Returns the "selected across clue index" property.
     * <p>
     * Value is -1 if no across clue is selected.
     *
     * @return the "selected across clue index" property
     */
    public IntegerProperty selectedAcrossClueIndexProperty() {
        return selectedAcrossClueIndex;
    }

    /**
     * Returns the "selected down clue index" property.
     * <p>
     * Value is -1 if no down clue is selected.
     *
     * @return the "selected down clue index" property
     */
    public IntegerProperty selectedDownClueIndexProperty() {
        return selectedDownClueIndex;
    }

    /**
     * Returns the default prompt text to use when no system content is set.
     *
     * @return the default prompt text to use when no system content is set
     */
    private static String defaultPromptText() {
        return ResourceBundle.getBundle(CluesPane.class.getName()).getString("clues.none");
    }

    /**
     * Initializes the control after object hierarchy has been loaded from FXML.
     */
    @FXML
    private void initialize() {
        initializeAcrossClueListView();
        initializeDownClueListView();
    }

    /**
     * Initializes across clue list view bindings.
     */
    private void initializeAcrossClueListView() {
        // pure appearance
        acrossClueListView.prefHeightProperty()
                          .bind(acrossClues.sizeProperty().multiply(CELL_HEIGHT));

        // model -> view
        acrossClueListView.setCellFactory(l -> new TextFieldListCell<>(USER_CLUE_STRING_CONVERTER,
                                                                       SYSTEM_CLUE_STRING_CONVERTER));
        acrossClues.addListener(this::clearAcrossViewSelectionUponItemDeletion);
        acrossClueListView.setItems(acrossClues);
        selectedAcrossClueIndex.addListener(
                this::updateViewAcrossSelectionUponModelSelectionIndexChange);

        // view -> model
        acrossClueListView.getSelectionModel().selectedIndexProperty().addListener(
                this::updateAcrossModelSelectionUponViewSelectionIndexChange);

    }

    /**
     * Initializes down clue list view bindings.
     */
    private void initializeDownClueListView() {
        // Pure appearance
        downClueListView.prefHeightProperty().bind(downClues.sizeProperty().multiply(CELL_HEIGHT));

        // model -> view
        downClueListView.setCellFactory(l -> new TextFieldListCell<>(USER_CLUE_STRING_CONVERTER,
                                                                     SYSTEM_CLUE_STRING_CONVERTER));
        downClues.addListener(this::clearDownSelectionUponItemDeletion);
        downClueListView.setItems(downClues);
        selectedDownClueIndex.addListener(
                this::updateViewDownSelectionUponModelSelectionIndexChange);

        // view -> model
        downClueListView.getSelectionModel().selectedIndexProperty().addListener(
                this::updateDownModelSelectionUponViewSelectionIndexChange);
    }

    /**
     * Clears view across clue selection upon selected item deletion from model.
     *
     * @param c the model change
     * @see #clearViewSelectionUponItemDeletion
     */
    private void clearAcrossViewSelectionUponItemDeletion(
            final ListChangeListener.Change<? extends ClueViewModel> c) {
        clearViewSelectionUponItemDeletion(c, acrossClueListView.getSelectionModel());
    }

    /**
     * Clears view down clue selection upon selected item deletion from model.
     *
     * @param c the model change
     * @see #clearViewSelectionUponItemDeletion
     */
    private void clearDownSelectionUponItemDeletion(
            final ListChangeListener.Change<? extends ClueViewModel> c) {
        clearViewSelectionUponItemDeletion(c, downClueListView.getSelectionModel());
    }

    /**
     * Clears view selection upon selected item deletion from model.
     * <p>
     * {@link ListView} has a non-trivial and non-customizable strategy to update the selection upon
     * model modification. In particular, if the selected item is deleted, it will try to shift the
     * selection to the previous row. This behaviour is <em>not</em> desirable here: Deleting a clue
     * does <em>not</em> mean we want to select the previous clue.
     * <p>
     * This behaviour is particularly visible when binding slots selection to clue selection
     * bi-directionally: Shading a box may delete a slot, which would delete a clue, which would
     * make ListView select the previous clue, which would trigger the selection of the previous
     * slot, which is definitely not desirable.
     * <p>
     * This method forces the list view to clear its selection when the selected item is deleted.
     * This method must be registered first to the model.
     *
     * @param c             the model change
     * @param viewSelection the view selection to clear
     */
    private void clearViewSelectionUponItemDeletion(
            final ListChangeListener.Change<? extends ClueViewModel> c,
            final SelectionModel<ClueViewModel> viewSelection) {
        while (c.next()) {
            final boolean removedWithoutReplacement = c.wasRemoved() && !c.wasAdded();
            final int selectedIndex = viewSelection.getSelectedIndex();
            final boolean selectionRemoved = selectedIndex >= c.getFrom() &&
                                             selectedIndex < c.getFrom() + c.getRemovedSize();
            if (removedWithoutReplacement && selectionRemoved) {
                viewSelection.clearSelection();
            }
        }
    }

    /**
     * Updates view across clue selection upon model change.
     *
     * @param observable the model observable
     */
    private void updateViewAcrossSelectionUponModelSelectionIndexChange(
            final Observable observable) {
        updateViewSelectionUponModelSelectionIndexChange(selectedAcrossClueIndex.get(),
                                                         acrossClueListView.getSelectionModel());
    }

    /**
     * Updates view down clue selection upon model change.
     *
     * @param observable the model observable
     */
    private void updateViewDownSelectionUponModelSelectionIndexChange(final Observable observable) {
        updateViewSelectionUponModelSelectionIndexChange(selectedDownClueIndex.get(),
                                                         downClueListView.getSelectionModel());
    }

    /**
     * Updates view selection upon model change.
     *
     * @param modelNewSelectedIndex the model new selected index
     * @param viewSelection         the view selection
     */
    private void updateViewSelectionUponModelSelectionIndexChange(
            final int modelNewSelectedIndex,
            final SelectionModel<ClueViewModel> viewSelection) {
        if (modelNewSelectedIndex < 0) {
            viewSelection.clearSelection();
        } else {
            viewSelection.select(modelNewSelectedIndex);
        }
    }

    /**
     * Updates the across model selection upon view selection change.
     *
     * @param observable the view selection observable
     */
    private void updateAcrossModelSelectionUponViewSelectionIndexChange(
            final Observable observable) {
        updateModelSelectionUponViewSelectionIndexChange(selectedAcrossClueIndex,
                                                         acrossClueListView.getSelectionModel(),
                                                         downClueListView.getSelectionModel());
    }

    /**
     * Updates the down model selection upon view selection change.
     *
     * @param observable the view selection observable
     */
    private void updateDownModelSelectionUponViewSelectionIndexChange(final Observable observable) {
        updateModelSelectionUponViewSelectionIndexChange(selectedDownClueIndex,
                                                         downClueListView.getSelectionModel(),
                                                         acrossClueListView.getSelectionModel());
    }

    /**
     * Updates the model selection upon view selection change.
     *
     * @param modelSelectedIndex the model selection to update
     * @param viewSelection      the view selection
     * @param otherViewSelection the other view selection
     */
    private void updateModelSelectionUponViewSelectionIndexChange(
            final IntegerProperty modelSelectedIndex,
            final SelectionModel<ClueViewModel> viewSelection,
            final SelectionModel<ClueViewModel> otherViewSelection) {
        modelSelectedIndex.set(viewSelection.getSelectedIndex());
        if (modelSelectedIndex.get() >= 0) {
            otherViewSelection.clearSelection();
        }
    }
}
