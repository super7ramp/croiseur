/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.puzzle.codec.xd.reader;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import re.belv.croiseur.puzzle.codec.xd.model.XdClues;

/**
 * Tests for {@link XdCluesReader}.
 */
final class XdCluesReaderTest {

    /** The reader under tests. */
    private final XdCluesReader reader = new XdCluesReader();

    @Test
    void nominal() throws XdClueReadException {
        final String rawClues =
                """
                A1. Sadness. ~ HEARTACHE
                A2. Progenitor. ~ ADAM
                A3. Mae West stand-by. ~ DIAMONDLIL

                D1. Vital throb. ~ HEARTBEAT
                D2. Having wings. ~ ALATE
                D3. Start the card game. ~ CUTANDDEAL
                """;

        final XdClues parsedClues = reader.read(rawClues);

        final XdClues expectedClues = new XdClues.Builder()
                .across(1, "Sadness.", "HEARTACHE")
                .across(2, "Progenitor.", "ADAM")
                .across(3, "Mae West stand-by.", "DIAMONDLIL")
                .down(1, "Vital throb.", "HEARTBEAT")
                .down(2, "Having wings.", "ALATE")
                .down(3, "Start the card game.", "CUTANDDEAL")
                .build();
        assertEquals(expectedClues, parsedClues);
    }

    /**
     * Allows empty clues; Useful when the puzzle is being edited.
     *
     * @throws XdClueReadException should not happen
     */
    @Test
    void emptyClues() throws XdClueReadException {
        final String rawClues =
                """
                A1. Sadness. ~ HEARTACHE
                A2.  ~ ADAM
                A3. Mae West stand-by. ~ DIAMONDLIL

                D1. Vital throb. ~ HEARTBEAT
                D2. Having wings. ~ ALATE
                D3.  ~ CUTANDDEAL
                """;

        final XdClues parsedClues = reader.read(rawClues);

        final XdClues expectedClues = new XdClues.Builder()
                .across(1, "Sadness.", "HEARTACHE")
                .across(2, "", "ADAM")
                .across(3, "Mae West stand-by.", "DIAMONDLIL")
                .down(1, "Vital throb.", "HEARTBEAT")
                .down(2, "Having wings.", "ALATE")
                .down(3, "", "CUTANDDEAL")
                .build();
        assertEquals(expectedClues, parsedClues);
    }

    /**
     * Verifies that reader accepts crosswords with missing clue numbers.
     * <p>
     * Format does not specify that all clue numbers shall be present.
     *
     * @throws XdClueReadException should not happen
     */
    @Test
    void missingClue() throws XdClueReadException {
        final String rawClues =
                """
                A1. Sadness. ~ HEARTACHE
                A4. Progenitor. ~ ADAM
                A5. Mae West stand-by. ~ DIAMONDLIL

                D1. Vital throb. ~ HEARTBEAT
                D2. Having wings. ~ ALATE
                D3. Start the card game. ~ CUTANDDEAL
                """;

        final XdClues parsedClues = reader.read(rawClues);

        final XdClues expectedClues = new XdClues.Builder()
                .across(1, "Sadness.", "HEARTACHE")
                .across(4, "Progenitor.", "ADAM")
                .across(5, "Mae West stand-by.", "DIAMONDLIL")
                .down(1, "Vital throb.", "HEARTBEAT")
                .down(2, "Having wings.", "ALATE")
                .down(3, "Start the card game.", "CUTANDDEAL")
                .build();
        assertEquals(expectedClues, parsedClues);
    }

    /**
     * Verifies that reader accepts crosswords with unsorted clue numbers.
     * <p>
     * Spec says "The clues should be sorted" (it's not a "shall").
     */
    @Test
    void notSorted() throws XdClueReadException {
        final String rawClues =
                """
                A3. Sadness. ~ HEARTACHE
                A1. Progenitor. ~ ADAM
                A2. Mae West stand-by. ~ DIAMONDLIL

                D2. Vital throb. ~ HEARTBEAT
                D3. Having wings. ~ ALATE
                D1. Start the card game. ~ CUTANDDEAL
                """;

        final XdClues parsedClues = reader.read(rawClues);

        final XdClues expectedClues = new XdClues.Builder()
                .across(3, "Sadness.", "HEARTACHE")
                .across(1, "Progenitor.", "ADAM")
                .across(2, "Mae West stand-by.", "DIAMONDLIL")
                .down(2, "Vital throb.", "HEARTBEAT")
                .down(3, "Having wings.", "ALATE")
                .down(1, "Start the card game.", "CUTANDDEAL")
                .build();
        assertEquals(expectedClues, parsedClues);
    }

    @Disabled("multi-line clues not supported yet")
    @Test
    void multiline() throws XdClueReadException {
        final String rawClues =
                """
                A1. Sadness. \\
                 Follow-up. ~ HEARTACHE
                A2. Progenitor. ~ ADAM
                A3. Mae West stand-by. ~ DIAMONDLIL

                D1. Vital throb. ~ HEARTBEAT
                D2. Having wings. ~ ALATE
                D3. Start the card game. ~ CUTANDDEAL
                """;

        final XdClues parsedClues = reader.read(rawClues);

        final XdClues expectedClues = new XdClues.Builder()
                .across(1, "Sadness.", "HEARTACHE")
                .across(2, "Progenitor.", "ADAM")
                .across(3, "Mae West stand-by.", "DIAMONDLIL")
                .down(1, "Vital throb.", "HEARTBEAT")
                .down(2, "Having wings.", "ALATE")
                .down(3, "Start the card game.", "CUTANDDEAL")
                .build();
        assertEquals(expectedClues, parsedClues);
    }
}
