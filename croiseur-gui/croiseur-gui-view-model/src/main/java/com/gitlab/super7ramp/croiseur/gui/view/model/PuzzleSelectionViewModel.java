/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;

/**
 * Puzzle selection view modeL.
 */
public final class PuzzleSelectionViewModel {

    /** The available puzzles. */
    private final ListProperty<PuzzleViewModel> availablePuzzles;

    /** The selected puzzle. Value is {@code null} if no puzzle is selected. */
    private final ObjectProperty<PuzzleViewModel> selectedPuzzle;

    /**
     * Constructs an instance.
     */
    PuzzleSelectionViewModel() {
        availablePuzzles = new SimpleListProperty<>(this, "availablePuzzles",
                                                    FXCollections.observableArrayList());
        selectedPuzzle = new SimpleObjectProperty<>(this, "selectedPuzzle");
    }

    /**
     * Returns the available puzzles property.
     *
     * @return the available puzzles property
     */
    public ListProperty<PuzzleViewModel> availablePuzzlesProperty() {
        return availablePuzzles;
    }

    /**
     * Returns the selected puzzle property.
     * <p>
     * Value is {@code null} if no puzzle is selected.
     *
     * @return the selected puzzle property
     */
    public ObjectProperty<PuzzleViewModel> selectedPuzzleProperty() {
        return selectedPuzzle;
    }

    /**
     * Returns the value of the selected puzzle property.
     *
     * @return the value of the selected puzzle property; {@code null} if no puzzle selected/saved
     */
    public PuzzleViewModel selectedPuzzle() {
        return selectedPuzzle.get();
    }

    /**
     * Sets the value of the selected puzzle property.
     *
     * @param savedPuzzle the value to set
     */
    public void selectedPuzzle(final PuzzleViewModel savedPuzzle) {
        selectedPuzzle.set(savedPuzzle);
    }
}
