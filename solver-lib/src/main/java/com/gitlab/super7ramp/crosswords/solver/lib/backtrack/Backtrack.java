package com.gitlab.super7ramp.crosswords.solver.lib.backtrack;

import com.gitlab.super7ramp.crosswords.solver.lib.core.Backtracker;
import com.gitlab.super7ramp.crosswords.solver.lib.history.BacktrackHistory;
import com.gitlab.super7ramp.crosswords.solver.lib.history.DeadEnd;
import com.gitlab.super7ramp.crosswords.solver.lib.history.InstantiationHistoryConsumer;

/**
 * {@link Backtracker} implementation that:
 * <ul>
 *     <li>chooses as variable to be unassigned the last assigned variable</li>
 *     <li>blacklist the unassigned value for the unassigned variable</li>
 *     <li>clear blacklist for other variables</li>
 * </ul>
 */
final class Backtrack extends AbstractBacktracker {

    /** Backtrack history. */
    private final BacktrackHistory backtrackHistory;

    /**
     * Constructor.
     *
     * @param anInstantiationHistory assignment history
     * @param aBacktrackHistory      un-assignment history
     */
    Backtrack(final InstantiationHistoryConsumer anInstantiationHistory, final BacktrackHistory aBacktrackHistory) {
        super(anInstantiationHistory);
        backtrackHistory = aBacktrackHistory;
    }

    @Override
    protected void updateBlackList(final DeadEnd deadEnd, final String unassignedValue) {
        if (!backtrackHistory.blacklist().containsKey(deadEnd.unassigned())) {
            backtrackHistory.clearBlacklist();
        }
        backtrackHistory.blacklist(deadEnd.unassigned(), unassignedValue);
    }
}
