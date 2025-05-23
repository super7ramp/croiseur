/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.view.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static re.belv.croiseur.gui.view.model.GridCoord.at;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import re.belv.croiseur.gui.view.model.testutil.ChangeEventCounter;

/**
 * Tests for {@link CrosswordGridViewModel}.
 *
 * <p>Legend of grid schemas:
 *
 * <ul>
 *   <li>{@code | |}: An empty cell
 *   <li>{@code | A |}: A cell containing the letter 'A'
 *   <li>{@code | # |}: A shaded cell
 *   <li>{@code |( ) |}: An empty cell part of the selected slot
 *   <li>{@code |(A) |}: A cell containing the letter 'A' and part of the selected slot
 *   <li>{@code |( )'|}: A focused cell part of the selected slot
 *   <li>{@code |(A)'|}: A focused cell containing the letter 'A' and part of the selected slot
 *   <li>{@code | # '|}: A focused shaded cell
 * </ul>
 */
final class CrosswordGridViewModelTest {

    /** The grid view model under test. */
    private CrosswordGridViewModel crosswordGridViewModel;

    @BeforeEach
    void beforeEach() {
        crosswordGridViewModel = CrosswordGridViewModel.newGrid();
    }

    /** Checks that default model is empty. */
    @Test
    void dimensions_empty() {
        assertEquals(0, crosswordGridViewModel.columnCount());
        assertEquals(0, crosswordGridViewModel.rowCount());
    }

    /** Checks that adding a row also updates the column count. */
    @Test
    void dimensions_1x1() {
        // First row implicitly adds a first column and vice-versa
        crosswordGridViewModel.addRow();

        assertEquals(1, crosswordGridViewModel.columnCount());
        assertEquals(1, crosswordGridViewModel.rowCount());
    }

    /**
     * Checks column/row additions with a 2x3 grid without any filled/shaded cells.
     *
     * <p>Grid schema:
     *
     * <pre>
     *     |    |    |
     *     |    |    |
     *     |    |    |
     * </pre>
     */
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

    /**
     * Checks column deletion with a 1x3 grid.
     *
     * <p>Grid schema:
     *
     * <pre>
     *     |    |
     *     |    |
     *     |    |
     * </pre>
     */
    @Test
    void deleteLastColumn_noMoreColumn() {
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addRow();
        crosswordGridViewModel.addRow();

        crosswordGridViewModel.deleteLastColumn();

        assertEquals(0, crosswordGridViewModel.columnCount());
        // Deleting very last column implicitly delete rows
        assertEquals(0, crosswordGridViewModel.rowCount());
        assertTrue(crosswordGridViewModel.boxesProperty().isEmpty());
    }

    /**
     * Checks row deletion with a 3x1 grid.
     *
     * <p>Grid schema:
     *
     * <pre>
     *     |    |    |    |
     * </pre>
     */
    @Test
    void deleteLastRow_noMoreRow() {
        crosswordGridViewModel.addRow();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();

        crosswordGridViewModel.deleteLastRow();

        assertEquals(0, crosswordGridViewModel.rowCount());
        // Deleting very last row implicitly delete columns
        assertEquals(0, crosswordGridViewModel.columnCount());
        assertTrue(crosswordGridViewModel.boxesProperty().isEmpty());
    }

    /** Verifies that direct modification of the map of boxes is rejected. */
    @Test
    void boxes_unmodifiable() {
        assertThrows(
                UnsupportedOperationException.class,
                () -> crosswordGridViewModel.boxesProperty().put(at(0, 0), new CrosswordBoxViewModel()));
    }

    /**
     * Checks horizontal selection with a 2x3 grid.
     *
     * <p>Grid schema:
     *
     * <pre>
     *     |( )'|( ) |
     *     |    |    |
     *     |    |    |
     * </pre>
     */
    @Test
    void selectedSlot_newHorizontalSelection() {
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addRow();
        crosswordGridViewModel.addRow();

        crosswordGridViewModel.currentBoxPosition(at(0, 0));

        assertEquals(List.of(at(0, 0), at(1, 0)), crosswordGridViewModel.currentSlotPositionsProperty());
    }

    /**
     * Checks horizontal selection between shaded cells with a 6x1 grid.
     *
     * <p>Grid schema:
     *
     * <pre>
     *     |    | #  |( )'|( ) | #  |    |
     * </pre>
     */
    @Test
    void selectedSlot_newHorizontalSelectionBetweenShaded() {
        for (int i = 0; i < 6; i++) {
            crosswordGridViewModel.addColumn();
        }
        crosswordGridViewModel.box(at(1, 0)).shade();
        crosswordGridViewModel.box(at(4, 0)).shade();

        crosswordGridViewModel.currentBoxPosition(at(2, 0));

        assertEquals(List.of(at(2, 0), at(3, 0)), crosswordGridViewModel.currentSlotPositionsProperty());
    }

    /**
     * Checks that horizontal selection between shaded cells with a 6x1 grid grows when shaded cells are removed.
     *
     * <p>Grid schema (before):
     *
     * <pre>
     *     |    | #  |( )'|( ) | #  |    |
     * </pre>
     *
     * Grid schema (after):
     *
     * <pre>
     *     |( ) |( ) |( )'|( ) |( ) |( ) |
     * </pre>
     */
    @Test
    void selectedSlot_horizontalSelectionGrows() {
        selectedSlot_newHorizontalSelectionBetweenShaded();

        crosswordGridViewModel.box(at(1, 0)).lighten();
        crosswordGridViewModel.box(at(4, 0)).lighten();

        assertEquals(
                List.of(at(0, 0), at(1, 0), at(2, 0), at(3, 0), at(4, 0), at(5, 0)),
                crosswordGridViewModel.currentSlotPositionsProperty());
    }

    /**
     * Checks vertical selection with a 2x3 grid.
     *
     * <p>Grid schema:
     *
     * <pre>
     *     |( )'|    |
     *     |( ) |    |
     *     |( ) |    |
     * </pre>
     */
    @Test
    void selectedSlot_newVerticalSelection() {
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addRow();
        crosswordGridViewModel.addRow();
        crosswordGridViewModel.currentSlotVertical();

        crosswordGridViewModel.currentBoxPosition(at(0, 0));

        assertEquals(List.of(at(0, 0), at(0, 1), at(0, 2)), crosswordGridViewModel.currentSlotPositionsProperty());
    }

    /**
     * Checks vertical selection between shaded cells with a 1x6 grid.
     *
     * <p>Grid schema:
     *
     * <pre>
     *     |    |
     *     | #  |
     *     |( )'|
     *     |( ) |
     *     | #  |
     *     |    |
     * </pre>
     */
    @Test
    void selectedSlot_newVerticalSelectionBetweenShaded() {
        for (int i = 0; i < 6; i++) {
            crosswordGridViewModel.addRow();
        }
        crosswordGridViewModel.box(at(0, 1)).shade();
        crosswordGridViewModel.box(at(0, 4)).shade();
        crosswordGridViewModel.currentSlotVertical();

        crosswordGridViewModel.currentBoxPosition(at(0, 2));

        assertEquals(List.of(at(0, 2), at(0, 3)), crosswordGridViewModel.currentSlotPositionsProperty());
    }

    /**
     * Checks vertical selection between shaded cells with a 1x6 grid grows when shaded cells are removed
     *
     * <p>Grid schema (before):
     *
     * <pre>
     *     |    |
     *     | #  |
     *     |( )'|
     *     |( ) |
     *     | #  |
     *     |    |
     * </pre>
     *
     * Grid schema (after):
     *
     * <pre>
     *     |( ) |
     *     |( ) |
     *     |( )'|
     *     |( ) |
     *     |( ) |
     *     |( ) |
     * </pre>
     */
    @Test
    void selectedSlot_verticalSelectionGrows() {
        selectedSlot_newVerticalSelectionBetweenShaded();

        crosswordGridViewModel.box(at(0, 1)).lighten();
        crosswordGridViewModel.box(at(0, 4)).lighten();

        assertEquals(
                List.of(at(0, 0), at(0, 1), at(0, 2), at(0, 3), at(0, 4), at(0, 5)),
                crosswordGridViewModel.currentSlotPositionsProperty().get());
    }

    /** Checks that when no box is focused, no slot is selected. */
    @Test
    void selectedSlot_noCurrentBox() {
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addRow();
        crosswordGridViewModel.addRow();

        assertTrue(crosswordGridViewModel.currentSlotPositionsProperty().isEmpty());
    }

    /**
     * Checks that selected horizontal slot is correctly cleared when the row it belongs to is deleted.
     *
     * <p>Grid schema (before):
     *
     * <pre>
     *     |    |    |
     *     |    |    |
     *     |( )'|( ) |
     * </pre>
     *
     * Grid schema (after):
     *
     * <pre>
     *     |    |    |
     *     |    |    |
     * </pre>
     */
    @Test
    void selectedSlot_currentBoxDeleted_horizontal() {
        // Set last line as current slot of a 2x3 grid
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addRow();
        crosswordGridViewModel.addRow();
        crosswordGridViewModel.currentBoxPosition(at(0, 2));
        assertEquals(List.of(at(0, 2), at(1, 2)), crosswordGridViewModel.currentSlotPositionsProperty());

        // Remove last line
        crosswordGridViewModel.deleteLastRow();

        // No more current slot nor current box
        assertNull(crosswordGridViewModel.currentBoxPosition());
        assertTrue(crosswordGridViewModel.currentSlotPositionsProperty().isEmpty());
    }

    /**
     * Checks that selected vertical slot is correctly cleared when the column it belongs to is deleted.
     *
     * <p>Grid schema (before):
     *
     * <pre>
     *     |    |( )'|
     *     |    |( ) |
     *     |    |( ) |
     * </pre>
     *
     * Grid schema (after):
     *
     * <pre>
     *     |    |
     *     |    |
     *     |    |
     * </pre>
     */
    @Test
    void selectedSlot_currentBoxDeleted_vertical() {
        // Set last column as current slot of a 2x3 grid
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addRow();
        crosswordGridViewModel.addRow();
        crosswordGridViewModel.currentSlotVertical();
        crosswordGridViewModel.currentBoxPosition(at(1, 0));
        assertEquals(List.of(at(1, 0), at(1, 1), at(1, 2)), crosswordGridViewModel.currentSlotPositionsProperty());

        // Remove last column
        crosswordGridViewModel.deleteLastColumn();

        // No more current slot nor current box
        assertNull(crosswordGridViewModel.currentBoxPosition());
        assertTrue(crosswordGridViewModel.currentSlotPositionsProperty().isEmpty());
    }

    /**
     * Checks that no selected slot is present when current box is a shaded box.
     *
     * <p>Grid schema:
     *
     * <pre>
     *     |    |    |
     *     | # '|    |
     *     |    |    |
     * </pre>
     */
    @Test
    void selectedSlot_currentBoxInitializedWithShaded() {
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addRow();
        crosswordGridViewModel.addRow();
        crosswordGridViewModel.box(at(0, 1)).shade();

        crosswordGridViewModel.currentBoxPosition(at(0, 1));

        assertTrue(crosswordGridViewModel.currentSlotPositionsProperty().isEmpty());
    }

    /**
     * Checks that selected slot is correctly cleared when current box moves from a non-shaded box to a shaded box.
     *
     * <p>Grid schema (before):
     *
     * <pre>
     *     |( )'|( ) |
     *     | #  |    |
     *     |    |    |
     * </pre>
     *
     * Grid schema (after):
     *
     * <pre>
     *     |    |    |
     *     | #' |    |
     *     |    |    |
     * </pre>
     */
    @Test
    void selectedSlot_currentBoxChangedWithShaded() {
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addRow();
        crosswordGridViewModel.addRow();
        crosswordGridViewModel.box(at(0, 1)).shade();
        crosswordGridViewModel.currentBoxPosition(at(0, 0));
        assertEquals(List.of(at(0, 0), at(1, 0)), crosswordGridViewModel.currentSlotPositionsProperty());

        crosswordGridViewModel.currentBoxPosition(at(0, 1));

        assertTrue(crosswordGridViewModel.currentSlotPositionsProperty().isEmpty());
    }

    /**
     * Checks that selected slot is correctly cleared when current non-shaded box becomes shaded.
     *
     * <p>Grid schema (before):
     *
     * <pre>
     *     |( )'|( ) |
     *     |    |    |
     *     |    |    |
     * </pre>
     *
     * Grid schema (after):
     *
     * <pre>
     *     | #' |    |
     *     |    |    |
     *     |    |    |
     * </pre>
     */
    @Test
    void selectedSlot_currentBoxMutatedFromBlankToShaded() {
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addRow();
        crosswordGridViewModel.addRow();
        crosswordGridViewModel.currentBoxPosition(at(0, 0));
        assertEquals(List.of(at(0, 0), at(1, 0)), crosswordGridViewModel.currentSlotPositionsProperty());

        crosswordGridViewModel.box(at(0, 0)).shade();

        assertTrue(crosswordGridViewModel.currentSlotPositionsProperty().isEmpty());
    }

    /**
     * Checks that selected slot is correctly created when current shaded box becomes non-shaded.
     *
     * <p>Grid schema (before):
     *
     * <pre>
     *     | #' |    |
     *     |    |    |
     *     |    |    |
     * </pre>
     *
     * Grid schema (after):
     *
     * <pre>
     *     |( )'|( ) |
     *     |    |    |
     *     |    |    |
     * </pre>
     */
    @Test
    void selectedSlot_currentBoxMutatedFromShadedToBlank() {
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addRow();
        crosswordGridViewModel.addRow();
        crosswordGridViewModel.box(at(0, 0)).shade();
        crosswordGridViewModel.currentBoxPosition(at(0, 0));
        assertTrue(crosswordGridViewModel.currentSlotPositionsProperty().isEmpty());

        crosswordGridViewModel.box(at(0, 0)).lighten();

        assertEquals(List.of(at(0, 0), at(1, 0)), crosswordGridViewModel.currentSlotPositionsProperty());
    }

    /**
     * Checks a fully filled current slot content on a 3x1 grid.
     *
     * <p>Grid schema:
     *
     * <pre>
     *     |(A)'|(B) |(C) |
     * </pre>
     *
     * Expected content is "ABC".
     */
    @Test
    void selectedSlotContent_filled() {
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.box(at(0, 0)).userContent("A");
        crosswordGridViewModel.box(at(1, 0)).userContent("B");
        crosswordGridViewModel.box(at(2, 0)).userContent("C");

        crosswordGridViewModel.currentBoxPosition(at(0, 0));

        assertEquals("ABC", crosswordGridViewModel.currentSlotContent());
    }

    /**
     * Checks a partially filled current slot content on a 3x1 grid.
     *
     * <p>Grid schema:
     *
     * <pre>
     *     |(A)'|( ) |(C) |
     * </pre>
     *
     * Expected content is "A.C".
     */
    @Test
    void selectedSlotContent_partiallyFilled() {
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.box(at(0, 0)).userContent("A");
        crosswordGridViewModel.box(at(2, 0)).userContent("C");

        crosswordGridViewModel.currentBoxPosition(at(0, 0));

        assertEquals("A.C", crosswordGridViewModel.currentSlotContent());
    }

    /**
     * Checks a blank current slot content on a 3x1 grid.
     *
     * <p>Grid schema:
     *
     * <pre>
     *     |( )'|( ) |( ) |
     * </pre>
     *
     * Expected content is "...".
     */
    @Test
    void selectedSlotContent_onlyBlanks() {
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();

        crosswordGridViewModel.currentBoxPositionProperty().set(at(0, 0));

        assertEquals("...", crosswordGridViewModel.currentSlotContent());
    }

    /** Checks that by default no selected content is present. */
    @Test
    void selectedSlotContent_noSelection() {
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();

        assertTrue(crosswordGridViewModel.currentSlotContent().isEmpty());
    }

    /**
     * Checks that slot content is updated when grid boxes are modified.
     *
     * <p>Grid schema (before):
     *
     * <pre>
     *     |(A)'|( ) |(C) |
     * </pre>
     *
     * Content was "A.C".
     *
     * <p>Grid schema (after):
     *
     * <pre>
     *     |(A)'|(B) |(C) |
     * </pre>
     *
     * Expected content is "ABC".
     */
    @Test
    void selectedSlotContent_contentChange() {
        selectedSlotContent_partiallyFilled();

        crosswordGridViewModel.box(at(1, 0)).userContent("B");

        assertEquals("ABC", crosswordGridViewModel.currentSlotContent());
    }

    /**
     * Checks that slot content is updated when grid structure is modified - here a column is added.
     *
     * <p>Grid schema (before):
     *
     * <pre>
     *     |(A)'|(B) |(C) |
     * </pre>
     *
     * Content was "ABC".
     *
     * <p>Grid schema (after):
     *
     * <pre>
     *     |(A)'|(B) |(C) |( ) |
     * </pre>
     *
     * Expected content is "ABC.".
     */
    @Test
    void selectedSlotContent_contentGrows() {
        selectedSlotContent_filled();

        crosswordGridViewModel.addColumn();

        assertEquals("ABC.", crosswordGridViewModel.currentSlotContent());
    }

    /**
     * Verifies that when slot is unselected, the unsolvable status of its boxes is cleared (because only current slot
     * unsolvable status is tracked).
     */
    @Test
    void unsolvableState_orientationChange() {
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addRow();
        crosswordGridViewModel.addRow();
        crosswordGridViewModel.currentBoxPosition(at(0, 0));
        crosswordGridViewModel.currentSlotUnsolvable();
        // boxes of selected horizontal slot are unsolvable
        assertTrue(crosswordGridViewModel.box(at(0, 0)).isUnsolvable());
        assertTrue(crosswordGridViewModel.box(at(1, 0)).isUnsolvable());

        crosswordGridViewModel.currentSlotVertical();

        // newly selected boxes are unsolvable (unsolvable slot status hasn't been cleared)
        assertTrue(crosswordGridViewModel.box(at(0, 0)).isUnsolvable());
        assertTrue(crosswordGridViewModel.box(at(0, 1)).isUnsolvable());
        // previously selected boxes not part of new current vertical slot is cleared
        assertFalse(crosswordGridViewModel.box(at(1, 0)).isUnsolvable());
    }

    /**
     * Verifies that {@link CrosswordGridViewModel#resetContentLettersFilledBySolverOnly()} deletes only solver content.
     *
     * <p>Grid schema (before):
     *
     * <pre>
     *     | A  | B  | #  |
     *       ^    ^
     *       |    `- solver content
     *       `- user content
     * </pre>
     *
     * Grid schema (after):
     *
     * <pre>
     *     | A  |    | #  |
     *       ^
     *       |
     *       `- user content
     * </pre>
     */
    @Test
    void resetContentLettersFilledBySolver() {
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.box(at(0, 0)).userContent("A");
        crosswordGridViewModel.box(at(1, 0)).solverContent("B");
        crosswordGridViewModel.box(at(2, 0)).shade();

        crosswordGridViewModel.resetContentLettersFilledBySolverOnly();

        assertEquals("A", crosswordGridViewModel.box(at(0, 0)).userContent());
        assertTrue(crosswordGridViewModel.box(at(1, 0)).solverContent().isEmpty());
        assertTrue(crosswordGridViewModel.box(at(2, 0)).isShaded());
    }

    /**
     * Verifies that {@link CrosswordGridViewModel#resetContentLettersOnly()} clears only letters (both user- and
     * solver-filled).
     *
     * <p>Grid schema (before):
     *
     * <pre>
     *     | A  | B  | #  |
     *       ^    ^
     *       |    `- solver content
     *       `- user content
     * </pre>
     *
     * Grid schema (after):
     *
     * <pre>
     *     |    |    | #  |
     * </pre>
     */
    @Test
    void resetContentLettersOnly() {
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.box(at(0, 0)).userContent("A");
        crosswordGridViewModel.box(at(1, 0)).solverContent("B");
        crosswordGridViewModel.box(at(2, 0)).shade();

        crosswordGridViewModel.resetContentLettersOnly();

        assertTrue(crosswordGridViewModel.box(at(0, 0)).userContent().isEmpty());
        assertTrue(crosswordGridViewModel.box(at(1, 0)).solverContent().isEmpty());
        assertTrue(crosswordGridViewModel.box(at(2, 0)).isShaded());
    }

    /**
     * Verifies that {@link CrosswordGridViewModel#resetContentLettersOnly()} clears both letters and shaded boxes.
     *
     * <p>Grid schema (before):
     *
     * <pre>
     *     | A  | B  | #  |
     *       ^    ^
     *       |    `- solver content
     *       `- user content
     * </pre>
     *
     * Grid schema (after):
     *
     * <pre>
     *     |    |    |    |
     * </pre>
     */
    @Test
    void resetContentAll() {
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.box(at(0, 0)).userContent("A");
        crosswordGridViewModel.box(at(1, 0)).solverContent("B");
        crosswordGridViewModel.box(at(2, 0)).shade();

        crosswordGridViewModel.resetContentAll();

        assertTrue(crosswordGridViewModel.box(at(0, 0)).userContent().isEmpty());
        assertTrue(crosswordGridViewModel.box(at(1, 0)).solverContent().isEmpty());
        assertFalse(crosswordGridViewModel.box(at(2, 0)).isShaded());
    }

    /** Verifies that filling the current slot does not generate an event for each box modified. */
    @Test
    void setCurrentSlot() {
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.currentBoxPosition(at(0, 0));
        final var changeEventCounter = new ChangeEventCounter<>(crosswordGridViewModel.currentSlotContentProperty());

        crosswordGridViewModel.currentSlotContent("ABC");

        assertEquals("ABC", crosswordGridViewModel.currentSlotContent());
        assertEquals(1, changeEventCounter.count());
    }

    /**
     * Verifies that a {@link NullPointerException} is raised when a {@code null} value is passed to
     * {@link CrosswordGridViewModel#currentSlotContent(String)}.
     */
    @Test
    void setCurrentSlot_null() {
        assertThrows(NullPointerException.class, () -> crosswordGridViewModel.currentSlotContent(null));
    }

    /**
     * Verifies that an {@link IllegalArgumentException} is raised when a string not matching current slot length is
     * passed to {@link CrosswordGridViewModel#currentSlotContent(String)}.
     */
    @Test
    void setCurrentSlot_invalidLength() {
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.currentBoxPosition(at(0, 0));

        assertThrows(IllegalArgumentException.class, () -> crosswordGridViewModel.currentSlotContent("ABCD"));
    }

    /**
     * Verifies {@link CrosswordGridViewModel#resizeTo(int, int)}: Case in which both columns and rows need to be added.
     */
    @Test
    void resizeTo_addColumnsAddRows() {
        crosswordGridViewModel.resizeTo(2, 3);

        assertEquals(2, crosswordGridViewModel.columnCount());
        assertEquals(3, crosswordGridViewModel.rowCount());
    }

    /**
     * Verifies {@link CrosswordGridViewModel#resizeTo(int, int)}: Case in which both columns and rows need to be
     * removed.
     */
    @Test
    void resizeTo_removeColumnsRemoveRows() {
        crosswordGridViewModel.addColumn(); // implicitly adds a row as well
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addRow(); // size is now 2x2
        crosswordGridViewModel.resizeTo(1, 1);

        assertEquals(1, crosswordGridViewModel.columnCount());
        assertEquals(1, crosswordGridViewModel.rowCount());
    }

    /**
     * Verifies {@link CrosswordGridViewModel#resizeTo(int, int)}: Case in which columns need to be added while rows
     * need to be removed.
     */
    @Test
    void resizeTo_addColumnsRemoveRows() {
        crosswordGridViewModel.addColumn(); // implicitly adds a row as well
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addRow(); // size is now 2x2
        crosswordGridViewModel.resizeTo(3, 1);

        assertEquals(3, crosswordGridViewModel.columnCount());
        assertEquals(1, crosswordGridViewModel.rowCount());
    }

    /**
     * Verifies {@link CrosswordGridViewModel#resizeTo(int, int)}: Case in which columns need to be removed while rows
     * need to be added.
     */
    @Test
    void resizeTo_removeColumnsAddRows() {
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addColumn();
        crosswordGridViewModel.addRow();
        crosswordGridViewModel.resizeTo(1, 3);

        assertEquals(1, crosswordGridViewModel.columnCount());
        assertEquals(3, crosswordGridViewModel.rowCount());
    }

    /**
     * Verifies {@link CrosswordGridViewModel#resizeTo(int, int)}: Case in which passed dimension is invalid (negative
     * values).
     */
    @Test
    void resizeTo_invalidNegative() {
        assertThrows(IllegalArgumentException.class, () -> crosswordGridViewModel.resizeTo(-1, -1));
    }
}
