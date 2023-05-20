/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static com.gitlab.super7ramp.croiseur.common.GridPosition.at;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link CrosswordGridViewModel}.
 */
final class CrosswordGridViewModelTest {

    /** The grid view model under test. */
    private CrosswordGridViewModel crosswordGridViewModel;

    @BeforeEach
    void beforeEach() {
        crosswordGridViewModel = CrosswordGridViewModel.newGrid();
    }

    @Test
    void dimensions_Empty() {
        assertEquals(0, crosswordGridViewModel.columnCount());
        assertEquals(0, crosswordGridViewModel.rowCount());
    }

    @Test
    void dimensions_1x1() {
        crosswordGridViewModel.boxesProperty().put(at(0, 0), blank());

        assertEquals(1, crosswordGridViewModel.columnCount());
        assertEquals(1, crosswordGridViewModel.rowCount());
    }

    @Test
    void dimensions_2x3() {
        crosswordGridViewModel.boxesProperty()
                              .putAll(Map.of(at(0, 0), blank(), at(1, 0), blank(),
                                             at(0, 1), blank(), at(1, 1), blank(),
                                             at(0, 2), blank(), at(1, 2), blank()));

        assertEquals(2, crosswordGridViewModel.columnCount());
        assertEquals(3, crosswordGridViewModel.rowCount());
    }

    @Test
    void dimensions_inconsistent() {
        crosswordGridViewModel.boxesProperty()
                              .putAll(Map.of(at(0, 0), blank(), at(1, 0), blank(),
                                             at(0, 1), blank(), at(1, 1), blank(),
                                             at(0, 2), blank() /* missing (1, 2) */));

        /* Only complete columns are taken into account */
        assertEquals(2, crosswordGridViewModel.columnCount());
        assertEquals(2, crosswordGridViewModel.rowCount());
    }

    @Test
    void selectedSlot_none() {
        crosswordGridViewModel.boxesProperty()
                              .putAll(Map.of(at(0, 0), blank(), at(1, 0), blank(),
                                             at(0, 1), blank(), at(1, 1), shaded(),
                                             at(0, 2), blank(), at(1, 2), blank()));

        assertTrue(crosswordGridViewModel.currentSlotPositionsProperty().isEmpty());
    }

    @Test
    void selectedSlot_newHorizontalSelection() {
        crosswordGridViewModel.boxesProperty()
                              .putAll(Map.of(at(0, 0), blank(), at(1, 0), blank(),
                                             at(0, 1), blank(), at(1, 1), shaded(),
                                             at(0, 2), blank(), at(1, 2), blank()));

        crosswordGridViewModel.currentBoxPositionProperty().set(at(0, 0));

        assertEquals(2, crosswordGridViewModel.currentSlotPositionsProperty().size());
        assertTrue(crosswordGridViewModel.currentSlotPositionsProperty()
                                         .containsAll(List.of(at(0, 0), at(1, 0))));
    }

    @Test
    void selectedSlot_newVerticalSelection() {
        crosswordGridViewModel.boxesProperty()
                              .putAll(Map.of(at(0, 0), blank(), at(1, 0), blank(),
                                             at(0, 1), blank(), at(1, 1), shaded(),
                                             at(0, 2), blank(), at(1, 2), blank()));

        crosswordGridViewModel.isCurrentSlotVerticalProperty().set(true);
        crosswordGridViewModel.currentBoxPositionProperty().set(at(0, 0));

        assertEquals(3, crosswordGridViewModel.currentSlotPositionsProperty().size());
        assertTrue(crosswordGridViewModel.currentSlotPositionsProperty()
                                         .containsAll(List.of(at(0, 0), at(0, 1), at(0, 2))));
    }

    private static CrosswordBoxViewModel blank() {
        return new CrosswordBoxViewModel();
    }

    private static CrosswordBoxViewModel shaded() {
        final var boxViewModel = new CrosswordBoxViewModel();
        boxViewModel.shadedProperty().set(true);
        return boxViewModel;
    }
}
