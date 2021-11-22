package com.gitlab.super7ramp.crosswords.solver.lib.history;

import com.gitlab.super7ramp.crosswords.solver.lib.core.SlotIdentifier;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of {@link BacktrackHistory}.
 */
// TODO split blacklist from history
final class BacktrackHistoryImpl implements BacktrackHistory {

    /** The encountered dead-end situations. */
    private final Map<DeadEnd, Set<String>> deadEnds;

    private final Map<SlotIdentifier, Set<String>> blacklist;

    /**
     * Constructor.
     */
    BacktrackHistoryImpl() {
        deadEnds = new HashMap<>();
        blacklist = new HashMap<>();
    }

    @Override
    public Set<String> record(final DeadEnd deadEnd, final String unassignedValue) {
        deadEnds.computeIfAbsent(deadEnd, key -> new HashSet<>()).add(unassignedValue);
        return deadEnds.get(deadEnd);
    }

    @Override
    public boolean contains(final DeadEnd deadEnd) {
        return deadEnds.containsKey(deadEnd);
    }

    @Override
    public void clearBlacklist() {
        blacklist.clear();
    }

    @Override
    public void blacklist(final SlotIdentifier unassignedSlot, final String unassignedValue) {
        blacklist.computeIfAbsent(unassignedSlot, key -> new HashSet<>()).add(unassignedValue);
    }

    @Override
    public Map<SlotIdentifier, Set<String>> blacklist() {
        return Collections.unmodifiableMap(blacklist);
    }
}
