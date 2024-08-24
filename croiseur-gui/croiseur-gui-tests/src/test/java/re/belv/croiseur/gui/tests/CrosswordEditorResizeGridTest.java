/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.tests;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.hasChildren;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;

/**
 * Tests on the editor view: Resize grid.
 */
final class CrosswordEditorResizeGridTest extends CroiseurGuiTest {

    @Test
    void test(final FxRobot robot) throws TimeoutException {
        step1_GoToEditorView(robot);
        step2_AddRow(robot);
        step3_AddColumn(robot);
        step4_DeleteRow(robot);
        step5_DeleteColumn(robot);
        step6_DeleteGrid(robot);
    }

    /**
     * Application starts on the welcome screen: Go to the editor view as first step.
     *
     * @param robot the TestFx robot
     */
    private void step1_GoToEditorView(final FxRobot robot) {
        robot.clickOn("#newPuzzleButton");
        verifyThat("#crossword-grid", isVisible());
        verifyThat("#crossword-grid", hasChildren(42 /* 6 columns, 7 rows */, ".crossword-box-text"));
        verifyThat("#solveButton", isEnabled());
    }

    /**
     * Adds a row.
     *
     * @param robot the TestFx robot
     */
    private void step2_AddRow(final FxRobot robot) {
        robot.clickOn("#resizeGridButton").clickOn("#addRowButton").clickOn("#resizeGridButton");
        verifyThat("#crossword-grid", hasChildren(48 /* 6 columns, 8 rows */, ".crossword-box-text"));
    }

    /**
     * Adds a column.
     *
     * @param robot the TestFx robot
     */
    private void step3_AddColumn(final FxRobot robot) {
        robot.clickOn("#resizeGridButton").clickOn("#addColumnButton").clickOn("#resizeGridButton");
        verifyThat("#crossword-grid", hasChildren(56 /* 7 columns, 8 rows */, ".crossword-box-text"));
    }

    /**
     * Deletes a row.
     *
     * @param robot the TestFx robot
     */
    private void step4_DeleteRow(final FxRobot robot) {
        robot.clickOn("#resizeGridButton").clickOn("#deleteRowButton").clickOn("#resizeGridButton");
        verifyThat("#crossword-grid", hasChildren(49 /* 7 columns, 7 rows */, ".crossword-box-text"));
    }

    /**
     * Deletes a column.
     *
     * @param robot the TestFx robot
     */
    private void step5_DeleteColumn(final FxRobot robot) {
        robot.clickOn("#resizeGridButton").clickOn("#deleteColumnButton").clickOn("#resizeGridButton");
        verifyThat("#crossword-grid", hasChildren(42 /* 6 columns, 7 rows */, ".crossword-box-text"));
    }

    /**
     * Deletes the entire grid.
     *
     * @param robot the TestFx robot
     */
    private void step6_DeleteGrid(final FxRobot robot) throws TimeoutException {
        robot.clickOn("#resizeGridButton").clickOn("#deleteGridButton").clickOn("#resizeGridButton");
        verifyThat("#crossword-grid", hasChildren(0, ".crossword-box-text"));
        verifyThat("#placeholder", isVisible());
        verifyThat("#solveButton", isDisabled());
    }
}
