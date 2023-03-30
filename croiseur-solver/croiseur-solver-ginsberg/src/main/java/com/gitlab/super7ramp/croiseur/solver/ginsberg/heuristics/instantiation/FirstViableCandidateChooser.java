/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg.heuristics.instantiation;

import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.Slot;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.sap.CandidateChooser;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.dictionary.CachedDictionary;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.elimination.EliminationSpace;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.grid.Puzzle;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.lookahead.Assignment;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.lookahead.Prober;

import java.util.Optional;

/**
 * An {@link CandidateChooser} which selects the first value resulting in a viable grid (i.e. a
 * grid with all its slots having a least one candidate).
 */
final class FirstViableCandidateChooser implements CandidateChooser<Slot, String> {

    /** The dictionary to pick candidates from. */
    private final CachedDictionary dictionary;

    /** Lookahead util. */
    private final Prober prober;

    /**
     * Constructs an instance.
     *
     * @param puzzleArg     the puzzle to solve
     * @param dictionaryArg the dictionary to pick candidates from
     * @param elsArg        the elimination space
     */
    FirstViableCandidateChooser(final Puzzle puzzleArg, final CachedDictionary dictionaryArg,
                                final EliminationSpace elsArg) {
        dictionary = dictionaryArg;
        prober = new Prober(puzzleArg, dictionaryArg, elsArg);
    }

    @Override
    public Optional<String> find(final Slot wordVariable) {
        return dictionary.candidates(wordVariable)
                         .filter(candidate -> isViable(wordVariable, candidate))
                         .findFirst();
    }

    /**
     * Assesses whether the grid would be viable after assigning the given candidate value to
     * given slot, i.e. a grid with all its slots having at least one candidate.
     *
     * @param wordVariable the slot to test
     * @param candidate    the candidate to test
     * @return {@code true} iff the grid would be viable after assigning the given candidate
     * value to given slot.
     */
    private boolean isViable(final Slot wordVariable, final String candidate) {
        return prober.hasSolutionAfter(Assignment.of(wordVariable.uid(), candidate));
    }
}
