/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.cli.controller.puzzle.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A clue.
 *
 * @param number  the clue number; It corresponds to its position in the clue list, which depends on
 *                the orientation (i.e. across and down clues are numbered independently)
 * @param content the clue content
 */
public record Clue(int number, String content) {

    /**
     * Textual representation pattern.
     */
    private static final Pattern PATTERN = Pattern.compile("(?<number>[1-9]+[0-9]*),(?<content>.*)");

    /**
     * Validates fields.
     *
     * @param number  the clue number
     * @param content the clue content
     */
    public Clue {
        if (number < 1) {
            throw new IllegalArgumentException("Clue number must be strictly superior to 0");
        }
        if (content == null) {
            throw new IllegalArgumentException(
                    "Clue content must be non-null. Use an empty string if clue content is empty.");
        }
    }

    /**
     * Creates a new {@link Clue} from its textual representation.
     *
     * @param text the textual representation
     * @return the value
     */
    public static Clue valueOf(final String text) {
        final Matcher matcher = PATTERN.matcher(text);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid format: Expected " + PATTERN.pattern() +
                                               ", was " + text);
        }
        final int number = Integer.parseInt(matcher.group("number"));
        final String content = matcher.group("content");
        return new Clue(number, content);
    }
}
