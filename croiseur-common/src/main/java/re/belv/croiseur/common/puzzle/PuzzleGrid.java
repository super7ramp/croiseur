/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.common.puzzle;

import static re.belv.croiseur.common.puzzle.GridPosition.at;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Definition of the grid of a crossword puzzle.
 *
 * @param width width of the grid
 * @param height height of the grid
 * @param shaded coordinates of the shaded boxes
 * @param filled prefilled boxes
 */
public record PuzzleGrid(int width, int height, Set<GridPosition> shaded, Map<GridPosition, Character> filled) {

    /** A {@link PuzzleGrid} builder. */
    public static final class Builder {

        /** Shaded boxes. */
        private final Set<GridPosition> shaded;

        /** Filled boxes. */
        private final Map<GridPosition, Character> filled;

        /** Width of the grid. */
        private int width;

        /** Height of the grid. */
        private int height;

        /** Constructor. */
        public Builder() {
            shaded = new HashSet<>();
            filled = new HashMap<>();
        }

        /**
         * Shades the given grid position.
         *
         * <p>Removes previous character associated to this position, if any.
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
         *
         * <p>Removes shading on the given position, if any.
         *
         * @param position the position to fill
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

    /** The minimal size of a group of boxes to be considered as a slot. */
    private static final int MIN_SLOT_LENGTH = 2;

    /**
     * Validates grid.
     *
     * @param width width of the grid
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
     * @param width width of the grid
     * @param height height of the grid
     * @return the validation function
     */
    private static Consumer<GridPosition> validateCoordinates(final int width, final int height) {
        return coordinate -> {
            if (coordinate.x() >= width || coordinate.y() >= height) {
                throw new IllegalArgumentException(
                        "Coordinates outside " + width + "x" + height + " grid: " + coordinate);
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

    /**
     * Returns the across slot contents, naturally sorted according to the slot position on the grid (top to down then
     * left to right).
     *
     * <p>Non-filled boxes will be replaced by the character '.'.
     *
     * <p>Note that a group of boxes must contain at least {@value MIN_SLOT_LENGTH} boxes to be considered as a slot and
     * have its content returned by this method.
     *
     * @return the across slot contents
     */
    public List<String> acrossSlotContents() {
        final List<String> slotContents = new ArrayList<>();
        final StringBuilder contentBuffer = new StringBuilder();
        for (int row = 0; row < height; row++) {
            int columnStart = 0;
            for (int column = columnStart; column < width; column++) {
                final GridPosition position = at(column, row);
                if (shaded.contains(position)) {
                    if (column - columnStart >= MIN_SLOT_LENGTH) {
                        slotContents.add(contentBuffer.toString());
                    }
                    columnStart = column + 1;
                    contentBuffer.delete(0, contentBuffer.length());
                } else {
                    contentBuffer.append(filled.getOrDefault(position, '.'));
                }
            }
            if (width - columnStart >= MIN_SLOT_LENGTH) {
                slotContents.add(contentBuffer.toString());
            }
            contentBuffer.delete(0, contentBuffer.length());
        }
        return slotContents;
    }

    /**
     * Returns the down slot contents, naturally sorted according to the slot position on the grid (left to right then
     * top to down).
     *
     * <p>Non-filled boxes will be replaced by the character '.'.
     *
     * <p>Note that a group of boxes must contain at least {@value MIN_SLOT_LENGTH} boxes to be considered as a slot and
     * have its content returned by this method.
     *
     * @return the down slot contents
     */
    public List<String> downSlotContents() {
        final List<String> slotContents = new ArrayList<>();
        final StringBuilder contentBuffer = new StringBuilder();
        for (int column = 0; column < width; column++) {
            int rowStart = 0;
            for (int row = rowStart; row < height; row++) {
                final GridPosition position = at(column, row);
                if (shaded.contains(position)) {
                    if (row - rowStart >= MIN_SLOT_LENGTH) {
                        slotContents.add(contentBuffer.toString());
                    }
                    rowStart = row + 1;
                    contentBuffer.delete(0, contentBuffer.length());
                } else {
                    contentBuffer.append(filled.getOrDefault(position, '.'));
                }
            }
            if (height - rowStart >= MIN_SLOT_LENGTH) {
                slotContents.add(contentBuffer.toString());
            }
            contentBuffer.delete(0, contentBuffer.length());
        }
        return slotContents;
    }

    /**
     * Returns all the slot contents, unordered.
     *
     * <p>Size may be less than the sum of the sizes of {@link #acrossSlotContents()} and {@link #downSlotContents()}
     * since dupe words are removed.
     *
     * @return all the slot contents
     */
    public Set<String> slotContents() {
        final Set<String> slots = new HashSet<>(acrossSlotContents());
        slots.addAll(downSlotContents());
        return slots;
    }
}
