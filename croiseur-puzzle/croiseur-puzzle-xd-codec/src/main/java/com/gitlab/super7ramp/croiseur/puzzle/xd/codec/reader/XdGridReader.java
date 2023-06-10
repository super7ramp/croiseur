/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.puzzle.xd.codec.reader;

import com.gitlab.super7ramp.croiseur.puzzle.xd.codec.model.XdGrid;

import java.util.Objects;

import static com.gitlab.super7ramp.croiseur.puzzle.xd.codec.model.XdGrid.Index.at;

/**
 * Parses text to {@link XdGrid}.
 */
public final class XdGridReader {

    /**
     * Constructs an instance.
     */
    public XdGridReader() {
        // nothing to do.
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
        final var builder = new XdGrid.Builder();
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
