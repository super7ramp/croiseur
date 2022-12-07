package com.gitlab.super7ramp.crosswords.solver.paulgb;

import java.util.Optional;

/**
 * A solver.
 */
public final class Solver {

    static {
        System.loadLibrary("crossword_composer_jni");
    }

    /**
     * Solves the given grid with the given dictionary.
     *
     * @param grid the grid
     * @param dictionary the dictionary
     * @return
     */
    public native Optional<char[]> solve(final Grid grid, final Dictionary dictionary);

}
