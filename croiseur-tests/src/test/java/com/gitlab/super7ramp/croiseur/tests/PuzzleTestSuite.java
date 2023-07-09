/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.tests;

import org.junit.platform.suite.api.ExcludeTags;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

/**
 * Suite testing the puzzle service.
 */
@Suite
@SelectClasspathResource("com/gitlab/super7ramp/croiseur/tests/puzzle")
@ExcludeTags("disabled")
public final class PuzzleTestSuite {
    // Nothing to add.
}