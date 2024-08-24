/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg.heuristics.instantiation;

import java.util.Optional;
import re.belv.croiseur.solver.ginsberg.core.Slot;
import re.belv.croiseur.solver.ginsberg.core.sap.CandidateChooser;
import re.belv.croiseur.solver.ginsberg.dictionary.CachedDictionary;
import re.belv.croiseur.solver.ginsberg.lookahead.Assignment;
import re.belv.croiseur.solver.ginsberg.lookahead.ProbePuzzle;

/**
 * An {@link CandidateChooser} which selects the first value resulting in a viable grid (i.e. a grid with all its slots
 * having a least one candidate).
 */
final class FirstViableCandidateChooser implements CandidateChooser<Slot, String> {

    /** The dictionary to pick candidates from. */
    private final CachedDictionary dictionary;

    /** Lookahead util. */
    private final ProbePuzzle probePuzzle;

    /**
     * Constructs an instance.
     *
     * @param probePuzzleArg the puzzle copy to use when evaluating candidates
     * @param dictionaryArg the dictionary to pick candidates from
     */
    FirstViableCandidateChooser(final ProbePuzzle probePuzzleArg, final CachedDictionary dictionaryArg) {
        dictionary = dictionaryArg;
        probePuzzle = probePuzzleArg;
    }

    @Override
    public Optional<String> find(final Slot wordVariable) {
        return dictionary
                .candidates(wordVariable)
                .filter(candidate -> isViable(wordVariable, candidate))
                .findFirst();
    }

    /**
     * Assesses whether the grid would be viable after assigning the given candidate value to given slot, i.e. a grid
     * with all its slots having at least one candidate.
     *
     * @param wordVariable the slot to test
     * @param candidate the candidate to test
     * @return {@code true} iff the grid would be viable after assigning the given candidate value to given slot.
     */
    private boolean isViable(final Slot wordVariable, final String candidate) {
        return probePuzzle.hasSolutionAfter(Assignment.of(wordVariable.uid(), candidate));
    }
}
