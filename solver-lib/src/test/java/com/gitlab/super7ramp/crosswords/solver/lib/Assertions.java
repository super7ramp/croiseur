package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.solver.api.SolverResult;

import static com.gitlab.super7ramp.crosswords.solver.lib.PuzzleDefinitionParser.parsePuzzle;

/**
 * A factory of assertions.
 */
final class Assertions {

    /**
     * Private constructor, static methods only.
     */
    private Assertions() {
        // Nothing to do.
    }

    /**
     * Assert {@link SolverResult} matches the expected solution, given as a multi-line string.
     *
     * @param expected the expected result
     * @param result   the actual result
     */
    static void assertEquals(final String expected, final SolverResult result) {
        final SolverResultImpl expectedResult = new SolverResultImpl(parsePuzzle(expected).filled());
        org.junit.jupiter.api.Assertions.assertEquals(expectedResult, result);
    }

}
