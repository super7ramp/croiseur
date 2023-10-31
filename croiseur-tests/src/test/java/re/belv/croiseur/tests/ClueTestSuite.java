/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.tests;

import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

/**
 * Suite testing clue service.
 */
@Suite
@SelectClasspathResource("re/belv/croiseur/tests/clue")
public final class ClueTestSuite {
    // Nothing to add.
}
