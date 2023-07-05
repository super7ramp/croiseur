/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model;

/**
 * Puzzle edition view model.
 */
public final class PuzzleEditionViewModel {

    /** The puzzle details view model. */
    private final PuzzleDetailsViewModel puzzleDetailsViewModel;

    /** The crossword grid view model. */
    private final CrosswordGridViewModel crosswordGridViewModel;

    /**
     * Constructs an instance.
     */
    PuzzleEditionViewModel() {
        puzzleDetailsViewModel = new PuzzleDetailsViewModel();
        crosswordGridViewModel = CrosswordGridViewModel.welcomeGrid();
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
}
