/*
 * SPDX-FileCopyrightText: 2025 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.tests;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isFocused;
import static org.testfx.matcher.base.NodeMatchers.isNotFocused;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;
import static re.belv.croiseur.gui.tests.CrosswordEditorFixtures.goToEditorView;
import static re.belv.croiseur.gui.tests.CrosswordEditorMatchers.hasChildThatAt;

import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;

/** Tests on the editor view: Auto-move current box when typing. */
final class CrosswordEditorAutoMoveCurrentBoxTest extends CroiseurGuiTest {

    @Test
    void test(final FxRobot robot) {
        step1_GoToEditorView(robot);
        step2_TypeLetterHorizontalSlot(robot);
        step3_DeleteLetterHorizontalSlot(robot);
        step4_SwitchSlotOrientation(robot);
        step5_TypeLetterVerticalSlot(robot);
        step6_AddBlockInNextBox(robot);
        step7_TypeLetterBeforeBlock(robot);
    }

    /**
     * Application starts on the welcome screen: Go to the editor view as first step.
     *
     * @param robot the TestFx robot
     */
    private void step1_GoToEditorView(final FxRobot robot) {
        goToEditorView(robot);
    }

    /**
     * Type a letter and verify focused box has moved to the next box (on the right).
     *
     * @param robot the TestFx robot
     */
    private void step2_TypeLetterHorizontalSlot(final FxRobot robot) {
        robot.clickOn(".crossword-box-text:first-child");
        verifyThat("#crossword-grid", hasChildThatAt(0, 0, hasText("")));
        verifyThat("#crossword-grid", hasChildThatAt(0, 0, isFocused()));

        robot.type(KeyCode.A);

        verifyThat("#crossword-grid", hasChildThatAt(0, 0, hasText("A")));
        verifyThat("#crossword-grid", hasChildThatAt(0, 0, isNotFocused()));
        verifyThat("#crossword-grid", hasChildThatAt(1, 0, isFocused()));
    }

    /**
     * Press backspace and verify focused box has moved to the previous box, then delete letter and verify focused box
     * is still the same (first box of the slot).
     *
     * @param robot the TestFx robot
     */
    private void step3_DeleteLetterHorizontalSlot(final FxRobot robot) {
        robot.type(KeyCode.BACK_SPACE);

        verifyThat("#crossword-grid", hasChildThatAt(0, 0, isFocused()));
        verifyThat("#crossword-grid", hasChildThatAt(0, 0, hasText("A")));

        robot.type(KeyCode.BACK_SPACE);

        verifyThat("#crossword-grid", hasChildThatAt(0, 0, isFocused()));
        verifyThat("#crossword-grid", hasChildThatAt(0, 0, hasText("")));
    }

    /**
     * Switch slot orientation to vertical.
     *
     * @param robot the TestFx robot
     */
    private void step4_SwitchSlotOrientation(final FxRobot robot) {
        robot.type(KeyCode.ENTER);
    }

    /**
     * Type a letter and verify focused box has moved to the next box (below).
     *
     * @param robot the TestFx robot
     */
    private void step5_TypeLetterVerticalSlot(final FxRobot robot) {
        verifyThat("#crossword-grid", hasChildThatAt(0, 0, isFocused()));

        robot.type(KeyCode.A);

        verifyThat("#crossword-grid", hasChildThatAt(0, 0, hasText("A")));
        verifyThat("#crossword-grid", hasChildThatAt(0, 1, isFocused()));
    }

    /**
     * Add a block in the next box.
     *
     * @param robot the TestFx robot
     */
    private void step6_AddBlockInNextBox(final FxRobot robot) {
        robot.type(KeyCode.DOWN, KeyCode.SPACE, KeyCode.UP);
    }

    /**
     * Type a letter before the block and verify focused box remains the same (last box of the slot).
     *
     * @param robot the TestFx robot
     */
    private void step7_TypeLetterBeforeBlock(final FxRobot robot) {
        verifyThat("#crossword-grid", hasChildThatAt(0, 1, isFocused()));

        robot.type(KeyCode.B);

        verifyThat("#crossword-grid", hasChildThatAt(0, 1, hasText("B")));
        verifyThat("#crossword-grid", hasChildThatAt(0, 1, isFocused()));
    }
}
