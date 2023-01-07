package com.gitlab.super7ramp.crosswords.solver.paulgb;

/**
 * A grid filled with characters.
 * <p>
 * The returned character arrays represent the filled crossword grid cells. The index in the
 * array is the identifier of the cell as given in the grid parameter. In image:
 * <p>
 * <img alt="Output Representation" src="https://raw.githubusercontent.com/paulgb/crossword-composer/master/images/output_representation.png"/>
 *
 * @param cells the filled grid cells
 */
public record Solution(char[] cells) {
    // Nothing to add.
}
