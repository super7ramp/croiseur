/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model;

import com.gitlab.super7ramp.croiseur.common.puzzle.SavedPuzzle;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

/**
 * Puzzle selection view modeL.
 */
public final class PuzzleSelectionViewModel {

    /** The available puzzles. */
    private final ListProperty<SavedPuzzle> availablePuzzles;

    /**
     * Constructs an instance.
     */
    PuzzleSelectionViewModel() {
        availablePuzzles = new SimpleListProperty<>(this, "puzzles", FXCollections.observableArrayList());
    }

    /**
     * Returns the available puzzles property.
     *
     * @return the available puzzles property
     */
    public ListProperty<SavedPuzzle> availablePuzzlesProperty() {
        return availablePuzzles;
    }
}
