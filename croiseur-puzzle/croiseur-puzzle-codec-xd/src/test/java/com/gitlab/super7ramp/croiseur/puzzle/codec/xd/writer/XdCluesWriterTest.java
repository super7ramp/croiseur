/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.puzzle.codec.xd.writer;

import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.model.XdClues;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link XdCluesWriter}.
 */
final class XdCluesWriterTest {

    @Test
    void write() {
        final XdClues model =
                new XdClues.Builder().across(1, "Sadness.", "HEARTACHE")
                                     .across(2, "Progenitor.", "ADAM")
                                     .across(3, "Mae West stand-by.", "DIAMONDLIL")
                                     .down(1, "Vital throb.", "HEARTBEAT")
                                     .down(2, "Having wings.", "ALATE")
                                     .down(3, "Start the card game.", "CUTANDDEAL")
                                     .build();
        final XdCluesWriter writer = new XdCluesWriter();

        final String text = writer.write(model);

        assertEquals("""
                     A1. Sadness. ~ HEARTACHE
                     A2. Progenitor. ~ ADAM
                     A3. Mae West stand-by. ~ DIAMONDLIL
                                     
                     D1. Vital throb. ~ HEARTBEAT
                     D2. Having wings. ~ ALATE
                     D3. Start the card game. ~ CUTANDDEAL
                     """, text);
    }
}