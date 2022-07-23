package com.gitlab.super7ramp.crosswords.spi.solver;

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
     * @return the width
     */
    public int width() {
        return width;
    }

    /**
     * @return the heigth
     */
    public int height() {
        return height;
    }

    /**
     * @return the shaded boxes
     */
    public Set<GridPosition> shaded() {
        return shaded;
    }

    /**
     * @return the filled boxes
     */
    public Map<GridPosition, Character> filled() {
        return filled;
    }
}
