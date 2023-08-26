/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model.puzzle.edition.slot;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.gitlab.super7ramp.croiseur.gui.view.model.puzzle.edition.GridCoord.at;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests on {@link DownSlotOutlineTest}.
 */
final class DownSlotOutlineTest {

    @Test
    void gridPositions() {
        final var slot = new DownSlotOutline(0, 4, 2);
        assertEquals(List.of(at(2, 0), at(2, 1), at(2, 2), at(2, 3)), slot.boxPositions());
    }

    @Test
    void contains() {
        final var slot = new DownSlotOutline(2, 4, 2);
        assertTrue(slot.contains(at(2, 2)));
        assertTrue(slot.contains(at(2, 3)));
        assertFalse(slot.contains(at(2, 1)));
        assertFalse(slot.contains(at(2, 4)));
        assertFalse(slot.contains(at(3, 3)));
    }
}
