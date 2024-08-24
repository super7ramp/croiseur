/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.cli.controller.solver.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Grid size.
 */
public record GridSize(int width, int height) {

    /**
     * Textual representation pattern.
     */
    private static final Pattern PATTERN = Pattern.compile("(?<width>[0-9]+)x(?<height>[0-9]+)");

    /**
     * Constructor.
     *
     * @param width  width
     * @param height height
     */
    public GridSize {
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
    public static GridSize valueOf(final String text) {
        final Matcher matcher = PATTERN.matcher(text);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid format: Expected " + PATTERN.pattern() + ", was " + text);
        }
        final int width = Integer.parseInt(matcher.group("width"));
        final int height = Integer.parseInt(matcher.group("height"));
        return new GridSize(width, height);
    }
}
