/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.cli.controller.solver.parser;

import com.gitlab.super7ramp.crosswords.common.GridPosition;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A parsed pre-filled box information.
 *
 * @param gridPosition the position of the box
 * @param value        the value inside the box
 */
public record PrefilledBox(GridPosition gridPosition, char value) {

    /** Textual representation pattern. */
    private static final Pattern PATTERN = Pattern.compile("\\(" +
            "(?<coordinate>.+)," +
            "(?<value>[a-zA-Z])" +
            "\\)");

    /**
     * Creates a new {@link PrefilledBox} from its textual representation.
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
        final GridPosition parsedGridPosition = GridPositionParser.parse(matcher.group(
                "coordinate"));
        final char parsedValue = Character.toUpperCase(matcher.group("value").charAt(0));
        return new PrefilledBox(parsedGridPosition, parsedValue);
    }
}
