package com.gitlab.super7ramp.crosswords.solver.api;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Coordinates on the grid.
 */
public record Coordinate(int x, int y) {

    /** Textual representation. */
    private static final Pattern PATTERN = Pattern.compile("\\((?<x>[0-9]+),(?<y>[0-9]+)\\)");

    /**
     * Constructor.
     *
     * @param x horizontal index, starting at 0
     * @param y vertical index, starting at 0
     */
    public Coordinate {
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException("Invalid coordinates");
        }
    }

    public static Coordinate valueOf(final String value) {
        final Matcher matcher = PATTERN.matcher(value);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid format: Expected " + PATTERN.pattern() +
                    ", was " + value);
        }

        final int x = Integer.parseInt(matcher.group("x"));
        final int y = Integer.parseInt(matcher.group("y"));
        return new Coordinate(x, y);
    }

    /**
     * Returns a new {@link Coordinate} at specified horizontal offset of this coordinate.
     *
     * @param offset the horizontal offset
     * @return a new {@link Coordinate} at specified horizontal offset of this coordinate.
     */
    public Coordinate atHorizontalOffset(final int offset) {
        return new Coordinate(x + offset, y);
    }

    /**
     * Returns a new {@link Coordinate} at specified vertical offset of this coordinate.
     *
     * @param offset the vertical offset
     * @return a new {@link Coordinate} at specified vertical offset of this coordinate.
     */
    public Coordinate atVerticalOffset(final int offset) {
        return new Coordinate(x, y + offset);
    }

    @Override
    public String toString() {
        return "Coordinate{" + "x=" + x + ", y=" + y + '}';
    }
}
