/*
 * SPDX-FileCopyrightText: 2024 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.benchmark;

import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import re.belv.croiseur.common.puzzle.PuzzleGrid;
import re.belv.croiseur.common.puzzle.PuzzleGridParser;

/**
 * The puzzle used in the benchmarks.
 *
 * <p>This class is not meant to be overridden, it is public and not final only for the JMH instrumentation to work.
 */
@State(Scope.Benchmark)
public class BenchPuzzle {

    @Param({
        """
            |#|#| | | |
            |#| | | | |
            | | | | | |
            | | | | |#|
            | | | |#|#|
            """,
        """
            |#|#|#| | | |#|#|#|
            |#|#| | | | | |#|#|
            |#| | | | | | | |#|
            | | | | |#| | | | |
            | | | |#|#|#| | | |
            | | | | |#| | | | |
            |#| | | | | | | |#|
            |#|#| | | | | |#|#|
            |#|#|#| | | |#|#|#|
            """,
        """
            | | | | |#| | | |#| | | | |
            | | | | |#| | | |#| | | | |
            | | | | |#| | | |#| | | | |
            | | | | | | |#| | | | | | |
            |#|#|#| | | |#| | | |#|#|#|
            | | | | | |#|#|#| | | | | |
            | | | |#|#|#|#|#|#|#| | | |
            | | | | | |#|#|#| | | | | |
            |#|#|#| | | |#| | | |#|#|#|
            | | | | | | |#| | | | | | |
            | | | | |#| | | |#| | | | |
            | | | | |#| | | |#| | | | |
            | | | | |#| | | |#| | | | |,
            """,
        """
            | | | | |#| | | |#| | | | |
            | | | | |#| | | |#| | | | |
            | | | | |#| | | |#| | | | |
            | | | | | | | | | | | | | |
            |#|#|#| | | |#| | | |#|#|#|
            | | | | | |#|#|#| | | | | |
            | | | | |#|#|#|#|#| | | | |
            | | | | | |#|#|#| | | | | |
            |#|#|#| | | |#| | | |#|#|#|
            | | | | | | | | | | | | | |
            | | | | |#| | | |#| | | | |
            | | | | |#| | | |#| | | | |
            | | | | |#| | | |#| | | | |
            """,
        """
            | | | | |#| | | | | |#| | | | |
            | | | | |#| | | | | |#| | | | |
            | | | | |#| | | | | |#| | | | |
            | | | | | | | | |#| | | | | | |
            |#|#|#| | | | |#| | | | |#|#|#|
            | | | | | | |#| | | | | | | | |
            | | | | | |#| | | | | |#| | | |
            | | | | |#| | | | | |#| | | | |
            | | | |#| | | | | |#| | | | | |
            | | | | | | | | |#| | | | | | |
            |#|#|#| | | | |#| | | | |#|#|#|
            | | | | | | |#| | | | | | | | |
            | | | | |#| | | | | |#| | | | |
            | | | | |#| | | | | |#| | | | |
            | | | | |#| | | | | |#| | | | |
        """,
    })
    private String puzzle;

    /**
     * Returns the puzzle.
     *
     * @return the puzzle
     */
    public final PuzzleGrid get() {
        return PuzzleGridParser.parse(puzzle);
    }
}
