/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg.heuristics.backtrack;

import re.belv.croiseur.solver.ginsberg.core.Slot;
import re.belv.croiseur.solver.ginsberg.core.SlotIdentifier;
import re.belv.croiseur.solver.ginsberg.core.sap.Backtracker;
import re.belv.croiseur.solver.ginsberg.grid.Puzzle;
import re.belv.croiseur.solver.ginsberg.history.History;
import re.belv.croiseur.solver.ginsberg.lookahead.ProbePuzzle;

/** A factory of backtracking strategies. */
public final class Backtrackers {

    /** Private constructor, static factory methods only. */
    private Backtrackers() {
        // Nothing to do.
    }

    /**
     * Returns the best {@link Backtracker} in most situations.
     *
     * @param puzzle the puzzle
     * @param probePuzzle the copy of the puzzle used for look-ahead
     * @param history assignment history
     * @return the best {@link Backtracker} in most situations.
     */
    public static Backtracker<Slot, SlotIdentifier> byDefault(
            final Puzzle puzzle, final ProbePuzzle probePuzzle, final History history) {
        return new DynamicBacktracker(puzzle, probePuzzle, history);
    }
}
