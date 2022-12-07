package com.gitlab.super7ramp.crosswords.solver.paulgb;

/**
 * A crossword grid.
 *
 * @param slots the slots
 */
// TODO long -> int: usize in Rust may be 32bit only depending on the platform
public record Grid(long[][] slots) {
    // Nothing to add.
}
