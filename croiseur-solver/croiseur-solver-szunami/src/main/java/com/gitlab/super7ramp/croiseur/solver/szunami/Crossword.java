/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.szunami;

import java.util.Objects;

/**
 * Representation of a crossword puzzle.
 *
 * @param contents a single string representing all the grid; '*' represents a shaded box and ' '
 *                 (represents an empty box)
 * @param width    the grid width (i.e. the number of columns)
 * @param height   the grid height (i.e. the number of rows)
 * @see
 * <a href="https://docs.rs/xwords/0.3.1/xwords/crossword/struct.Crossword.html">Crate Documentation</a>
 */
public record Crossword(String contents, int width, int height) {

    /**
     * Constructs an instance.
     *
     * @param contents a single string representing all the grid; '*' represents a shaded box and
     *                 ' '
     *                 (represents an empty box)
     * @param width    the grid width (i.e. the number of columns)
     * @param height   the grid height (i.e. the number of rows)
     * @throws NullPointerException     if given contents is {@code null}
     * @throws IllegalArgumentException if width or height is inferior or equal to 0
     * @see
     * <a href="https://docs.rs/xwords/0.3.1/xwords/crossword/struct.Crossword.html">Crate Documentation</a>
     */
    public Crossword {
        Objects.requireNonNull(contents, "Contents cannot be null");
        if (width <= 0) {
            throw new IllegalArgumentException("Width must be superior to 0, got " + width);
        }
        if (height <= 0) {
            throw new IllegalArgumentException("Width must be superior to 0, got " + height);
        }
    }
}
