/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.common.puzzle;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Definition of the grid of a crossword puzzle.
 *
 * @param width  width of the grid
 * @param height height of the grid
 * @param shaded coordinates of the shaded boxes
 * @param filled prefilled boxes
 */
public record PuzzleGrid(int width, int height, Set<GridPosition> shaded,
                         Map<GridPosition, Character> filled) {

    /**
     * A {@link PuzzleGrid} builder.
     */
    public static final class Builder {

        /** Shaded boxes. */
        private final Set<GridPosition> shaded;

        /** Filled boxes. */
        private final Map<GridPosition, Character> filled;

        /** Width of the grid. */
        private int width;

        /** Height of the grid. */
        private int height;

        /**
         * Constructor.
         */
        public Builder() {
            shaded = new HashSet<>();
            filled = new HashMap<>();
        }

        /**
         * Shades the given grid position.
         * <p>
         * Removes previous character associated to this position, if any.
         *
         * @param position the position to shade
         * @return this builder
         */
        public Builder shade(final GridPosition position) {
            filled.remove(position);
            shaded.add(position);
            return this;
        }

        /**
         * Fills the given position with the given character.
         * <p>
         * Removes shading on the given position, if any.
         *
         * @param position  the position to fill
         * @param character the character to set
         * @return this builder
         */
        public Builder fill(final GridPosition position, final Character character) {
            shaded.remove(position);
            filled.put(position, character);
            return this;
        }

        /**
         * Sets the width of the grid.
         *
         * @param widthArg the new width
         * @return this builder
         */
        public Builder width(final int widthArg) {
            width = widthArg;
            return this;
        }

        /**
         * Sets the height of the grid.
         *
         * @param heightArg the new height
         * @return this builder
         */
        public Builder height(final int heightArg) {
            height = heightArg;
            return this;
        }

        /**
         * Builds the {@link PuzzleGrid} from this builder.
         *
         * @return a new {@link PuzzleGrid}
         */
        public PuzzleGrid build() {
            return new PuzzleGrid(width, height, shaded, filled);
        }
    }

    /**
     * Validates grid.
     *
     * @param width  width of the grid
     * @param height height of the grid
     * @param shaded coordinates of the shaded boxes
     * @param filled prefilled boxes
     */
    public PuzzleGrid {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Invalid grid dimensions");
        }
        Objects.requireNonNull(shaded);
        shaded.forEach(validateCoordinates(width, height));

        Objects.requireNonNull(filled);
        filled.keySet().forEach(validateCoordinates(width, height));
        filled.values().forEach(Objects::requireNonNull);
    }

    /**
     * Return a coordinate validation function.
     *
     * @param width  width of the grid
     * @param height height of the grid
     * @return the validation function
     */
    private static Consumer<GridPosition> validateCoordinates(final int width, final int height) {
        return coordinate -> {
            if (coordinate.x() >= width || coordinate.y() >= height) {
                throw new IllegalArgumentException("Coordinates outside grid: " + coordinate);
            }
        };
    }

    /**
     * Returns the shaded boxes.
     *
     * @return the shaded boxes
     */
    public Set<GridPosition> shaded() {
        return Collections.unmodifiableSet(shaded);
    }

    /**
     * Returns the filled boxes.
     *
     * @return the filled boxes
     */
    public Map<GridPosition, Character> filled() {
        return Collections.unmodifiableMap(filled);
    }

}
