/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.puzzle.xd.codec.reader;

import com.gitlab.super7ramp.croiseur.puzzle.xd.codec.model.XdMetadata;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link XdMetadataReader}.
 */
final class XdMetadataReaderTest {

    /** The reader under tests. */
    private final XdMetadataReader reader = new XdMetadataReader();

    @Test
    void nominalCase() throws XdReadException {
        final String rawMetadata =
                """
                Title: New York Times, Saturday, January 1, 1955
                Author: Anthony Morse
                Editor: Margaret Farrar
                Date: 1955-01-01
                """;

        final XdMetadata xdMetadata = reader.read(rawMetadata);

        assertTrue(xdMetadata.title().isPresent());
        assertEquals("New York Times, Saturday, January 1, 1955", xdMetadata.title().get());
        assertTrue(xdMetadata.author().isPresent());
        assertEquals("Anthony Morse", xdMetadata.author().get());
        assertTrue(xdMetadata.editor().isPresent());
        assertEquals("Margaret Farrar", xdMetadata.editor().get());
        assertTrue(xdMetadata.date().isPresent());
        assertEquals(LocalDate.of(1955, 1, 1), xdMetadata.date().get());
    }

    /**
     * Verify that metadata missing standard fields is still allowed.
     * <p>
     * Spec does not say whether standard fields are mandatory or not, assuming optional.
     *
     * @throws XdReadException should not happen
     */
    @Test
    void missingStandardFields() throws XdReadException {
        final String rawMetadata =
                """
                Title: New York Times, Saturday, January 1, 1955
                """;

        final XdMetadata xdMetadata = reader.read(rawMetadata);

        assertTrue(xdMetadata.title().isPresent());
        assertTrue(xdMetadata.author().isEmpty());
        assertTrue(xdMetadata.editor().isEmpty());
        assertTrue(xdMetadata.date().isEmpty());
    }

    @Test
    void extraFields() throws XdReadException {
        final String rawMetadata =
                """
                Extra: Field
                Extra Extra: Field Field
                """;

        final XdMetadata xdMetadata = reader.read(rawMetadata);

        assertEquals(Map.of("Extra", "Field",
                            "Extra Extra", "Field Field"),
                     xdMetadata.otherProperties());
        assertTrue(xdMetadata.title().isEmpty());
        assertTrue(xdMetadata.author().isEmpty());
        assertTrue(xdMetadata.editor().isEmpty());
        assertTrue(xdMetadata.date().isEmpty());
    }

    @Test
    void malformedField_missingColon() {
        final var rawMetadata =
                """
                Author Anthony Morse
                """;

        final var exception = assertThrows(XdReadException.class, () -> reader.read(rawMetadata));

        assertEquals(
                "Invalid metadata: Invalid property 'Author Anthony Morse'. Property must respect the format 'Key: Value'.",
                exception.getMessage());
    }

    /**
     * No empty line is expected.
     */
    @Test
    void malformedField_emptyLine() {
        final String rawMetadata =
                """
                Title: New York Times, Saturday, January 1, 1955
                                
                Author: Anthony Morse
                """;

        final var exception = assertThrows(XdReadException.class, () -> reader.read(rawMetadata));

        assertEquals("Invalid metadata: Blank line", exception.getMessage());
    }
}
