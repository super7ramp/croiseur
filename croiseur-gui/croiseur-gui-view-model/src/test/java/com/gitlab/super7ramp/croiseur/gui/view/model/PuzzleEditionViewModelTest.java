/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model;

import com.gitlab.super7ramp.croiseur.gui.view.model.slot.SlotOutline;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link PuzzleEditionViewModel}.
 */
final class PuzzleEditionViewModelTest {

    /** The model under tests. */
    private PuzzleEditionViewModel model;

    @BeforeEach
    void setup() {
        model = new PuzzleEditionViewModel();
    }

    @Test
    void constructor() {
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

    /**
     * Don't clear clues if slot is not modified.
     */
    @Test
    void addSlot_dontChangeNonImpactedClues() {
        model.cluesViewModel().downClue(0).userContent("A clue");

        model.crosswordGridViewModel().addColumn();

        assertEquals("A clue", model.cluesViewModel().downClue(0).userContent());
    }

    /**
     * Clear clue if slot is modified.
     */
    @Test
    void addSlot_clearInvalidatedClues() {
        model.cluesViewModel().acrossClue(0).userContent("A clue");

        model.crosswordGridViewModel().addColumn();

        assertEquals("", model.cluesViewModel().acrossClue(0).userContent());
    }

    @Test
    void deleteSlot() {
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
        model.crosswordGridViewModel().clear();

        assertEquals(Collections.emptyList(), model.crosswordGridViewModel().acrossSlotsProperty());
        assertEquals(Collections.emptyList(), model.cluesViewModel().acrossCluesProperty());
        assertEquals(Collections.emptyList(), model.crosswordGridViewModel().downSlotsProperty());
        assertEquals(Collections.emptyList(), model.cluesViewModel().downCluesProperty());
    }
}