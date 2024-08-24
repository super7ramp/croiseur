/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.szunami.plugin;

import java.util.ResourceBundle;
import re.belv.croiseur.common.puzzle.PuzzleGrid;
import re.belv.croiseur.solver.szunami.Crossword;
import re.belv.croiseur.solver.szunami.Filler;
import re.belv.croiseur.solver.szunami.Result;
import re.belv.croiseur.spi.solver.CrosswordSolver;
import re.belv.croiseur.spi.solver.Dictionary;
import re.belv.croiseur.spi.solver.ProgressListener;
import re.belv.croiseur.spi.solver.SolverResult;

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
        return ResourceBundle.getBundle("re.belv.croiseur.solver.szunami.plugin.Messages")
                .getString("description");
    }

    @Override
    public SolverResult solve(
            final PuzzleGrid puzzle, final Dictionary dictionary, final ProgressListener progressListener)
            throws InterruptedException {
        final re.belv.croiseur.solver.szunami.Dictionary adaptedDictionary = DictionaryAdapter.adapt(dictionary);
        final Crossword crossword = PuzzleAdapter.adapt(puzzle);

        final Result result = filler.fill(crossword, adaptedDictionary);

        return new AdaptedSolverResult(result, puzzle);
    }
}
