/*
 * SPDX-FileCopyrightText: 2025 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.puzzle.codec.xd.plugin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static re.belv.croiseur.common.puzzle.GridPosition.at;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import re.belv.croiseur.common.puzzle.Puzzle;
import re.belv.croiseur.common.puzzle.PuzzleClues;
import re.belv.croiseur.common.puzzle.PuzzleDetails;
import re.belv.croiseur.common.puzzle.PuzzleGrid;
import re.belv.croiseur.spi.puzzle.codec.PuzzleDecodingException;

/** Tests for {@link XdDecoder}. */
final class XdDecoderTest {

    /** The decoder under tests. */
    private final XdDecoder decoder = new XdDecoder();

    @Test
    void decode() throws PuzzleDecodingException {
        final String crossword = """
                                 Title: Example Grid
                                 Author: Me
                                 Editor: Croiseur
                                 Date: 2023-06-19


                                 .#C
                                 DEF
                                 GHI


                                 A1. Start. ~ ABC
                                 A2. Middle. ~ DEF
                                 A3. End. ~ GHI

                                 D1. Some Very. ~ ADG
                                 D2. Dummy. ~ BEH
                                 D3. Clues. ~ CFI
                                 """;
        final InputStream is = new ByteArrayInputStream(crossword.getBytes());

        final Puzzle puzzle = decoder.decode(is);

        final var expectedDetails =
                new PuzzleDetails("Example Grid", "Me", "Croiseur", "", Optional.of(LocalDate.of(2023, 6, 19)));
        assertEquals(expectedDetails, puzzle.details());
        final var expectedGrid = new PuzzleGrid.Builder()
                .height(3)
                .width(3)
                .shade(at(1, 0))
                .fill(at(2, 0), 'C')
                .fill(at(0, 1), 'D')
                .fill(at(1, 1), 'E')
                .fill(at(2, 1), 'F')
                .fill(at(0, 2), 'G')
                .fill(at(1, 2), 'H')
                .fill(at(2, 2), 'I')
                .build();
        assertEquals(expectedGrid, puzzle.grid());

        final var expectedClues =
                new PuzzleClues(List.of("Start.", "Middle.", "End."), List.of("Some Very.", "Dummy.", "Clues."));
        assertEquals(expectedClues, puzzle.clues());
    }

    @Test
    void decode_unsupportedSpaces() {
        final String crossword = """
                                 Title: Example Grid
                                 Author: Me
                                 Editor: Croiseur
                                 Date: 2023-06-19


                                 .#_
                                 DEF
                                 GHI


                                 A1. Start. ~ ABC
                                 A2. Middle. ~ DEF
                                 A3. End. ~ GHI

                                 D1. Some Very. ~ ADG
                                 D2. Dummy. ~ BEH
                                 D3. Clues. ~ CFI
                                 """;
        final var is = new ByteArrayInputStream(crossword.getBytes());

        final var exception = assertThrows(PuzzleDecodingException.class, () -> decoder.decode(is));
        assertEquals("Cannot convert grid with spaces: This is not supported by Croiseur.", exception.getMessage());
    }
}
