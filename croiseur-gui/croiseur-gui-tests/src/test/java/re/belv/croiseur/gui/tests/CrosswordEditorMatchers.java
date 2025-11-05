/*
 * SPDX-FileCopyrightText: 2025 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.tests;

import static org.testfx.matcher.base.GeneralMatchers.typeSafeMatcher;

import java.util.Objects;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import org.hamcrest.Matcher;

/** Matchers pertaining to the crossword editor view. */
final class CrosswordEditorMatchers {

    /** Private constructor for utility class. */
    private CrosswordEditorMatchers() {
        // Nothing to do.
    }

    /**
     * Returns a matcher matching a GridPane containing at the given column and row a child node matching the given
     * matcher.
     *
     * @param column the column
     * @param row the row
     * @param itemMatcher the matcher for the child at the given position
     * @return the matcher
     */
    public static <T> Matcher<GridPane> hasChildThatAt(final int column, final int row, final Matcher<T> itemMatcher) {
        return typeSafeMatcher(
                GridPane.class,
                "contains at column " + column + ", row " + row + " a child matching " + itemMatcher,
                gridPane -> Objects.toString(getCell(gridPane, column, row)),
                gridPane -> itemMatcher.matches(getCell(gridPane, column, row)));
    }

    private static Node getCell(final GridPane gridPane, final int column, final int row) {
        final int index = row * gridPane.getColumnCount() + column;
        if (index < 0 || index >= gridPane.getChildren().size()) {
            return null;
        }
        return gridPane.getChildren().get(index);
    }
}
