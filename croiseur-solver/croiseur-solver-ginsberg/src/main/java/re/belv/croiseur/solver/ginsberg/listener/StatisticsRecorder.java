/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg.listener;

import re.belv.croiseur.solver.ginsberg.SolverResult;
import re.belv.croiseur.solver.ginsberg.core.Slot;

/**
 * Records solver statistics.
 */
public final class StatisticsRecorder implements SolverListener {

    /**
     * Implementation of {@link SolverResult.Statistics}.
     */
    private static final class StatisticsImpl implements SolverResult.Statistics {

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
    public StatisticsRecorder() {
        stat = new StatisticsImpl();
    }

    @Override
    public void onUnassignment(final Slot slot, final String unassignedWord) {
        stat.numberOfUnassignments++;
    }

    @Override
    public void onAssignment(final Slot slot, final String word) {
        stat.numberOfAssignments++;
    }

    /**
     * Return the current statistics.
     *
     * @return the current statistics
     */
    public SolverResult.Statistics statistics() {
        return stat;
    }
}
