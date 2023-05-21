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
import static org.junit.jupiter.api.Assertions.assertNull;
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
    void dimensions_empty() {
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

        /* Only complete rows are taken into account */
        assertEquals(2, crosswordGridViewModel.columnCount());
        assertEquals(2, crosswordGridViewModel.rowCount());
    }

    @Test
    void selectedSlot_newHorizontalSelection() {
        crosswordGridViewModel.boxesProperty()
                              .putAll(Map.of(at(0, 0), blank(), at(1, 0), blank(),
                                             at(0, 1), blank(), at(1, 1), shaded(),
                                             at(0, 2), blank(), at(1, 2), blank()));

        crosswordGridViewModel.currentBoxPositionProperty().set(at(0, 0));

        assertEquals(List.of(at(0, 0), at(1, 0)),
                     crosswordGridViewModel.currentSlotPositionsProperty());
    }

    @Test
    void selectedSlot_newHorizontalSelectionBetweenShaded() {
        crosswordGridViewModel.boxesProperty()
                              .putAll(Map.of(at(0, 0), blank(), at(1, 0), shaded(),
                                             at(2, 0), blank(), at(3, 0), blank(),
                                             at(4, 0), shaded(), at(5, 0), blank()));

        crosswordGridViewModel.currentBoxPositionProperty().set(at(2, 0));

        assertEquals(List.of(at(2, 0), at(3, 0)),
                     crosswordGridViewModel.currentSlotPositionsProperty());
    }

    @Test
    void selectedSlot_horizontalSelectionGrows() {
        selectedSlot_newHorizontalSelectionBetweenShaded();

        crosswordGridViewModel.boxesProperty().get(at(1, 0)).shadedProperty().set(false);
        crosswordGridViewModel.boxesProperty().get(at(4, 0)).shadedProperty().set(false);

        assertEquals(List.of(at(0, 0), at(1, 0), at(2, 0), at(3, 0),
                             at(4, 0), at(5, 0)),
                     crosswordGridViewModel.currentSlotPositionsProperty());
    }

    @Test
    void selectedSlot_newVerticalSelection() {
        crosswordGridViewModel.boxesProperty()
                              .putAll(Map.of(at(0, 0), blank(), at(1, 0), blank(),
                                             at(0, 1), blank(), at(1, 1), shaded(),
                                             at(0, 2), blank(), at(1, 2), blank()));

        crosswordGridViewModel.isCurrentSlotVerticalProperty().set(true);
        crosswordGridViewModel.currentBoxPositionProperty().set(at(0, 0));

        assertEquals(List.of(at(0, 0), at(0, 1), at(0, 2)),
                     crosswordGridViewModel.currentSlotPositionsProperty());
    }

    @Test
    void selectedSlot_newVerticalSelectionBetweenShaded() {
        crosswordGridViewModel.boxesProperty()
                              .putAll(Map.of(at(0, 0), blank(), at(0, 1), shaded(),
                                             at(0, 2), blank(), at(0, 3), blank(),
                                             at(0, 4), shaded(), at(0, 5), blank()));
        crosswordGridViewModel.isCurrentSlotVerticalProperty().set(true);

        crosswordGridViewModel.currentBoxPositionProperty().set(at(0, 2));

        assertEquals(List.of(at(0, 2), at(0, 3)),
                     crosswordGridViewModel.currentSlotPositionsProperty());
    }

    @Test
    void selectedSlot_verticalSelectionGrows() {
        selectedSlot_newVerticalSelectionBetweenShaded();

        crosswordGridViewModel.boxesProperty().get(at(0, 1)).shadedProperty().set(false);
        crosswordGridViewModel.boxesProperty().get(at(0, 4)).shadedProperty().set(false);

        assertEquals(List.of(at(0, 0), at(0, 1), at(0, 2), at(0, 3), at(0, 4), at(0, 5)),
                     crosswordGridViewModel.currentSlotPositionsProperty().get());
    }

    @Test
    void selectedSlot_noCurrentBox() {
        crosswordGridViewModel.boxesProperty()
                              .putAll(Map.of(at(0, 0), blank(), at(1, 0), blank(),
                                             at(0, 1), blank(), at(1, 1), shaded(),
                                             at(0, 2), blank(), at(1, 2), blank()));

        assertTrue(crosswordGridViewModel.currentSlotPositionsProperty().isEmpty());
    }

    @Test
    void selectedSlot_currentBoxDeleted_horizontal() {
        // Set last line as current slot of a 2x3 grid
        crosswordGridViewModel.boxesProperty()
                              .putAll(Map.of(at(0, 0), blank(), at(1, 0), blank(),
                                             at(0, 1), shaded(), at(1, 1), blank(),
                                             at(0, 2), blank(), at(1, 2), blank()));
        crosswordGridViewModel.currentBoxPositionProperty().set(at(0, 2));
        assertEquals(List.of(at(0, 2), at(1, 2)),
                     crosswordGridViewModel.currentSlotPositionsProperty());

        // Remove last line
        crosswordGridViewModel.boxesProperty().remove(at(1, 2));
        crosswordGridViewModel.boxesProperty().remove(at(0, 2));

        // No more current slot nor current box
        assertNull(crosswordGridViewModel.currentBoxPositionProperty().get());
        assertTrue(crosswordGridViewModel.currentSlotPositionsProperty().isEmpty());
    }

    @Test
    void selectedSlot_currentBoxDeleted_vertical() {
        // Set last column as current slot of a 2x3 grid
        crosswordGridViewModel.boxesProperty()
                              .putAll(Map.of(at(0, 0), blank(), at(1, 0), blank(),
                                             at(0, 1), shaded(), at(1, 1), blank(),
                                             at(0, 2), blank(), at(1, 2), blank()));
        crosswordGridViewModel.isCurrentSlotVerticalProperty().set(true);
        crosswordGridViewModel.currentBoxPositionProperty().set(at(1, 0));
        assertEquals(List.of(at(1, 0), at(1, 1), at(1, 2)),
                     crosswordGridViewModel.currentSlotPositionsProperty());

        // Remove last column
        crosswordGridViewModel.boxesProperty().remove(at(1, 0));
        crosswordGridViewModel.boxesProperty().remove(at(1, 1));
        crosswordGridViewModel.boxesProperty().remove(at(1, 2));

        // No more current slot nor current box
        assertNull(crosswordGridViewModel.currentBoxPositionProperty().get());
        assertTrue(crosswordGridViewModel.currentSlotPositionsProperty().isEmpty());
    }

    @Test
    void selectedSlot_currentBoxInitializedWithShaded() {
        crosswordGridViewModel.boxesProperty()
                              .putAll(Map.of(at(0, 0), blank(), at(1, 0), blank(),
                                             at(0, 1), shaded(), at(1, 1), blank(),
                                             at(0, 2), blank(), at(1, 2), blank()));

        crosswordGridViewModel.currentBoxPositionProperty().set(at(0, 1));

        assertTrue(crosswordGridViewModel.currentSlotPositionsProperty().isEmpty());
    }

    @Test
    void selectedSlot_currentBoxChangedWithShaded() {
        crosswordGridViewModel.boxesProperty()
                              .putAll(Map.of(at(0, 0), blank(), at(1, 0), blank(),
                                             at(0, 1), shaded(), at(1, 1), blank(),
                                             at(0, 2), blank(), at(1, 2), blank()));
        crosswordGridViewModel.currentBoxPositionProperty().set(at(0, 0));
        assertEquals(List.of(at(0, 0), at(1, 0)),
                     crosswordGridViewModel.currentSlotPositionsProperty());

        crosswordGridViewModel.currentBoxPositionProperty().set(at(0, 1));
        assertTrue(crosswordGridViewModel.currentSlotPositionsProperty().isEmpty());
    }

    @Test
    void selectedSlot_currentBoxMutatedFromBlankToShaded() {
        crosswordGridViewModel.boxesProperty()
                              .putAll(Map.of(at(0, 0), blank(), at(1, 0), blank(),
                                             at(0, 1), shaded(), at(1, 1), blank(),
                                             at(0, 2), blank(), at(1, 2), blank()));
        crosswordGridViewModel.currentBoxPositionProperty().set(at(0, 0));
        assertEquals(List.of(at(0, 0), at(1, 0)),
                     crosswordGridViewModel.currentSlotPositionsProperty());

        crosswordGridViewModel.boxesProperty().get(at(0, 0)).shadedProperty().set(true);

        assertTrue(crosswordGridViewModel.currentSlotPositionsProperty().isEmpty());
    }

    @Test
    void selectedSlot_currentBoxMutatedFromShadedToBlank() {
        crosswordGridViewModel.boxesProperty()
                              .putAll(Map.of(at(0, 0), blank(), at(1, 0), blank(),
                                             at(0, 1), shaded(), at(1, 1), blank(),
                                             at(0, 2), blank(), at(1, 2), blank()));
        crosswordGridViewModel.currentBoxPositionProperty().set(at(0, 1));
        assertTrue(crosswordGridViewModel.currentSlotPositionsProperty().isEmpty());

        crosswordGridViewModel.boxesProperty().get(at(0, 1)).shadedProperty().set(false);

        assertEquals(List.of(at(0, 1), at(1, 1)),
                     crosswordGridViewModel.currentSlotPositionsProperty());
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
