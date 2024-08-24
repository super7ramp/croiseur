/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.view.model.slot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static re.belv.croiseur.gui.view.model.GridCoord.at;

import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Tests on {@link AcrossSlotOutline}s.
 */
final class AcrossSlotOutlineTest {

    @Test
    void gridPositions() {
        final var slot = new AcrossSlotOutline(0, 4, 2);
        assertEquals(List.of(at(0, 2), at(1, 2), at(2, 2), at(3, 2)), slot.boxPositions());
    }

    @Test
    void contains() {
        final var slot = new AcrossSlotOutline(2, 4, 2);
        assertTrue(slot.contains(at(2, 2)));
        assertTrue(slot.contains(at(3, 2)));
        assertFalse(slot.contains(at(1, 2)));
        assertFalse(slot.contains(at(4, 2)));
        assertFalse(slot.contains(at(3, 3)));
    }
}
