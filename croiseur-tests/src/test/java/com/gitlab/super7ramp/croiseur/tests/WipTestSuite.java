/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.tests;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

/**
 * Test suite for work in progress.
 * <p>
 * Annotate a test you want to launch with {@literal @wip} so that you can launch it with this
 * test suite.
 */
@Suite(failIfNoTests = false)
@SelectClasspathResource("com/gitlab/super7ramp/croiseur/tests")
@IncludeTags("wip")
public final class WipTestSuite {
    // Nothing to add.
}
