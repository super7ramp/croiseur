package com.gitlab.super7ramp.crosswords.solver.paulgb;

import java.util.Optional;

/**
 * A solver wrapping paulgb's crossword-composer written in Rust.
 *
 * @see <a href="https://github.com/paulgb/crossword-composer/blob/master/README.md#auto-filler">
 * The crossword-composer documentation
 * </a>
 */
public final class Solver {

    static {
        NativeLibLoader.loadLibrary("crossword_composer_jni");
    }

    /**
     * Solves the given puzzle with the given dictionary.
     *
     * @param puzzle     the puzzle to solve
     * @param dictionary the dictionary
     * @return the {@link Solution}
     * @throws SolverErrorException if solver encountered an error
     */
    public native Optional<Solution> solve(final Puzzle puzzle, final Dictionary dictionary)
            throws SolverErrorException;

}
