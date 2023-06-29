/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model;

import javafx.beans.property.BooleanProperty;

/**
 * Access to the application view models.
 */
public final class ApplicationViewModel {

    /** The view model of the crossword grid. */
    private final CrosswordGridViewModel crosswordGridViewModel;

    /** The view model of the dictionary. */
    private final DictionariesViewModel dictionariesViewModel;

    /** The view model of the solver selection. */
    private final SolverSelectionViewModel solverSelectionViewModel;

    /** The view model of the solver progress. */
    private final SolverProgressViewModel solverProgressViewModel;

    /** The puzzle selection view model. */
    private final PuzzleSelectionViewModel puzzleSelectionViewModel;

    /** The errors view model. */
    private final ErrorsViewModel errorsViewModel;

    /**
     * Constructs an instance.
     */
    public ApplicationViewModel() {
        crosswordGridViewModel = CrosswordGridViewModel.welcomeGrid();
        dictionariesViewModel = new DictionariesViewModel();
        solverSelectionViewModel = new SolverSelectionViewModel();
        solverProgressViewModel = new SolverProgressViewModel();
        puzzleSelectionViewModel = new PuzzleSelectionViewModel();
        errorsViewModel = new ErrorsViewModel();

        // Initializes special bindings between dictionary and grid view models.
        dictionariesViewModel.suggestionFilterProperty()
                           .bind(crosswordGridViewModel.currentSlotContentProperty());
        crosswordGridViewModel.currentSlotUnsolvableProperty()
                     .bind(dictionariesViewModel.suggestionsProperty().emptyProperty());
    }

    /**
     * Returns the view model of the crossword grid.
     *
     * @return the view model of the crossword grid
     */
    public CrosswordGridViewModel crosswordGridViewModel() {
        return crosswordGridViewModel;
    }

    /**
     * Returns the view model of the dictionary.
     *
     * @return the view model of the dictionary
     */
    public DictionariesViewModel dictionaryViewModel() {
        return dictionariesViewModel;
    }

    /**
     * Returns the view model of the solver selection.
     *
     * @return the view model of the solver selection
     */
    public SolverSelectionViewModel solverSelectionViewModel() {
        return solverSelectionViewModel;
    }

    /**
     * Returns the view model of the solver progress.
     *
     * @return the view model of the solver progress
     */
    public SolverProgressViewModel solverProgressViewModel() {
        return solverProgressViewModel;
    }

    /**
     * Returns whether the solver is running.
     * <p>
     * Shortcut for {@code solverProgressViewModel().solverRunningProperty()}.
     *
     * @return whether the solver is running
     */
    public BooleanProperty solverRunning() {
        return solverProgressViewModel.solverRunningProperty();
    }

    /**
     * Returns the puzzle selection view model.
     *
     * @return the puzzle selection view model
     */
    public PuzzleSelectionViewModel puzzleSelectionViewModel() {
        return puzzleSelectionViewModel;
    }

    /**
     * Returns the errors view model.
     *
     * @return the errors view model
     */
    public ErrorsViewModel errorsViewModel() {
        return errorsViewModel;
    }
}
