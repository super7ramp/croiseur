package com.gitlab.super7ramp.crosswords.solver.paulgb;

import java.util.Optional;

 /**
 * A solver wrapping paulgb's crossword-composer written in Rust.
  *
  * @see <a href="https://github.com/paulgb/crossword-composer/blob/master/README.md#auto-filler">
  *     The crossword-composer documentation
  *     </a>
 */
public final class Solver {

    static {
        NativeLibLoader.loadLibrary("crossword_composer_jni");
    }

    /**
     * Solves the given grid with the given dictionary.
     * <p>
     * The returned character arrays represent the filled crossword grid cells. The index in the
     * array is the identifier of the cell as given in the grid parameter. In image:
     * <p>
     * <img src="https://raw.githubusercontent.com/paulgb/crossword-composer/master/images/output_representation.png"/>
     *
     * @param grid the grid
     * @param dictionary the dictionary
     * @return an {@link Optional} containing the filled crossword grid cells if solver has
     * succeeded; {@link Optional#empty()} otherwise
     */
    // TODO maybe create a Solution type encapsulating the char array
    public native Optional<char[]> solve(final Grid grid, final Dictionary dictionary);

}
