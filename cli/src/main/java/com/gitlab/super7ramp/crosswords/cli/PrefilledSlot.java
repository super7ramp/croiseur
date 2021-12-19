package com.gitlab.super7ramp.crosswords.cli;

import com.gitlab.super7ramp.crosswords.solver.api.Coordinate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

record PrefilledSlot(Coordinate startCoordinate, String value) {

    /** Textual representation pattern. */
    private static final Pattern PATTERN = Pattern.compile("\\(" +
            "(?<coordinate>.+)," +
            "(?<value>[a-zA-Z]+)" +
            "\\)");

    /**
     * Create a new {@link PrefilledSlot} from its textual representation.
     *
     * @param text the textual representation
     * @return the parsed {@link PrefilledSlot}
     */
    static PrefilledSlot valueOf(final String text) {
        final Matcher matcher = PATTERN.matcher(text);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid format: Expected " + PATTERN.pattern() +
                    ", was " + text);
        }
        final Coordinate parsedCoordinate = Coordinate.valueOf(matcher.group("coordinate"));
        final String parsedValue = matcher.group("value").toUpperCase();
        return new PrefilledSlot(parsedCoordinate, parsedValue);
    }
}
