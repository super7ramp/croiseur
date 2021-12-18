package com.gitlab.super7ramp.crosswords.solver.lib.history;

/**
 * History of encountered dead-ends and their resolution.
 * <p>
 * This class aims to provides a way to quickly identify visited branches of the solution tree
 * without actually storing the entire state of all the visited branches. It does so by only storing
 * information about the dead-end situations, i.e. when a variable cannot be assigned anymore.
 * </p>
 * Rationale behind this choice is the following: In a program trying to find only one solution
 * to a constraint problem, identifying already visited situations is only necessary when
 * backtracking, i.e. when trying to resolve a dead-end situation.
 *
 * @see com.gitlab.super7ramp.crosswords.solver.lib.core.Backtracker
 */
public interface BacktrackHistory extends BacktrackHistoryReader, BacktrackHistoryWriter {
    // For convenience.
}
