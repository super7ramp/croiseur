/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg.heuristics.instantiation;

import re.belv.croiseur.solver.ginsberg.core.Slot;
import re.belv.croiseur.solver.ginsberg.core.sap.CandidateChooser;
import re.belv.croiseur.solver.ginsberg.dictionary.CachedDictionary;
import re.belv.croiseur.solver.ginsberg.lookahead.ProbePuzzle;

/** Factory of {@link CandidateChooser}s. */
public final class CandidateChoosers {

    /** Prevents instantiation. */
    private CandidateChoosers() {
        // Nothing to do.
    }

    /**
     * Returns the default {@link CandidateChooser}.
     *
     * @param probePuzzle the probed puzzle
     * @param dictionary the dictionary
     * @return the default {@link CandidateChooser}
     */
    public static CandidateChooser<Slot, String> byDefault(
            final ProbePuzzle probePuzzle, final CachedDictionary dictionary) {
        return leastConstraining(probePuzzle, dictionary);
    }

    /**
     * Creates a {@link CandidateChooser} selecting the first viable value.
     *
     * @param probePuzzle the probed puzzle
     * @param dictionary the dictionary
     * @return a {@link CandidateChooser} selecting the first viable value
     */
    public static CandidateChooser<Slot, String> firstViable(
            final ProbePuzzle probePuzzle, final CachedDictionary dictionary) {
        return new FirstViableCandidateChooser(probePuzzle, dictionary);
    }

    /**
     * Creates a {@link CandidateChooser} selecting the viable value which applies the least amount of constraints on
     * the grid.
     *
     * <p>Although intellectually interesting, this chooser comes with a cost which may not worth it.
     *
     * @param probePuzzle the probed puzzle
     * @param dictionary the dictionary
     * @return a {@link CandidateChooser} selecting the first viable value
     */
    public static CandidateChooser<Slot, String> leastConstraining(
            final ProbePuzzle probePuzzle, final CachedDictionary dictionary) {
        return new LeastConstrainingCandidateChooser(probePuzzle, dictionary);
    }
}
