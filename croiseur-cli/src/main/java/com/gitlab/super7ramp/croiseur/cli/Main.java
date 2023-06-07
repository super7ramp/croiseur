/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.cli;

/**
 * Main.
 */
public final class Main {

    /**
     * Private constructor.
     */
    private Main() {
        // Nothing to do.
    }

    /**
     * Program entry point.
     *
     * @param args command-line arguments
     */
    public static void main(final String[] args) {
        final CroiseurCliApplication app = new CroiseurCliApplication();
        final int exitCode = app.run(args);
        System.exit(exitCode);
    }
}
