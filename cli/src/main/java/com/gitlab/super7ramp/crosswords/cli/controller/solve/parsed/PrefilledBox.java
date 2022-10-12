package com.gitlab.super7ramp.crosswords.cli.controller.solve.parsed;

import com.gitlab.super7ramp.crosswords.spi.solver.GridPosition;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record PrefilledBox(GridPosition gridPosition, char value) {

    /** Textual representation pattern. */
    private static final Pattern PATTERN = Pattern.compile("\\(" +
            "(?<coordinate>.+)," +
            "(?<value>[a-zA-Z])" +
            "\\)");

    /**
     * Create a new {@link PrefilledBox} from its textual representation.
     *
     * @param text the textual representation
     * @return the parsed {@link PrefilledBox}
     */
    public static PrefilledBox valueOf(final String text) {
        final Matcher matcher = PATTERN.matcher(text);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid format: Expected " + PATTERN.pattern() +
                    ", was " + text);
        }
        final GridPosition parsedGridPosition = GridPosition.valueOf(matcher.group("coordinate"));
        final char parsedValue = Character.toUpperCase(matcher.group("value").charAt(0));
        return new PrefilledBox(parsedGridPosition, parsedValue);
    }
}
