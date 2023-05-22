/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.gitlab.super7ramp.croiseur.common.GridPosition.at;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        // First row implicitly adds a first column and vice-versa
        crosswordGridViewModel.addRow();

        assertEquals(1, crosswordGridViewModel.columnCount());
        assertEquals(1, crosswordGridViewModel.rowCount());
    }

    @Test
    void dimensions_2x3() {
        // First column implicitly adds a first row and vice-versa
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addRow();
        crosswordGridViewModel.addRow();

        assertEquals(2, crosswordGridViewModel.columnCount());
        assertEquals(3, crosswordGridViewModel.rowCount());
    }

    @Test
    void boxes_unmodifiable() {
        assertThrows(UnsupportedOperationException.class,
                     () -> crosswordGridViewModel.boxesProperty()
                                                 .put(at(0, 0), new CrosswordBoxViewModel()));
    }

    @Test
    void selectedSlot_newHorizontalSelection() {
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addRow();
        crosswordGridViewModel.addRow();

        crosswordGridViewModel.currentBoxPosition(at(0, 0));

        assertEquals(List.of(at(0, 0), at(1, 0)),
                     crosswordGridViewModel.currentSlotPositionsProperty());
    }

    @Test
    void selectedSlot_newHorizontalSelectionBetweenShaded() {
        for (int i = 0; i < 6; i++) {
            crosswordGridViewModel.addColumn();
        }
        crosswordGridViewModel.boxesProperty().get(at(1, 0)).shade();
        crosswordGridViewModel.boxesProperty().get(at(4, 0)).shade();

        crosswordGridViewModel.currentBoxPosition(at(2, 0));

        assertEquals(List.of(at(2, 0), at(3, 0)),
                     crosswordGridViewModel.currentSlotPositionsProperty());
    }

    @Test
    void selectedSlot_horizontalSelectionGrows() {
        selectedSlot_newHorizontalSelectionBetweenShaded();

        crosswordGridViewModel.boxesProperty().get(at(1, 0)).unshade();
        crosswordGridViewModel.boxesProperty().get(at(4, 0)).unshade();

        assertEquals(List.of(at(0, 0), at(1, 0), at(2, 0), at(3, 0),
                             at(4, 0), at(5, 0)),
                     crosswordGridViewModel.currentSlotPositionsProperty());
    }

    @Test
    void selectedSlot_newVerticalSelection() {
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addRow();
        crosswordGridViewModel.addRow();
        crosswordGridViewModel.currentSlotVertical();

        crosswordGridViewModel.currentBoxPosition(at(0, 0));

        assertEquals(List.of(at(0, 0), at(0, 1), at(0, 2)),
                     crosswordGridViewModel.currentSlotPositionsProperty());
    }

    @Test
    void selectedSlot_newVerticalSelectionBetweenShaded() {
        for (int i = 0; i < 6; i++) {
            crosswordGridViewModel.addRow();
        }
        crosswordGridViewModel.boxesProperty().get(at(0, 1)).shade();
        crosswordGridViewModel.boxesProperty().get(at(0, 4)).shade();
        crosswordGridViewModel.currentSlotVertical();

        crosswordGridViewModel.currentBoxPosition(at(0, 2));

        assertEquals(List.of(at(0, 2), at(0, 3)),
                     crosswordGridViewModel.currentSlotPositionsProperty());
    }

    @Test
    void selectedSlot_verticalSelectionGrows() {
        selectedSlot_newVerticalSelectionBetweenShaded();

        crosswordGridViewModel.boxesProperty().get(at(0, 1)).unshade();
        crosswordGridViewModel.boxesProperty().get(at(0, 4)).unshade();

        assertEquals(List.of(at(0, 0), at(0, 1), at(0, 2), at(0, 3), at(0, 4), at(0, 5)),
                     crosswordGridViewModel.currentSlotPositionsProperty().get());
    }

    @Test
    void selectedSlot_noCurrentBox() {
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addRow();
        crosswordGridViewModel.addRow();

        assertTrue(crosswordGridViewModel.currentSlotPositionsProperty().isEmpty());
    }

    @Test
    void selectedSlot_currentBoxDeleted_horizontal() {
        // Set last line as current slot of a 2x3 grid
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addRow();
        crosswordGridViewModel.addRow();
        crosswordGridViewModel.currentBoxPosition(at(0, 2));
        assertEquals(List.of(at(0, 2), at(1, 2)),
                     crosswordGridViewModel.currentSlotPositionsProperty());

        // Remove last line
        crosswordGridViewModel.deleteLastRow();

        // No more current slot nor current box
        assertNull(crosswordGridViewModel.currentBoxPosition());
        assertTrue(crosswordGridViewModel.currentSlotPositionsProperty().isEmpty());
    }

    @Test
    void selectedSlot_currentBoxDeleted_vertical() {
        // Set last column as current slot of a 2x3 grid
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addRow();
        crosswordGridViewModel.addRow();
        crosswordGridViewModel.currentSlotVertical();
        crosswordGridViewModel.currentBoxPosition(at(1, 0));
        assertEquals(List.of(at(1, 0), at(1, 1), at(1, 2)),
                     crosswordGridViewModel.currentSlotPositionsProperty());

        // Remove last column
        crosswordGridViewModel.deleteLastColumn();

        // No more current slot nor current box
        assertNull(crosswordGridViewModel.currentBoxPosition());
        assertTrue(crosswordGridViewModel.currentSlotPositionsProperty().isEmpty());
    }

    @Test
    void selectedSlot_currentBoxInitializedWithShaded() {
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addRow();
        crosswordGridViewModel.addRow();
        crosswordGridViewModel.boxesProperty().get(at(0, 1)).shade();

        crosswordGridViewModel.currentBoxPosition(at(0, 1));

        assertTrue(crosswordGridViewModel.currentSlotPositionsProperty().isEmpty());
    }

    @Test
    void selectedSlot_currentBoxChangedWithShaded() {
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addRow();
        crosswordGridViewModel.addRow();
        crosswordGridViewModel.boxesProperty().get(at(0, 1)).shade();
        crosswordGridViewModel.currentBoxPosition(at(0, 0));
        assertEquals(List.of(at(0, 0), at(1, 0)),
                     crosswordGridViewModel.currentSlotPositionsProperty());

        crosswordGridViewModel.currentBoxPosition(at(0, 1));

        assertTrue(crosswordGridViewModel.currentSlotPositionsProperty().isEmpty());
    }

    @Test
    void selectedSlot_currentBoxMutatedFromBlankToShaded() {
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addRow();
        crosswordGridViewModel.addRow();
        crosswordGridViewModel.currentBoxPosition(at(0, 0));
        assertEquals(List.of(at(0, 0), at(1, 0)),
                     crosswordGridViewModel.currentSlotPositionsProperty());

        crosswordGridViewModel.boxesProperty().get(at(0, 0)).shade();

        assertTrue(crosswordGridViewModel.currentSlotPositionsProperty().isEmpty());
    }

    @Test
    void selectedSlot_currentBoxMutatedFromShadedToBlank() {
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addRow();
        crosswordGridViewModel.addRow();
        crosswordGridViewModel.boxesProperty().get(at(0, 1)).shade();
        crosswordGridViewModel.currentBoxPosition(at(0, 1));
        assertTrue(crosswordGridViewModel.currentSlotPositionsProperty().isEmpty());

        crosswordGridViewModel.boxesProperty().get(at(0, 1)).unshade();

        assertEquals(List.of(at(0, 1), at(1, 1)),
                     crosswordGridViewModel.currentSlotPositionsProperty());
    }

    @Test
    void selectedSlotContent_filled() {
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.boxesProperty().get(at(0, 0)).content("A");
        crosswordGridViewModel.boxesProperty().get(at(1, 0)).content("B");
        crosswordGridViewModel.boxesProperty().get(at(2, 0)).content("C");

        crosswordGridViewModel.currentBoxPosition(at(0, 0));

        assertEquals("ABC", crosswordGridViewModel.currentSlotContent());
    }

    @Test
    void selectedSlotContent_partiallyFilled() {
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.boxesProperty().get(at(0, 0)).content("A");
        crosswordGridViewModel.boxesProperty().get(at(2, 0)).content("C");

        crosswordGridViewModel.currentBoxPosition(at(0, 0));

        assertEquals("A.C", crosswordGridViewModel.currentSlotContent());
    }

    @Test
    void selectedSlotContent_onlyBlanks() {
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();

        crosswordGridViewModel.currentBoxPositionProperty().set(at(0, 0));

        assertEquals("...", crosswordGridViewModel.currentSlotContent());
    }

    @Test
    void selectedSlotContent_noSelection() {
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();

        assertTrue(crosswordGridViewModel.currentSlotContent().isEmpty());
    }

    @Test
    void selectedSlotContent_contentChange() {
        selectedSlotContent_partiallyFilled();

        crosswordGridViewModel.boxesProperty().get(at(1, 0)).content("B");

        assertEquals("ABC", crosswordGridViewModel.currentSlotContent());
    }

    @Test
    void selectedSlotContent_contentGrows() {
        selectedSlotContent_filled();

        crosswordGridViewModel.addColumn();

        assertEquals("ABC.", crosswordGridViewModel.currentSlotContent());
    }

}
