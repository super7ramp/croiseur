/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.sat;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link Slot}.
 */
final class SlotTest {

    @Test
    void positions_across() {
        final var slot = Slot.across(42, 0, 3, 1);

        final List<Pos> actualPositions = slot.positions();

        final List<Pos> expectedPositions = List.of(new Pos(0, 1), new Pos(1, 1), new Pos(2, 1));
        assertEquals(expectedPositions, actualPositions);
    }

    @Test
    void positions_down() {
        final var slot = Slot.down(42, 0, 3, 1);

        final List<Pos> actualPositions = slot.positions();

        final List<Pos> expectedPositions = List.of(new Pos(1, 0), new Pos(1, 1), new Pos(1, 2));
        assertEquals(expectedPositions, actualPositions);
    }
}
