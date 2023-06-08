/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * This package contains only (almost) black-box tests.
 * <p>
 * Tests are not perfectly black-box since they are made at the boundaries of the package-private
 * {@link com.gitlab.super7ramp.croiseur.cli.CroiseurCliApplication CroiseurCliApplication} class
 * instead of being made at the boundary of the main class
 * {@link com.gitlab.super7ramp.croiseur.cli.Main Main}.
 * <p>
 * The reason is that:
 * <ul>
 *     <li>{@link com.gitlab.super7ramp.croiseur.cli.Main Main} calls
 * {@link java.lang.System#exit(int)}
 *     <li>The only way to capture the call is to set up a custom {@code SecurityManager}
 *     <li>{@code SecurityManager} has been deprecated and marked for removal
 *     (<a href="https://bugs.openjdk.org/browse/JDK-8199704">JDK-8199704</a>).
 * </ul>
 * Once a solution not using deprecated method is found, these tests should be moved to a dedicated
 * test module.
 */
package com.gitlab.super7ramp.croiseur.cli;