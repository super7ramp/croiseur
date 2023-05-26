/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Access to the application view models.
 */
public final class CrosswordSolverViewModel {

    /** The view model of the crossword grid. */
    private final CrosswordGridViewModel crosswordGridViewModel;

    /** The view model of the dictionary. */
    private final DictionariesViewModel dictionariesViewModel;

    /** The view model of the solver selection. */
    private final SolverSelectionViewModel solverSelectionViewModel;

    /** Whether the solver is running. */
    private final BooleanProperty solverRunning;

    /**
     * Constructs an instance.
     */
    public CrosswordSolverViewModel() {
        crosswordGridViewModel = CrosswordGridViewModel.welcomeGrid();
        dictionariesViewModel = new DictionariesViewModel();
        solverSelectionViewModel = new SolverSelectionViewModel();
        solverRunning = new SimpleBooleanProperty(this, "solverRunning", false);

        // Initializes special bindings between dictionary and grid view models.
        dictionariesViewModel.suggestionFilterProperty()
                           .bind(crosswordGridViewModel.currentSlotContentProperty());
        crosswordGridViewModel.isCurrentSlotUnsolvableProperty()
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
     * Returns whether the solver is running.
     *
     * @return whether the solver is running
     */
    public BooleanProperty solverRunning() {
        return solverRunning;
    }

}
