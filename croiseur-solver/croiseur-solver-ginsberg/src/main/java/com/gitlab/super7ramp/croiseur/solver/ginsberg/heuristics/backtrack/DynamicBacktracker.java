/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg.heuristics.backtrack;

import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.Slot;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.SlotIdentifier;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.sap.Backtracker;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.sap.Elimination;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.grid.Puzzle;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.history.History;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.lookahead.Prober;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
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

    private final BacktrackCandidateChooser chooser;

    private final Puzzle puzzle;

    private final History history;

    /**
     * Constructs an instance.
     *
     * @param proberArg  the prober
     * @param puzzleArg  the puzzle
     * @param historyArg the history
     */
    DynamicBacktracker(final Prober proberArg, final Puzzle puzzleArg, final History historyArg) {
        chooser = new BacktrackCandidateChooser(proberArg);
        puzzle = puzzleArg;
        history = historyArg;
    }

    @Override
    public List<Elimination<Slot, SlotIdentifier>> backtrackFrom(final Slot variable) {

        LOGGER.fine(() -> variable + " is not assignable, looking for a backtrack point");
        final Set<SlotIdentifier> backtrackCandidates = shortListBacktrackCandidates(variable);
        final List<SlotIdentifier> eliminatedSlots = chooser.choose(backtrackCandidates,
                variable);
        final List<Elimination<Slot, SlotIdentifier>> eliminations =
                buildEliminations(backtrackCandidates, eliminatedSlots);
        LOGGER.fine(() -> "Backtrack gave the following eliminations: " + eliminations);
        return eliminations;
    }

    private List<Elimination<Slot, SlotIdentifier>> buildEliminations(final Set<SlotIdentifier> backtrackCandidates, final List<SlotIdentifier> eliminatedSlots) {
        final List<Elimination<Slot, SlotIdentifier>> eliminations;
        if (!eliminatedSlots.isEmpty()) {
            final Set<SlotIdentifier> reasons =
                    backtrackCandidates.stream()
                                       .filter(uid -> !eliminatedSlots.contains(uid))
                                       .collect(toSet());
            if (reasons.isEmpty()) {
                LOGGER.info("No reason found, adding global reason.");
                reasons.add(new SlotIdentifier(-1));
            }
            eliminations =
                    eliminatedSlots.stream()
                                   .map(eliminated -> new Elimination<>(puzzle.slot(eliminated)
                                           , reasons))
                                   .toList();
        } else {
            eliminations = Collections.emptyList();
        }
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
    private Set<SlotIdentifier> shortListBacktrackCandidates(final Slot unassignable) {
        // direct candidates
        final Stream<SlotIdentifier> directCandidates
                = unassignable.connectedSlots()
                              .filter(Slot::isInstantiated)
                              .map(Slot::uid)
                              .sorted(comparingLong(history::assignmentDate).reversed());

        // candidates at distance 2
        final Stream<SlotIdentifier> indirectCandidates =
                unassignable.connectedSlots()
                            .filter(not(Slot::isInstantiated))
                            .flatMap(Slot::connectedSlots)
                            .filter(Slot::isInstantiated)
                            .map(Slot::uid)
                            .sorted(comparingLong(history::assignmentDate).reversed());

        return Stream.of(directCandidates, indirectCandidates)
                     .flatMap(Function.identity())
                     .collect(toCollection(LinkedHashSet::new));
    }
}
