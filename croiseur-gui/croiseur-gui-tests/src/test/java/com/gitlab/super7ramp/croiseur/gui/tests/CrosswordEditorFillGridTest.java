/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.tests;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.hasChildren;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.testfx.util.WaitForAsyncUtils.waitFor;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

/**
 * Tests on the editor view: Fill grid.
 */
final class CrosswordEditorFillGridTest extends CroiseurGuiTest {

    @Test
    void test(final FxRobot robot) throws TimeoutException {
        step1_GoToEditorView(robot);
        step2_ResizeTo3x3(robot);
        step3_RunSolver(robot);
    }

    /**
     * Application starts on the welcome screen: Go to the editor view as first step.
     *
     * @param robot the TestFx robot
     */
    private void step1_GoToEditorView(final FxRobot robot) {
        robot.clickOn("#newPuzzleButton");
        verifyThat("#crossword-grid", isVisible());
        verifyThat("#crossword-grid",
                   hasChildren(42 /* 6 columns, 7 rows */, ".crossword-box-text"));
        verifyThat("#solveButton", isEnabled());
    }

    /**
     * Resizes grid to 3x3.
     *
     * @param robot the TestFx robot
     */
    private void step2_ResizeTo3x3(final FxRobot robot) {
        robot.clickOn("#resizeGridButton")
             .clickOn("#deleteGridButton")
             .clickOn("#addColumnButton") // implicitly creates the first row
             .clickOn("#addColumnButton")
             .clickOn("#addColumnButton")
             .clickOn("#addRowButton")
             .clickOn("#addRowButton")
             .clickOn("#resizeGridButton");
        verifyThat("#crossword-grid",
                   hasChildren(9 /* 3 columns, 3 rows */, ".crossword-box-text"));
    }

    /**
     * Runs the solver.
     *
     * @param robot the TestFx robot
     */
    private void step3_RunSolver(final FxRobot robot) throws TimeoutException {
        robot.clickOn("#solveButton");

        // Wait until the solver finishes
        final GridPane grid = robot.lookup("#crossword-grid").query();
        waitFor(3L, TimeUnit.SECONDS, grid.disabledProperty().not());
        waitForFxEvents();

        assertEquals("""
                     TTT
                     TAP
                     TPN
                     """, gridAsString(robot));
    }

    /**
     * Returns a textual representation of the grid
     *
     * @param robot the TestFx robot
     * @return a textual representation of the grid
     */
    private String gridAsString(final FxRobot robot) {
        final GridPane grid = robot.lookup("#crossword-grid").query();
        final int columnCount = grid.getColumnCount();
        final int rowCount = grid.getRowCount();
        final List<Node> boxes = grid.getChildren();

        final StringBuilder sb = new StringBuilder();
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                final TextField box = (TextField) boxes.get(column * row);
                sb.append(box.getPromptText());
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}