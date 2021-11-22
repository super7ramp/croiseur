package com.gitlab.super7ramp.crosswords.solver.lib.backtrack;

import com.gitlab.super7ramp.crosswords.solver.lib.core.Backtracker;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Connectable;
import com.gitlab.super7ramp.crosswords.solver.lib.core.InternalDictionary;
import com.gitlab.super7ramp.crosswords.solver.lib.history.History;

/**
 * A factory of backtracking strategies.
 */
public final class Backtrackers {

    /**
     * Private constructor, static factory methods only.
     */
    Backtrackers() {
        // Nothing to do.
    }

    /**
     * A {@link Backtracker} implementation that simply selects the last assigned variable and maintains a blacklist for
     * a single slot at a time.
     *
     * @param history assignment history
     * @return a {@link Backtracker} implementation that simply selects the last assigned variable
     */
    public static Backtracker backtrack(final History history) {
        return new Backtrack(history.instantiation(), history.backtrack());
    }

    /**
     * Similar to {@link #backtrack(History) simple backtrack} but avoids re-testing some candidate values by keeping
     * all previously blacklisted values in memory and use them when necessary.
     * <p>
     * In other words, it remembers already visited branches of the search tree in order to avoid visiting them again.
     * <p>
     * To be preferred over {@link #backtrack(History)} unless the search space is very small. The CPU-time gain is such
     * that the memory overhead is negligible.
     *
     * @param history assignment history
     * @return a {@link Backtracker} implementation that simply selects the last assigned variable
     */
    public static Backtracker backmark(final History history) {
        return new Backmark(history.instantiation(), history.backtrack());
    }

    /**
     * Backjump.
     * <p>
     * Selects the last assigned variable which is connected to the slot that initiated the backtrack. If no connected
     * variable found, fallback to {@link #backtrack(History)} simple backtrack.
     *
     * @param puzzle  access to slot connection information
     * @param history assignment history
     * @return the backjump {@link Backtracker}
     */
    public static Backtracker backjump(final Connectable puzzle, final History history) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Backjump with lookahead.
     * <p>
     * Similar to {@link #backjump(Connectable, History)} with the difference that a lookahead is performed to ensure
     * that the selected variable to unassign actually addresses the difficulty that lead to backtracking.
     *
     * @param puzzle     access to slot connection information and lookahead functions
     * @param history    assignment history
     * @param dictionary the dictionary
     * @return the smart backjump {@link Backtracker}
     */
    public static Backtracker smartBackjump(final Backjumpable puzzle, final History history,
                                            final InternalDictionary dictionary) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
