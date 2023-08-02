/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model.slot;

import com.gitlab.super7ramp.croiseur.gui.view.model.CrosswordGridViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.testutil.ChangeEventCounter;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static com.gitlab.super7ramp.croiseur.gui.view.model.GridCoord.at;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link SlotsViewModel}.
 */
final class SlotsViewModelTest {

    @Test
    void constructor_emptyGrid() {
        final var grid = CrosswordGridViewModel.newGrid();

        final var slots = new SlotsViewModel(grid.boxesProperty(),
                                             grid.columnCountProperty(),
                                             grid.rowCountProperty());

        assertEquals(Collections.emptyList(), slots.acrossSlotsProperty());
        assertEquals(Collections.emptyList(), slots.downSlotsProperty());
    }

    @Test
    void constructor_3x3() {
        final var grid = CrosswordGridViewModel.newGrid();
        grid.addColumn(); // implicitly creates first row
        grid.addColumn();
        grid.addColumn();
        grid.addRow();
        grid.addRow();

        final var slots = new SlotsViewModel(grid.boxesProperty(),
                                             grid.columnCountProperty(),
                                             grid.rowCountProperty());

        assertEquals(List.of(SlotOutline.across(0, 3, 0),
                             SlotOutline.across(0, 3, 1),
                             SlotOutline.across(0, 3, 2)),
                     slots.acrossSlotsProperty().get());
        assertEquals(List.of(SlotOutline.down(0, 3, 0),
                             SlotOutline.down(0, 3, 1),
                             SlotOutline.down(0, 3, 2)),
                     slots.downSlotsProperty());
    }

    @Test
    void constructor_4x4WithShades() {
        final var grid = CrosswordGridViewModel.newGrid();
        grid.addColumn(); // implicitly creates first row
        grid.addColumn();
        grid.addColumn();
        grid.addColumn();
        grid.addRow();
        grid.addRow();
        grid.addRow();
        grid.box(at(0, 0)).shade();
        grid.box(at(3, 0)).shade();
        grid.box(at(1, 2)).shade();
        grid.box(at(3, 3)).shade();

        final var slots = new SlotsViewModel(grid.boxesProperty(),
                                             grid.columnCountProperty(),
                                             grid.rowCountProperty());

        assertEquals(List.of(SlotOutline.across(1, 3, 0),
                             SlotOutline.across(0, 4, 1),
                             SlotOutline.across(2, 4, 2),
                             SlotOutline.across(0, 3, 3)),
                     slots.acrossSlotsProperty().get());
        assertEquals(List.of(SlotOutline.down(1, 4, 0),
                             SlotOutline.down(0, 2, 1),
                             SlotOutline.down(0, 4, 2),
                             SlotOutline.down(1, 3, 3)),
                     slots.downSlotsProperty());
    }

    @Test
    void shadeBox() {
        final var grid = CrosswordGridViewModel.newGrid();
        grid.addColumn(); // implicitly creates first row
        grid.addColumn();
        grid.addColumn();
        grid.addColumn();
        grid.addRow();
        grid.addRow();
        grid.addRow();
        final var slots = new SlotsViewModel(grid.boxesProperty(),
                                             grid.columnCountProperty(),
                                             grid.rowCountProperty());
        final var acrossChangeCounter = new ChangeEventCounter<>(slots.acrossSlotsProperty());
        final var downChangeCounter = new ChangeEventCounter<>(slots.downSlotsProperty());

        grid.box(at(0, 0)).shade();
        grid.box(at(3, 0)).shade();
        grid.box(at(1, 2)).shade();
        grid.box(at(3, 3)).shade();

        assertEquals(List.of(SlotOutline.across(1, 3, 0),
                             SlotOutline.across(0, 4, 1),
                             SlotOutline.across(2, 4, 2),
                             SlotOutline.across(0, 3, 3)),
                     slots.acrossSlotsProperty().get());
        assertEquals(4, acrossChangeCounter.count());
        assertEquals(List.of(SlotOutline.down(1, 4, 0),
                             SlotOutline.down(0, 2, 1),
                             SlotOutline.down(0, 4, 2),
                             SlotOutline.down(1, 3, 3)),
                     slots.downSlotsProperty());
        assertEquals(4, downChangeCounter.count());
    }
}
