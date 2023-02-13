/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg.heuristics.backtrack;

import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.Slot;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.SlotIdentifier;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.lookahead.Prober;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.lookahead.Unassignment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Chooses the backtrack candidate chooser.
 */
final class BacktrackCandidateChooser {

    /** The logger. */
    private static final Logger LOGGER =
            Logger.getLogger(BacktrackCandidateChooser.class.getName());

    /** The tool to probe the grid. */
    private final Prober prober;

    /**
     * Constructs an instance.
     *
     * @param proberArg the prober
     */
    BacktrackCandidateChooser(final Prober proberArg) {
        prober = proberArg;
    }

    List<SlotIdentifier> choose(final Set<SlotIdentifier> backtrackCandidates,
                                final Slot variable) {
        final Optional<SlotIdentifier> eliminated =
                backtrackCandidates.stream()
                                   .filter(backtrackCandidate -> prober.doesUnassignmentSolveIssue(Unassignment.of(backtrackCandidate), variable))
                                   .findFirst();

        final List<SlotIdentifier> eliminatedSlots;
        if (eliminated.isEmpty()) {
            LOGGER.info("No direct backtracking solves the problem, trying to backjump");
            // Accumulate eliminations until a combination works
            final Iterator<SlotIdentifier> candidatesLeft =
                    new LinkedHashSet<>(backtrackCandidates).iterator();
            eliminatedSlots = new ArrayList<>();
            boolean solutionFound = false;
            while (candidatesLeft.hasNext() && !solutionFound) {
                eliminatedSlots.add(candidatesLeft.next());
                candidatesLeft.remove();
                final List<Unassignment> unassignments = eliminatedSlots.stream()
                                                                        .map(Unassignment::of)
                                                                        .toList();
                LOGGER.info(() -> "Trying the following combined unassignments " + unassignments);
                solutionFound = prober.doesUnassignmentSolveIssue(unassignments, variable);
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

}
