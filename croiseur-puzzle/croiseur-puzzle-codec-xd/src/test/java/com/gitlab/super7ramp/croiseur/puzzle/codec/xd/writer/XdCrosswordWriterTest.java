/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.puzzle.codec.xd.writer;

import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.model.XdClues;
import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.model.XdCrossword;
import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.model.XdGrid;
import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.model.XdMetadata;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.gitlab.super7ramp.croiseur.puzzle.codec.xd.model.XdGrid.Index.at;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link XdCrosswordWriter}.
 */
final class XdCrosswordWriterTest {

    @Test
    void write() {
        final XdCrosswordWriter writer = new XdCrosswordWriter();
        final XdMetadata metadata = new XdMetadata.Builder().title("Example Grid")
                                                            .author("Me")
                                                            .editor("Croiseur")
                                                            .date(LocalDate.of(2023, 6, 20))
                                                            .build();
        final XdGrid grid = new XdGrid.Builder().filled(at(0, 0), 'A')
                                                .filled(at(1, 0), 'B')
                                                .filled(at(2, 0), 'C')
                                                .filled(at(0, 1), 'D')
                                                .filled(at(1, 1), 'E')
                                                .filled(at(2, 1), 'F')
                                                .filled(at(0, 2), 'G')
                                                .filled(at(1, 2), 'H')
                                                .filled(at(2, 2), 'I')
                                                .build();
        final XdClues clues =
                new XdClues.Builder().across(1, "Start.", "ABC")
                                     .across(2, "Middle.", "DEF")
                                     .across(3, "End.", "GHI")
                                     .down(1, "Some Very.", "ADG")
                                     .down(2, "Dummy.", "BEH")
                                     .down(3, "Clues.", "CFI")
                                     .build();
        final XdCrossword model = new XdCrossword(metadata, grid, clues);

        final String text = writer.write(model);

        assertEquals("""
                     Title: Example Grid
                     Author: Me
                     Editor: Croiseur
                     Date: 2023-06-20
                                     
                                     
                     ABC
                     DEF
                     GHI
                                     
                                     
                     A1. Start. ~ ABC
                     A2. Middle. ~ DEF
                     A3. End. ~ GHI
                                                    
                     D1. Some Very. ~ ADG
                     D2. Dummy. ~ BEH
                     D3. Clues. ~ CFI
                     """, text);
    }
}
