/*
 * SPDX-FileCopyrightText: 2024 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.sat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link Variables}.
 */
final class VariablesTest {

    /** The variables under tests. */
    private Variables variables;

    @BeforeEach
    void setup() {
        final var grid = new Grid(new char[][]{{'.', '.', '.'}, {'.', '.', '.'}, {'.', '.', '.'}});
        variables = new Variables(grid, 100_000);
    }

    @Test
    void representingCell() {
        assertEquals(1, variables.representingCell(0,0,0));
        assertEquals(2, variables.representingCell(0,0,1));
        assertEquals(27, variables.representingCell(0,0,26));
        assertEquals(28, variables.representingCell(0,1,0));
        assertEquals(29, variables.representingCell(0,1,1));
        assertEquals(54, variables.representingCell(0,1,26));
        assertEquals(243, variables.representingCell(2,2,26));
    }

    @Test
    void representingSlot() {
        assertEquals(244, variables.representingSlot(0, 0));
        assertEquals(245, variables.representingSlot(0, 1));
        assertEquals(100_243, variables.representingSlot(0, 99_999));
        assertEquals(100_244, variables.representingSlot(1, 0));
        assertEquals(100_245, variables.representingSlot(1, 1));
        assertEquals(600_243, variables.representingSlot(5, 99_999));
    }

    @Test
    void representingCellCount() {
        assertEquals(243, variables.representingCellCount());
    }

    @Test
    void representingSlotCount() {
        assertEquals(600_000, variables.representingSlotCount());
    }

    @Test
    void variableCount() {
        assertEquals(600_243, variables.count());
    }
}
