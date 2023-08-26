/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.clue;

import com.gitlab.super7ramp.croiseur.gui.view.javafx.fxml.FxmlLoaderHelper;
import com.gitlab.super7ramp.croiseur.gui.view.model.clue.ClueViewModel;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionModel;
import javafx.scene.layout.HBox;

import java.util.ResourceBundle;

/**
 * Clues pane.
 */
public final class CluesPane extends HBox {

    /** The across clues. */
    private final ListProperty<ClueViewModel> acrossClues;

    /** The down clues. */
    private final ListProperty<ClueViewModel> downClues;

    /** The selected across clue index. Value is -1 if no across clue is selected. */
    private final IntegerProperty selectedAcrossClueIndex;

    /** The selected down clue index. Value is -1 if no down clue is selected. */
    private final IntegerProperty selectedDownClueIndex;

    /** The action to perform when fill clue button is clicked. */
    private final ObjectProperty<EventHandler<ActionEvent>> onFillClueButtonAction;

    /** Whether fill clue button should be disabled. */
    private final BooleanProperty fillClueButtonDisable;

    /** Whether fill clue button should be hidden. */
    private final BooleanProperty fillClueButtonHide;

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
        onFillClueButtonAction = new SimpleObjectProperty<>(this, "onFillClueButtonAction");
        fillClueButtonDisable = new SimpleBooleanProperty(this, "fillClueButtonDisable");
        fillClueButtonHide = new SimpleBooleanProperty(this, "fillClueButtonHide");
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
     * The "on fill clue button action" property.
     *
     * @return the "on fill clue button action" property
     */
    public ObjectProperty<EventHandler<ActionEvent>> onFillClueButtonActionProperty() {
        return onFillClueButtonAction;
    }

    /**
     * The "fill clue button disable" property.
     *
     * @return "fill clue button disable" property
     */
    public BooleanProperty fillClueButtonDisableProperty() {
        return fillClueButtonDisable;
    }

    /**
     * The "fill clue button hide" property.
     * <p>
     * Note that a {@code false} value does not imply the button will be visible. The fill clue
     * button of a cell is visible when this property value is {@code false} and cell is selected.
     *
     * @return the "fill clue button hide" property.
     */
    public BooleanProperty fillClueButtonHideProperty() {
        return fillClueButtonHide;
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
        // model -> view
        acrossClueListView.setCellFactory(l -> newClueListCell());
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
        // model -> view
        downClueListView.setCellFactory(l -> newClueListCell());
        downClues.addListener(this::clearDownSelectionUponItemDeletion);
        downClueListView.setItems(downClues);
        selectedDownClueIndex.addListener(
                this::updateViewDownSelectionUponModelSelectionIndexChange);

        // view -> model
        downClueListView.getSelectionModel().selectedIndexProperty().addListener(
                this::updateDownModelSelectionUponViewSelectionIndexChange);
    }

    /**
     * Creates a new {@link ClueListCell} whose fill button action property is bound to this pane
     * {@link #onFillClueButtonActionProperty()}.
     *
     * @return a new {@link ClueListCell}
     */
    private ClueListCell newClueListCell() {
        final var cell = new ClueListCell();
        cell.onFillClueButtonActionProperty().bind(onFillClueButtonAction);
        cell.fillClueButtonDisableProperty().bind(fillClueButtonDisable);
        cell.fillButtonHideProperty().bind(fillClueButtonHide);
        return cell;
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
