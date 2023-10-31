/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.tests;

import org.junit.jupiter.api.Test;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.testfx.matcher.control.ListViewMatchers.isEmpty;
import static org.testfx.matcher.control.TextMatchers.hasText;

/**
 * Tests on welcome screen.
 */
final class WelcomeScreenTest extends CroiseurGuiTest {

    @Test
    void emptyPuzzleListOnStartup() {
        verifyThat(".list-view", isEmpty());
        verifyThat("#puzzle-placeholder-text", isVisible());
        verifyThat("#puzzle-placeholder-text", hasText("No puzzle found."));
    }
}
