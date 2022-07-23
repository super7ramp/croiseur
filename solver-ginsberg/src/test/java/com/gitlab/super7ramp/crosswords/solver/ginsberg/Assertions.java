package com.gitlab.super7ramp.crosswords.solver.ginsberg;

import com.gitlab.super7ramp.crosswords.spi.solver.SolverResult;

import static com.gitlab.super7ramp.crosswords.solver.ginsberg.PuzzleDefinitionParser.parsePuzzle;

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
    static void assertSuccess(final String expected, final SolverResult result) {
        final SolverResultImpl expectedResult =
                SolverResultImpl.success(parsePuzzle(expected).filled());
        org.junit.jupiter.api.Assertions.assertEquals(expectedResult, result);
    }

    /**
     * Assert {@link SolverResult} matches the expected solution, given as a multi-line string.
     *
     * @param expected the expected result
     * @param result   the actual result
     */
    static void assertImpossible(final String expected, final SolverResult result) {
        final SolverResultImpl expectedResult =
                SolverResultImpl.impossible(parsePuzzle(expected).filled());
        org.junit.jupiter.api.Assertions.assertEquals(expectedResult, result);
    }

}
