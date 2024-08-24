/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.puzzle.repository.filesystem.plugin;

import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import re.belv.croiseur.common.puzzle.SavedPuzzle;
import re.belv.croiseur.puzzle.codec.xd.model.XdCrossword;
import re.belv.croiseur.puzzle.codec.xd.reader.XdCrosswordReader;
import re.belv.croiseur.puzzle.codec.xd.reader.XdReadException;

/** Reads puzzles from disk and converts them to domain {@link SavedPuzzle}s. */
final class PuzzleReader {

    /** The logger. */
    private static final Logger LOGGER = Logger.getLogger(PuzzleReader.class.getName());

    /** The file reader. */
    private final XdCrosswordReader reader;

    /** Constructs an instance. */
    PuzzleReader() {
        reader = new XdCrosswordReader();
    }

    /**
     * Reads the puzzle at given path.
     *
     * @param path the persisted puzzle path
     * @return the read puzzle, or {@link Optional#empty()} if no puzzle could be read at given path
     */
    Optional<SavedPuzzle> read(final Path path) {
        try {
            final XdCrossword persistedCrosswordModel = reader.read(path);
            final int id = idFrom(path);
            final SavedPuzzle domainCrosswordModel = PuzzleConverter.toDomain(id, persistedCrosswordModel);
            return Optional.of(domainCrosswordModel);
        } catch (final XdReadException | PuzzleConversionException e) {
            LOGGER.warning(() -> "Failed to read " + path + ": " + e.getMessage());
            LOGGER.log(Level.FINE, "", e);
            return Optional.empty();
        }
    }

    /**
     * Extracts the id from the filename.
     *
     * @param path the full path to file
     * @return the extracted it
     */
    private static int idFrom(final Path path) {
        final String fileName = path.getFileName().toString();
        // Parse is guaranteed to succeed given the filter made on filenames by repository.
        return Integer.parseInt(fileName.substring(0, fileName.lastIndexOf('.')));
    }
}
