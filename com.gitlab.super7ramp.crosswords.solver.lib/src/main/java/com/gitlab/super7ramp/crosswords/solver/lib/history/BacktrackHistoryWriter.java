package com.gitlab.super7ramp.crosswords.solver.lib.history;

import com.gitlab.super7ramp.crosswords.solver.lib.core.SlotIdentifier;

import java.util.Set;

public interface BacktrackHistoryWriter {

    /**
     * Assess whether a dead-end has already occurred.
     *
     * @param deadEnd the dead-end situation to assess
     * @return <code>true</code> iff given situation has already occurred
     */
    boolean contains(final DeadEnd deadEnd);

    void clearBlacklist();

    void blacklist(final SlotIdentifier slotId, final String value);

    /**
     * Record a solution for a dead-end situation.
     *
     * @param deadEnd         the dead-end situation
     * @param unassignedValue the solution, i.e. the unassigned value
     * @return the updated dead-end values for this situation
     */
    Set<String> record(final DeadEnd deadEnd, final String unassignedValue);

}
