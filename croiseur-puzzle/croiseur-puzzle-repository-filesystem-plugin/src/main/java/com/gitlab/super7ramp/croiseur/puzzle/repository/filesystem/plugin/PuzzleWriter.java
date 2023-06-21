/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.puzzle.repository.filesystem.plugin;

import com.gitlab.super7ramp.croiseur.common.puzzle.SavedPuzzle;
import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.model.XdCrossword;
import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.writer.XdCrosswordWriter;
import com.gitlab.super7ramp.croiseur.spi.puzzle.repository.WriteException;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Writes {@link SavedPuzzle}s to disk.
 */
final class PuzzleWriter {

    /** The file writer. */
    private final XdCrosswordWriter writer;

    /**
     * Constructs an instance.
     */
    PuzzleWriter() {
        writer = new XdCrosswordWriter();
    }

    /**
     * Writes the puzzle to file at given path.
     *
     * @param puzzle the puzzle to write
     * @param path where to write
     * @throws WriteException if an error occurs
     */
    void write(final SavedPuzzle puzzle, final Path path) throws WriteException {
        final XdCrossword persistenceCrosswordModel = PuzzleConverter.toPersistence(puzzle);
        try {
            writer.write(persistenceCrosswordModel, path);
        } catch (final IOException e) {
            throw new WriteException("Failed to write " + puzzle, e);
        }
    }
}
