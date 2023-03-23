/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg.heuristics.backtrack;

import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.Slot;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.SlotIdentifier;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.sap.Backtracker;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.sap.Elimination;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.dictionary.CachedDictionary;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.grid.Puzzle;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.history.History;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.lookahead.Prober;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.lookahead.Unassignment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static java.util.Comparator.comparingLong;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toSet;

/**
 * A dynamic backtracker, inspired by the "Dynamic Backtracking" paper.
 */
final class DynamicBacktracker implements Backtracker<Slot, SlotIdentifier> {

    /** The logger. */
    private static final Logger LOGGER = Logger.getLogger(DynamicBacktracker.class.getName());

    /** The puzzle. */
    private final Puzzle puzzle;

    /** The assignment history. */
    private final History history;

    /** Lookahead utils. */
    private final Prober prober;

    /**
     * Constructs an instance.
     *
     * @param puzzleArg     the puzzle
     * @param dictionaryArg the dictionary
     * @param historyArg    the history
     */
    DynamicBacktracker(final Puzzle puzzleArg, final CachedDictionary dictionaryArg,
                       final History historyArg) {
        prober = new Prober(puzzleArg, dictionaryArg);
        puzzle = puzzleArg;
        history = historyArg;
    }

    @Override
    public List<Elimination<Slot, SlotIdentifier>> backtrackFrom(final Slot variable) {
        LOGGER.fine(() -> variable + " is not assignable, looking for a backtrack point");
        final Set<SlotIdentifier> candidates = candidatesFrom(variable);
        final List<SlotIdentifier> chosen = choose(candidates, variable);
        final List<Elimination<Slot, SlotIdentifier>> eliminations = eliminationsFrom(candidates,
                chosen);
        LOGGER.fine(() -> "Backtrack gave the following eliminations: " + eliminations);
        return eliminations;
    }

    /**
     * Short-lists the backtrack candidates.
     * <p>
     * The backtrack candidates are typically the connected slots of the unassignable variable.
     *
     * @param unassignable the unassignable variable
     * @return the backtrack candidates
     */
    private Set<SlotIdentifier> candidatesFrom(final Slot unassignable) {
        // direct candidates
        final Stream<SlotIdentifier> directCandidates
                = unassignable.connectedSlots()
                              .filter(Slot::isInstantiated)
                              .map(Slot::uid)
                              .sorted(comparingLong(history::assignmentNumber).reversed());

        // candidates at distance 2
        final Stream<SlotIdentifier> indirectCandidates =
                unassignable.connectedSlots()
                            .filter(not(Slot::isInstantiated))
                            .flatMap(Slot::connectedSlots)
                            .filter(Slot::isInstantiated)
                            .map(Slot::uid)
                            .sorted(comparingLong(history::assignmentNumber).reversed());

        return Stream.of(directCandidates, indirectCandidates)
                     .flatMap(Function.identity())
                     .collect(toCollection(LinkedHashSet::new));
    }

    /**
     * Chooses the slots to eliminate among given candidates.
     *
     * @param candidates   the backtrack candidates
     * @param unassignable the unassignable variable that lead to backtrack
     * @return the chosen eliminated slots
     */
    List<SlotIdentifier> choose(final Set<SlotIdentifier> candidates,
                                final Slot unassignable) {
        final Optional<SlotIdentifier> eliminated =
                candidates.stream()
                          .filter(candidate -> prober.hasSolutionAfter(Unassignment.of(candidate)
                                  , unassignable))
                          .findFirst();

        final List<SlotIdentifier> eliminatedSlots;
        if (eliminated.isEmpty()) {
            LOGGER.info("No direct backtracking solves the problem, trying to backjump");
            // Accumulate eliminations until a combination works
            final Iterator<SlotIdentifier> candidatesLeft =
                    new LinkedHashSet<>(candidates).iterator();
            eliminatedSlots = new ArrayList<>();
            boolean solutionFound = false;
            while (candidatesLeft.hasNext() && !solutionFound) {
                eliminatedSlots.add(candidatesLeft.next());
                candidatesLeft.remove();
                final List<Unassignment> unassignments =
                        eliminatedSlots.stream().map(Unassignment::of).toList();
                LOGGER.info(() -> "Trying the following combined unassignments " + unassignments);
                solutionFound = prober.hasSolutionAfter(unassignments, unassignable);
            }
            if (!solutionFound) {
                // definitely no solution
                eliminatedSlots.clear();
            }
        } else {
            eliminatedSlots = Collections.singletonList(eliminated.get());
        }
        return eliminatedSlots;
    }


    /**
     * Builds the eliminations corresponding to the given eliminated slots.
     *
     * @param candidates all the backtrack candidates
     * @param chosen     the chosen eliminated slots
     * @return the eliminations corresponding to the given eliminated slots
     */
    private List<Elimination<Slot, SlotIdentifier>> eliminationsFrom(final Set<SlotIdentifier> candidates, final List<SlotIdentifier> chosen) {
        final List<Elimination<Slot, SlotIdentifier>> eliminations;
        if (!chosen.isEmpty()) {
            final Set<SlotIdentifier> reasons =
                    candidates.stream().filter(uid -> !chosen.contains(uid)).collect(toSet());
            if (reasons.isEmpty()) {
                LOGGER.info("No reason found, adding global reason.");
                reasons.add(new SlotIdentifier(-1));
            }
            eliminations =
                    chosen.stream()
                          .map(eliminated -> new Elimination<>(puzzle.slot(eliminated), reasons))
                          .toList();
        } else {
            eliminations = Collections.emptyList();
        }
        return eliminations;
    }
}
