/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.puzzle.codec.xd.plugin;

import org.junit.jupiter.api.Test;
import re.belv.croiseur.common.puzzle.Puzzle;
import re.belv.croiseur.common.puzzle.PuzzleClues;
import re.belv.croiseur.common.puzzle.PuzzleDetails;
import re.belv.croiseur.common.puzzle.PuzzleGrid;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static re.belv.croiseur.common.puzzle.GridPosition.at;

/**
 * Tests for {@link XdEncoder}.
 */
final class XdEncoderTest {

    @Test
    void encode() {
        final var details = new PuzzleDetails("Example Grid", "Me", "Croiseur", "", Optional.of(
                LocalDate.of(2023, 6, 19)));
        final var grid = new PuzzleGrid.Builder().height(3).width(3)
                                                 .shade(at(1, 0)).fill(at(2, 0), 'C')
                                                 .fill(at(0, 1), 'D').fill(at(1, 1), 'E')
                                                 .fill(at(2, 1), 'F')
                                                 .fill(at(0, 2), 'G').fill(at(1, 2), 'H')
                                                 .fill(at(2, 2), 'I')
                                                 .build();
        final var clues = new PuzzleClues(List.of("Middle.", "End."),
                                          List.of("Some Very.", "Dummy.", "Clues."));
        final var puzzle = new Puzzle(details, grid, clues);
        final var out = new ByteArrayOutputStream();

        new XdEncoder().encode(puzzle, out);

        assertEquals("""
                     Title: Example Grid
                     Author: Me
                     Editor: Croiseur
                     Date: 2023-06-19


                     .#C
                     DEF
                     GHI


                     A1. Middle. ~ DEF
                     A2. End. ~ GHI

                     D1. Some Very. ~ .DG
                     D2. Dummy. ~ EH
                     D3. Clues. ~ CFI
                     """, out.toString());
    }
}
