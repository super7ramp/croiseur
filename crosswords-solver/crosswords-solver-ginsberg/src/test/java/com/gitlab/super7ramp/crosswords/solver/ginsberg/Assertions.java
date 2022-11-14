package com.gitlab.super7ramp.crosswords.solver.ginsberg;

import static com.gitlab.super7ramp.crosswords.solver.ginsberg.PuzzleDefinitionParser.parsePuzzle;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        assertEquals(SolverResult.Kind.SUCCESS, result.kind());
        assertEquals(parsePuzzle(expected).filled(), result.filledBoxes());
        assertTrue(result.unsolvableBoxes().isEmpty());
        // statistics ignored
    }

 }
