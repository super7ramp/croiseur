/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.puzzle.codec.xd.reader;

import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.model.XdGrid;

import java.util.Objects;

import static com.gitlab.super7ramp.croiseur.puzzle.codec.xd.model.XdGrid.Index.at;

/**
 * Parses text to {@link XdGrid}.
 */
public final class XdGridReader {

    /** The grid model builder. */
    private final XdGrid.Builder builder;

    /**
     * Constructs an instance.
     */
    public XdGridReader() {
        builder = new XdGrid.Builder();
    }

    /**
     * Reads the given grid.
     *
     * @param rawGrid the grid to read
     * @return the parsed grid
     * @throws NullPointerException if given string is {@code null}
     */
    public XdGrid read(final String rawGrid) {
        Objects.requireNonNull(rawGrid);
        builder.reset();
        final String[] lines = rawGrid.split("\\R");
        for (int lineIndex = 0; lineIndex < lines.length; lineIndex++) {
            final String line = lines[lineIndex];
            for (int columnIndex = 0; columnIndex < line.length(); columnIndex++) {
                final char character = line.charAt(columnIndex);
                final XdGrid.Index position = at(columnIndex, lineIndex);
                switch (character) {
                    case '#' -> builder.block(position);
                    case '.' -> builder.nonFilled(position);
                    case '_' -> builder.space(position);
                    default -> builder.filled(position, character);
                }
            }
        }
        return builder.build();
    }
}
