/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static re.belv.croiseur.solver.ginsberg.PuzzleGridParser.parse;

/** A factory of assertions. */
final class Assertions {

    /** Private constructor, static methods only. */
    private Assertions() {
        // Nothing to do.
    }

    /**
     * Assert {@link SolverResult} matches the expected solution, given as a multi-line string.
     *
     * @param expected the expected result
     * @param result the actual result
     */
    static void assertSuccess(final String expected, final SolverResult result) {
        assertEquals(SolverResult.Kind.SUCCESS, result.kind());
        assertEquals(parse(expected).filled(), result.filledBoxes());
        assertTrue(result.unsolvableBoxes().isEmpty());
        // statistics ignored
    }
}
