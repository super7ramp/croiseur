package com.gitlab.super7ramp.crosswords.common;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Definition of a crossword puzzle.
 */
public final class PuzzleDefinition {

    /**
     * A {@link PuzzleDefinition} builder.
     */
    public static final class PuzzleDefinitionBuilder {

        /** Width of the grid. */
        private int width;

        /** Height of the grid. */
        private int height;

        /** Shaded boxes. */
        private Set<GridPosition> shaded;

        /** Filled boxes. */
        private Map<GridPosition, Character> filled;

        /**
         * Constructor.
         */
        public PuzzleDefinitionBuilder() {
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
        public PuzzleDefinitionBuilder shade(final GridPosition position) {
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
        public PuzzleDefinitionBuilder fill(final GridPosition position,
                                            final Character character) {
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
        public PuzzleDefinitionBuilder width(final int widthArg) {
            width = widthArg;
            return this;
        }

        /**
         * Sets the height of the grid.
         *
         * @param heightArg the new height
         * @return this builder
         */
        public PuzzleDefinitionBuilder height(final int heightArg) {
            height = heightArg;
            return this;
        }

        /**
         * Builds the {@link PuzzleDefinition} from this builder.
         *
         * @return a new {@link PuzzleDefinition}
         */
        public PuzzleDefinition build() {
            return new PuzzleDefinition(width, height, shaded, filled);
        }
    }

    /** Width of the grid. */
    private final int width;

    /** Height of the grid. */
    private final int height;

    /** Shaded boxes. */
    private final Set<GridPosition> shaded;

    /** Filled boxes. */
    private final Map<GridPosition, Character> filled;

    /**
     * Constructor.
     *
     * @param aWidth        width of the grid
     * @param aHeight       height of the grid
     * @param someShaded    coordinates of the shaded boxes
     * @param somePrefilled prefilled boxes
     */
    public PuzzleDefinition(final int aWidth, final int aHeight, final Set<GridPosition> someShaded,
                            final Map<GridPosition, Character> somePrefilled) {
        validate(aWidth, aHeight, someShaded, somePrefilled);

        width = aWidth;
        height = aHeight;
        shaded = new HashSet<>(someShaded);
        filled = new HashMap<>(somePrefilled);
    }

    /**
     * Validate inputs.
     *
     * @param aWidth        width of the grid
     * @param aHeight       height of the grid
     * @param someShaded    coordinates of the shaded boxes
     * @param somePrefilled prefilled boxes
     */
    private static void validate(final int aWidth, final int aHeight,
                                 final Set<GridPosition> someShaded,
                                 final Map<GridPosition, Character> somePrefilled) {
        if (aWidth <= 0 || aHeight <= 0) {
            throw new IllegalArgumentException("Invalid grid dimensions");
        }
        Objects.requireNonNull(someShaded);
        someShaded.forEach(validateCoordinates(aWidth, aHeight));

        Objects.requireNonNull(somePrefilled);
        somePrefilled.keySet().forEach(validateCoordinates(aWidth, aHeight));
        somePrefilled.values().forEach(Objects::requireNonNull);
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
     * Returns the width of the grid (i.e. the number of columns).
     *
     * @return the width of the grid (i.e. the number of columns)
     */
    public int width() {
        return width;
    }

    /**
     * Returns the height of the grid (i.e. the number of rows).
     *
     * @return the height of the grid (i.e. the number of rows)
     */
    public int height() {
        return height;
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
