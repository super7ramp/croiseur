/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model;

import com.gitlab.super7ramp.croiseur.gui.view.model.slot.SlotOutline;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link PuzzleEditionViewModel}.
 */
final class PuzzleEditionViewModelTest {

    @Test
    void constructor() {
        final var model = new PuzzleEditionViewModel();

        // Default model is a 6x7 grid.
        assertEquals(List.of(SlotOutline.across(0, 6, 0),
                             SlotOutline.across(0, 6, 1),
                             SlotOutline.across(0, 6, 2),
                             SlotOutline.across(0, 6, 3),
                             SlotOutline.across(0, 6, 4),
                             SlotOutline.across(0, 6, 5),
                             SlotOutline.across(0, 6, 6)),
                     model.crosswordGridViewModel().acrossSlotsProperty().get());

        assertEquals(List.of(new ClueViewModel(), new ClueViewModel(), new ClueViewModel(),
                             new ClueViewModel(), new ClueViewModel(), new ClueViewModel(),
                             new ClueViewModel()),
                     model.cluesViewModel().acrossCluesProperty().get());

        assertEquals(List.of(SlotOutline.down(0, 7, 0),
                             SlotOutline.down(0, 7, 1),
                             SlotOutline.down(0, 7, 2),
                             SlotOutline.down(0, 7, 3),
                             SlotOutline.down(0, 7, 4),
                             SlotOutline.down(0, 7, 5)),
                     model.crosswordGridViewModel().downSlotsProperty().get());

        assertEquals(List.of(new ClueViewModel(), new ClueViewModel(), new ClueViewModel(),
                             new ClueViewModel(), new ClueViewModel(), new ClueViewModel()),
                     model.cluesViewModel().downCluesProperty().get());
    }

    @Test
    void addSlot() {
        // Default model is a 6x7 grid.
        final var model = new PuzzleEditionViewModel();

        model.crosswordGridViewModel().addColumn();

        assertEquals(List.of(SlotOutline.across(0, 7, 0),
                             SlotOutline.across(0, 7, 1),
                             SlotOutline.across(0, 7, 2),
                             SlotOutline.across(0, 7, 3),
                             SlotOutline.across(0, 7, 4),
                             SlotOutline.across(0, 7, 5),
                             SlotOutline.across(0, 7, 6)),
                     model.crosswordGridViewModel().acrossSlotsProperty().get());

        assertEquals(List.of(new ClueViewModel(), new ClueViewModel(), new ClueViewModel(),
                             new ClueViewModel(), new ClueViewModel(), new ClueViewModel(),
                             new ClueViewModel()),
                     model.cluesViewModel().acrossCluesProperty().get());

        assertEquals(List.of(SlotOutline.down(0, 7, 0),
                             SlotOutline.down(0, 7, 1),
                             SlotOutline.down(0, 7, 2),
                             SlotOutline.down(0, 7, 3),
                             SlotOutline.down(0, 7, 4),
                             SlotOutline.down(0, 7, 5),
                             SlotOutline.down(0, 7, 6)),
                     model.crosswordGridViewModel().downSlotsProperty().get());

        assertEquals(List.of(new ClueViewModel(), new ClueViewModel(), new ClueViewModel(),
                             new ClueViewModel(), new ClueViewModel(), new ClueViewModel(),
                             new ClueViewModel()),
                     model.cluesViewModel().downCluesProperty().get());
    }


    @Test
    void deleteSlot() {
        // Default model is a 6x7 grid.
        final var model = new PuzzleEditionViewModel();

        model.crosswordGridViewModel().deleteLastColumn();

        assertEquals(List.of(SlotOutline.across(0, 5, 0),
                             SlotOutline.across(0, 5, 1),
                             SlotOutline.across(0, 5, 2),
                             SlotOutline.across(0, 5, 3),
                             SlotOutline.across(0, 5, 4),
                             SlotOutline.across(0, 5, 5),
                             SlotOutline.across(0, 5, 6)),
                     model.crosswordGridViewModel().acrossSlotsProperty().get());

        assertEquals(List.of(new ClueViewModel(), new ClueViewModel(), new ClueViewModel(),
                             new ClueViewModel(), new ClueViewModel(), new ClueViewModel(),
                             new ClueViewModel()),
                     model.cluesViewModel().acrossCluesProperty().get());

        assertEquals(List.of(SlotOutline.down(0, 7, 0),
                             SlotOutline.down(0, 7, 1),
                             SlotOutline.down(0, 7, 2),
                             SlotOutline.down(0, 7, 3),
                             SlotOutline.down(0, 7, 4)),
                     model.crosswordGridViewModel().downSlotsProperty().get());

        assertEquals(List.of(new ClueViewModel(), new ClueViewModel(), new ClueViewModel(),
                             new ClueViewModel(), new ClueViewModel()),
                     model.cluesViewModel().downCluesProperty().get());
    }

    @Test
    void clear() {
        final var model = new PuzzleEditionViewModel();

        model.crosswordGridViewModel().clear();

        assertEquals(Collections.emptyList(), model.crosswordGridViewModel().acrossSlotsProperty());
        assertEquals(Collections.emptyList(), model.cluesViewModel().acrossCluesProperty());
        assertEquals(Collections.emptyList(), model.crosswordGridViewModel().downSlotsProperty());
        assertEquals(Collections.emptyList(), model.cluesViewModel().downCluesProperty());
    }
}