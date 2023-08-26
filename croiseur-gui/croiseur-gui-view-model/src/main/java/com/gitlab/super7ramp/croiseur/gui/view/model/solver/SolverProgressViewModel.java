/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model.solver;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * The solver progress view model.
 */
public final class SolverProgressViewModel {

    /** Value representing an indeterminate progress. */
    private static final double INDETERMINATE_PROGRESS = -1.0;

    /** Whether the solver is running. */
    private final BooleanProperty solverRunning;

    /** The solver progress (0.0 - 1.0). */
    private final DoubleProperty solverProgress;

    /**
     * Constructs an instance.
     */
    public SolverProgressViewModel() {
        solverRunning = new SimpleBooleanProperty(this, "solverRunning");
        solverProgress = new SimpleDoubleProperty(this, "solverProgress", INDETERMINATE_PROGRESS);
        solverRunning.addListener((observable, wasRunning, isRunning) -> {
            if (!isRunning) {
                // Reset progress for next run
                solverProgress.set(INDETERMINATE_PROGRESS);
            }
        });
    }

    /**
     * Returns the property indicating whether the solver is running.
     *
     * @return the property indicating whether the solver is running
     */
    public BooleanProperty solverRunningProperty() {
        return solverRunning;
    }

    /**
     * Returns the solver progress property.
     * <p>
     * When progress is determinate, its value is between 0.0 (= 0 %) and 1.0 (= 100%). When
     * progress is indeterminate, value is negative.
     *
     * @return the the solver progress property
     */
    public DoubleProperty solverProgressProperty() {
        return solverProgress;
    }

    /**
     * Sets the value of the solver progress property.
     *
     * @param solverProgressValue the value to set
     */
    public void solverProgress(final double solverProgressValue) {
        solverProgress.set(solverProgressValue);
    }
}
