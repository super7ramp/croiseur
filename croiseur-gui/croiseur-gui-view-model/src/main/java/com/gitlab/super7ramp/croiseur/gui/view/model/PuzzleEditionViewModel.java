/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Puzzle edition view model.
 */
public final class PuzzleEditionViewModel {

    /** The puzzle details view model. */
    private final PuzzleDetailsViewModel puzzleDetailsViewModel;

    /** The crossword grid view model. */
    private final CrosswordGridViewModel crosswordGridViewModel;

    /** Property following the saving state. */
    private final BooleanProperty savingInProgress;

    /**
     * Constructs an instance.
     */
    PuzzleEditionViewModel() {
        puzzleDetailsViewModel = new PuzzleDetailsViewModel();
        crosswordGridViewModel = CrosswordGridViewModel.welcomeGrid();
        savingInProgress = new SimpleBooleanProperty(this, "savingInProgress");
    }

    /**
     * Returns the puzzle details view model.
     *
     * @return the puzzle details view model
     */
    public PuzzleDetailsViewModel puzzleDetailsViewModel() {
        return puzzleDetailsViewModel;
    }

    /**
     * Returns the crossword grid view model.
     *
     * @return the crossword grid view model
     */
    public CrosswordGridViewModel crosswordGridViewModel() {
        return crosswordGridViewModel;
    }

    /**
     * Returns the property indicating whether the puzzle is being saved.
     *
     * @return the property indicating whether the puzzle is being saved
     */
    public BooleanProperty savingInProgressProperty() {
        return savingInProgress;
    }

    /**
     * Resets edition view model to default.
     */
    public void reset() {
        puzzleDetailsViewModel.reset();
        crosswordGridViewModel.reset();
    }
}
