/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg.grid;

import com.gitlab.super7ramp.croiseur.common.puzzle.GridPosition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests on {@link GridDataBuilder}.
 */
final class GridDataTest {

    @Test
    void squareGrid() {
        final GridData grid = new GridDataBuilder().withHeight(8).withWidth(8).build();

        assertEquals(16, grid.slots().size());
    }

    @Test
    void squareGridWithShaded() {
        final GridData grid = new GridDataBuilder().withHeight(8)
                                                   .withWidth(8)
                                                   .withShaded(new GridPosition(3, 3))
                                                   .build();

        assertEquals(18, grid.slots().size());
    }

    @Test
    void wideGrid() {
        final GridData grid = new GridDataBuilder().withHeight(4).withWidth(8).build();

        assertEquals(12, grid.slots().size());
    }

    @Test
    void wideGridWithShadedAndOneSingleLetterSlot() {
        final GridData grid = new GridDataBuilder().withHeight(4)
                                                   .withWidth(8)
                                                   .withShaded(new GridPosition(2, 2))
                                                   .build();

        assertEquals(13, grid.slots().size());
    }

    @Test
    void wideGridWithShadedAndTwoSingleLetterSlot() {
        final GridData grid = new GridDataBuilder().withHeight(4)
                                                   .withWidth(8)
                                                   .withShaded(new GridPosition(1, 2))
                                                   .build();

        assertEquals(12, grid.slots().size());
    }

    @Test
    void narrowGrid() {
        final GridData grid = new GridDataBuilder().withHeight(8).withWidth(4).build();

        assertEquals(12, grid.slots().size());
    }
}
