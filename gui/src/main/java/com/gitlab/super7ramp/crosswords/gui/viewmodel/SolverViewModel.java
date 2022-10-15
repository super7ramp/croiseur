package com.gitlab.super7ramp.crosswords.gui.viewmodel;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public final class SolverViewModel {

    private final CrosswordViewModel crosswordViewModel;

    private final DictionaryViewModel dictionaryViewModel;

    private final BooleanProperty solverRunning;

    /**
     * Constructs an instance.
     */
    public SolverViewModel() {
        crosswordViewModel = new CrosswordViewModel();
        dictionaryViewModel = new DictionaryViewModel();
        solverRunning = new SimpleBooleanProperty(this, "solverRunning", false);
    }

    public CrosswordViewModel crosswordViewModel() {
        return crosswordViewModel;
    }

    public DictionaryViewModel dictionaryViewModel() {
        return dictionaryViewModel;
    }

    public BooleanProperty solverRunning() {
        return solverRunning;
    }
}
