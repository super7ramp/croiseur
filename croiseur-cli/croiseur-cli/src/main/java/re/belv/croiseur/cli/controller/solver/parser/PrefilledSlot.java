/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.cli.controller.solver.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import re.belv.croiseur.common.puzzle.GridPosition;

/**
 * A parsed pre-filled slot information,
 *
 * @param startGridPosition the slot's start position
 * @param value the word in the slot
 */
public record PrefilledSlot(GridPosition startGridPosition, String value) {

    /** Textual representation pattern. */
    private static final Pattern PATTERN =
            Pattern.compile("\\(" + "(?<coordinate>.+)," + "(?<value>[a-zA-Z]+)" + "\\)");

    /**
     * Create a new {@link PrefilledSlot} from its textual representation.
     *
     * @param text the textual representation
     * @return the parsed {@link PrefilledSlot}
     */
    public static PrefilledSlot valueOf(final String text) {
        final Matcher matcher = PATTERN.matcher(text);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid format: Expected " + PATTERN.pattern() + ", was " + text);
        }
        final GridPosition parsedGridPosition = GridPositionParser.parse(matcher.group("coordinate"));
        final String parsedValue = matcher.group("value").toUpperCase();
        return new PrefilledSlot(parsedGridPosition, parsedValue);
    }
}
