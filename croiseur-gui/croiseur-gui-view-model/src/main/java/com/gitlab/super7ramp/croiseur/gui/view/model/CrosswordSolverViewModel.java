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

    /** Whether the solver is running. */
    private final BooleanProperty solverRunning;

    /**
     * Constructs an instance.
     */
    public CrosswordSolverViewModel() {
        crosswordGridViewModel = new CrosswordGridViewModel();
        dictionariesViewModel = new DictionariesViewModel();
        solverRunning = new SimpleBooleanProperty(this, "solverRunning", false);
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
     * Returns whether the solver is running.
     *
     * @return whether the solver is running
     */
    public BooleanProperty solverRunning() {
        return solverRunning;
    }
}
