/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model.slot;

import com.gitlab.super7ramp.croiseur.gui.view.model.CrosswordGridViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.testutil.ListChangeEventCounter;
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

        final var slots = new SlotsViewModel(grid.boxesProperty(), grid.columnCountProperty(),
                                             grid.rowCountProperty());

        assertEquals(Collections.emptyList(), slots.acrossSlotsProperty());
        assertEquals(Collections.emptyList(), slots.downSlotsProperty());
    }

    @Test
    void constructor_3x3() {
        final var grid = new3x3Grid();

        final var slots = new SlotsViewModel(grid.boxesProperty(), grid.columnCountProperty(),
                                             grid.rowCountProperty());

        assertEquals(List.of(SlotOutline.across(0, 3, 0), SlotOutline.across(0, 3, 1),
                             SlotOutline.across(0, 3, 2)), slots.acrossSlotsProperty().get());
        assertEquals(List.of(SlotOutline.down(0, 3, 0), SlotOutline.down(0, 3, 1),
                             SlotOutline.down(0, 3, 2)), slots.downSlotsProperty());
    }

    @Test
    void constructor_4x4WithShades() {
        final var grid = new4x4Grid();
        grid.box(at(0, 0)).shade();
        grid.box(at(3, 0)).shade();
        grid.box(at(1, 2)).shade();
        grid.box(at(3, 3)).shade();

        final var slots = new SlotsViewModel(grid.boxesProperty(), grid.columnCountProperty(),
                                             grid.rowCountProperty());

        assertEquals(List.of(SlotOutline.across(1, 3, 0), SlotOutline.across(0, 4, 1),
                             SlotOutline.across(0, 1, 2), SlotOutline.across(2, 4, 2),
                             SlotOutline.across(0, 3, 3)),
                     slots.acrossSlotsProperty().get());
        assertEquals(List.of(SlotOutline.down(1, 4, 0), SlotOutline.down(0, 2, 1),
                             SlotOutline.down(3, 4, 1), SlotOutline.down(0, 4, 2),
                             SlotOutline.down(1, 3, 3)),
                     slots.downSlotsProperty().get());
    }

    @Test
    void shadeBox() {
        final var grid = new4x4Grid();
        final var slots = new SlotsViewModel(grid.boxesProperty(), grid.columnCountProperty(),
                                             grid.rowCountProperty());
        final var acrossChangeCounter = new ListChangeEventCounter<>(slots.acrossSlotsProperty());
        final var downChangeCounter = new ListChangeEventCounter<>(slots.downSlotsProperty());

        grid.box(at(0, 0)).shade();
        grid.box(at(3, 0)).shade();
        grid.box(at(1, 2)).shade();
        grid.box(at(3, 3)).shade();

        assertEquals(List.of(SlotOutline.across(1, 3, 0), SlotOutline.across(0, 4, 1),
                             SlotOutline.across(0, 1, 2), SlotOutline.across(2, 4, 2),
                             SlotOutline.across(0, 3, 3)),
                     slots.acrossSlotsProperty().get());
        assertEquals(5, acrossChangeCounter.count());
        assertEquals(5, acrossChangeCounter.replacedSize());

        assertEquals(List.of(SlotOutline.down(1, 4, 0), SlotOutline.down(0, 2, 1),
                             SlotOutline.down(3, 4, 1), SlotOutline.down(0, 4, 2),
                             SlotOutline.down(1, 3, 3)),
                     slots.downSlotsProperty().get());
        assertEquals(5, downChangeCounter.count());
        assertEquals(5, downChangeCounter.replacedSize());
    }

    @Test
    void shadeBox_singleBox() {
        final var grid = CrosswordGridViewModel.newGrid();
        grid.addColumn();
        grid.addColumn();
        grid.addColumn(); // 3x1
        final var slots = new SlotsViewModel(grid.boxesProperty(), grid.columnCountProperty(),
                                             grid.rowCountProperty());

        grid.box(at(1, 0)).shade();

        assertEquals(List.of(SlotOutline.across(0, 1, 0), SlotOutline.across(2, 3, 0)),
                     slots.acrossSlotsProperty().get());
        assertEquals(List.of(SlotOutline.down(0, 1, 0), SlotOutline.down(0, 1, 2)),
                     slots.downSlotsProperty().get());
    }

    @Test
    void lightenBox() {
        final var grid = new4x4Grid();
        grid.box(at(0, 0)).shade();
        grid.box(at(3, 0)).shade();
        grid.box(at(1, 2)).shade();
        grid.box(at(3, 3)).shade();
        final var slots = new SlotsViewModel(grid.boxesProperty(), grid.columnCountProperty(),
                                             grid.rowCountProperty());
        final var acrossChangeCounter = new ListChangeEventCounter<>(slots.acrossSlotsProperty());
        final var downChangeCounter = new ListChangeEventCounter<>(slots.downSlotsProperty());

        grid.box(at(0, 0)).lighten();
        grid.box(at(3, 0)).lighten();
        grid.box(at(1, 2)).lighten();
        grid.box(at(3, 3)).lighten();

        assertEquals(List.of(SlotOutline.across(0, 4, 0), SlotOutline.across(0, 4, 1),
                             SlotOutline.across(0, 4, 2), SlotOutline.across(0, 4, 3)),
                     slots.acrossSlotsProperty().get());
        assertEquals(5, acrossChangeCounter.count());
        assertEquals(4, acrossChangeCounter.replacedSize());

        assertEquals(List.of(SlotOutline.down(0, 4, 0), SlotOutline.down(0, 4, 1),
                             SlotOutline.down(0, 4, 2), SlotOutline.down(0, 4, 3)),
                     slots.downSlotsProperty().get());
        assertEquals(5, downChangeCounter.count());
        assertEquals(4, downChangeCounter.replacedSize());
    }

    @Test
    void lightenBox_singleBox() {
        final var grid = CrosswordGridViewModel.newGrid();
        grid.addColumn();
        grid.addColumn();
        grid.addColumn(); // 3x1
        grid.box(at(1, 0)).shade();
        final var slots = new SlotsViewModel(grid.boxesProperty(), grid.columnCountProperty(),
                                             grid.rowCountProperty());

        grid.box(at(1, 0)).lighten();

        assertEquals(List.of(SlotOutline.across(0, 3, 0)), slots.acrossSlotsProperty().get());
        assertEquals(List.of(SlotOutline.down(0, 1, 0), SlotOutline.down(0, 1, 1),
                             SlotOutline.down(0, 1, 2)),
                     slots.downSlotsProperty().get());
    }

    @Test
    void addColumn() {
        final var grid = new3x3Grid();
        final var slots = new SlotsViewModel(grid.boxesProperty(), grid.columnCountProperty(),
                                             grid.rowCountProperty());
        final var acrossChangeCounter = new ListChangeEventCounter<>(slots.acrossSlotsProperty());
        final var downChangeCounter = new ListChangeEventCounter<>(slots.downSlotsProperty());

        grid.addColumn();

        assertEquals(List.of(SlotOutline.across(0, 4, 0), SlotOutline.across(0, 4, 1),
                             SlotOutline.across(0, 4, 2)), slots.acrossSlotsProperty().get());
        assertEquals(3, acrossChangeCounter.count());
        assertEquals(3, acrossChangeCounter.replacedSize());
        assertEquals(List.of(SlotOutline.down(0, 3, 0), SlotOutline.down(0, 3, 1),
                             SlotOutline.down(0, 3, 2), SlotOutline.down(0, 3, 3)),
                     slots.downSlotsProperty().get());
        assertEquals(1, downChangeCounter.count());
        assertEquals(3, acrossChangeCounter.replacedSize());
    }

    @Test
    void deleteColumn() {
        final var grid = new3x3Grid();
        final var slots = new SlotsViewModel(grid.boxesProperty(), grid.columnCountProperty(),
                                             grid.rowCountProperty());
        final var acrossChangeCounter = new ListChangeEventCounter<>(slots.acrossSlotsProperty());
        final var downChangeCounter = new ListChangeEventCounter<>(slots.downSlotsProperty());

        grid.deleteLastColumn();

        assertEquals(List.of(SlotOutline.across(0, 2, 0), SlotOutline.across(0, 2, 1),
                             SlotOutline.across(0, 2, 2)), slots.acrossSlotsProperty().get());
        assertEquals(3, acrossChangeCounter.count());
        assertEquals(3, acrossChangeCounter.replacedSize());
        assertEquals(List.of(SlotOutline.down(0, 3, 0), SlotOutline.down(0, 3, 1)),
                     slots.downSlotsProperty().get());
        assertEquals(1, downChangeCounter.count());
        assertEquals(3, acrossChangeCounter.replacedSize());
    }

    @Test
    void addRow() {
        final var grid = new3x3Grid();
        final var slots = new SlotsViewModel(grid.boxesProperty(), grid.columnCountProperty(),
                                             grid.rowCountProperty());
        final var acrossChangeCounter = new ListChangeEventCounter<>(slots.acrossSlotsProperty());
        final var downChangeCounter = new ListChangeEventCounter<>(slots.downSlotsProperty());

        grid.addRow();

        assertEquals(List.of(SlotOutline.across(0, 3, 0), SlotOutline.across(0, 3, 1),
                             SlotOutline.across(0, 3, 2), SlotOutline.across(0, 3, 3)),
                     slots.acrossSlotsProperty().get());
        assertEquals(1, acrossChangeCounter.count());
        assertEquals(1, acrossChangeCounter.addedSize());
        assertEquals(0, acrossChangeCounter.removedSize());
        assertEquals(List.of(SlotOutline.down(0, 4, 0), SlotOutline.down(0, 4, 1),
                             SlotOutline.down(0, 4, 2)), slots.downSlotsProperty());
        assertEquals(3, downChangeCounter.count());
        assertEquals(1, acrossChangeCounter.addedSize());
        assertEquals(0, acrossChangeCounter.removedSize());
    }

    @Test
    void clearGrid() {
        final var grid = new3x3Grid();
        final var slots = new SlotsViewModel(grid.boxesProperty(), grid.columnCountProperty(),
                                             grid.rowCountProperty());

        grid.clear();

        assertEquals(Collections.emptyList(), slots.acrossSlotsProperty().get());
        assertEquals(Collections.emptyList(), slots.downSlotsProperty().get());
    }

    private static CrosswordGridViewModel new3x3Grid() {
        final var grid = CrosswordGridViewModel.newGrid();
        grid.addColumn(); // implicitly creates first row
        grid.addColumn();
        grid.addColumn();
        grid.addRow();
        grid.addRow();
        return grid;
    }

    private static CrosswordGridViewModel new4x4Grid() {
        final var grid = new3x3Grid();
        grid.addColumn();
        grid.addRow();
        return grid;
    }
}
