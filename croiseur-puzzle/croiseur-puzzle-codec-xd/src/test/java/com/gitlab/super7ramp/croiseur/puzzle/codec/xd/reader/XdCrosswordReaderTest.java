/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.puzzle.codec.xd.reader;

import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.model.XdClues;
import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.model.XdCrossword;
import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.model.XdGrid;
import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.model.XdMetadata;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static java.util.Collections.emptySet;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link XdCrosswordReader}.
 */
final class XdCrosswordReaderTest {

    /** The reader under tests. */
    private final XdCrosswordReader reader = new XdCrosswordReader();

    @Test
    void nominal() throws XdReadException {
        final String crossword =
                """
                Title: Example Grid
                Author: Me
                Editor: Croiseur
                Date: 2023-06-19
                                
                                
                ABC
                DEF
                GHI
                                
                                
                A1. Start. ~ ABC
                A2. Middle. ~ DEF
                A3. End. ~ GHI
                                               
                D1. Some Very. ~ ADG
                D2. Dummy. ~ BEH
                D3. Clues. ~ CFI
                """;

        final XdCrossword xdCrossword = reader.read(crossword);

        final XdMetadata xdMetadata = xdCrossword.metadata();
        assertEquals("Example Grid", xdMetadata.title().orElse(""));
        assertEquals("Me", xdMetadata.author().orElse(""));
        assertEquals("Croiseur", xdMetadata.editor().orElse(""));

        final XdGrid xdGrid = xdCrossword.grid();
        assertEquals(Map.of(XdGrid.Index.at(0, 0), "A", XdGrid.Index.at(1, 0), "B", XdGrid.Index.at(2, 0), "C",
                            XdGrid.Index.at(0, 1), "D", XdGrid.Index.at(1, 1), "E", XdGrid.Index.at(2, 1), "F",
                            XdGrid.Index.at(0, 2), "G", XdGrid.Index.at(1, 2), "H", XdGrid.Index.at(2, 2), "I"),
                     xdGrid.filled());
        assertEquals(emptySet(), xdGrid.blocks());
        assertEquals(emptySet(), xdGrid.nonFilled());
        assertEquals(emptySet(), xdGrid.spaces());

        final XdClues xdClues = xdCrossword.clues();
        final XdClues expectedClues =
                new XdClues.Builder().across(1, "Start.", "ABC")
                                     .across(2, "Middle.", "DEF")
                                     .across(3, "End.", "GHI")
                                     .down(1, "Some Very.", "ADG")
                                     .down(2, "Dummy.", "BEH")
                                     .down(3, "Clues.", "CFI")
                                     .build();
        assertEquals(expectedClues, xdClues);
    }
}