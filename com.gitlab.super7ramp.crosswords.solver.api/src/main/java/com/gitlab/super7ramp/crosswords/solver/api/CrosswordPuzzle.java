package com.gitlab.super7ramp.crosswords.solver.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Definition of the crossword puzzle.
 */
public class CrosswordPuzzle {

    /**
     * Width of the grid.
     */
    private final int width;

    /**
     * Height of the grid.
     */
    private final int height;

    /**
     * Shaded boxes.
     */
    private final Set<Coordinates> shaded;

    /**
     * Prefilled boxes.
     */
    private final Map<Coordinates, Character> prefilled;

    /**
     * Constructor.
     *
     * @param aWidth        width of the grid
     * @param aHeight       height of the grid
     * @param someShaded    coordinates of the shaded boxes
     * @param somePrefilled prefilled boxes
     */
    public CrosswordPuzzle(int aWidth, int aHeight, Set<Coordinates> someShaded,
                           Map<Coordinates, Character> somePrefilled) {
        validate(aWidth, aHeight, someShaded, somePrefilled);

        width = aWidth;
        height = aHeight;
        shaded = new HashSet<>(someShaded);
        prefilled = new HashMap<>(somePrefilled);
    }

    /**
     * Validate inputs.
     *
     * @param aWidth        width of the grid
     * @param aHeight       height of the grid
     * @param someShaded    coordinates of the shaded boxes
     * @param somePrefilled prefilled boxes
     */
    private static void validate(int aWidth, int aHeight, Set<Coordinates> someShaded,
                                 Map<Coordinates, Character> somePrefilled) {
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
    private static Consumer<Coordinates> validateCoordinates(final int width, final int height) {
        return coordinates -> {
            if (coordinates.x() >= width || coordinates.y() >= height) {
                throw new IllegalArgumentException("Invalid coordinates");
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
    public Set<Coordinates> shaded() {
        return shaded;
    }

    /**
     * @return the prefilled boxes
     */
    public Map<Coordinates, Character> getPrefilled() {
        return prefilled;
    }
}
