/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.impl.solver.postrun;


import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleGrid;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverResult;

/**
 * Converts from {@link com.gitlab.super7ramp.croiseur.spi.solver.SolverResult} to
 * {@link com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverResult}.
 */
public final class SolverResultConverter {

    /** Private constructor to prevent instantiation, static methods only. */
    private SolverResultConverter() {
        // Nothing to do.
    }

    /**
     * Converts solver result from solver SPI - which contains partial information about the boxes
     * filled during the solving process - to a solver result from presenter SPI - which contains
     * everything needed to present the grid.
     *
     * @param solverResult the solver result from solver SPI
     * @param originalGrid the original grid, before the solving process started
     */
    public static SolverResult toPresentable(
            final com.gitlab.super7ramp.croiseur.spi.solver.SolverResult solverResult, final
    PuzzleGrid originalGrid) {
        final PuzzleGrid updatedGrid =
                new PuzzleGrid(originalGrid.width(), originalGrid.height(),
                               originalGrid.shaded(), solverResult.filledBoxes());
        final boolean isSuccess = solverResult.kind() ==
                                  com.gitlab.super7ramp.croiseur.spi.solver.SolverResult.Kind.SUCCESS;
        return new SolverResult(isSuccess, updatedGrid, solverResult.unsolvableBoxes());
    }
}
