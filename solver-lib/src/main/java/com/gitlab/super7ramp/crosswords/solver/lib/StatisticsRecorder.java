package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.solver.api.SolverResult;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;
import com.gitlab.super7ramp.crosswords.solver.lib.core.SolverListener;

final class StatisticsRecorder implements SolverListener {

    /**
     * Implementation of {@link SolverResult.Statistics}.
     */
    private static class StatisticsImpl implements SolverResult.Statistics {

        private long numberOfUnassignments;

        private long numberOfAssignments;

        /**
         * Constructor.
         */
        StatisticsImpl() {
            // Nothing to do.
        }

        @Override
        public long numberOfAssignments() {
            return numberOfAssignments;
        }

        @Override
        public long numberOfUnassignments() {
            return numberOfUnassignments;
        }

        @Override
        public long eliminationSetSize() {
            // TODO use blacklist
            return 0;
        }

        @Override
        public String toString() {
            return "{" +
                    "numberOfUnassignments=" + numberOfUnassignments +
                    ", numberOfAssignments=" + numberOfAssignments +
                    '}';
        }
    }

    private final StatisticsImpl stat;

    /**
     * Constructor.
     */
    StatisticsRecorder() {
        stat = new StatisticsImpl();
    }

    @Override
    public void onPartialUnassignment(final Slot slot) {
        stat.numberOfUnassignments++;
    }

    @Override
    public void onUnassignment(final Slot slot, String unassignedWord) {
        stat.numberOfUnassignments++;
    }

    @Override
    public void onAssignment(final Slot slot, String word) {
        stat.numberOfAssignments++;
    }

    /**
     * Return the current statistics.
     *
     * @return the current statistics
     */
    SolverResult.Statistics statistics() {
        return stat;
    }
}
