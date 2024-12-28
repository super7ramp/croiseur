/*
 * SPDX-FileCopyrightText: 2024 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg.benchmark;

import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import re.belv.croiseur.common.puzzle.PuzzleGrid;
import re.belv.croiseur.common.puzzle.PuzzleGridParser;

@State(Scope.Benchmark)
public class PuzzleGridProvider {

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

    public PuzzleGrid get() {
        return PuzzleGridParser.parse(puzzle);
    }
}
