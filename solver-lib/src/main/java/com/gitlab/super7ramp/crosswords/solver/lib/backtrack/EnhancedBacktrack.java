package com.gitlab.super7ramp.crosswords.solver.lib.backtrack;

import com.gitlab.super7ramp.crosswords.solver.lib.core.Backtracker;
import com.gitlab.super7ramp.crosswords.solver.lib.history.BacktrackHistoryWriter;
import com.gitlab.super7ramp.crosswords.solver.lib.history.DeadEnd;
import com.gitlab.super7ramp.crosswords.solver.lib.history.InstantiationHistoryConsumer;

import java.util.Set;
import java.util.logging.Logger;

/**
 * {@link Backtracker} implementation that:
 * <ul>
 *     <li>chooses as variable to be unassigned the last assigned variable</li>
 *     <li>clear blacklist if dead-end never occurred between last assigned variable and unassignable variable,
 *     otherwise restore it</li>
 *     <li>blacklist the value of the unassigned variable</li>
 * </ul>
 */
final class EnhancedBacktrack extends AbstractBacktracker {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(EnhancedBacktrack.class.getName());

    /**
     * Backtrack history.
     */
    private final BacktrackHistoryWriter backtrackHistory;

    /**
     * Constructor
     *
     * @param anInstantiationHistory assignment history
     * @param aBacktrackHistory      un-assignment history
     */
    EnhancedBacktrack(final InstantiationHistoryConsumer anInstantiationHistory, final BacktrackHistoryWriter aBacktrackHistory) {
        super(anInstantiationHistory);
        backtrackHistory = aBacktrackHistory;
    }

    @Override
    protected void updateBlackList(final DeadEnd deadEnd, final String unassignedValue) {
        if (!backtrackHistory.contains(deadEnd)) {
            // It's the first time this dead-end is being solved. Let's replace pruned search tree by a new one.
            LOGGER.info(() -> "Dead-end " + deadEnd + " is new, creating new blacklist");
            backtrackHistory.clearBlacklist();
        } else {
            // This dead-end was already solved before: It means the part of the search tree which was pruned before
            // can be combined with the currently pruned search tree.
            LOGGER.info(() -> "Dead-end " + deadEnd + " already encountered, adding it to current blacklist");
        }

        backtrackHistory.record(deadEnd, unassignedValue);
        final Set<String> deadEndValues = backtrackHistory.record(deadEnd, unassignedValue);
        deadEndValues.forEach(s -> backtrackHistory.blacklist(deadEnd.unassigned(), s));
    }
}
