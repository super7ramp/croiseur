/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.cli.controller.solver.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import re.belv.croiseur.common.puzzle.GridPosition;

/** Parser for {@link GridPosition}. */
public final class GridPositionParser {

    /** Textual representation. */
    private static final Pattern PATTERN = Pattern.compile("\\((?<x>\\d+),(?<y>\\d+)\\)");

    /** Private constructor to prevent instantiation. */
    private GridPositionParser() {
        // Nothing to do.
    }

    /**
     * Parses the textual representation of a {@link GridPosition}.
     *
     * @param value the value to parse
     * @return the parsed {@link GridPosition}
     * @throws IllegalArgumentException if given string cannot be parsed
     */
    public static GridPosition parse(final String value) {
        final Matcher matcher = PATTERN.matcher(value);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid format: Expected " + PATTERN.pattern() + ", was " + value);
        }

        final int x = Integer.parseInt(matcher.group("x"));
        final int y = Integer.parseInt(matcher.group("y"));
        return new GridPosition(x, y);
    }
}
