/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.puzzle.codec.xd.writer;

import org.junit.jupiter.api.Test;
import re.belv.croiseur.puzzle.codec.xd.model.XdMetadata;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link XdMetadataWriter}.
 */
final class XdMetadataWriterTest {

    @Test
    void write() {
        final XdMetadata model =
                new XdMetadata.Builder().title("New York Times, Saturday, January 1, 1955")
                                        .author("Anthony Morse")
                                        .editor("Margaret Farrar")
                                        .date(LocalDate.of(1955, 1, 1))
                                        .build();
        final XdMetadataWriter writer = new XdMetadataWriter();

        final String text = writer.write(model);

        assertEquals("""
                     Title: New York Times, Saturday, January 1, 1955
                     Author: Anthony Morse
                     Editor: Margaret Farrar
                     Date: 1955-01-01
                     """, text);
    }
}
