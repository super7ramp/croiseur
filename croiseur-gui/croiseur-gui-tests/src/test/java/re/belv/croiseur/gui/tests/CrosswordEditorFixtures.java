/*
 * SPDX-FileCopyrightText: 2025 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.tests;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.hasChildren;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

import org.testfx.api.FxRobot;

/** Fixtures pertaining to the crossword editor view. */
final class CrosswordEditorFixtures {

    /** Private constructor for utility class. */
    private CrosswordEditorFixtures() {
        // Nothing to do.
    }

    /**
     * Assuming being on the welcome screen, goes to the editor view.
     *
     * @param robot the TestFx robot
     */
    static void goToEditorView(final FxRobot robot) {
        robot.clickOn("#newPuzzleButton");
        verifyThat("#crossword-grid", isVisible());
        verifyThat("#crossword-grid", hasChildren(42 /* 6 columns, 7 rows */, ".crossword-box-text"));
        verifyThat("#solveButton", isEnabled());
    }
}
