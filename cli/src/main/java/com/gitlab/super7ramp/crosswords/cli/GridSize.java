package com.gitlab.super7ramp.crosswords.cli;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Grid size.
 */
record GridSize(int width, int height) {

    /**
     * Textual representation pattern.
     */
    private static final Pattern PATTERN = Pattern.compile("(?<width>[0-9]+)x(?<height>[0-9]+)");

    GridSize {
        if (width <= 0) {
            throw new IllegalArgumentException("Invalid zero or negative width");
        }

        if (height <= 0) {
            throw new IllegalArgumentException("Invalid zero or negative height");
        }
    }

    /**
     * Create a new {@link GridSize} from its textual representation.
     *
     * @param text the textual representation
     * @return the value
     */
    static GridSize valueOf(final String text) {
        final Matcher matcher = PATTERN.matcher(text);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid format: Expected " + PATTERN.pattern() + ", was " + text);
        }
        final int width = Integer.parseInt(matcher.group("width"));
        final int height = Integer.parseInt(matcher.group("height"));
        return new GridSize(width, height);
    }
}
