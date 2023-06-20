/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.puzzle.codec.xd.writer;

import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.model.XdGrid;
import org.junit.jupiter.api.Test;

import static com.gitlab.super7ramp.croiseur.puzzle.codec.xd.model.XdGrid.Index.at;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
