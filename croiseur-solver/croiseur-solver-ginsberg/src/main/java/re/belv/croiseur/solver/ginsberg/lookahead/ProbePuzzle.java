/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg.lookahead;

import static java.util.function.Predicate.not;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import re.belv.croiseur.solver.ginsberg.core.Slot;
import re.belv.croiseur.solver.ginsberg.core.SlotIdentifier;
import re.belv.croiseur.solver.ginsberg.dictionary.CachedDictionary;
import re.belv.croiseur.solver.ginsberg.elimination.EliminationSpace;
import re.belv.croiseur.solver.ginsberg.grid.Puzzle;

/**
 * A {@link Puzzle} with additional lookahead functions.
 * <p>
 * This class is <em>not</em> thread-safe: It works on a single copy of the {@link Puzzle} passed at
 * construction time, without any locking.
 */
public final class ProbePuzzle implements Puzzle {

    /** The probe puzzle. */
    private final Puzzle puzzle;

    /** The dictionary. */
    private final CachedDictionary dictionary;

    /** The elimination space. */
    private final EliminationSpace els;

    /**
     * Constructs an instance.
     *
     * @param puzzleArg     a puzzle to probe
     * @param dictionaryArg a dictionary
     * @param elsArg        an elimination space
     */
    public ProbePuzzle(final Puzzle puzzleArg, final CachedDictionary dictionaryArg, final EliminationSpace elsArg) {
        puzzle = puzzleArg.copy();
        dictionary = dictionaryArg;
        els = elsArg;
    }

    @Override
    public Collection<Slot> slots() {
        return puzzle.slots();
    }

    @Override
    public Slot slot(final SlotIdentifier slotIdentifier) {
        return puzzle.slot(slotIdentifier);
    }

    @Override
    public ProbePuzzle copy() {
        return new ProbePuzzle(puzzle, dictionary, els);
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
     * Compute <em>an estimation</em> of the number of solutions for the grid zone impacted by the
     * given assignment if it were applied. It corresponds to the product of the estimated number of
     * candidates left for each connected variable.
     * <p>
     * Note that the estimation may return a value > 0 despite the grid not having an actual
     * solution.
     *
     * @param assignment the assignment to evaluate
     * @return the estimated number of local solutions for the grid after assignment; As the
     * estimation can be very large computation is made using {@link BigInteger}
     */
    public BigInteger computeNumberOfLocalSolutionsAfter(final Assignment assignment) {
        final Slot probedSlot = puzzle.slot(assignment.slotUid());
        probedSlot.assign(assignment.word());
        // @formatter:off
        final BigInteger numberOfSolutions = probedSlot
                .connectedSlots()
                .reduce(
                        BigInteger.ONE, // default value if no connected slot
                        (previous, slot) -> previous.signum() == 0
                                ? previous
                                : // already 0, don't probe
                                previous.multiply(BigInteger.valueOf(
                                        dictionary.candidates(slot).count())),
                        BigInteger::multiply);
        // @formatter:on
        probedSlot.unassign();
        return numberOfSolutions;
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
    public boolean hasSolutionAfter(final Unassignment unassignment, final Slot unassignable) {
        return hasSolutionAfter(Collections.singletonList(unassignment), unassignable);
    }

    /**
     * Returns whether after performing the given unassignment the given unassignable slot would
     * become assignable again.
     *
     * @param unassignments the unassignments to test
     * @param unassignable  the unassignable variable
     * @return whether after performing the given unassignment the given unassignable slot would
     * become assignable again
     */
    public boolean hasSolutionAfter(final List<Unassignment> unassignments, final Slot unassignable) {
        final List<String> unassignedValues = unassign(unassignments);
        final Set<String> probedEliminations = probeEliminationSpace(unassignments, unassignable);

        final Slot probedSlot = puzzle.slot(unassignable.uid());
        final boolean hasSolution =
                dictionary.reevaluatedCandidates(probedSlot).anyMatch(not(probedEliminations::contains));

        reassign(unassignedValues, unassignments);
        return hasSolution;
    }

    /**
     * Applies the given unassignements to this probe puzzle.
     *
     * @param unassignments the unassignments to apply
     * @return the unassigned values
     */
    private List<String> unassign(final List<Unassignment> unassignments) {
        final List<String> unassignedValues = new ArrayList<>(unassignments.size());
        for (final Unassignment unassignment : unassignments) {
            final String unassignedValue = puzzle.slot(unassignment.slotUid()).unassign();
            unassignedValues.add(unassignedValue);
        }
        return unassignedValues;
    }

    /**
     * Reassign the given unassigned values.
     *
     * @param unassignedValues the unassigned values
     * @param unassignments the unassignments (in same order as unassigned values)
     */
    private void reassign(final List<String> unassignedValues, final List<Unassignment> unassignments) {
        for (int i = 0; i < unassignments.size(); i++) {
            puzzle.slot(unassignments.get(i).slotUid()).assign(unassignedValues.get(i));
        }
    }

    /**
     * Probes the {@link #els elimination space} against the given unassignments.
     *
     * @param unassignments the unassignments to apply
     * @param unassignable  the unassignable slot
     * @return the set of eliminated values for the unassignable variable
     */
    private Set<String> probeEliminationSpace(final Collection<Unassignment> unassignments, final Slot unassignable) {
        // TODO that would be simpler if elimination space provided a deep copy method like puzzle
        final List<SlotIdentifier> modifiedVariables =
                unassignments.stream().map(Unassignment::slotUid).toList();
        final Map<String, Set<SlotIdentifier>> refreshedEliminations =
                new HashMap<>(els.eliminations(unassignable.uid()));
        final Iterator<Map.Entry<String, Set<SlotIdentifier>>> it =
                refreshedEliminations.entrySet().iterator();
        while (it.hasNext()) {
            final Map.Entry<String, Set<SlotIdentifier>> elimination = it.next();
            final Set<SlotIdentifier> reasons = elimination.getValue();
            if (!Collections.disjoint(reasons, modifiedVariables)) {
                it.remove();
            }
        }
        return refreshedEliminations.keySet();
    }
}
