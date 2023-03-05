/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.szunami.plugin;

import com.gitlab.super7ramp.croiseur.common.PuzzleDefinition;
import com.gitlab.super7ramp.croiseur.solver.szunami.Crossword;
import com.gitlab.super7ramp.croiseur.solver.szunami.Filler;
import com.gitlab.super7ramp.croiseur.solver.szunami.NativePanicException;
import com.gitlab.super7ramp.croiseur.solver.szunami.Result;
import com.gitlab.super7ramp.croiseur.spi.solver.CrosswordSolver;
import com.gitlab.super7ramp.croiseur.spi.solver.Dictionary;
import com.gitlab.super7ramp.croiseur.spi.solver.ProgressListener;
import com.gitlab.super7ramp.croiseur.spi.solver.SolverResult;

import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of {@link CrosswordSolver} adapting {@link Filler szunami's xwords-rs solver}.
 */
public final class SzunamiSolver implements CrosswordSolver {

    /** The logger. */
    private static final Logger LOGGER = Logger.getLogger(SzunamiSolver.class.getName());

    /** The solver name. */
    private static final String NAME = "XWords RS";

    /** The solver description. */
    private static final String DESCRIPTION = ResourceBundle
            .getBundle("com.gitlab.super7ramp.croiseur.solver.szunami.plugin.Messages")
            .getString("description");

    /** The adapted solver. */
    private final Filler filler;

    /**
     * Constructs an instance.
     */
    public SzunamiSolver() {
        filler = new Filler();
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }

    // TODO #43 make native code interruptible
    @Override
    public SolverResult solve(final PuzzleDefinition puzzle, final Dictionary dictionary,
                              final ProgressListener progressListener) {
        final com.gitlab.super7ramp.croiseur.solver.szunami.Dictionary adaptedDictionary =
                DictionaryAdapter.adapt(dictionary);
        final Crossword crossword = PuzzleAdapter.adapt(puzzle);

        final Result result = safeSolve(crossword, adaptedDictionary);

        return new AdaptedSolverResult(result, puzzle);
    }

    /**
     * Solves the given puzzle using the given dictionary, catching any error raised by the solver.
     *
     * @param adaptedPuzzle     the puzzle to solve
     * @param adaptedDictionary the dictionary
     * @return the {@link Result}, if any; {@link Optional#empty()} otherwise
     */
    private Result safeSolve(final Crossword adaptedPuzzle,
                             final com.gitlab.super7ramp.croiseur.solver.szunami.Dictionary adaptedDictionary) {
        try {
            return filler.fill(adaptedPuzzle, adaptedDictionary);
        } catch (final NativePanicException e) {
            LOGGER.log(Level.WARNING, NAME + " solver encountered an error.", e);
            return Result.err("Native solver code panicked");
        }
    }
}
