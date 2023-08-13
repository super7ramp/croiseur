/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view;

import com.gitlab.super7ramp.croiseur.gui.view.control.cell.TextFieldListCell;
import com.gitlab.super7ramp.croiseur.gui.view.model.ClueViewModel;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
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
        acrossClueListView.setCellFactory(l -> new TextFieldListCell<>(USER_CLUE_STRING_CONVERTER,
                                                                       SYSTEM_CLUE_STRING_CONVERTER));
        acrossClueListView.setItems(acrossClues);

        // view -> model
        final var acrossClueSelectionModel = acrossClueListView.getSelectionModel();
        acrossClueSelectionModel.selectedIndexProperty().addListener(observable -> {
            selectedAcrossClueIndex.set(acrossClueSelectionModel.getSelectedIndex());
            if (selectedAcrossClueIndex.get() >= 0) {
                downClueListView.getSelectionModel().clearSelection();
            }
        });

        // model -> view
        selectedAcrossClueIndex.addListener(observable -> {
            final int index = selectedAcrossClueIndex.get();
            if (index < 0) {
                acrossClueSelectionModel.clearSelection();
            } else {
                acrossClueSelectionModel.select(index);
            }
        });
    }

    /**
     * Initializes down clue list view bindings.
     */
    private void initializeDownClueListView() {
        downClueListView.setCellFactory(l -> new TextFieldListCell<>(USER_CLUE_STRING_CONVERTER,
                                                                     SYSTEM_CLUE_STRING_CONVERTER));
        downClueListView.setItems(downClues);

        // view -> model
        final var downClueSelectionModel = downClueListView.getSelectionModel();
        downClueSelectionModel.selectedIndexProperty().addListener(observable -> {
            selectedDownClueIndex.set(downClueSelectionModel.getSelectedIndex());
            if (selectedDownClueIndex.get() >= 0) {
                acrossClueListView.getSelectionModel().clearSelection();
            }
        });

        // model -> view
        selectedDownClueIndex.addListener(observable -> {
            final int index = selectedDownClueIndex.get();
            if (index < 0) {
                downClueSelectionModel.clearSelection();
            } else {
                downClueSelectionModel.select(index);
            }
        });
    }
}
