/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.puzzle.xd.codec.reader;

import com.gitlab.super7ramp.croiseur.puzzle.xd.codec.model.XdClues;
import com.gitlab.super7ramp.croiseur.puzzle.xd.codec.model.XdCrossword;
import com.gitlab.super7ramp.croiseur.puzzle.xd.codec.model.XdGrid;
import com.gitlab.super7ramp.croiseur.puzzle.xd.codec.model.XdMetadata;

/**
 * Parses text to {@link XdCrossword}.
 */
final class XdCrosswordReader {

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
    XdCrossword read(final String rawCrossword) throws XdReadException {
        final Sections sections = splitSections(rawCrossword);
        final XdMetadata metadata = metadataReader.read(sections.metadata);
        final XdGrid grid = gridReader.read(sections.grid);
        final XdClues clues = cluesReader.read(sections.clues);
        return new XdCrossword(metadata, grid, clues);
    }

    private static Sections splitSections(final String rawCrossword)
            throws XdCrosswordReadException {
        final String[] sections = rawCrossword.split("\n\n\n");
        if (sections.length != 3 && sections.length != 4 /* a 4th "notes" section may exist. */) {
            throw new XdCrosswordReadException("Failed to recognize crossword sections");
        }
        return new Sections(sections[0], sections[1], sections[2]);
    }
}
