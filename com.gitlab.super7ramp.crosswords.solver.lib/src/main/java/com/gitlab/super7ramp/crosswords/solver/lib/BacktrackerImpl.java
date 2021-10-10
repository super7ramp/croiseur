package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.solver.lib.comparators.Comparators;
import com.gitlab.super7ramp.crosswords.solver.lib.db.WordDatabase;

import java.util.Collections;
import java.util.Set;

/**
 * Implementation of {@link Backtracker}.
 */
final class BacktrackerImpl implements Backtracker {

    /**
     * Access to the grid.
     */
    private final Puzzle puzzle;

    /**
     * Dictionary.
     */
    private final WordDatabase dictionary;

    /**
     * Constructor.
     *
     * @param aPuzzle access to the grid
     */
    BacktrackerImpl(final Puzzle aPuzzle, final WordDatabase aDictionary) {
        puzzle = aPuzzle;
        dictionary = aDictionary;
    }

    @Override
    public Set<Slot> backtrackFrom(final Slot variable) {

        final Slot connectedToRevert = puzzle.connectedSlots(variable).stream()
                .filter(connected -> connected.value().isPresent())
                .min(Comparators.byNumberOfCandidates(dictionary))
                .orElseThrow(() -> new IllegalStateException("Failed to backtrack, aborting"));

        dictionary.resetBlacklist();
        dictionary.blacklist(connectedToRevert.uid(), connectedToRevert.value().get());

        return Collections.singleton(connectedToRevert);
    }
}
