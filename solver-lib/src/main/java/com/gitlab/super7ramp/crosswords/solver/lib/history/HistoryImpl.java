package com.gitlab.super7ramp.crosswords.solver.lib.history;

/**
 * Implementation of {@link History}.
 */
public final class HistoryImpl implements History {

    private final InstantiationHistoryImpl instantiationHistory;

    /**
     * Dead-end history.
     */
    private final BacktrackHistoryImpl backtrackHistory;

    /**
     * Constructor.
     */
    public HistoryImpl() {
        instantiationHistory = new InstantiationHistoryImpl();
        backtrackHistory = new BacktrackHistoryImpl();
    }

    @Override
    public InstantiationHistory instantiation() {
        return instantiationHistory;
    }

    @Override
    public BacktrackHistory backtrack() {
        return backtrackHistory;
    }
}
