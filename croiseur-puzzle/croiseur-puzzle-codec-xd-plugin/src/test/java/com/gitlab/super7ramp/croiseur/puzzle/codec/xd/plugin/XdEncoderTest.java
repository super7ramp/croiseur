/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.puzzle.codec.xd.plugin;

import com.gitlab.super7ramp.croiseur.common.puzzle.Puzzle;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleDetails;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleGrid;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.Optional;

import static com.gitlab.super7ramp.croiseur.common.puzzle.GridPosition.at;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        final var puzzle = new Puzzle(details, grid);
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



                     """, out.toString());
    }
}