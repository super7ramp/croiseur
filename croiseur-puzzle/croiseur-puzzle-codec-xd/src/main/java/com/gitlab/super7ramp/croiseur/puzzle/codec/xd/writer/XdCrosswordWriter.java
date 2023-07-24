/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.puzzle.codec.xd.writer;

import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.model.XdCrossword;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Encodes {@link XdCrossword} to its textual representation.
 */
public final class XdCrosswordWriter {

    /** The section delimiter. */
    private static final String SECTION_DELIMITER = "\n\n";

    /** The metadata writer. */
    private final XdMetadataWriter metadataWriter;

    /** The grid writer. */
    private final XdGridWriter gridWriter;

    /** The clues writer. */
    private final XdCluesWriter cluesWriter;

    /**
     * Constructs an instance.
     */
    public XdCrosswordWriter() {
        metadataWriter = new XdMetadataWriter();
        gridWriter = new XdGridWriter();
        cluesWriter = new XdCluesWriter();
    }

    /**
     * Writes the given crossword to a string.
     *
     * @param crossword the crossword to write
     * @return the written string
     * @throws NullPointerException if given crossword or any of its fields is {@code null}
     */
    public String write(final XdCrossword crossword) {
        Objects.requireNonNull(crossword);
        return metadataWriter.write(crossword.metadata()) + SECTION_DELIMITER +
               gridWriter.write(crossword.grid()) + SECTION_DELIMITER +
               cluesWriter.write(crossword.clues());
    }

    /**
     * Writes the given crossword to a file at given path.
     *
     * @param crossword the crossword to write
     * @param path      the path of the file to write
     * @throws NullPointerException if given crossword or any of its fields is {@code null}
     * @throws IOException          if an I/O error occurs when writing the text to file
     */
    public void write(final XdCrossword crossword, final Path path) throws IOException {
        Objects.requireNonNull(path);
        try (final var outputStream = Files.newOutputStream(path)) {
            write(crossword, outputStream);
        }
    }

    /**
     * Writes the given crossword to a given output stream.
     *
     * @param crossword    the crossword to write
     * @param outputStream where to write the crossword
     * @throws NullPointerException if any of the arguments is {@code null}
     */
    public void write(final XdCrossword crossword, final OutputStream outputStream) {
        Objects.requireNonNull(outputStream);
        final String text = write(crossword);
        try (final var writer = new PrintWriter(outputStream, false, StandardCharsets.UTF_8)) {
            writer.write(text);
            writer.flush();
        }
    }
}
