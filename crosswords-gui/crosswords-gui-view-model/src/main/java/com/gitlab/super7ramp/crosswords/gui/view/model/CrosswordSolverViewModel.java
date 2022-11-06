package com.gitlab.super7ramp.crosswords.gui.view.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * The main view model.
 */
public final class CrosswordSolverViewModel {

    /** The view model of the crossword grid. */
    private final CrosswordGridViewModel crosswordGridViewModel;

    /** The view model of the dictionary. */
    private final DictionaryViewModel dictionaryViewModel;

    /** Whether the solver is running. */
    private final BooleanProperty solverRunning;

    /**
     * Constructs an instance.
     */
    public CrosswordSolverViewModel() {
        crosswordGridViewModel = new CrosswordGridViewModel();
        dictionaryViewModel = new DictionaryViewModel();
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
    public DictionaryViewModel dictionaryViewModel() {
        return dictionaryViewModel;
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
