/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.puzzle.codec.xd.writer;

import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.model.XdClue;
import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.model.XdClues;

import java.util.Objects;

/**
 * Encodes {@link XdClues} to its textual representation.
 */
final class XdCluesWriter {

    /**
     * Constructs an instance.
     */
    XdCluesWriter() {
        // Nothing to do.
    }

    /**
     * Writes the given clues to a string.
     *
     * @param clues the clues to write
     * @return the written string
     */
    String write(final XdClues clues) {
        Objects.requireNonNull(clues);
        final StringBuilder sb = new StringBuilder();
        clues.acrossClues().forEach(clue -> append(sb, true, clue));
        sb.append('\n');
        clues.downClues().forEach(clue -> append(sb, false, clue));
        return sb.toString();
    }

    /**
     * Appends the textual representation of the given clue to the given string builder.
     *
     * @param sb the string builder
     * @param across whether the clue is across or down
     * @param clue the clue
     */
    private static void append(final StringBuilder sb, final boolean across, final XdClue clue) {
        sb.append(across ? 'A' : 'D')
          .append(clue.number())
          .append(". ")
          .append(clue.clue())
          .append(" ~ ")
          .append(clue.answer())
          .append('\n');
    }
}
