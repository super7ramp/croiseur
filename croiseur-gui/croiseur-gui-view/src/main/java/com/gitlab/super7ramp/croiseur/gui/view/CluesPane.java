/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view;

import com.gitlab.super7ramp.croiseur.gui.view.control.cell.TextFieldListCell;
import com.gitlab.super7ramp.croiseur.gui.view.model.ClueViewModel;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.util.StringConverter;

import java.util.ResourceBundle;
import java.util.function.Function;

/**
 * Clues pane.
 */
public final class CluesPane extends TitledPane {

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
                    return clueViewModel;
                }
            };

    /** Converter for system clue (prompt text). */
    private static final Function<ClueViewModel, String> SYSTEM_CLUE_STRING_CONVERTER =
            viewModel ->
                    viewModel.systemContent().isEmpty() ?
                            ResourceBundle.getBundle(CluesPane.class.getName())
                                          .getString("clues.none") :
                            viewModel.systemContent();

    /** The across clues. */
    private final ListProperty<ClueViewModel> acrossClues;

    /** The down clues. */
    private final ListProperty<ClueViewModel> downClues;

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
     * Initializes the control after object hierarchy has been loaded from FXML.
     */
    @FXML
    private void initialize() {
        initializeAcrossClueListView();
        initializeDownClueListView();
    }

    private void initializeAcrossClueListView() {
        acrossClueListView.setCellFactory(l -> new TextFieldListCell<>(USER_CLUE_STRING_CONVERTER,
                                                                       SYSTEM_CLUE_STRING_CONVERTER));
        acrossClueListView.setItems(acrossClues);
    }

    private void initializeDownClueListView() {
        downClueListView.setCellFactory(l -> new TextFieldListCell<>(USER_CLUE_STRING_CONVERTER,
                                                                     SYSTEM_CLUE_STRING_CONVERTER));
        downClueListView.setItems(downClues);
    }
}
