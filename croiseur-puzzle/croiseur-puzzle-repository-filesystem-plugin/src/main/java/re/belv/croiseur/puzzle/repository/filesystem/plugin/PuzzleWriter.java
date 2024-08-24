/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.puzzle.repository.filesystem.plugin;

import java.io.IOException;
import java.nio.file.Path;
import re.belv.croiseur.common.puzzle.SavedPuzzle;
import re.belv.croiseur.puzzle.codec.xd.model.XdCrossword;
import re.belv.croiseur.puzzle.codec.xd.writer.XdCrosswordWriter;
import re.belv.croiseur.spi.puzzle.repository.WriteException;

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
