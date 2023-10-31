/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.puzzle.codec.xd.writer;

import org.junit.jupiter.api.Test;
import re.belv.croiseur.puzzle.codec.xd.model.XdGrid;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static re.belv.croiseur.puzzle.codec.xd.model.XdGrid.Index.at;

/**
 * Tests for {@link XdGridWriter}.
 */
final class XdGridWriterTest {

    @Test
    void write() {
        final XdGrid model =
                new XdGrid.Builder().filled(at(0, 0), 'A').nonFilled(at(1, 0))
                                    .space(at(0, 1)).block(at(1, 1))
                                    .filled(at(0, 2), 'B').nonFilled(at(1, 2))
                                    .build();
        final XdGridWriter writer = new XdGridWriter();

        final String text = writer.write(model);
        assertEquals("""
                     A.
                     _#
                     B.
                     """, text);
    }
}
