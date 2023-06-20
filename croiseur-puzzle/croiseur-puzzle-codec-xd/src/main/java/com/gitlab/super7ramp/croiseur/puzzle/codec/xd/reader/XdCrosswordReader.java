/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.puzzle.codec.xd.reader;

import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.model.XdClues;
import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.model.XdCrossword;
import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.model.XdGrid;
import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.model.XdMetadata;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Parses text to {@link XdCrossword}.
 * <p>
 * Reader is <em>not</em> thread-safe.
 */
public final class XdCrosswordReader {

    /**
     * The crossword sections.
     */
    private record Sections(String metadata, String grid, String clues) {
        // Nothing to add.
    }

    /** The metadata section reader. */
    private final XdMetadataReader metadataReader;

    /** The grid section reader. */
    private final XdGridReader gridReader;

    /** The clues section reader. */
    private final XdCluesReader cluesReader;

    /**
     * Constructs an instance.
     */
    public XdCrosswordReader() {
        metadataReader = new XdMetadataReader();
        gridReader = new XdGridReader();
        cluesReader = new XdCluesReader();
    }

    /**
     * Reads the given crossword.
     *
     * @param rawCrossword the crossword to read
     * @return the read {@link XdCrossword}
     * @throws XdReadException if read fails
     */
    public XdCrossword read(final String rawCrossword) throws XdReadException {
        final Sections sections = splitSections(rawCrossword);
        final XdMetadata metadata = metadataReader.read(sections.metadata);
        final XdGrid grid = gridReader.read(sections.grid);
        final XdClues clues = cluesReader.read(sections.clues);
        return new XdCrossword(metadata, grid, clues);
    }

    /**
     * Reads the crossword at given path.
     *
     * @param crosswordPath the path of the crossword to read
     * @return the read {@link XdCrossword}
     * @throws XdReadException if read fails
     */
    public XdCrossword read(final Path crosswordPath) throws XdReadException {
        try {
            final String rawCrossword = Files.readString(crosswordPath);
            return read(rawCrossword);
        } catch (final IOException e) {
            throw new XdCrosswordReadException(e);
        }
    }

    /**
     * Splits given crossword string into {@link Sections}.
     *
     * @param rawCrossword the crossword to read
     * @return the {@link Sections} of the crossword
     * @throws XdCrosswordReadException if the actual number of sections is not 3 or 4
     */
    private static Sections splitSections(final String rawCrossword)
            throws XdCrosswordReadException {
        final String[] sections = rawCrossword.split("\n\n\n");
        if (sections.length != 3 && sections.length != 4 /* a 4th "notes" section may exist. */) {
            throw new XdCrosswordReadException("Failed to recognize crossword sections");
        }
        return new Sections(sections[0], sections[1], sections[2]);
    }
}
