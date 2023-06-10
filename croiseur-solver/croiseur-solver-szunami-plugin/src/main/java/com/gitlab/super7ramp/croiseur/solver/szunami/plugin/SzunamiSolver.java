/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.szunami.plugin;

import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleGrid;
import com.gitlab.super7ramp.croiseur.solver.szunami.Crossword;
import com.gitlab.super7ramp.croiseur.solver.szunami.Filler;
import com.gitlab.super7ramp.croiseur.solver.szunami.Result;
import com.gitlab.super7ramp.croiseur.spi.solver.CrosswordSolver;
import com.gitlab.super7ramp.croiseur.spi.solver.Dictionary;
import com.gitlab.super7ramp.croiseur.spi.solver.ProgressListener;
import com.gitlab.super7ramp.croiseur.spi.solver.SolverResult;

import java.util.ResourceBundle;

/**
 * Implementation of {@link CrosswordSolver} adapting {@link Filler szunami's xwords-rs solver}.
 */
public final class SzunamiSolver implements CrosswordSolver {

    /** The solver name. */
    private static final String NAME = "XWords RS";

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
        return ResourceBundle
                .getBundle("com.gitlab.super7ramp.croiseur.solver.szunami.plugin.Messages")
                .getString("description");
    }

    @Override
    public SolverResult solve(final PuzzleGrid puzzle, final Dictionary dictionary,
                              final ProgressListener progressListener) throws InterruptedException {
        final com.gitlab.super7ramp.croiseur.solver.szunami.Dictionary adaptedDictionary =
                DictionaryAdapter.adapt(dictionary);
        final Crossword crossword = PuzzleAdapter.adapt(puzzle);

        final Result result = filler.fill(crossword, adaptedDictionary);

        return new AdaptedSolverResult(result, puzzle);
    }
}
