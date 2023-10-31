/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.view.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import re.belv.croiseur.gui.view.model.slot.SlotOutline;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static re.belv.croiseur.gui.view.model.GridCoord.at;

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
        model.crosswordGridViewModel().box(at(3, 0)).shade();
        model.cluesViewModel().acrossClue(0).systemContent("An across clue");
        model.cluesViewModel().downClue(0).userContent("A down clue");

        model.crosswordGridViewModel().addColumn();

        assertEquals("An across clue", model.cluesViewModel().acrossClue(0).systemContent());
        assertEquals("A down clue", model.cluesViewModel().downClue(0).userContent());
    }

    /**
     * Clear clue if slot is modified.
     */
    @Test
    void addSlot_clearInvalidatedClues() {
        model.crosswordGridViewModel().box(at(3, 0)).shade();
        model.cluesViewModel().acrossClue(1).systemContent("An across clue");
        model.cluesViewModel().acrossClue(2).userContent("Another across clue");

        model.crosswordGridViewModel().addColumn();

        assertEquals("", model.cluesViewModel().acrossClue(1).systemContent());
        assertEquals("", model.cluesViewModel().acrossClue(2).userContent());
    }

    @Test
    void shade() {
        model.crosswordGridViewModel().box(at(1, 1)).shade();

        assertEquals(List.of(SlotOutline.across(0, 6, 0),
                             SlotOutline.across(0, 1, 1),
                             SlotOutline.across(2, 6, 1),
                             SlotOutline.across(0, 6, 2),
                             SlotOutline.across(0, 6, 3),
                             SlotOutline.across(0, 6, 4),
                             SlotOutline.across(0, 6, 5),
                             SlotOutline.across(0, 6, 6)),
                     model.crosswordGridViewModel().acrossSlotsProperty().get());

        // Only 7 clues: Slot with less than 2 boxes is ignored
        assertEquals(List.of(new ClueViewModel(), new ClueViewModel(), new ClueViewModel(),
                             new ClueViewModel(), new ClueViewModel(), new ClueViewModel(),
                             new ClueViewModel()),
                     model.cluesViewModel().acrossCluesProperty().get());

        assertEquals(List.of(SlotOutline.down(0, 7, 0),
                             SlotOutline.down(0, 1, 1),
                             SlotOutline.down(2, 7, 1),
                             SlotOutline.down(0, 7, 2),
                             SlotOutline.down(0, 7, 3),
                             SlotOutline.down(0, 7, 4),
                             SlotOutline.down(0, 7, 5)),
                     model.crosswordGridViewModel().downSlotsProperty().get());

        // Only 6 clues: Slot with less than 2 boxes is ignored
        assertEquals(List.of(new ClueViewModel(), new ClueViewModel(), new ClueViewModel(),
                             new ClueViewModel(), new ClueViewModel(), new ClueViewModel()),
                     model.cluesViewModel().downCluesProperty().get());
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

    @Test
    void reset() {
        model.cluesViewModel().downClue(0).userContent("A down clue");
        model.cluesViewModel().acrossClue(0).systemContent("An across clue");
        model.crosswordGridViewModel().box(at(3, 3)).shade();
        model.crosswordGridViewModel().box(at(4, 4)).solverContent("A");
        model.crosswordGridViewModel().box(at(5, 5)).userContent("B");
        model.crosswordGridViewModel().addColumn();
        model.crosswordGridViewModel().deleteLastColumn();

        model.reset();

        assertEquals("", model.cluesViewModel().downClue(0).userContent());
        assertEquals("", model.cluesViewModel().acrossClue(0).systemContent());
        assertFalse(model.crosswordGridViewModel().box(at(3, 3)).isShaded());
        assertEquals("", model.crosswordGridViewModel().box(at(4, 4)).solverContent());
        assertEquals("", model.crosswordGridViewModel().box(at(5, 5)).userContent());
        assertEquals(6, model.crosswordGridViewModel().columnCount());
        assertEquals(7, model.crosswordGridViewModel().rowCount());
    }
}