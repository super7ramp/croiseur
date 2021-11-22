package com.gitlab.super7ramp.crosswords.solver.lib.history;

import com.gitlab.super7ramp.crosswords.solver.lib.core.SlotIdentifier;

import java.util.Map;
import java.util.Set;

/**
 * Allows to prune the search tree.
 */
public interface BacktrackHistoryReader {

    /**
     * The blacklist deduced from the history.
     *
     * @return the blacklist computed from this history.
     */
    Map<SlotIdentifier, Set<String>> blacklist();
}
