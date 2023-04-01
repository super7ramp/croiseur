/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg.result;

import com.gitlab.super7ramp.croiseur.common.GridPosition;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.SolverResult;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.listener.StatisticsRecorder;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.state.Crossword;

import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 * Factory of {@link SolverResult}.
 */
public final class SolverResultFactory {

    /**
     * Private constructor, static factory methods only.
     */
    private SolverResultFactory() {
        // Nothing to do.
    }

    /**
     * Creates a solver result from the given state.
     *
     * @param crossword          the crossword state
     * @param statisticsRecorder the statistics record
     * @param success            whether solver has succeeded
     * @return the solver result
     */
    public static SolverResult createFrom(final Crossword crossword,
                                          final StatisticsRecorder statisticsRecorder,
                                          final boolean success) {
        final Map<GridPosition, Character> solvedBoxes = crossword.grid().boxes();
        final SolverResultImpl solverResult;
        if (success) {
            solverResult = SolverResultImpl.success(solvedBoxes, statisticsRecorder.statistics());
        } else {
            final Set<GridPosition> unsolvableBoxes =
                    crossword.grid()
                             .puzzle()
                             .slots()
                             .stream()
                             .filter(slot -> crossword.dictionary()
                                                      .cachedCandidatesCount(slot) == 0L)
                             .flatMap(slot -> crossword.grid()
                                                       .slotPositions(slot)
                                                       .stream())
                             .collect(toSet());

            solverResult = SolverResultImpl.impossible(solvedBoxes, unsolvableBoxes,
                    statisticsRecorder.statistics());
        }
        return solverResult;
    }
}
