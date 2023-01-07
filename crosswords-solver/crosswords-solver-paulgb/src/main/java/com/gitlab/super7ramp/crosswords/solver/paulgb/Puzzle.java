package com.gitlab.super7ramp.crosswords.solver.paulgb;

/**
 * The crossword grid puzzle.
 * <p>
 * This structure represents the structure of an <em>empty</em> grid (Crossword Composer doesn't
 * support partially pre-filled grids):
 * <ul>
 *     <li>An integer identifies a <em>cell</em> of the grid (i.e. a box for a single letter);</li>
 *     <li>An array of integers represents a <em>slot</em> of the grid (i.e. for traditional
 *     crossword puzzle a contiguous slice of cells in which a word must be placed);
 *     <li>The array of arrays of integers represents all the slots of the grid</li>
 * </ul>
 * <img alt="Input Representation" src="https://raw.githubusercontent.com/paulgb/crossword-composer/master/images/input_representation.png"/>
 * <p>
 * Note: There is a wording difference with Crossword Composer documentation here: Here, the word
 * <em>slot</em> designates the place for a word whereas Crossword Composer documentation refers to
 * a <em>slot</em> as a place for a single letter (which is here a <em>cell</em>).
 *
 * @param slots the slots
 * @see <a href="https://github.com/paulgb/crossword-composer/blob/master/README.md#auto-filler">
 *     The Crossword Composer documentation
 *     </a>
 */
public record Puzzle(int[][] slots) {
    // Nothing to add.
}
