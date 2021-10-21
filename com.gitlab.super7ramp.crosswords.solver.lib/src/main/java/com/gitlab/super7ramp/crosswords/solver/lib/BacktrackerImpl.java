package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.solver.lib.comparators.Comparators;
import com.gitlab.super7ramp.crosswords.solver.lib.core.AdaptedDictionary;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Backtracker;
import com.gitlab.super7ramp.crosswords.solver.lib.core.History;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Puzzle;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;

import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Implementation of {@link Backtracker}.
 */
final class BacktrackerImpl implements Backtracker {

    /** Logger. */
    private static Logger LOGGER = Logger.getLogger(BacktrackerImpl.class.getName());

    /**
     * Access to the grid.
     */
    private final Puzzle puzzle;

    private final Comparator<Slot> byNumberOfCandidatesThenByAge;

    /**
     * Dictionary.
     */
    private final AdaptedDictionary dictionary;

    /**
     * History of assignments and unassignments.
     */
    private final History history;

    /**
     * Constructor.
     *
     * @param aPuzzle access to the grid
     */
    BacktrackerImpl(final Puzzle aPuzzle, final AdaptedDictionary aDictionary, final History aHistory) {
        puzzle = aPuzzle;
        history = aHistory;
        dictionary = aDictionary;
        byNumberOfCandidatesThenByAge = Comparators.byNumberOfCandidates(dictionary).thenComparing(Comparators.byAssignmentAge(history));
    }

    @Override
    public Set<Slot> backtrackFrom(final Slot variable) {

        final Optional<Slot> connectedSlotToRevert = puzzle.connectedSlots(variable).stream()
                .filter(slot -> slot.value().isPresent())
                .min(byNumberOfCandidatesThenByAge);

        final Slot slotToRevert;
        if (connectedSlotToRevert.isPresent()) {
            slotToRevert = connectedSlotToRevert.get();
        } else {
            LOGGER.warning(() -> "Couldn't find a connected variable to revert, looking in the whole grid");
            slotToRevert = puzzle.slots().stream()
                    .filter(slot -> slot.value().isPresent())
                    .min(byNumberOfCandidatesThenByAge)
                    .orElseThrow(() -> new IllegalStateException("Failed to backtrack, aborting"));
        }

        return Collections.singleton(slotToRevert);
    }
}
