/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.spi.solver;

import com.gitlab.super7ramp.croiseur.common.PuzzleDefinition;

/**
 * A crossword solver.
 */
// TODO create a CrosswordSolverProvider interface (e.g. a factory). This would allow client to
//  avoid loading the solver itself if it doesn't end up using it (e.g. avoid loading native
//  libraries for nothing)
public interface CrosswordSolver {

    /**
     * Returns a unique name for this solver.
     *
     * @return the solver name
     */
    String name();

    /**
     * Returns a description for this solver.
     *
     * @return a description for this solver.
     */
    String description();

    /**
     * Solves the given puzzle, using the given dictionary.
     *
     * @param puzzle     the puzzle to solve
     * @param dictionary the dictionary to use
     * @return the result
     * @throws InterruptedException if interrupted while solving
     */
    default SolverResult solve(final PuzzleDefinition puzzle, final Dictionary dictionary) throws InterruptedException {
        return solve(puzzle, dictionary, ProgressListener.DUMMY_LISTENER);
    }

    /**
     * Solves the given puzzle, using the given dictionary.
     *
     * @param puzzle           the puzzle to solve
     * @param dictionary       the dictionary to use
     * @param progressListener the progress listener
     * @return the result
     * @throws InterruptedException if interrupted while solving
     */
    SolverResult solve(final PuzzleDefinition puzzle, final Dictionary dictionary,
                       final ProgressListener progressListener) throws InterruptedException;

}
