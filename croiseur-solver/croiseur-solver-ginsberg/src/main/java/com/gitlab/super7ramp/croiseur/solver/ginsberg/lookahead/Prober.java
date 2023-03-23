/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg.lookahead;

import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.Slot;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.dictionary.CachedDictionary;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.grid.Puzzle;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;

/**
 * Provides some common lookahead functions.
 */
public final class Prober {

    /** The puzzle to probe. */
    private final Puzzle puzzle;

    /** The dictionary. */
    private final CachedDictionary dictionary;

    /**
     * Constructor.
     *
     * @param aPuzzle     a puzzle to probe
     * @param aDictionary a dictionary
     */
    public Prober(final Puzzle aPuzzle, final CachedDictionary aDictionary) {
        puzzle = aPuzzle;
        dictionary = aDictionary;
    }

    /**
     * Returns {@code true} iff the grid has a solution after the given assignment.
     *
     * @param assignment the assignment to probe
     * @return {@code true} iff the grid has a solution after the given assignment
     */
    public boolean hasSolutionAfter(final Assignment assignment) {
        return computeNumberOfLocalSolutionsAfter(assignment).signum() > 0;
    }

    /**
     * Compute <em>an estimation</em> of the number of solutions for the grid zone impacted by
     * the given assignment if it were applied. It corresponds to the product of the estimated
     * number of candidates left for each connected variable.
     * <p>
     * Note that the estimation may return a value > 0 despite the grid not having an actual
     * solution.
     *
     * @param assignment the assignment to evaluate
     * @return the estimated number of local solutions for the grid after assignment; As the
     * estimation can be very large computation is made using {@link BigInteger}
     */
    public BigInteger computeNumberOfLocalSolutionsAfter(final Assignment assignment) {
        final Slot probedSlot = probeAssignment(assignment);
        return probedSlot.connectedSlots()
                         .map(slot -> dictionary.refinedCandidatesCount(slot, assignment.slotUid()))
                         .map(BigInteger::valueOf)
                         .reduce(BigInteger.ONE, BigInteger::multiply);

    }

    /**
     * Probes the puzzle against the given assignment.
     *
     * @param assignment the assignment
     * @return a slot deep copy as it the assignment were applied
     */
    private Slot probeAssignment(final Assignment assignment) {
        final Puzzle probePuzzle = puzzle.copy();
        final Slot probedSlot = probePuzzle.slot(assignment.slotUid());
        probedSlot.assign(assignment.word());
        return probedSlot;
    }

    /**
     * Returns whether after performing the given unassignment the given unassignable slot would
     * become assignable again.
     *
     * @param unassignment the unassignment to test
     * @param unassignable the unassignable variable
     * @return whether after performing the given unassignment the given unassignable slot would
     * become assignable again
     */
    public boolean hasSolutionAfter(final Unassignment unassignment,
                                    final Slot unassignable) {
        return hasSolutionAfter(Collections.singletonList(unassignment), unassignable);
    }

    /**
     * Returns whether after performing the given unassignment the given unassignable slot would
     * become assignable again.
     *
     * @param unassignment the unassignment to test
     * @param unassignable the unassignable variable
     * @return whether after performing the given unassignment the given unassignable slot would
     * become assignable again
     */
    public boolean hasSolutionAfter(final Collection<Unassignment> unassignment,
                                    final Slot unassignable) {
        final Slot probedSlot = probeUnassignment(unassignment, unassignable);
        return dictionary.reevaluatedCandidatesCount(probedSlot,
                unassignment.stream().map(Unassignment::slotUid).toList()) > 0;
    }

    /**
     * Probes the given unassignable variable against the given unassignments.
     *
     * @param unassignments the unassignments to apply
     * @param unassignable  the unassignment variable to probe
     * @return a deep copy of the given unassignable variable with the effects of the
     * unassignment applied
     */
    private Slot probeUnassignment(final Collection<Unassignment> unassignments,
                                   final Slot unassignable) {
        final Puzzle probedPuzzle = puzzle.copy();
        for (final Unassignment unassignment : unassignments) {
            probedPuzzle.slot(unassignment.slotUid()).unassign();
        }
        return probedPuzzle.slot(unassignable.uid());
    }
}
