/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model.solver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link SolverProgressViewModel}.
 */
final class SolverProgressViewModelTest {

    /** The model under test. */
    private SolverProgressViewModel solverProgressViewModel;

    /**
     * Creates a fresh model for each test.
     */
    @BeforeEach
    void beforeEach() {
        solverProgressViewModel = new SolverProgressViewModel();
    }

    /**
     * Verifies that default value is negative, i.e. indeterminate (because it's easy to forget and
     * incorrectly use the default double value 0.0).
     */
    @Test
    void defaultToIndeterminate() {
        assertTrue(solverProgressViewModel.solverProgressProperty().get() < 0.0);
    }

    /**
     * Verifies that progress is reset to a negative (= indeterminate) value when solver stops.
     */
    @Test
    void resetToIndeterminate() {
        solverProgressViewModel.solverRunningProperty().set(true);
        solverProgressViewModel.solverProgressProperty().set(0.5);
        solverProgressViewModel.solverRunningProperty().set(false);

        assertTrue(solverProgressViewModel.solverProgressProperty().get() < 0.0);
    }
}
